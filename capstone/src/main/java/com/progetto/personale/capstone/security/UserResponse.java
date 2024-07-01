package com.progetto.personale.capstone.security;

import com.progetto.personale.capstone.post.PostResponse;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class UserResponse {
    private Long id;
    private String nome;
    private String cognome;
    private String username;
    private String email;
    // Aggiungi getter e setter per savedPosts
    @Setter
    @Getter
    private List<PostResponse> savedPosts;

}
