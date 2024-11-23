package com.nninjoon.common.commentservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nninjoon.common.commentservice.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findByPostId(Long postId);

	List<Comment> findByUserId(String userId);
}
