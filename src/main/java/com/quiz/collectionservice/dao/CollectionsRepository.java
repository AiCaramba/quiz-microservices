package com.quiz.collectionservice.dao;

import com.quiz.collectionservice.model.CollectionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionsRepository extends JpaRepository<CollectionModel, String> {
}
