package com.example.read_sphere_server.service;

import com.example.read_sphere_server.model.Book;
import com.example.read_sphere_server.requestValidation.BookRequest;
import com.example.read_sphere_server.responseValidator.BookResponse;
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
                .shareable(request.sharable())
                .build();
    }

    public BookResponse toBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorName(book.getAuthorName())
                .isbn(book.getIsbn())
                .synopsis(book.getSynopsis())
                .rate(book.getRate())
                .archived(book.isArchived())
                .shareable(book.isShareable())
                .owner(book.getOwner().fullname())
//                .cover(FileUtils.readFileFromLocation(book.getBookCover()))
                .build();
    }
}
