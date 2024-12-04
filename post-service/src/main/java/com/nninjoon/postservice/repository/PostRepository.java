package com.nninjoon.postservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nninjoon.postservice.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	List<Post> findByUserId(String userId);

	@Modifying
	@Query("UPDATE Post p SET p.userId = 'anonymous' WHERE p.userId = :userId")
	void anonymizePostsByUserId(@Param("userId") String userId);
}
