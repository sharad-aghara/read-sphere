package com.example.read_sphere_server.feedback;

import com.example.read_sphere_server.common.PageResponse;
import com.example.read_sphere_server.exception.OperationNotPermittedException;
import com.example.read_sphere_server.model.Book;
import com.example.read_sphere_server.model.Feedback;
import com.example.read_sphere_server.model.User;
import com.example.read_sphere_server.repo.BookRepository;
import com.example.read_sphere_server.repo.FeedbackRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final BookRepository bookRepository;
    private final FeedbackMapper feedbackMapper;
    private final FeedbackRepo feedbackRepository;

    public Integer save(FeedbackRequest request, Authentication connectedUser) {
        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new EntityNotFoundException("No book found with Id: " + request.bookId()));

        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("Can't give feedback on this book, Since it is archived or not shareable.");
        }

        User user = ((User) connectedUser.getPrincipal());
        // check is borrower is owner of the book
        if (Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You can't feedback your own book.");
        }

        Feedback feedback = feedbackMapper.tofeedback(request);

        return feedbackRepository.save(feedback).getId();
    }

    public PageResponse<FeedbackResponse> findAllFeedbacksByBook(int bookId, int page, int size, Authentication connectedUser) {
        Pageable pageable = PageRequest.of(page, size);

        User user = ((User) connectedUser.getPrincipal());
        Page<Feedback> feedbacks = feedbackRepository.findAllbyBookId(bookId, pageable);
        List<FeedbackResponse> feedbackResponses = feedbacks.stream()
                .map(f -> feedbackMapper.toFeedbackResponse(f, user.getId()))
                .toList();

        return new PageResponse<>(
                feedbackResponses,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                feedbacks.isFirst(),
                feedbacks.isLast()
        );
    }
}
