package com.progetto.personale.capstone.user;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long Id;

    @Column(length = 50, unique = true)
    private String nome;

    @Column(length = 50, unique = true)
    private String cognome;

    @Column(unique = true)
    private int eta;

    @Column(length = 50, unique = true)
    private String email;

    @Column(length = 50, unique = true)
    private String password;

}
