package com.example.read_sphere_server.repo;

import com.example.read_sphere_server.model.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FeedbackRepo extends JpaRepository<Feedback, Integer> {

    @Query("""
            SELECT feedback
            FROM Feedback feedback
            WHERE feedback.book.id = :bookid
            """)
    Page<Feedback> findAllbyBookId(int bookId, Pageable pageable);
}
