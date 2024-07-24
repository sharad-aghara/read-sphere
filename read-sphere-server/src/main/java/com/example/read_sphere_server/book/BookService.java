package com.example.read_sphere_server.book;

import com.example.read_sphere_server.common.PageResponse;
import com.example.read_sphere_server.exception.OperationNotPermittedException;
import com.example.read_sphere_server.file.FileStorageService;
import com.example.read_sphere_server.model.Book;
import com.example.read_sphere_server.model.BookTransactionHistory;
import com.example.read_sphere_server.model.User;
import com.example.read_sphere_server.repo.BookRepository;
import com.example.read_sphere_server.repo.BookTransactionHistoryRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

import static com.example.read_sphere_server.book.BookSpecification.withOwnerId;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepo;
    private final BookMapper bookMapper;
    private final BookTransactionHistoryRepo bookTransactionHistoryRepo;
    private final FileStorageService fileStorageService;

    public Integer save(BookRequest request, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Book book = bookMapper.toBook(request);
        book.setOwner(user);

        return bookRepo.save(book).getId();
    }

    public BookResponse findById(Integer bookId) {
        return bookRepo.findById(bookId)
                .map(bookMapper::toBookResponse)
                .orElseThrow(() -> new EntityNotFoundException("No book found with boo Id: " + bookId));
    }

    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser) {

        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepo.findAllDisplayableBooks(pageable, user.getId());
        List<BookResponse> bookResponses = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();

        return new PageResponse<>(
                bookResponses,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        // JpaSpecificationExecutor<Book> in BookRepo will give access to use .findAll(withOwnerId(user.getId()), pageable)
        Page<Book> books = bookRepo.findAll(withOwnerId(user.getId()), pageable);
        List<BookResponse> bookResponses = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();

        return new PageResponse<>(
                bookResponses,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> allBorrowedBooks = bookTransactionHistoryRepo.findAllBorrowedBooks(pageable, user.getId());
        List<BorrowedBookResponse> bookResponses = allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();

        return new PageResponse<>(
                bookResponses,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> allBorrowedBooks = bookTransactionHistoryRepo.findAllReturnedBooks(pageable, user.getId());
        List<BorrowedBookResponse> bookResponses = allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();

        return new PageResponse<>(
                bookResponses,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    public Integer updateShareableStatus(Integer bookId, Authentication connectedUser) {
        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book with Id: " + bookId + " is found."));

        User user = ((User) connectedUser.getPrincipal());
        if (!Objects.equals(book.getOwner().getBooks(), user.getId())) {
            throw new OperationNotPermittedException("You can't update book shareable status.");
        }
        book.setShareable(!book.isShareable());
        bookRepo.save(book);

        return bookId;
    }

    public Integer updateArchivedStatus(Integer bookId, Authentication connectedUser) {
        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book with Id: " + bookId + " is found."));

        User user = ((User) connectedUser.getPrincipal());
        if (!Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You can't update other's book archived status.");
        }
        book.setArchived(!book.isArchived());
        bookRepo.save(book);

        return bookId;
    }

    public Integer borrowBook(int bookId, Authentication connectedUser) {
        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with Id: " + bookId));

        // check if book is archived or not shareable
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("Requested book can't be borrowed at this time, Since it is archived or not shareable.");
        }

        User user = ((User) connectedUser.getPrincipal());
        // check is borrower is owner of the book
        if (Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You can't borrow your own book.");
        }

        // make sure user doesn't borrow his/her own book
        final boolean isAlreadyBorrowed = bookTransactionHistoryRepo.isAlreadyBorrowedByUser(bookId, user.getId());
        if (isAlreadyBorrowed) {
            throw new OperationNotPermittedException("The requested book is already borrowed");
        }

        BookTransactionHistory bookTransactionHistory = BookTransactionHistory.builder()
                .user(user)
                .book(book)
                .returned(false)
                .returnApproved(false)
                .build();

        return bookTransactionHistoryRepo.save(bookTransactionHistory).getId();
    }

    public Integer returnBorrowedBook(int bookId, Authentication connectedUser) {
        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with Id: " + bookId));

        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("Requested book can't be borrowed at this time, Since it is archived or not shareable.");
        }

        User user = ((User) connectedUser.getPrincipal());
        // check is borrower is owner of the book
        if (Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You can't borrow or return your own book.");
        }

        // if book is borrowed by user or not
        BookTransactionHistory bookTransactionHistory = bookTransactionHistoryRepo.findByBookIdAndUserId(bookId, user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("You did not borrow this book"));

        bookTransactionHistory.setReturned(true);

        return bookTransactionHistoryRepo.save(bookTransactionHistory).getId();
    }


    public Integer approveReturnBorrowedBook(int bookId, Authentication connectedUser) {
        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with Id: " + bookId));

        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("Requested book can't be borrowed at this time, Since it is archived or not shareable.");
        }

        User user = ((User) connectedUser.getPrincipal());
        // check is borrower is owner of the book
        if (Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You can't borrow or return your own book.");
        }

        BookTransactionHistory bookTransactionHistory = bookTransactionHistoryRepo.findByBookIdAndOwnerId(bookId, user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("The book is not returned yet, so you can't approve return."));

        bookTransactionHistory.setReturnApproved(true);

        return bookTransactionHistoryRepo.save(bookTransactionHistory).getId();
    }

    public void uploadBookCoverPicture(MultipartFile file, Authentication connectedUser, int bookId) {
        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with Id: " + bookId));
        User user = ((User) connectedUser.getPrincipal());

        var bookCover = fileStorageService.saveFile(file, user.getId());
        book.setBookCover(bookCover);
        bookRepo.save(book);
    }
}
