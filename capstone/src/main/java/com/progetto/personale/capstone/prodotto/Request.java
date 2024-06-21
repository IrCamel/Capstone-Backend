package com.progetto.personale.capstone.prodotto;

import lombok.Data;

@Data
public class Request {
    private String nomeProdotto;
    private String descrizioneProdotto;
    private Integer prezzo;
    private String nomeCategoria;
}