package com.progetto.personale.capstone.post;

import lombok.Data;
import java.util.Set;

@Data
public class PostResponse {
    private Long id;
    private String titolo;
    private String descrizione;
    private String imageUrl;
    private String username;
    private int likeCount;
    private Set<Long> likedBy; // Aggiungi questo campo

    public PostResponse() {
    }

    public PostResponse(Long id, String titolo, String descrizione, String imageUrl, String username, int likeCount, Set<Long> likedBy) {
        this.id = id;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.imageUrl = imageUrl;
        this.username = username;
        this.likeCount = likeCount;
        this.likedBy = likedBy;
    }
}
