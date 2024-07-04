package com.progetto.personale.capstone.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


public record RegisterUserModel(
        @Getter
        @Setter
        @NotBlank(message = "Il tuo nome non può essere vuoto")
        String nome,
        @Getter
        @Setter
        @NotBlank(message = "Il tuo cognome non può essere vuoto")
        String cognome,
        @Getter
        @Setter
        @NotNull(message = "Inserisci un eta valida")
        Integer eta,
        @Getter
        @Setter
        @NotBlank(message = "Lo username  non può contenere solo spazi vuoti")
        @Size(max = 50, message ="Il tuo username è troppo lungo max 50 caratteri")
        String username,
        @Getter
        @Setter
        @Email(message = "Inserisci una email valida")
        String email,
        @Getter
        @Setter
        String avatar,
        @Getter
        @Setter
        @NotBlank(message = "La password non può contenere solo spazi vuoti")
        @Size(max = 125, message ="La password è troppo lunga max 20 caratteri")
        String password


) {

}