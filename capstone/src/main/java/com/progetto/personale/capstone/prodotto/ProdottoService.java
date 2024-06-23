package com.progetto.personale.capstone.prodotto;

import com.cloudinary.Cloudinary;
import com.progetto.personale.capstone.categoria.Categoria;
import com.progetto.personale.capstone.categoria.CategoriaRepository;
import com.progetto.personale.capstone.security.User;
import com.progetto.personale.capstone.security.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.annotation.MultipartConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdottoService {

    private final  ProdottoRepository repository;
    private final CategoriaRepository categoriaRepository;
    private final UserRepository userRepository;
    private final Cloudinary cloudinary;

    /////////////////FIND ALL//////////////////////

    public List<Prodotto> findAll(){
        return repository.findAll();
    }

    /////////////////FIND BY ID//////////////////////

    public Response findById(Long id) {
        Prodotto entity = repository.findByIdWithUser(id).orElseThrow(() -> new EntityNotFoundException("Prodotto inesistente"));
        Response prodottoResponse = new Response();
        BeanUtils.copyProperties(entity, prodottoResponse);
        prodottoResponse.setUsername(entity.getUser().getUsername()); // Assumendo che Response abbia il campo username
        return prodottoResponse;
    }

    /////////////////CREATE PRODOTTO//////////////////////
    @Transactional
    public CompleteResponse createProdotto(Request prodottoRequest, MultipartFile file) throws IOException {
        var uploadResult = cloudinary.uploader().upload(file.getBytes(),
                com.cloudinary.utils.ObjectUtils.asMap("public_id", prodottoRequest.getNomeProdotto() + "_avatar"));
        String url = uploadResult.get("url").toString();

        Prodotto entity = new Prodotto();
        entity.setImgUrl(url);
        BeanUtils.copyProperties(prodottoRequest, entity);
        Categoria categoria = categoriaRepository.findByNomeCategoria(prodottoRequest.getNomeCategoria());
        entity.setCategoria(categoria);

        // Assume che l'ID dell'utente sia passato nel prodottoRequest
        User user = userRepository.findById(prodottoRequest.getUserId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        entity.setUser(user);

        repository.save(entity);

        CompleteResponse response = new CompleteResponse();
        BeanUtils.copyProperties(entity, response);
        response.setUsername(user.getUsername());

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
