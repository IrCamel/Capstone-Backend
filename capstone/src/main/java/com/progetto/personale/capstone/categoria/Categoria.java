package com.progetto.personale.capstone.categoria;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.progetto.personale.capstone.prodotto.Prodotto;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "categorie")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(length = 50, unique = true)
    private String nomeCategoria;

    @OneToMany(mappedBy = "categoria")
    @JsonBackReference
    private List<Prodotto> prodotti;
}
