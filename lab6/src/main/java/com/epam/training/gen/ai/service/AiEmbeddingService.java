package com.epam.training.gen.ai.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class AiEmbeddingService implements EmbeddingService {
    private final VectorStore vectorStore;
    private final Function<String, List<String>> textSplitService;

    public AiEmbeddingService(VectorStore vectorStore, Function<String, List<String>> textSplitService) {
        this.vectorStore = vectorStore;
        this.textSplitService = textSplitService;
    }

    @Override
    public boolean addDocumentToVectorDB(File uploadedFile) {
        return addPDFDocumentToVectorDB(uploadedFile);
    }

    @Override
    public Stream<Document> getDocumentsFromVectorDB(String searchText) {
        var searchRequest = SearchRequest.query(searchText);
        searchRequest.withTopK(2);
        searchRequest.withSimilarityThreshold(0.8);
        return vectorStore.similaritySearch(searchRequest).stream();
    }

    private boolean addPDFDocumentToVectorDB(File pdfFile) {
        try (var pdDocument = Loader.loadPDF(pdfFile)) {
            var textStripper = new PDFTextStripper();
            int numberOfPages = pdDocument.getNumberOfPages();
            IntStream.rangeClosed(1, numberOfPages)
                    .forEach(
                            pageNumber -> {
                                try {
                                    textStripper.setStartPage(pageNumber);
                                    textStripper.setEndPage(pageNumber);
                                    var pageText = textStripper
                                            .getText(pdDocument)
                                            .replace("\n", " ")
                                            .replaceAll("\\s{2,}", " ");

                                    // If the text on one page exceeds 7500 characters, split it
                                    if (pageText.length() > TextSplitService.MAX_LENGTH) {
                                        textSplitService.apply(pageText).stream()
                                                .map(Document::new)
                                                .map(List::of)
                                                .forEach(vectorStore::add);
                                    } else if (StringUtils.hasText(pageText)) {
                                        vectorStore.add(List.of(new Document(pageText)));
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
}
