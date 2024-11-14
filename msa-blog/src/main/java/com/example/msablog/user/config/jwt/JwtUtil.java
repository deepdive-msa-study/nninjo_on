package com.example.msablog.user.config.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.msablog.user.entity.User;
import com.example.msablog.user.repository.UserRepository;
import com.example.msablog.user.service.custom.CustomUserDetails;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {
    private final UserRepository userRepository;

    public JwtUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long getCurrentUserId() {
        CustomUserDetails userDetails = getCurrentCustomUserDetails();
        return userDetails.getUserId();
    }
    
    public String getCurrentUserEmail() {
        Authentication authentication = getAuthentication();
        return authentication.getName();
    }

    public User getCurrentUser() {
        String email = getCurrentUserEmail();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            log.error("User not found");
            new UsernameNotFoundException("User not found");
        }
        return user;
    }

    private Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            log.error("No authentication information");
            throw new RuntimeException("No authentication information.");
        }
        return authentication;
    }

    private CustomUserDetails getCurrentCustomUserDetails() {
        return (CustomUserDetails) getAuthentication().getPrincipal();
    }
}
