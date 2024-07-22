package com.example.read_sphere_server;

import com.example.read_sphere_server.model.Role;
import com.example.read_sphere_server.repo.RoleRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableAsync
public class ReadSphereServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReadSphereServerApplication.class, args);
	}

	// if Role table doesn't have USER role inside it, this commandline runner will add a USER role inside table
	@Bean
	public CommandLineRunner runner(RoleRepo roleRepository) {
		return args -> {
			if (roleRepository.findByName("USER").isEmpty()) {
				roleRepository.save(
						Role.builder().name("USER").build()
				);
			}
		};
	}
}
