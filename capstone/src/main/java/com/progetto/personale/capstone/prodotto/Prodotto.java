package com.progetto.personale.capstone.prodotto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.progetto.personale.capstone.categoria.Categoria;
import com.progetto.personale.capstone.security.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "prodotti")
public class Prodotto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(length = 50)
    private String nomeProdotto;

    @Column(length = 500)
    private String descrizioneProdotto;

    @Column
    private Integer prezzo;

    @NotNull
    @ElementCollection
    private List<String> imgUrl = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "nome_categoria")
    @JsonIgnoreProperties("prodotti") // Prevent reference loop
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "username")
    @JsonIgnoreProperties("prodotti") // Prevent reference loop
    private User user;

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
