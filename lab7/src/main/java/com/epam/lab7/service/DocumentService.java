package com.epam.lab7.service;

import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {
    void addDocument(MultipartFile file);

    String queryDocument(String query);
}
