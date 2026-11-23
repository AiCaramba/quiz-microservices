package com.quiz.collectionservice.model;

import jakarta.persistence.*;


@Entity
@Table(name = "collections")
public class CollectionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String id;
    @Column(nullable = false)
    public String words;
    @Column(nullable = false)
    public Boolean isShared;
}
