package com.example.read_sphere_server.repo;

import com.example.read_sphere_server.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepo extends JpaRepository<Token, Integer> {

    Optional<Token> findByToken(String token);
}
