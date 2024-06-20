package com.progetto.personale.capstone.prodotto;

import com.progetto.personale.capstone.categoria.Categoria;
import lombok.Data;

import java.util.List;

@Data
public class Response {
    private Long id;
    private String nomeProdotto;
    private List<Categoria> nomeCategoria;
}
