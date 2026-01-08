package com.example.library.config;

import com.example.library.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserPrincipal loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .map(UserPrincipal::new)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}