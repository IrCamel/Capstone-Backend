package com.progetto.personale.capstone.categoria;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "categorie")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(length = 20, unique = true)
    private String nome;

}
