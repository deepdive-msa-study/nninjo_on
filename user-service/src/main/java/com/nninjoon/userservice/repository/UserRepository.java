package com.nninjoon.userservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nninjoon.userservice.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);

	Optional<User> findByUserId(String userId);
}
