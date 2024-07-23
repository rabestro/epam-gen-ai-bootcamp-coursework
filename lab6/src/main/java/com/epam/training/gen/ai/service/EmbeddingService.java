package com.epam.training.gen.ai.service;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.ai.document.Document;

public interface EmbeddingService {
  boolean addDocumentToVectorDB(File uploadedFile);

  Stream<Document> getDocumentsFromVectorDB(String searchText);
}
