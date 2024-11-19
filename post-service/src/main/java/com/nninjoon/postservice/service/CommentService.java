package com.example.msablog.post.service;

import com.example.msablog.post.dto.CommentUploadRequestDto;
import com.example.msablog.post.entity.Comment;
import com.example.msablog.post.entity.Post;
import com.example.msablog.post.repository.CommentRepository;
import com.example.msablog.post.repository.PostRepository;
import com.example.msablog.user.config.jwt.JwtUtil;
import com.example.msablog.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public Comment writeComment(Long postId, CommentUploadRequestDto dto) {
        Comment savedComment = commentRepository.save(Comment.builder()
                .content(dto.getContent())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .author(jwtUtil.getCurrentUser())
                .post(
                        postRepository.findById(postId)
                                .orElseThrow(
                                        () -> new RuntimeException("No Such Post to Comment")
                                )
                )
                .build()
        );

        Post post = postRepository.findById(postId)
                .orElseThrow(
                        () -> new RuntimeException("No Such Post to Comment")
                );

        post.getComments().add(savedComment);
        return savedComment;
    }

    @Transactional
    public Comment update(Long commentId, CommentUploadRequestDto commentDto) {
        Comment found = authenticateByUsingJWT(commentId);

        found.setContent(commentDto.getContent());
        found.setUpdatedAt(LocalDateTime.now());
        Comment newlySaved = commentRepository.save(found);

        Post post = found.getPost();
        post.getComments().remove(found);
        post.getComments().add(newlySaved);

        return newlySaved;
    }

    @Transactional
    public void delete(Long commentId) {
        authenticateByUsingJWT(commentId);
        commentRepository.deleteById(commentId);
    }


    @Transactional
    public Comment authenticateByUsingJWT(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new RuntimeException("Commment not found")
        );

        User current = jwtUtil.getCurrentUser();

        if (current.getId().equals(comment.getAuthor().getId())) {
            return comment;
        } else {
            throw new RuntimeException("Current user is not author");
        }
    }
}
