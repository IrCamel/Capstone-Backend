package com.progetto.personale.capstone.categoria;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private  CategoriaRepository repository;

    public List<Categoria> findAll(){
        return repository.findAll();
    }

    public Response findById(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Categoria inesistente");
        }
        Categoria entity = repository.findById(id).get();
        Response categoriaResponse = new Response();

        //Codice che sostituisce il mapper
        BeanUtils.copyProperties(entity, categoriaResponse);
        return  categoriaResponse;
    }

    public Response createCategoria(Request categoriaRequest){
        Categoria entity = new Categoria();

        BeanUtils.copyProperties(categoriaRequest, entity);
        Response categoriaResponse = new Response();

        BeanUtils.copyProperties(entity, categoriaResponse);
        repository.save(entity);
        return categoriaResponse;
    }

    public Response editCategoria(Long id, Request categoriaRequest){
        if(!repository.existsById(id)){
            throw new EntityNotFoundException("Categoria non trovata");
        }
        Categoria entity = repository.findById(id).get();
        BeanUtils.copyProperties(categoriaRequest, entity);
        repository.save(entity);

        Response categoriaResponse = new Response();
        BeanUtils.copyProperties(entity, categoriaResponse);

        return  categoriaResponse;

    }

    public String deleteCategoria(Long id){
        if(!repository.existsById(id)){
            throw new EntityNotFoundException("Categoria non trovata");
        }
        repository.deleteById(id);
        return "Categoria eliminata correttamente";
    }
}
