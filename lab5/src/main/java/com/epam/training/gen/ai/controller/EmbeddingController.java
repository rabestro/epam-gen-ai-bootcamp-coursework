package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.service.EmbeddingService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class EmbeddingController {
    private final EmbeddingModel embeddingModel;
    private final EmbeddingService embeddingService;

    @GetMapping("/ai/embedding")
    public Map<String, EmbeddingResponse> embed(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        var embeddingResponse = this.embeddingModel.embedForResponse(List.of(message));
        return Map.of("embedding", embeddingResponse);
    }

    @PostMapping("/ai/embedding")
    public void addDocument(@RequestBody String document) {
        this.embeddingService.addDocument(document);
    }

    @GetMapping("/ai/embedding/search")
    public List<Document> search(@RequestParam(value = "message", defaultValue = "prompt engineering") String message) {
        return this.embeddingService.getDocuments(message);
    }

    @PostMapping("/ai/upload-embedding")
    public void uploadFile(@RequestParam("file") MultipartFile file) {
        var uploadedFile = getUploadedFile(file);
        try {
            embeddingService.addDocument(uploadedFile);
        } finally {
            uploadedFile.delete();
        }
    }

    @GetMapping("/ai/dimensions")
    public int getDimensions() {
        return embeddingModel.dimensions();
    }

    private File getUploadedFile(MultipartFile file) {
        var fileName = file.getOriginalFilename();
        var uploadPath = Paths.get("target", "uploads");

        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create upload directory", e);
            }
        }

        var filePath = uploadPath.resolve(fileName);
        try {
            Files.copy(file.getInputStream(), filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to copy uploaded file", e);
        }
        return filePath.toFile();
    }
}
