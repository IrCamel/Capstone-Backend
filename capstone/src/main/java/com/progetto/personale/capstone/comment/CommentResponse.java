package com.progetto.personale.capstone.comment;

import lombok.Data;

@Data
public class CommentResponse {
    private Long id;
    private String content;
    private String username;
    private Long postId;

    public CommentResponse(Long id, String content, String username, Long postId) {
        this.id = id;
        this.content = content;
        this.username = username;
        this.postId = postId;
    }

    public CommentResponse() {

    }
}