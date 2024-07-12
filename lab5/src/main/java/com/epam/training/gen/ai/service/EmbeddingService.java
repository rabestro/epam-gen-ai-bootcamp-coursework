package com.epam.training.gen.ai.services;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmbeddingService {
    private final VectorStore vectorStore;
    private final EmbeddingClient embeddingClient;

    public void addDocument(File file) {
        var document = convertPdfToDocument(file);
        vectorStore.add(List.of(document));
    }

    public List<Double> embedDocument(File file) {
        var document = convertPdfToDocument(file);
        return embeddingClient.embed(document);
    }

    public void addDocument(String text) {
        Document document = new Document(text);
        vectorStore.add(List.of(document));
    }

    public List<Document> getDocuments(String searchText) {
        return vectorStore.similaritySearch(searchText);
    }

    public List<Document> getDocuments(SearchRequest searchText) {
        return vectorStore.similaritySearch(searchText);
    }

    private Document convertPdfToDocument(File pdfFile) {
        String text;
        PDFTextStripper pdfTextStripper = new PDFTextStripper();
        try (PDDocument pdDocument = Loader.loadPDF(pdfFile);) {
            text = pdfTextStripper.getText(pdDocument);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new Document(text);
    }

}
