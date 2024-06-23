package com.progetto.personale.capstone.post;

import lombok.Data;

@Data
public class PostResponse {
    private Long id;
    private String titolo;
    private String descrizione;
    private String imgUrl;
    private String username;
}
