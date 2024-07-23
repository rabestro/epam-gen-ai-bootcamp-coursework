package com.epam.training.gen.ai.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class AiEmbeddingService implements EmbeddingService {
    private static final int MAX_LENGTH = 7500;
    private static final int SEARCH_START_OFFSET = 300;
    private final VectorStore vectorStore;

    public AiEmbeddingService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Override
    public boolean addDocumentToVectorDB(File uploadedFile) {
        return addPDFDocumentToVectorDB(uploadedFile);
    }

    @Override
    public List<Document> getDocumentsFromVectorDB(String searchText) {
        var searchRequest = SearchRequest.query(searchText);

        searchRequest.withTopK(2);
        searchRequest.withSimilarityThreshold(0.8);
        return vectorStore.similaritySearch(searchRequest);
    }

    private boolean addPDFDocumentToVectorDB(File pdfFile) {
        try (PDDocument pdDocument = Loader.loadPDF(pdfFile)) {
            PDFTextStripper textStripper = new PDFTextStripper();
            int numberOfPages = pdDocument.getNumberOfPages();
            IntStream.rangeClosed(1, numberOfPages)
                    .forEach(
                            pageNumber -> {
                                try {
                                    textStripper.setStartPage(pageNumber);
                                    textStripper.setEndPage(pageNumber);
                                    String pageText = textStripper.getText(pdDocument);
                                    // Replace newline characters with whitespace
                                    pageText = pageText.replace("\n", " ");
                                    pageText = pageText.replaceAll("\\s{2,}", " ");

                                    // If the text on one page exceeds 7500 characters, split it
                                    if (pageText.length() > MAX_LENGTH) {
                                        List<String> splitText = splitText(pageText);
                                        splitText.forEach(
                                                text -> {
                                                    vectorStore.add(List.of(new Document(text)));
                                                });
                                    } else {
                                        if (StringUtils.hasText(pageText)) {
                                            vectorStore.add(List.of(new Document(pageText)));
                                        }
                                    }

                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    private List<String> splitText(String text) {
        List<String> chunks = new ArrayList<>();
        while (text.length() > MAX_LENGTH) {
            int splitIndex = findSplitIndex(text);
            chunks.add(text.substring(0, splitIndex));
            text = text.substring(splitIndex);
        }
        chunks.add(text);
        return chunks;
    }

    private int findSplitIndex(String text) {
        for (int i = MAX_LENGTH - SEARCH_START_OFFSET; i < MAX_LENGTH; i++) {
            if (isPunctuation(text.charAt(i))) return i;
        }
        return MAX_LENGTH;
    }

    private boolean isPunctuation(char c) {
        return ".:;?!".indexOf(c) >= 0;
    }
}
