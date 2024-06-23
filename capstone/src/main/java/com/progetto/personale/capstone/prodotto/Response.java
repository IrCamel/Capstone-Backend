package com.progetto.personale.capstone.prodotto;

import com.progetto.personale.capstone.categoria.Categoria;
import lombok.Data;

import java.util.List;

@Data
public class Response {
    private Long id;
    private String nomeProdotto;
    private String descrizioneProdotto;
    private Integer prezzo;
    private String imgUrl;
    private String username; // Aggiungi questo campo
    private List<Categoria> categoria;
}
