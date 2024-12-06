package com.nninjoon.postservice.service;

import static com.nninjoon.postservice.messagequeue.producer.model.Type.POST_DELETED;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nninjoon.postservice.client.CommentServiceClient;
import com.nninjoon.postservice.client.model.CommentResponse;
import com.nninjoon.postservice.dto.PostDetailResponse;
import com.nninjoon.postservice.dto.PostPersistResponse;
import com.nninjoon.postservice.dto.PostUploadRequest;
import com.nninjoon.postservice.dto.PostResponse;
import com.nninjoon.postservice.entity.Post;
import com.nninjoon.postservice.messagequeue.producer.PostProducer;
import com.nninjoon.postservice.repository.PostJQueryRepository;
import com.nninjoon.postservice.repository.PostRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostJQueryRepository postJQueryRepository;
    private final CommentServiceClient commentServiceClient;
    private final CircuitBreakerFactory circuitBreakerFactory;
    private final PostProducer postProducer;

    @Transactional(readOnly = true)
    public Page<PostResponse> findAll(Pageable pageable) {
        return postJQueryRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> findAllByUserId(String userId) {
        List<Post> getPosts = postRepository.findByUserId(userId);

        return getPosts.stream()
            .map(PostResponse::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public PostDetailResponse findById(Long postId) {
        Post post = getPost(postId);
        return postToPostDetailDto(post);
    }

    @Transactional(readOnly = true)
    public PostDetailResponse findById(Long postId, String userId) {
        Post post = getPost(postId);
        validateUserAuthorization(post.getUserId(), userId);

        return postToPostDetailDto(post);
    }

    private PostDetailResponse postToPostDetailDto(Post post) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        List<CommentResponse> comments = circuitBreaker.run(() ->commentServiceClient.getComments(post.getId()),
            throwable -> {
                log.error("Failed to fetch comments for postId: {}", post.getId(), throwable);
                return new ArrayList<>();
            });

        return PostDetailResponse.of(
            post.getTitle(),
            post.getContent(),
            post.getUserId(),
            post.getCreatedAt().format(formatter),
            comments
        );
    }

    // 글 작성
    @Transactional
    public PostPersistResponse savePost(PostUploadRequest request, String userId) {
        Post post = Post.create(request.title(), request.content(), userId);
        return PostPersistResponse.from(postRepository.save(post));
    }

    @Transactional
    public PostDetailResponse updatePost(PostUploadRequest request, Long postId, String userId) {
        Post post = getPost(postId);
        validateUserAuthorization(post.getUserId(), userId);

        post.updateTitle(request.title());
        post.updateContent(request.content());

        return postToPostDetailDto(post);
    }

    @Transactional
    public void deletePost(Long postId, String userId) {
        Post post = getPost(postId);
        validateUserAuthorization(post.getUserId(), userId);

        postProducer.sendMessage(POST_DELETED, postId);
        postRepository.delete(post);
    }

    private void validateUserAuthorization(String authorId, String userId) {
        if (!authorId.equals(userId)) {
            throw new RuntimeException("권한이 없습니다.");
        }
    }

    private Post getPost(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(()-> new RuntimeException("Post not found."));
    }
}
