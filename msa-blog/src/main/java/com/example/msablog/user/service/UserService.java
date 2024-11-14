package com.example.msablog.user.service;

import com.example.msablog.user.config.jwt.JwtTokenProvider;
import com.example.msablog.user.config.jwt.JwtUtil;
import com.example.msablog.user.dto.*;
import com.example.msablog.user.entity.User;
import com.example.msablog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtUtil jwtUtil;

    @Transactional
    public ReadUserDto register(RegisterDto dto) {
        if (userRepository.findByEmail(dto.getEmail()) != null) {
            throw new IllegalArgumentException("Email already in use: " + dto.getEmail());
        }
        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();
        userRepository.save(user);

        return ReadUserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .isDeleted(user.getIsDeleted())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public JwtTokenDto login(LoginDto dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        JwtTokenDto jwtTokenDto = jwtTokenProvider.generateToken(authentication);

        return jwtTokenDto;
    }

    public ReadUserDto getMe() {
        User user = jwtUtil.getCurrentUser();

        return ReadUserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .isDeleted(user.getIsDeleted())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    @Transactional
    public ReadUserDto updateMe(UpdateUserDto dto) {
        User user = jwtUtil.getCurrentUser();
        user.setName(dto.getName());
        // TODO: update 속성 추가
        userRepository.save(user);

        return ReadUserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .isDeleted(user.getIsDeleted())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    @Transactional
    public void deleteMe() {
        User user = jwtUtil.getCurrentUser();
        user.setIsDeleted(true);
        userRepository.save(user);
    }

    // 마이페이지에서 병합

    @Transactional(readOnly = true)
    public UserProfileDto getUserProfile(Long id) {
        User found = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("No Such User Found")
        );
        return new UserProfileDto(found.getName(), found.getEmail());
    }

    @Transactional
    public UserProfileDto updateUserProfile(Long id, UserProfileDto dto) {
        User found = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("No Such User Found")
        );

        found.setName(dto.getName());
        User save = userRepository.save(found);

        return new UserProfileDto(save.getName(), save.getEmail());
    }
}
