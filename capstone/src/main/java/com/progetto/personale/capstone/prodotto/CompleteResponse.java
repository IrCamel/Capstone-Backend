package com.progetto.personale.capstone.prodotto;

import com.progetto.personale.capstone.categoria.Categoria;
import lombok.Data;

import java.util.List;

@Data
public class CompleteResponse {
    private Long id;
    private String nomeProdotto;
    private String descrizioneProdotto;
    private Integer prezzo;
    private List<String> imgUrl; // Modifica il tipo di imgUrl a List<String>
    private String username;
    private String avatar; // Aggiungi questo campo
    private List<Categoria> categoria;
}
