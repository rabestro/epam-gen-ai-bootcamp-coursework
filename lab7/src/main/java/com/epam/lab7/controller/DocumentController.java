package com.epam.lab7.controller;

import com.epam.lab7.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static java.util.function.Predicate.not;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;

    @PostMapping
    public void uploadFiles(@RequestParam("files") List<MultipartFile> files) {
        files.stream()
                .filter(not(MultipartFile::isEmpty))
                .forEach(documentService::addDocument);
    }

    @PostMapping("/query")
    public String queryDocument(@RequestBody String query) {
        return documentService.queryDocument(query);
    }

}
