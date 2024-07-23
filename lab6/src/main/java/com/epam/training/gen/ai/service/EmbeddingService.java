package com.epam.training.gen.ai.service;

import java.io.File;
import java.util.List;
import org.springframework.ai.document.Document;

public interface EmbeddingService {
  boolean addDocumentToVectorDB(File uploadedFile);

  List<Document> getDocumentsFromVectorDB(String searchText);
}
