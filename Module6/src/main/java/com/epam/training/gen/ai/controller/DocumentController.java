package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.service.StoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {
    private final StoreService storeService;

    public DocumentController(StoreService storeService) {
        this.storeService = storeService;
    }

    @PostMapping
    public ResponseEntity<Void> uploadMultipleFiles(@RequestParam("file") MultipartFile file) {
        storeService.save(file);
        return ResponseEntity.noContent().build();
    }
}
