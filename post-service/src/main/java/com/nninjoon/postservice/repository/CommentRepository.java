package com.nninjoon.postservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.msablog.post.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
