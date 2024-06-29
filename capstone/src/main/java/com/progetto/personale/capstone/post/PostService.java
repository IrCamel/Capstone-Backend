package com.progetto.personale.capstone.post;

import com.cloudinary.Cloudinary;
import com.progetto.personale.capstone.security.User;
import com.progetto.personale.capstone.security.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository repository;
    private final UserRepository userRepository;
    private final Cloudinary cloudinary;

    @Value("${app.images.base-url}")
    private String imagesBaseUrl;

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    // FIND ALL
    public List<Post> findAll() {
        return repository.findAll();
    }

    // FIND BY ID
    public PostResponse findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("The given id must not be null");
        }
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Post inesistente");
        }
        Post entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post inesistente"));
        PostResponse postResponse = new PostResponse();
        BeanUtils.copyProperties(entity, postResponse);
        postResponse.setUsername(entity.getUser().getUsername());
        postResponse.setImageUrl(entity.getImgUrl());
        postResponse.setLikeCount(entity.getLikeCount());
        return postResponse;
    }

    // CREATE POST
    @Transactional
    public PostResponse createPost(PostRequest postRequest, MultipartFile file) throws IOException {
        logger.info("Creating post with title: {}", postRequest.getTitolo());
        var uploadResult = cloudinary.uploader().upload(file.getBytes(),
                com.cloudinary.utils.ObjectUtils.asMap("public_id", postRequest.getTitolo() + "_avatar"));
        String url = uploadResult.get("url").toString();

        Post entity = new Post();
        entity.setImgUrl(url);
        BeanUtils.copyProperties(postRequest, entity);

        // Fetching user by ID and setting to post
        User user = userRepository.findById(postRequest.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        entity.setUser(user);

        repository.save(entity);

        PostResponse response = new PostResponse();
        BeanUtils.copyProperties(entity, response);
        response.setUsername(user.getUsername());
        response.setLikeCount(entity.getLikeCount());

        return response;
    }

    // EDIT POST
    public PostResponse editPost(Long id, PostRequest postRequest) {
        if (id == null) {
            throw new IllegalArgumentException("The given id must not be null");
        }
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Post non trovato");
        }
        Post entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post non trovato"));
        BeanUtils.copyProperties(postRequest, entity);
        repository.save(entity);
        PostResponse postResponse = new PostResponse();
        BeanUtils.copyProperties(entity, postResponse);
        postResponse.setUsername(entity.getUser().getUsername());
        postResponse.setImageUrl(entity.getImgUrl());
        postResponse.setLikeCount(entity.getLikeCount());
        return postResponse;
    }

    // DELETE POST
    public String deletePost(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("The given id must not be null");
        }
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return "Post eliminato con successo";
        } else {
            throw new EntityNotFoundException("Post non trovato");
        }
    }

    // GET IMAGE URL
    public String getImageUrl(Long postId) {
        Post post = repository.findById(postId).orElseThrow(() -> new EntityNotFoundException("Post non trovato"));
        return imagesBaseUrl + "/" + post.getImgUrl();
    }

    // TOGGLE LIKE
    @Transactional
    public PostResponse toggleLike(Long postId, Long userId) {
        Post post = repository.findById(postId).orElseThrow(() -> new EntityNotFoundException("Post non trovato"));
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (post.getLikedBy().contains(user)) {
            post.getLikedBy().remove(user);
        } else {
            post.getLikedBy().add(user);
        }
        repository.save(post);

        PostResponse postResponse = new PostResponse();
        BeanUtils.copyProperties(post, postResponse);
        postResponse.setUsername(post.getUser().getUsername());
        postResponse.setImageUrl(post.getImgUrl());
        postResponse.setLikeCount(post.getLikeCount());
        return postResponse;
    }
}
