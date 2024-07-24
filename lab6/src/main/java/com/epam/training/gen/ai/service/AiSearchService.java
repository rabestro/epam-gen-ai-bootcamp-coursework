package com.epam.training.gen.ai.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Stream;

@Service
public class AiSearchService implements Function<String, Stream<Document>> {
    private static final int TOP_K = 2;
    private static final double SIMILARITY_THRESHOLD = 0.8;

    private final VectorStore vectorStore;

    public AiSearchService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    /**
     * Searches for documents similar to the given text.
     *
     * @param searchText the text to search for
     * @return a stream of similar documents
     */
    @Override
    public Stream<Document> apply(String searchText) {
        var searchRequest = SearchRequest.query(searchText);
        searchRequest.withTopK(TOP_K);
        searchRequest.withSimilarityThreshold(SIMILARITY_THRESHOLD);
        return vectorStore.similaritySearch(searchRequest).stream();
    }
}
