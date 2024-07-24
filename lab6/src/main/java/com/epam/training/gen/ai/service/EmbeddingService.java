package com.epam.training.gen.ai.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class EmbeddingService implements Consumer<File> {
    private final VectorStore vectorStore;
    private final Function<String, Stream<String>> textSplitService;

    public EmbeddingService(VectorStore vectorStore, Function<String, Stream<String>> textSplitService) {
        this.vectorStore = vectorStore;
        this.textSplitService = textSplitService;
    }

    private void addPDFDocumentToVectorDB(File pdfFile) {
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
                                    textSplitService.apply(pageText)
                                            .map(Document::new)
                                            .map(List::of)
                                            .forEach(vectorStore::add);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void accept(File file) {
        addPDFDocumentToVectorDB(file);
    }
}
