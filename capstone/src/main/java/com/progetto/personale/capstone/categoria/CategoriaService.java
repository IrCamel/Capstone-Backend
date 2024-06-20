package com.progetto.personale.capstone.categoria;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repository;

    // GET ALL
    public List<Categoria> findAll(){
        return repository.findAll();
    }

    // GET per ID
    public Response findById(Long id){
        if(!repository.existsById(id)){
            throw new EntityNotFoundException("Categoria non trovata");
        }
        Categoria entity = repository.findById(id).get();
        Response response = new Response();
        BeanUtils.copyProperties(entity, response);
        return response;
    }

    // POST
    @Transactional
    public Response createCategoria(@Valid Categoria categoria) {
        Categoria entity = new Categoria();
        BeanUtils.copyProperties(categoria, entity);
        repository.save(entity);
        Response response = new Response();
        BeanUtils.copyProperties(entity, response);
        return response;
    }

    // PUT
    public Response editCategoria(Long id, Request request){

        if(!repository.existsById(id)){
            throw new EntityNotFoundException("Categoria non trovata");
        }
        Categoria entity = repository.findById(id).get();
        BeanUtils.copyProperties(request, entity);
        repository.save(entity);
        Response response = new Response();
        BeanUtils.copyProperties(entity, response);
        return response;
    }


    //DELETE
    public String deleteCategoria(Long id){

        if(!repository.existsById(id)){
            throw  new EntityNotFoundException("Categoria non trovata");
        }
        repository.deleteById(id);
        return "Categoria eliminata";
    }
}