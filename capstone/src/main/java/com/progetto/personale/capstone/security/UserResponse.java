package com.progetto.personale.capstone.security;

import com.progetto.personale.capstone.post.PostResponse;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserResponse {
    private Long id;
    private String nome;
    private String cognome;
    private String username;
    private String email;
    private String avatar;
    @Setter
    @Getter
    private List<PostResponse> savedPosts;
    @Setter
    @Getter
    private int followersCount = 0;
    @Setter
    @Getter
    private int followingCount = 0;
    @Getter
    @Setter
    private List<UserResponse> followers = new ArrayList<>();
    private boolean followedByCurrentUser;

    public UserResponse() {
        this.followersCount = 0;
        this.followingCount = 0;
        this.savedPosts = new ArrayList<>();
        this.followers = new ArrayList<>();
    }
}
