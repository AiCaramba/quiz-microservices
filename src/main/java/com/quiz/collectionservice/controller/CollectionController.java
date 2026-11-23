package com.quiz.collectionservice.controller;
import com.quiz.collectionservice.dao.CollectionsRepository;
import com.quiz.collectionservice.model.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/v1")
@RestController
public class CollectionController {
    private final CollectionsRepository collectionsRepository;
    public CollectionController(CollectionsRepository collectionsRepository) {
        this.collectionsRepository = collectionsRepository;
    }

    @GetMapping("collections")
    public ResponseEntity<Object> getAllCollections(@RequestBody CollectionModel body) {
        try {
            List<CollectionModel> collections = collectionsRepository.findAll();
            return ResponseEntity.ok().body(collections);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("collections")
    public ResponseEntity<Object> createCollection(@RequestBody CollectionModel body) {
        try {
            collectionsRepository.save(body);
            return ResponseEntity.ok().body(body.id);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
