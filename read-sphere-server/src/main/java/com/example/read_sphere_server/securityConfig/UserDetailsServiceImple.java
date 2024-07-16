package com.example.read_sphere_server.securityConfig;

import com.example.read_sphere_server.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImple implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    @Transactional  // since we want to load user role along with it
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        return userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
    }
}
