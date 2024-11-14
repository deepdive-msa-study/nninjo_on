package com.example.msablog.post.controller;

import com.example.msablog.post.dto.CommentUploadRequestDto;
import com.example.msablog.post.dto.PostUploadRequestDto;
import com.example.msablog.post.dto.ResponseDto;
import com.example.msablog.post.service.CommentService;
import com.example.msablog.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final CommentService commentService;

    @GetMapping("/list")
    public ResponseDto findAll(@RequestParam(value = "page", defaultValue = "1") int page,
                               @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return ResponseDto.success(postService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseDto findById(@PathVariable Long id) {
        return ResponseDto.success(postService.findById(id));
    }

    @PostMapping("/upload")
    public ResponseDto writePost(@RequestBody PostUploadRequestDto dto) {
        return ResponseDto.success(postService.savePost(dto));
    }


    @GetMapping("/update/{id}")
    public ResponseDto updatePostForm(@PathVariable Long id) {
        return ResponseDto.success(postService.findById(id));
    }

    @PostMapping("/update/{id}")
    public ResponseDto updatePost(@PathVariable Long id,
                                  @RequestBody PostUploadRequestDto dto) {
        return ResponseDto.success(postService.updatePost(dto, id));
    }

    @GetMapping("/delete/{id}")
    public ResponseDto deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseDto.success();
    }

    // 댓글 작성
    @PostMapping("/{postId}")
    public ResponseDto writeComment(@PathVariable Long postId,
                                    @RequestBody CommentUploadRequestDto commentDto) {
        return ResponseDto.success(commentService.writeComment(postId, commentDto));
    }


    // 댓글 수정
    @PostMapping("/{postId}/{commentId}/update")
    public ResponseDto updateComment(@PathVariable Long postId,
                                     @PathVariable Long commentId,
                                     @RequestBody CommentUploadRequestDto commentDto) {
        return ResponseDto.success(commentService.update(commentId, commentDto));
    }


    // 댓글 삭제
    @GetMapping("/{postId}/{commentId}/delete")
    public ResponseDto deleteComment(@PathVariable Long postId,
                                     @PathVariable Long commentId) {
        commentService.delete(commentId);
        return ResponseDto.success();
    }
}
