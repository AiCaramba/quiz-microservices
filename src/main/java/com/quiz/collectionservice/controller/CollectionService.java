package com.quiz.collectionservice.controller;
import com.quiz.collectionservice.model.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/v1")
@RestController
public class CollectionService {

    @PostMapping("collections")
    public ResponseEntity<Object> createCollection(@RequestBody CollectionModel body) {
        try {
            return ResponseEntity.ok().body(body.id);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
