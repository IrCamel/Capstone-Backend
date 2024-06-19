package com.progetto.personale.capstone.security;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RegisteredUserDTO {
    Long id;
    String nome;
    String cognome;
    int eta;
    String username;
    String email;
    private List<Roles> roles;

    @Builder(setterPrefix = "with")
    public RegisteredUserDTO(Long id, String nome, String cognome, int eta, String username, String email, List<Roles> roles) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.eta = eta;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}