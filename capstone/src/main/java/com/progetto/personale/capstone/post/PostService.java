package com.progetto.personale.capstone.post;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private  PostRepository repository;

    public List<Post> findAll(){
        return repository.findAll();
    }

    public Response findById(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Post inesistente");
        }
        Post entity = repository.findById(id).get();
        Response postResponse = new Response();

        //Codice che sostituisce il mapper
        BeanUtils.copyProperties(entity, postResponse);
        return  postResponse;
    }

    public Response createPost(Request postRequest){
        Post entity = new Post();

        BeanUtils.copyProperties(postRequest, entity);
        Response postResponse = new Response();

        BeanUtils.copyProperties(entity, postResponse);
        repository.save(entity);
        return postResponse;
    }

    public Response editPost(Long id, Request postRequest){
        if(!repository.existsById(id)){
            throw new EntityNotFoundException("Post non trovato");
        }
        Post entity = repository.findById(id).get();
        BeanUtils.copyProperties(postRequest, entity);
        repository.save(entity);

        Response postResponse = new Response();
        BeanUtils.copyProperties(entity, postResponse);

        return  postResponse;

    }

    public String deletePost(Long id){
        if(!repository.existsById(id)){
            throw new EntityNotFoundException("Post non trovato");
        }
        repository.deleteById(id);
        return "Post eliminato correttamente";
    }
}
