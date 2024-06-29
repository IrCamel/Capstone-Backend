package com.progetto.personale.capstone.post;

import com.progetto.personale.capstone.security.User;
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

    @Column(length = 500, unique = true)
    private String descrizione;

    @Column
    private String imgUrl;

    @ManyToOne
    @JoinColumn(name = "username")
    private User user;

}