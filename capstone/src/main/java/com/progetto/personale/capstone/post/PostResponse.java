package com.progetto.personale.capstone.post;

import lombok.Data;

@Data
public class PostResponse {
    private Long id;
    private String titolo;
    private String descrizione;
    private String imageUrl;
    private String username;

    // Costruttore senza argomenti
    public PostResponse() {
    }

    // Costruttore con argomenti
    public PostResponse(Long id, String titolo, String descrizione, String imageUrl, String username) {
        this.id = id;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.imageUrl = imageUrl;
        this.username = username;
    }

    public PostResponse(String s) {
    }
}
