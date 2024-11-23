package com.nninjoon.postservice.service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nninjoon.postservice.dto.CommentResponse;
import com.nninjoon.postservice.dto.PostDetailResponse;
import com.nninjoon.postservice.dto.PostPersistResponse;
import com.nninjoon.postservice.dto.PostUploadRequest;
import com.nninjoon.postservice.dto.PostResponse;
import com.nninjoon.postservice.entity.Comment;
import com.nninjoon.postservice.entity.Hashtag;
import com.nninjoon.postservice.entity.Post;
import com.nninjoon.postservice.entity.PostHashtag;
import com.nninjoon.postservice.repository.HashtagRepository;
import com.nninjoon.postservice.repository.PostHashtagRepository;
import com.nninjoon.postservice.repository.PostJQueryRepository;
import com.nninjoon.postservice.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostJQueryRepository postJQueryRepository;
    private final HashtagRepository hashtagRepository;
    private final PostHashtagRepository postHashtagRepository;
    @Transactional(readOnly = true)
    public Page<PostResponse> findAll(Pageable pageable) {
        return postJQueryRepository.findAll(pageable);
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

        List<String> hashtags = new ArrayList<>();
        // for (PostHashtag postHashtag : post.getPostHashtags()) {
        //     hashtags.add(postHashtag.getHashtag().getName());
        // }
        //
        // List<CommentResponse> comments = new ArrayList<>();
        // for (Comment comment : post.getComments()) {
        //     comments.add(CommentResponse.from(comment));
        // }

        return PostDetailResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author("author")
                .createdAt(post.getCreatedAt().format(formatter))
                // .hashtags(hashtags)
                // .comments(comments)
                .build();
    }

    // 글 작성
    @Transactional
    public PostPersistResponse savePost(PostUploadRequest request, String userId) {
        Post post = Post.create(request.title(), request.content(), userId);

        // List<String> hashtags = request.hashtags();
        // saveHashtags(hashtags, post.getId());

        postRepository.save(post);

        return PostPersistResponse.from(post);
    }

    // 해시태그 저장 및 연결
    // @Transactional
    // public void saveHashtags(List<String> hashtags, Long postId) {
    //     for (String hashtag : hashtags) {
    //         Optional<Hashtag> foundHashtag = hashtagRepository.findByName(hashtag);
    //         Hashtag tag = foundHashtag.orElseGet(
    //                 () -> hashtagRepository.save(
    //                         Hashtag.builder()
    //                                 .name(hashtag)
    //                                 .build()
    //                 ));
    //         connectByUsingPostHashtag(postId, tag);
    //     }
    //
    // }

    // private void connectByUsingPostHashtag(Long postId, Hashtag tag) {
    //     PostHashtag postHashtag = PostHashtag.builder()
    //             .post(postRepository.findById(postId)
    //                     .orElseThrow(() -> new RuntimeException("Hashtag not found")))
    //             .hashtag(tag)
    //             .build();
    //     postHashtagRepository.save(postHashtag);
    //
    //     tag.getPostHashtags().add(postHashtag);
    // }

    @Transactional
    public PostDetailResponse updatePost(PostUploadRequest request, Long postId, String userId) {
        Post post = getPost(postId);
        validateUserAuthorization(post.getUserId(), userId);

        post.updateTitle(request.title());
        post.updateContent(request.content());

        // saveHashtags(request.hashtags(), post.getId());

        return postToPostDetailDto(post);
    }

    @Transactional
    public void deletePost(Long postId, String userId) {
        Post post = getPost(postId);
        validateUserAuthorization(post.getUserId(), userId);

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
