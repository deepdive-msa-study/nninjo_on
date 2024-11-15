package com.nninjoon.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nninjoon.userservice.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String id);
}
