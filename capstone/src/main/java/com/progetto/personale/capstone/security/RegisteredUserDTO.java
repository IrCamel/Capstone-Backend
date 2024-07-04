package com.progetto.personale.capstone.security;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RegisteredUserDTO {
    Long id;
    String username;
    String avatar;
    private List<Roles> roles;

    @Builder(setterPrefix = "with")
    public RegisteredUserDTO(Long id, String nome, String cognome, int eta, String username, String email, String avatar, List<Roles> roles) {
        this.id = id;
        this.username = username;
        this.roles = roles;
    }
}