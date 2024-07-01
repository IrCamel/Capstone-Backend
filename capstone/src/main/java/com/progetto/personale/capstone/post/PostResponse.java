package com.progetto.personale.capstone.post;

import com.progetto.personale.capstone.comment.CommentResponse;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.List;

@Data
public class PostResponse {
    private Long id;
    private String titolo;
    private String descrizione;
    private String imageUrl;
    private String username;
    private int likeCount;
    private Set<Long> likedBy;
    private List<CommentResponse> comments;
    @Setter
    @Getter
    private boolean savedByCurrentUser;  // Aggiungi questo campo
    @Setter
    private int saveCount; // Aggiungi questo campo
    @Setter
    private Set<Long> savedBy;

    public PostResponse() {
    }

    public PostResponse(Long id, String titolo, String descrizione, String imageUrl, String username, int likeCount, Set<Long> likedBy, List<CommentResponse> comments) {
        this.id = id;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.imageUrl = imageUrl;
        this.username = username;
        this.likeCount = likeCount;
        this.likedBy = likedBy;
        this.comments = comments;
    }

}
