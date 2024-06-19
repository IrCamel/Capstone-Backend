package com.progetto.personale.capstone.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class UserRunner implements ApplicationRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRespository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userRespository.count() == 0) {
            List<RegisterUserDTO> users = Arrays.asList(

            );

            users.forEach(userService::register);
            System.out.println("--- Utenti registrati ---");
        }
    }
}
