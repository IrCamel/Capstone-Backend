package com.progetto.personale.capstone.prodotto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.progetto.personale.capstone.comment.CommentResponse;
import com.progetto.personale.capstone.post.*;
import com.progetto.personale.capstone.security.UserService;
import com.progetto.personale.capstone.security.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService service;
    private final UserService userService;
    private final PostRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<PostResponse> createPost(@RequestPart("post") String postJson, @RequestPart("file") MultipartFile file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        PostRequest postRequest = objectMapper.readValue(postJson, PostRequest.class);

        System.out.println("Received PostRequest: " + postRequest);
        System.out.println("Received File: " + file.getOriginalFilename());

        return ResponseEntity.ok(service.createPost(postRequest, file));
    }

    @GetMapping("/image-url/{postId}")
    public ResponseEntity<String> getImageUrl(@PathVariable Long postId) {
        return ResponseEntity.ok(service.getImageUrl(postId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> modify(@PathVariable Long id, @RequestBody PostRequest postRequest) {
        return ResponseEntity.ok(service.editPost(id, postRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return ResponseEntity.ok(service.deletePost(id));
    }

    @PutMapping("/{postId}/like/{userId}")
    public ResponseEntity<PostResponse> toggleLike(@PathVariable Long postId, @PathVariable Long userId) {
        return ResponseEntity.ok(service.toggleLike(postId, userId));
    }

    @PutMapping("/{postId}/save/{userId}")
    public ResponseEntity<PostResponse> toggleSave(@PathVariable Long postId, @PathVariable Long userId) {
        return ResponseEntity.ok(service.toggleSave(postId, userId));
    }

    @PostMapping("/{postId}/comment/{userId}")
    public ResponseEntity<CommentResponse> addComment(@PathVariable Long postId, @PathVariable Long userId, @RequestBody String content) {
        return ResponseEntity.ok(service.addComment(postId, userId, content));
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getCommentsByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(service.getCommentsByPostId(postId));
    }

    @GetMapping("/saved/{userId}")
    public ResponseEntity<List<PostResponse>> getSavedPosts(@PathVariable Long userId) {
        logger.info("Received request to get saved posts for user ID: {}", userId);
        List<PostResponse> savedPosts = service.getSavedPostsByUserId(userId);
        logger.info("Returning saved posts: {}", savedPosts);
        return ResponseEntity.ok(savedPosts);
    }

}
