package com.epam.lab7.documentreader;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;

    @PostMapping("/upload")
    public void uploadFiles(@RequestParam("files") List<MultipartFile> files) {
        files.stream()
                .filter(file -> !file.isEmpty())
                .forEach(this::processFile);
    }

    @PostMapping("/query")
    public String queryDocument(@RequestBody String query) {
        return documentService.queryDocument(query);
    }

    private void processFile(MultipartFile uploadedFile) {
            documentService.addDocument(uploadedFile);
    }
}
