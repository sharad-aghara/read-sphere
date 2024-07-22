package com.example.read_sphere_server.repo;

import com.example.read_sphere_server.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {

}
