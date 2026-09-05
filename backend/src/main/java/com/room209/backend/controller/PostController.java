package com.room209.backend.controller;

import com.room209.backend.dto.CommentDto;
import com.room209.backend.dto.CommentRequest;
import com.room209.backend.dto.PostCreateRequest;
import com.room209.backend.dto.PostDto;
import com.room209.backend.entity.Post;
import com.room209.backend.entity.User;
import com.room209.backend.service.AuthService;
import com.room209.backend.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms/{roomId}/posts")
public class PostController {

    private final PostService postService;
    private final AuthService authService;

    public PostController(PostService postService, AuthService authService) {
        this.postService = postService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<List<PostDto>> getFeed(
            @PathVariable Long roomId,
            @RequestParam(required = false, defaultValue = "ALL") Post.Category category) {
        return ResponseEntity.ok(postService.getFeed(roomId, category));
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(
            @PathVariable Long roomId,
            @RequestBody PostCreateRequest request) {
        User currentUser = authService.getCurrentUser();
        return ResponseEntity.ok(postService.createPost(roomId, currentUser, request));
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentDto>> getComments(
            @PathVariable Long roomId,
            @PathVariable Long postId) {
        return ResponseEntity.ok(postService.getComments(postId));
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentDto> addComment(
            @PathVariable Long roomId,
            @PathVariable Long postId,
            @RequestBody CommentRequest request) {
        User currentUser = authService.getCurrentUser();
        return ResponseEntity.ok(postService.addComment(postId, currentUser, request));
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<PostDto> toggleLike(
            @PathVariable Long roomId,
            @PathVariable Long postId) {
        return ResponseEntity.ok(postService.toggleLike(postId));
    }
}
