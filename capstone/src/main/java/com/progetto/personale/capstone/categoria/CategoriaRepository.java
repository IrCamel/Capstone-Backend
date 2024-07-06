package com.progetto.personale.capstone.categoria;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Categoria findByNomeCategoria(String nomeCategoria);
    List<Categoria> findAll();;
}

