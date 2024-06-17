package com.progetto.personale.capstone.user;

import lombok.Data;

@Data
public class UserRequest {
    private String nome;
    private String cognome;
    private int eta;
    private String email;
    private String password;
}
