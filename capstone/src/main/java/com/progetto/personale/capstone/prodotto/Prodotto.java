package com.progetto.personale.capstone.prodotto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.progetto.personale.capstone.categoria.Categoria;
import jakarta.persistence.*;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringExclude;

import java.util.List;


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

    @ManyToOne
    @ToStringExclude
    @JsonIgnoreProperties("prodotto")
    private List<Categoria> categorie;



}
