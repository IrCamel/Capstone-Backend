package com.progetto.personale.capstone.prodotto;

import com.progetto.personale.capstone.categoria.Categoria;
import com.progetto.personale.capstone.categoria.CategoriaRepository;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProdottoService {

    @Autowired
    private  ProdottoRepository repository;
    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Prodotto> findAll(){
        return repository.findAll();
    }

    public Response findById(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Prodotto inesistente");
        }
        Prodotto entity = repository.findById(id).get();
        Response prodottoResponse = new Response();

        //Codice che sostituisce il mapper
        BeanUtils.copyProperties(entity, prodottoResponse);
        return  prodottoResponse;
    }

    //CREATE PRODOTTO

    @Transactional
    public Response createProdotto(Request request){
        Prodotto entity = new Prodotto();
        BeanUtils.copyProperties(request, entity);
        List<Categoria> categorie = categoriaRepository.findAllById(request.getIdCategorie());
        Response response = new Response();
        repository.save(entity);
        BeanUtils.copyProperties(entity, response);
        return response;
    }

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

    public String deleteProdotto(Long id){
        if(!repository.existsById(id)){
            throw new EntityNotFoundException("Prodotto non trovato");
        }
        repository.deleteById(id);
        return "Prodotto eliminato correttamente";
    }
}
