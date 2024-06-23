package com.progetto.personale.capstone.prodotto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.progetto.personale.capstone.categoria.Categoria;
import com.progetto.personale.capstone.security.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "prodotti")
public class Prodotto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(length = 50, unique = true)
    private String nomeProdotto;

    @Column(length = 250)
    private String descrizioneProdotto;

    @Column(unique = true)
    private Integer prezzo;

    @Column
    private String imgUrl;

    @ManyToOne
    @JoinColumn(name = "nome_categoria")  // Definire la colonna che conterrà il riferimento alla categoria
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "username")
    private User user;

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}

