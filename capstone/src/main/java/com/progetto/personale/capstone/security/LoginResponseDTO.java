package com.progetto.personale.capstone.security;

import lombok.Builder;
import lombok.Data;

@Data
public class LoginResponseDTO {
    String username;
    String token;
    String avatar;
    Long id;

    @Builder(setterPrefix = "with")
    public LoginResponseDTO(RegisteredUserDTO user, String token) {
        this.username = user.getUsername();
        this.token = token;
        this.avatar = user.getAvatar();
        this.id = user.getId();
    }
}


