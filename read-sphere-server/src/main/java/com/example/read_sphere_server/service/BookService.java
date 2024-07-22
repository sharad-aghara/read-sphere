package com.example.read_sphere_server.service;

import com.example.read_sphere_server.model.Book;
import com.example.read_sphere_server.model.User;
import com.example.read_sphere_server.repo.BookRepository;
import com.example.read_sphere_server.requestValidation.BookRequest;
import com.example.read_sphere_server.responseValidator.BookResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private BookRepository bookRepo;
    private final BookMapper bookMapper;

    public Integer save(BookRequest request, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = bookMapper.toBook(request);
        book.setOwner(user);

        return bookRepo.save(book).getId();
    }

    public BookResponse findById(Integer bookId) {
        return bookRepo.findById(bookId)
                .map(bookMapper::toBookResponse)
                .orElseThrow(() -> new EntityNotFoundException("No book found with boo Id: " + bookId));
    }
}
