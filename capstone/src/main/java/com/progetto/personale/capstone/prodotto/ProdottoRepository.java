package com.progetto.personale.capstone.prodotto;

import com.progetto.personale.capstone.categoria.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {

    @Query("SELECT p FROM Prodotto p JOIN FETCH p.user WHERE p.id = :id")
    Optional<Prodotto> findByIdWithUser(@Param("id") Long id);


}
