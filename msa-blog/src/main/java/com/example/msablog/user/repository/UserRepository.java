package com.example.msablog.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.msablog.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String id);
}