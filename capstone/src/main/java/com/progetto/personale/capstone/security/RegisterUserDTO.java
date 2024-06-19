package com.progetto.personale.capstone.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class RegisterUserDTO {
    String nome;
    String cognome;
    Integer eta;
    String username;
    String email;
    String password;
    String avatar;
}