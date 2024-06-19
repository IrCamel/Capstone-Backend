package com.progetto.personale.capstone.prodotto;

import lombok.Data;

import java.util.List;

@Data
public class Request {
    private String nomeProdotto;
    private String descrizioneProdotto;
    private Integer prezzo;
    private List<Long> idCategorie;
}
