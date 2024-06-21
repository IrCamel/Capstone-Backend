package com.progetto.personale.capstone.categoria;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.progetto.personale.capstone.prodotto.Prodotto;
import jakarta.persistence.*;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "categorie")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonProperty("nome")
    @Column(length = 50, unique = true)
    private String nomeCategoria;

    @JsonIgnore
    @OneToMany(mappedBy = "categoria")
    private Set<Prodotto> prodotti = new HashSet<>();

    // Getter e setter
}
