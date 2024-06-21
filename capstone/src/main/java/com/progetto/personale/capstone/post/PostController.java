package com.progetto.personale.capstone.prodotto;

import com.progetto.personale.capstone.post.*;
import com.progetto.personale.capstone.security.User;
import com.progetto.personale.capstone.security.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<Post>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost( @Valid @RequestBody PostRequest postRequest){
        PostResponse postResponse = service.createPost(postRequest);
        return ResponseEntity.ok(postResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> modify(@PathVariable Long id, @RequestBody PostRequest PostRequest){
        return ResponseEntity.ok(service.editPost(id, PostRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        return ResponseEntity.ok(service.deletePost(id));
    }
}

