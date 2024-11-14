package com.example.msablog.post.service;

import com.example.msablog.post.dto.CommentResponseDto;
import com.example.msablog.post.dto.PostDetailResponseDto;
import com.example.msablog.post.dto.PostUploadRequestDto;
import com.example.msablog.post.dto.ReadPostResponseDto;
import com.example.msablog.post.entity.Comment;
import com.example.msablog.post.entity.Hashtag;
import com.example.msablog.post.entity.Post;
import com.example.msablog.post.entity.PostHashtag;
import com.example.msablog.post.repository.HashtagRepository;
import com.example.msablog.post.repository.PostHashtagRepository;
import com.example.msablog.post.repository.PostJQueryRepository;
import com.example.msablog.post.repository.PostRepository;
import com.example.msablog.user.config.jwt.JwtUtil;
import com.example.msablog.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostJQueryRepository postJQueryRepository;
    private final HashtagRepository hashtagRepository;
    private final PostHashtagRepository postHashtagRepository;
    private final JwtUtil jwtUtil;

    @Transactional(readOnly = true)
    public Page<ReadPostResponseDto> findAll(Pageable pageable) {
        return postJQueryRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public PostDetailResponseDto findById(Long id) {
        return postToPostDetailDto(authenticateByUsingJWT(id));
    }

    @Transactional
    public Post authenticateByUsingJWT(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new RuntimeException("Post not found")
        );

        User current = jwtUtil.getCurrentUser();

        if (current.getId().equals(post.getAuthor().getId())) {
            return post;
        } else {
            throw new RuntimeException("Current user is not author");
        }
    }

    private static PostDetailResponseDto postToPostDetailDto(Post post) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<String> hashtags = new ArrayList<>();
        for (PostHashtag postHashtag : post.getPostHashtags()) {
            hashtags.add(postHashtag.getHashtag().getName());
        }

        List<CommentResponseDto> comments = new ArrayList<>();
        for (Comment comment : post.getComments()) {
            comments.add(
                    CommentResponseDto.builder()
                            .id(comment.getId())
                            .content(comment.getContent())
                            .createdAt(comment.getCreatedAt().format(formatter))
                            .build()
            );
        }

        return PostDetailResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getAuthor().getName())
                .createdAt(post.getCreatedAt().format(formatter))
                .hashtags(hashtags)
                .comments(comments)
                .build();
    }

    // 글 작성
    @Transactional
    public PostDetailResponseDto savePost(PostUploadRequestDto dto) {
        User user = jwtUtil.getCurrentUser();

        Post post = Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .author(user)
                .hits(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        ArrayList<String> hashtags = dto.getHashtags();
        saveHashtags(hashtags, post.getId());

        postRepository.save(post);

        return postToPostDetailDto(post);
    }

    // 해시태그 저장 및 연결
    @Transactional
    public void saveHashtags(ArrayList<String> hashtags, Long postId) {
        for (String hashtag : hashtags) {
            Optional<Hashtag> foundHashtag = hashtagRepository.findByName(hashtag);
            Hashtag tag = foundHashtag.orElseGet(
                    () -> hashtagRepository.save(
                            Hashtag.builder()
                                    .name(hashtag)
                                    .build()
                    ));
            connectByUsingPostHashtag(postId, tag);
        }

    }

    private void connectByUsingPostHashtag(Long postId, Hashtag tag) {
        PostHashtag postHashtag = PostHashtag.builder()
                .post(postRepository.findById(postId)
                        .orElseThrow(() -> new RuntimeException("Hashtag not found")))
                .hashtag(tag)
                .build();
        postHashtagRepository.save(postHashtag);

        tag.getPostHashtags().add(postHashtag);
    }

    // 바뀐 포스트 저장
    @Transactional
    public PostDetailResponseDto updatePost(PostUploadRequestDto dto, Long id) {
        Post post = authenticateByUsingJWT(id);

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setUpdatedAt(LocalDateTime.now());
        postRepository.save(post);

        saveHashtags(dto.getHashtags(), post.getId());

        return postToPostDetailDto(post);
    }

    // 포스트 삭제
    @Transactional
    public void deletePost(Long id) {
        authenticateByUsingJWT(id);
        postRepository.deleteById(id);
    }

}
