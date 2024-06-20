package com.progetto.personale.capstone.categoria;

import lombok.Data;

@Data
public class Request {
    private String nomeCategoria;

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }
}
