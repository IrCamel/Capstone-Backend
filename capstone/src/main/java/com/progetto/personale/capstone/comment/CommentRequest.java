package com.progetto.personale.capstone.comment;

import lombok.Data;

@Data
public class CommentRequest {
    private String content;
    private Long userId;
}
