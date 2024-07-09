package com.progetto.personale.capstone.prodotto;

import com.cloudinary.Cloudinary;
import com.progetto.personale.capstone.categoria.Categoria;
import com.progetto.personale.capstone.categoria.CategoriaRepository;
import com.progetto.personale.capstone.security.User;
import com.progetto.personale.capstone.security.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProdottoService {

    private final ProdottoRepository repository;
    private final CategoriaRepository categoriaRepository;
    private final UserRepository userRepository;
    private final Cloudinary cloudinary;

    public List<CompleteResponse> findAll() {
        return repository.findAll().stream()
                .map(this::convertToCompleteResponse)
                .collect(Collectors.toList());
    }

    public CompleteResponse findById(Long id) {
        Prodotto entity = repository.findByIdWithUser(id).orElseThrow(() -> new EntityNotFoundException("Prodotto inesistente"));
        return convertToResponse(entity);
    }

    @Transactional
    public CompleteResponse createProdotto(Request prodottoRequest, MultipartFile[] files) throws IOException {
        List<String> imgUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            var uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    com.cloudinary.utils.ObjectUtils.asMap("public_id", prodottoRequest.getNomeProdotto() + "_" + file.getOriginalFilename()));
            imgUrls.add(uploadResult.get("url").toString());
        }

        Prodotto entity = new Prodotto();
        entity.setImgUrl(imgUrls);
        BeanUtils.copyProperties(prodottoRequest, entity);
        Categoria categoria = categoriaRepository.findByNomeCategoria(prodottoRequest.getNomeCategoria());
        entity.setCategoria(categoria);

        User user = userRepository.findById(prodottoRequest.getUserId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        entity.setUser(user);

        repository.save(entity);

        return convertToCompleteResponse(entity);
    }

    public CompleteResponse editProdotto(Long id, Request prodottoRequest) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Prodotto non trovato");
        }
        Prodotto entity = repository.findById(id).get();
        BeanUtils.copyProperties(prodottoRequest, entity);
        repository.save(entity);

        return convertToResponse(entity);
    }

    public String deleteProdotto(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return "Prodotto eliminato con successo";
        } else {
            throw new EntityNotFoundException("Prodotto non trovato");
        }
    }

    private CompleteResponse convertToCompleteResponse(Prodotto prodotto) {
        CompleteResponse response = new CompleteResponse();
        BeanUtils.copyProperties(prodotto, response);
        response.setUsername(prodotto.getUser().getUsername());
        response.setAvatar(prodotto.getUser().getAvatar());
        response.setCategoria(List.of(prodotto.getCategoria()));
        response.setImgUrl(prodotto.getImgUrl());
        return response;
    }

    private CompleteResponse convertToResponse(Prodotto prodotto) {
        CompleteResponse response = new CompleteResponse();
        BeanUtils.copyProperties(prodotto, response);
        response.setUsername(prodotto.getUser().getUsername());
        response.setAvatar(prodotto.getUser().getAvatar());
        response.setImgUrl(prodotto.getImgUrl());
        return response;
    }

    public List<Categoria> getCategorie() {
        return categoriaRepository.findAll();
    }
}
