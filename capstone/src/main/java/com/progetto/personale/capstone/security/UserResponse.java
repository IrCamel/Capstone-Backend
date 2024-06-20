package com.progetto.personale.capstone.security;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String nome;
    private String cognome;
    private Integer eta;
    private String username;
    private String email;
    private String password;
    private String avatar;
}
