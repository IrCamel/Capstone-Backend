package com.progetto.personale.capstone.prodotto;

import com.progetto.personale.capstone.categoria.Categoria;
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

    @Column(length = 50, unique = true)
    private String descrizioneProdotto;

    @Column(unique = true)
    private int prezzo;

    @ManyToOne
    private Categoria categoria;



}
