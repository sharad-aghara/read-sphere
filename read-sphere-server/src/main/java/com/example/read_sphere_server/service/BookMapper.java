package com.example.read_sphere_server.service;

import com.example.read_sphere_server.model.Book;
import com.example.read_sphere_server.requestValidation.BookRequest;
import org.springframework.stereotype.Service;

@Service
public class BookMapper {


    public Book toBook(BookRequest request) {
        return Book.builder()
                .id(request.id())
                .title(request.title())
                .authorName(request.authorName())
                .synopsis(request.synopsis())
                .archived(false)
                .sharable(request.sharable())
                .build();
    }
}
