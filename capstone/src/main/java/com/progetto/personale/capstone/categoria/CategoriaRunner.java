package com.progetto.personale.capstone.categoria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class CategoriaRunner implements ApplicationRunner {

    @Autowired
    private CategoriaRepository repository;

    @Autowired
    private CategoriaService service;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(repository.count() == 0) {
            List<Request> categorie = Arrays.asList(

            );

            categorie.forEach(request -> {
                Categoria categoria = new Categoria();
                categoria.setNomeCategoria(request.getNomeCategoria()); // Supponendo che Request abbia un metodo getNome()
                service.createCategoria(categoria);
            });

            System.out.println("Categorie inserite con successo");
        } else {
            System.out.println("Categorie già esistenti");
        }
    }
}

