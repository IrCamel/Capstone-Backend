package com.progetto.personale.capstone.post;

import lombok.Data;

@Data
public class PostRequest {
    private String titolo;
    private String descrizione;
    private Long userId;  // Assicurati che userId sia incluso
}
