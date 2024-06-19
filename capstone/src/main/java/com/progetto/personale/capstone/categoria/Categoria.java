package com.progetto.personale.capstone.categoria;

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

    @Column(length = 20, unique = true)
    private String nome;

    @OneToMany(mappedBy = "categorie")
    private List<Prodotto> prodotti;

}
