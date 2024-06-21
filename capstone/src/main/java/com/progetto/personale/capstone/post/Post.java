package com.progetto.personale.capstone.post;

import com.progetto.personale.capstone.security.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @ManyToOne
    @JoinColumn(name = "username")
    private User user;

    // Aggiungi getter e setter per userId se non già presenti
    public String getUserUsername() {
        return user.getUsername();
    }


    public String setUserUsername(String username) {
        if (this.user == null) {
            this.user = new User();
        }
        this.user.setUsername(username);
        return username;
    }

    public @NotNull User getUser(User user1) {
        return this.user = user1;
    }
}
