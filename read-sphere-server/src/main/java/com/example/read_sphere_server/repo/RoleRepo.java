package com.example.read_sphere_server.repo;

import com.example.read_sphere_server.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(String role);
}
