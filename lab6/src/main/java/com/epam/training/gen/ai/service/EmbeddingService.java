package com.epam.training.gen.ai.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

@Service
public class EmbeddingService implements Consumer<File> {
    private final VectorStore vectorStore;
    private final Function<String, Stream<String>> textSplitService;
    private final Function<PDDocument, Stream<String>> textStripperService;
    private final UnaryOperator<String> textCleanupService;

    public EmbeddingService(
            VectorStore vectorStore,
            Function<String, Stream<String>> textSplitService,
            Function<PDDocument, Stream<String>> textStripperService,
            UnaryOperator<String> textCleanupService) {
        this.vectorStore = vectorStore;
        this.textSplitService = textSplitService;
        this.textStripperService = textStripperService;
        this.textCleanupService = textCleanupService;
    }

    private void addPDFDocumentToVectorDB(PDDocument pdDocument) {
        textStripperService.apply(pdDocument)
                .map(textCleanupService)
                .flatMap(textSplitService)
                .map(Document::new)
                .map(List::of)
                .forEach(vectorStore::add);
    }

    @Override
    public void accept(File file) {
        try (var pdDocument = Loader.loadPDF(file)) {
            addPDFDocumentToVectorDB(pdDocument);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
