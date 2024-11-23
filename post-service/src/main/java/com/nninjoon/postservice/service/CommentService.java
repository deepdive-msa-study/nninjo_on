package com.nninjoon.postservice.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nninjoon.postservice.dto.CommentPersistResponse;
import com.nninjoon.postservice.dto.CommentUploadRequest;
import com.nninjoon.postservice.entity.Comment;
import com.nninjoon.postservice.entity.Post;
import com.nninjoon.postservice.repository.CommentRepository;
import com.nninjoon.postservice.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public CommentPersistResponse writeComment(Long postId, CommentUploadRequest request, String userId) {
        Comment comment = Comment.create(request.content(), userId, postId);
        commentRepository.save(comment);

        return CommentPersistResponse.from(comment);
    }

    @Transactional
    public void update(Long commentId, CommentUploadRequest request, String userId) {
        Comment comment = validateCommentAuthor(userId, commentId);
        comment.update(request.content());
    }

    @Transactional
    public void delete(Long commentId, String userId) {
        Comment comment = validateCommentAuthor(userId, commentId);
        commentRepository.delete(comment);
    }


    private Comment validateCommentAuthor(String userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new RuntimeException("Comment not found")
        );

        if (userId.equals(comment.getUserId())) {
            return comment;
        } else {
            throw new RuntimeException("Current user is not author");
        }
    }
}
