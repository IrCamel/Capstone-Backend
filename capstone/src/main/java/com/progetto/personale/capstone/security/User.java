package com.progetto.personale.capstone.security;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(length = 50, nullable = false)
    private String nome;
    @Column(length = 50, nullable = false)
    private String cognome;
    @Column(nullable = false)
    private Integer eta;
    private String username;
    private String email;
    @Column(length = 125, nullable = false)
    private String password;
    private String avatar;
    @ManyToMany(fetch = FetchType.EAGER)
    private final List<Roles> roles = new ArrayList<>();
}