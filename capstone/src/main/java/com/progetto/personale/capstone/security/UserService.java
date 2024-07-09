package com.progetto.personale.capstone.security;

import com.cloudinary.Cloudinary;
import com.progetto.personale.capstone.comment.CommentResponse;
import com.progetto.personale.capstone.email.EmailService;
import com.cloudinary.utils.ObjectUtils;
import com.progetto.personale.capstone.post.Post;
import com.progetto.personale.capstone.post.PostRepository;
import com.progetto.personale.capstone.post.PostResponse;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final AuthenticationManager auth;
    private final JwtUtils jwt;
    private final EmailService emailService;
    private final Cloudinary cloudinary;
    private final PostRepository postRepository;

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    @Value("${app.images.base-url}")
    private String imagesBaseUrl;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(PasswordEncoder encoder,
                       UserRepository userRepository,
                       RolesRepository rolesRepository,
                       AuthenticationManager auth,
                       JwtUtils jwt,
                       EmailService emailService,
                       Cloudinary cloudinary,
                       PostRepository postRepository) {
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
        this.auth = auth;
        this.jwt = jwt;
        this.emailService = emailService;
        this.cloudinary = cloudinary;
        this.postRepository = postRepository;
    }

    public Optional<LoginResponseDTO> login(String username, String password) {
        try {
            var a = auth.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(a);

            var user = userRepository.findOneByUsername(username).orElseThrow();
            var dto = LoginResponseDTO.builder()
                    .withUser(RegisteredUserDTO.builder()
                            .withId(user.getId())
                            .withRoles(user.getRoles())
                            .withUsername(user.getUsername())
                            .withAvatar(user.getAvatar())
                            .build())
                    .withToken(jwt.generateToken(a))
                    .build();

            return Optional.of(dto);
        } catch (NoSuchElementException | AuthenticationException e) {
            logger.error("Authentication failed", e);
            throw new InvalidLoginException(username, password);
        }
    }

    @Transactional
    public RegisteredUserDTO register(RegisterUserDTO register, MultipartFile file) throws IOException {
        logger.info("Registering user: {}", register.getUsername());
        var uploadResult = cloudinary.uploader().upload(file.getBytes(),
                com.cloudinary.utils.ObjectUtils.asMap("public_id", register.getNome() + "avatar"));
        String url = uploadResult.get("url").toString();

        if (userRepository.existsByUsername(register.getUsername())) {
            throw new EntityExistsException("Utente già esistente");
        }
        if (userRepository.existsByEmail(register.getEmail())) {
            throw new EntityExistsException("Email già registrata");
        }

        Roles roles = rolesRepository.findById(Roles.ROLES_USER).get();
        User u = new User();
        u.setAvatar(url);
        BeanUtils.copyProperties(register, u);
        u.setPassword(encoder.encode(register.getPassword()));
        u.getRoles().add(roles);
        userRepository.save(u);

        RegisteredUserDTO response = new RegisteredUserDTO();
        BeanUtils.copyProperties(u, response);
        response.setRoles(List.of(roles));
        emailService.sendWelcomeEmail(u.getEmail());

        return response;
    }

    public RegisteredUserDTO registerAdmin(RegisterUserDTO register) {
        if (userRepository.existsByUsername(register.getUsername())) {
            throw new EntityExistsException("Utente gia' esistente");
        }
        if (userRepository.existsByEmail(register.getEmail())) {
            throw new EntityExistsException("Email gia' registrata");
        }
        Roles roles = rolesRepository.findById(Roles.ROLES_ADMIN).get();
        User u = new User();
        BeanUtils.copyProperties(register, u);
        u.setPassword(encoder.encode(register.getPassword()));
        u.getRoles().add(roles);
        userRepository.save(u);
        RegisteredUserDTO response = new RegisteredUserDTO();
        BeanUtils.copyProperties(u, response);
        response.setRoles(List.of(roles));
        return response;

    }

    public UserResponse findById(Long id, Long currentUserId) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User entity = optionalUser.get();
            UserResponse userResponse = new UserResponse();
            BeanUtils.copyProperties(entity, userResponse);
            userResponse.setFollowersCount(entity.getFollowers().size());
            userResponse.setFollowingCount(entity.getFollowing().size());
            userResponse.setAvatar(entity.getAvatar()); // Aggiungi questa linea
            return userResponse;
        } else {
            throw new EntityNotFoundException("L'utente con id " + id + " non è stato trovato");
        }
    }

    public String deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return "Utente eliminato con successo";
        } else {
            throw new EntityExistsException("L'utente con id +" + id + "non è stato trovato");
        }
    }

    @Transactional
    public String uploadAvatar(Long id, MultipartFile image) throws IOException {
        long maxFileSize = getMaxFileSizeInBytes();
        if (image.getSize() > maxFileSize) {
            throw new FileSizeExceededException("File size exceeds the maximum allowed size");
        }

        Optional<User> optionalUser = userRepository.findById(id);
        User user = optionalUser.orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        String existingPublicId = user.getAvatar();
        if (existingPublicId != null && !existingPublicId.isEmpty()) {
            cloudinary.uploader().destroy(existingPublicId, ObjectUtils.emptyMap());
        }

        Map<String, Object> uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
        String publicId = (String) uploadResult.get("public_id");
        String url = (String) uploadResult.get("url");

        user.setAvatar(publicId);
        userRepository.save(user);

        return url;
    }

    @Transactional
    public String deleteAvatar(Long id) throws IOException {
        Optional<User> optionalUser = userRepository.findById(id);
        User user = optionalUser.orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        String publicId = user.getAvatar();
        if (publicId != null && !publicId.isEmpty()) {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            user.setAvatar(null);
            userRepository.save(user);
            return "Avatar deleted successfully";
        } else {
            return "No avatar found for deletion";
        }
    }

    @Transactional
    public String updateAvatar(Long id, MultipartFile updatedImage) throws IOException {
        deleteAvatar(id);
        return uploadAvatar(id, updatedImage);
    }

    public long getMaxFileSizeInBytes() {
        String[] parts = maxFileSize.split("(?i)(?<=[0-9])(?=[a-z])");
        long size = Long.parseLong(parts[0]);
        String unit = parts[1].toUpperCase();
        switch (unit) {
            case "KB":
                size *= 1024;
                break;
            case "MB":
                size *= 1024 * 1024;
                break;
            case "GB":
                size *= 1024 * 1024 * 1024;
                break;
        }
        return size;
    }

    public List<PostResponse> getSavedPosts(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return user.getSavedPosts().stream()
                .map(this::convertToPostResponse)
                .collect(Collectors.toList());
    }

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

    @Transactional
    public UserResponse followUser(Long followerId, Long followeeId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new EntityNotFoundException("Follower not found with id: " + followerId));
        User followee = userRepository.findById(followeeId)
                .orElseThrow(() -> new EntityNotFoundException("Followee not found with id: " + followeeId));

        if (follower.getFollowing().contains(followee)) {
            follower.getFollowing().remove(followee);
            followee.getFollowers().remove(follower);
        } else {
            follower.getFollowing().add(followee);
            followee.getFollowers().add(follower);
        }

        userRepository.save(follower);
        userRepository.save(followee);

        return convertToUserResponse(followee, followerId);
    }

    @Transactional
    public FollowResponseDTO toggleFollow(Long followerId, Long followeeId) {
        if (followerId.equals(followeeId)) {
            throw new IllegalArgumentException("A user cannot follow themselves.");
        }

        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new EntityNotFoundException("Follower not found"));
        User followee = userRepository.findById(followeeId)
                .orElseThrow(() -> new EntityNotFoundException("Followee not found"));

        boolean isFollowing = follower.getFollowing().contains(followee);
        if (isFollowing) {
            follower.getFollowing().remove(followee);
            followee.getFollowers().remove(follower);
        } else {
            follower.getFollowing().add(followee);
            followee.getFollowers().add(follower);
        }

        userRepository.save(follower);
        userRepository.save(followee);

        int followersCount = followee.getFollowers().size();
        int followingCount = follower.getFollowing().size();

        logger.info("User {} now has {} followers and {} following", followee.getUsername(), followersCount, followingCount);

        return new FollowResponseDTO(followersCount, followingCount, !isFollowing);
    }


    public List<UserResponse> getFollowers(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return user.getFollowers().stream()
                .map(follower -> convertToUserResponse(follower, userId))
                .collect(Collectors.toList());
    }

    public List<UserResponse> getFollowing(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return user.getFollowing().stream()
                .map(following -> convertToUserResponse(following, userId))
                .collect(Collectors.toList());
    }

    private UserResponse convertToUserResponse(User user, Long currentUserId) {
        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(user, userResponse);
        userResponse.setFollowersCount(user.getFollowers().size());
        userResponse.setFollowingCount(user.getFollowing().size());
        userResponse.setFollowedByCurrentUser(user.getFollowers().stream()
                .anyMatch(follower -> follower.getId().equals(currentUserId)));
        return userResponse;
    }
}
