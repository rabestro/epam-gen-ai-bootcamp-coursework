package com.epam.training.gen.ai.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class StoreService implements Consumer<MultipartFile> {
    private final EmbeddingService embeddingService;
    private final Path uploadPath;

    {
        uploadPath = Paths.get("target", "uploads");

        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create upload directory", e);
            }
        }
    }

    public StoreService(EmbeddingService embeddingService) {
        this.embeddingService = embeddingService;
    }

    private static void saveToPath(MultipartFile multipartFile, Path filePath) {
        try {
            Files.copy(multipartFile.getInputStream(), filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to copy uploaded file", e);
        }
    }

    @Override
    public void accept(MultipartFile multipartFile) {
        var filePath = Optional.ofNullable(multipartFile)
                .map(MultipartFile::getOriginalFilename)
                .map(uploadPath::resolve)
                .orElseThrow();
        saveToPath(multipartFile, filePath);
        var uploadedFile = filePath.toFile();
        embeddingService.addDocumentToVectorDB(uploadedFile);
        uploadedFile.delete();
    }
}
