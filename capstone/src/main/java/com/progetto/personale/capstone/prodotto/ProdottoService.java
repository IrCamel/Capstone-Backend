package com.progetto.personale.capstone.prodotto;

import com.progetto.personale.capstone.categoria.Categoria;
import com.progetto.personale.capstone.categoria.CategoriaRepository;
import com.progetto.personale.capstone.security.User;
import com.progetto.personale.capstone.security.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdottoService {

    private final  ProdottoRepository repository;
    private final CategoriaRepository categoriaRepository;
    private final UserRepository userRepository;

    /////////////////FIND ALL//////////////////////

    public List<Prodotto> findAll(){
        return repository.findAll();
    }

    /////////////////FIND BY ID//////////////////////

    public Response findById(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Prodotto inesistente");
        }
        Prodotto entity = repository.findById(id).get();
        Response prodottoResponse = new Response();

        BeanUtils.copyProperties(entity, prodottoResponse);
        return  prodottoResponse;
    }

    /////////////////CREATE PRODOTTO//////////////////////
    @Transactional
    public CompleteResponse createProdotto(Request prodottoRequest) {
        Prodotto entity = new Prodotto();
        BeanUtils.copyProperties(prodottoRequest, entity);
        Categoria categoria = categoriaRepository.findByNomeCategoria(prodottoRequest.getNomeCategoria());
        entity.setCategoria(categoria);
        repository.save(entity);
        CompleteResponse response = new CompleteResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }

    /////////////////EDIT PRODOTTO//////////////////////

    public Response editProdotto(Long id, Request prodottoRequest){
        if(!repository.existsById(id)){
            throw new EntityNotFoundException("Prodotto non trovato");
        }
        Prodotto entity = repository.findById(id).get();
        BeanUtils.copyProperties(prodottoRequest, entity);
        repository.save(entity);

        Response prodottoResponse = new Response();
        BeanUtils.copyProperties(entity, prodottoResponse);

        return  prodottoResponse;

    }

    /////////////////DELETE PRODOTTO//////////////////////

    public String deleteProdotto(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return "Prodotto eliminato con successo";
        } else {
            throw new EntityNotFoundException("Prodotto non trovato");
        }
    }
}
