package com.nninjoon.common.commentservice.service;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nninjoon.common.commentservice.domain.Comment;
import com.nninjoon.common.commentservice.model.CommentRequest;
import com.nninjoon.common.commentservice.model.CommentPersistResponse;
import com.nninjoon.common.commentservice.model.CommentResponse;
import com.nninjoon.common.commentservice.repository.CommentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    @Transactional
    public CommentPersistResponse create(Long postId, CommentRequest request, String userId) {
        Comment comment = Comment.create(request.content(), userId, postId);
        commentRepository.save(comment);

        return CommentPersistResponse.from(comment);
    }

    @Transactional
    public void update(Long commentId, CommentRequest request, String userId) {
        Comment comment = validateCommentAuthor(userId, commentId);
        comment.update(request.content());
    }

    @Transactional
    public void delete(Long commentId, String userId) {
        Comment comment = validateCommentAuthor(userId, commentId);
        commentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getComments(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
            .map(CommentResponse::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByUserId(String userId) {
        List<Comment> comments = commentRepository.findByUserId(userId);
        return comments.stream()
            .map(CommentResponse::from)
            .toList();
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
