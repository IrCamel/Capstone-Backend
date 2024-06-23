package com.progetto.personale.capstone.prodotto;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/prodotti")
public class ProdottoController {

    @Autowired
    ProdottoService service;

    @GetMapping("/{id}")
    public ResponseEntity<Response> findById(@PathVariable Long id){
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<Prodotto>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<CompleteResponse> createProdotto(@RequestPart("prodotto") String prodottoJson, @RequestPart("file") MultipartFile file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Request request = objectMapper.readValue(prodottoJson, Request.class);

        // Supponiamo di ottenere il nome dell'utente dal contesto di sicurezza
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        CompleteResponse response = service.createProdotto(request, file, username);
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Response> modify(@PathVariable Long id, @RequestBody Request request){
        return ResponseEntity.ok(service.editProdotto(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        return ResponseEntity.ok(service.deleteProdotto(id));
    }
}
