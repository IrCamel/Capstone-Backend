package com.progetto.personale.capstone.prodotto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ProdottoRunner implements ApplicationRunner {

    @Autowired
    private ProdottoService service;

    @Autowired
    private ProdottoRepository repository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (repository.count() == 0) {
            List<Request> prodotti = Arrays.asList(

            );
            prodotti.forEach(service::createProdotto);
            System.out.println("Prodotti inseriti");
        }
    }
}
