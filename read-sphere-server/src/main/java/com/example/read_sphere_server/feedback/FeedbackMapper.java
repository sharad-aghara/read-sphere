package com.example.read_sphere_server.feedback;

import com.example.read_sphere_server.model.Book;
import com.example.read_sphere_server.model.Feedback;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FeedbackMapper {
    public Feedback tofeedback(FeedbackRequest request) {
        return Feedback.builder()
                .note(request.note())
                .comment(request.comment())
                .book(Book.builder()
                        .id(request.bookId())
                        .archived(false)    // not required / no impact :: justo satisfy lombok
                        .shareable(false)   // not required / no impact :: justo satisfy lombok
                        .build()
                )
                .build()
                ;
    }

    public FeedbackResponse toFeedbackResponse(Feedback feedback, Integer id) {
        return FeedbackResponse.builder()
                .note(feedback.getNote())
                .comment(feedback.getComment())
                .ownFeedback(Objects.equals(feedback.getCreatedBy(), id))   // Current user's (self) feedback
                .build()
                ;
    }
}
