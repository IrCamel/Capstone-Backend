package com.progetto.personale.capstone.post;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(length = 50, unique = true)
    private String titolo;

    @Column(length = 50, unique = true)
    private String descrizione;
}
