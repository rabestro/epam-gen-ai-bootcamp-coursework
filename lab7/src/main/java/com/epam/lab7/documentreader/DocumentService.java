package com.epam.lab7.v1.documentreader;

import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {
    void addDocument(MultipartFile file);

    String queryDocument(String query);
}
