package com.epam.training.gen.ai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.function.Consumer;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {
    private final Consumer<MultipartFile> storeService;

    public DocumentController(Consumer<MultipartFile> storeService) {
        this.storeService = storeService;
    }

    @PostMapping
    public ResponseEntity<Void> uploadMultipleFiles(@RequestParam("file") MultipartFile file) {
        storeService.accept(file);
        return ResponseEntity.noContent().build();
    }
}
