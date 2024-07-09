package com.progetto.personale.capstone.prodotto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/prodotti")
public class ProdottoController {

    private static final Logger logger = LoggerFactory.getLogger(ProdottoController.class);


    @Autowired
    ProdottoService service;

    @GetMapping("/{id}")
    public ResponseEntity<CompleteResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<CompleteResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<CompleteResponse> createProdotto(@RequestPart("prodotto") String prodottoJson, @RequestPart("file") MultipartFile file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Request request = objectMapper.readValue(prodottoJson, Request.class);

        CompleteResponse response = service.createProdotto(request, file);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompleteResponse> modify(@PathVariable Long id, @RequestBody Request request) {
        return ResponseEntity.ok(service.editProdotto(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return ResponseEntity.ok(service.deleteProdotto(id));
    }
}
