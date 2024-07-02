package com.progetto.personale.capstone.post;

import com.cloudinary.Cloudinary;
import com.progetto.personale.capstone.comment.Comment;
import com.progetto.personale.capstone.comment.CommentRepository;
import com.progetto.personale.capstone.comment.CommentResponse;
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
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository repository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final Cloudinary cloudinary;

    @Value("${app.images.base-url}")
    private String imagesBaseUrl;

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    // FIND ALL
    public List<PostResponse> findAll() {
        return repository.findAll().stream().map(post -> {
            PostResponse postResponse = new PostResponse();
            BeanUtils.copyProperties(post, postResponse);
            postResponse.setUsername(post.getUser().getUsername());
            postResponse.setImageUrl(post.getImgUrl());
            postResponse.setLikeCount(post.getLikeCount());
            postResponse.setLikedBy(post.getLikedBy().stream().map(User::getId).collect(Collectors.toSet()));
            postResponse.setComments(post.getComments().stream().map(comment -> new CommentResponse(
                    comment.getId(), comment.getContent(), comment.getUser().getUsername(), comment.getPost().getId()
            )).collect(Collectors.toList()));
            postResponse.setSavedBy(post.getSavedBy().stream().map(User::getId).collect(Collectors.toSet()));
            return postResponse;
        }).collect(Collectors.toList());
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
        postResponse.setLikedBy(entity.getLikedBy().stream().map(User::getId).collect(Collectors.toSet()));
        postResponse.setComments(entity.getComments().stream().map(comment -> new CommentResponse(
                comment.getId(), comment.getContent(), comment.getUser().getUsername(), comment.getPost().getId()
        )).collect(Collectors.toList()));
        postResponse.setSavedBy(entity.getSavedBy().stream().map(User::getId).collect(Collectors.toSet()));
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
        postResponse.setLikedBy(post.getLikedBy().stream().map(User::getId).collect(Collectors.toSet()));
        postResponse.setComments(post.getComments().stream().map(comment -> new CommentResponse(
                comment.getId(), comment.getContent(), comment.getUser().getUsername(), comment.getPost().getId()
        )).collect(Collectors.toList()));
        postResponse.setSavedBy(post.getSavedBy().stream().map(User::getId).collect(Collectors.toSet()));
        return postResponse;
    }



    // TOGGLE SAVE
    @Transactional
    public PostResponse toggleSave(Long postId, Long userId) {
        Post post = repository.findById(postId).orElseThrow(() -> new EntityNotFoundException("Post non trovato"));
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (post.getSavedBy().contains(user)) {
            post.getSavedBy().remove(user);
        } else {
            post.getSavedBy().add(user);
        }
        repository.save(post);

        PostResponse postResponse = new PostResponse();
        BeanUtils.copyProperties(post, postResponse);
        postResponse.setUsername(post.getUser().getUsername());
        postResponse.setImageUrl(post.getImgUrl());
        postResponse.setLikeCount(post.getLikeCount());
        postResponse.setSavedBy(post.getSavedBy().stream().map(User::getId).collect(Collectors.toSet()));
        return postResponse;
    }

    // ADD COMMENT
    @Transactional
    public CommentResponse addComment(Long postId, Long userId, String content) {
        Post post = repository.findById(postId).orElseThrow(() -> new EntityNotFoundException("Post non trovato"));
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User non trovato"));

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setPost(post);
        comment.setUser(user);

        commentRepository.save(comment);

        return new CommentResponse(comment.getId(), comment.getContent(), user.getUsername(), post.getId());
    }

    // GET COMMENTS BY POST ID
    public List<CommentResponse> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(comment -> new CommentResponse(comment.getId(), comment.getContent(), comment.getUser().getUsername(), comment.getPost().getId()))
                .collect(Collectors.toList());
    }

    // CONVERT TO POST RESPONSE
    private PostResponse convertToPostResponse(Post post) {
        PostResponse postResponse = new PostResponse();
        BeanUtils.copyProperties(post, postResponse);
        postResponse.setUsername(post.getUser().getUsername());
        postResponse.setImageUrl(post.getImgUrl());
        postResponse.setLikeCount(post.getLikeCount());
        postResponse.setSaveCount(post.getSaveCount());
        postResponse.setLikedBy(post.getLikedBy().stream().map(User::getId).collect(Collectors.toSet()));
        postResponse.setSavedBy(post.getSavedBy().stream().map(User::getId).collect(Collectors.toSet()));
        postResponse.setComments(post.getComments().stream().map(comment -> {
            CommentResponse commentResponse = new CommentResponse();
            BeanUtils.copyProperties(comment, commentResponse);
            commentResponse.setUsername(comment.getUser().getUsername());
            return commentResponse;
        }).collect(Collectors.toList()));
        return postResponse;
    }

    public List<PostResponse> getSavedPosts(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<PostResponse> savedPosts = user.getSavedPosts().stream()
                .map(this::convertToPostResponse)
                .collect(Collectors.toList());

        System.out.println("Saved posts for user " + userId + ": " + savedPosts);
        return savedPosts;
    }

    public List<PostResponse> getSavedPostsByUserId(Long userId) {
        logger.info("Finding saved posts for user: {}", userId);
        List<Post> savedPosts = repository.findAllSavedPostsByUserId(userId);
        logger.info("Saved posts for user {}: {}", userId, savedPosts);
        return savedPosts.stream().map(this::convertToPostResponse).collect(Collectors.toList());
    }

}
