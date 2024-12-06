package com.nninjoon.common.commentservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nninjoon.common.commentservice.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findByPostId(Long postId);

	List<Comment> findByUserId(String userId);

	@Modifying
	@Query("UPDATE Comment p SET p.userId = 'anonymous' WHERE p.userId = :userId")
	void anonymizePostsByUserId(@Param("userId") String userId);

	void deleteByPostId(Long postId);
}
