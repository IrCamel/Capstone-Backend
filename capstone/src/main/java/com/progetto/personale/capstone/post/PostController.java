package com.progetto.personale.capstone.prodotto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.progetto.personale.capstone.post.*;
import com.progetto.personale.capstone.security.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService service;
    private final UserService userService;
    private final PostRepository repository;

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> findAll(){
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
    public ResponseEntity<PostResponse> modify(@PathVariable Long id, @RequestBody PostRequest PostRequest){
        return ResponseEntity.ok(service.editPost(id, PostRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        return ResponseEntity.ok(service.deletePost(id));
    }

    @PutMapping("/{postId}/like/{userId}")
    public ResponseEntity<PostResponse> toggleLike(@PathVariable Long postId, @PathVariable Long userId) {
        return ResponseEntity.ok(service.toggleLike(postId, userId));
    }
}
