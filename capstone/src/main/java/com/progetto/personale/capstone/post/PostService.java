package com.progetto.personale.capstone.post;

import com.progetto.personale.capstone.security.User;
import com.progetto.personale.capstone.security.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final  PostRepository repository;
    private final UserRepository userRepository;

    /////////////////FIND ALL//////////////////////

    public List<Post> findAll(){
        return repository.findAll();
    }

    /////////////////FIND BY ID//////////////////////

    public PostResponse findById(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Post inesistente");
        }
        Post entity = repository.findById(id).get();
        PostResponse postResponse = new PostResponse();
        BeanUtils.copyProperties(entity, postResponse);
        return  postResponse;
    }

    /////////////////CREATE POST//////////////////////

    @Transactional
    public PostResponse createPost(@Valid PostRequest postRequest) {
        Post entity = new Post();
        BeanUtils.copyProperties(postRequest, entity);
        repository.save(entity);
        PostResponse response = new PostResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }
    /////////////////EDIT POST//////////////////////

    public PostResponse editPost(Long id,PostRequest postRequest){
        if(!repository.existsById(id)){
            throw new EntityNotFoundException("Post non trovato");
        }
        Post entity = repository.findById(id).get();
        BeanUtils.copyProperties(postRequest, entity);
        repository.save(entity);
        PostResponse postResponse = new PostResponse();
        BeanUtils.copyProperties(entity, postResponse);
        return  postResponse;

    }

    /////////////////DELETE POST//////////////////////

    public String deletePost(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return "Post eliminato con successo";
        } else {
            throw new EntityNotFoundException("Post non trovato");
        }
    }
}
