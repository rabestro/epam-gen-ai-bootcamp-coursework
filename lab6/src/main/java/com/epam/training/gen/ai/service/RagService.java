package com.epam.training.gen.ai.service;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.azure.AzureOpenAiEmbeddingModel;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RagService {
    private final EmbeddingStoreIngestor ingestor;
    private final AzureOpenAiEmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;
    private final ChatLanguageModel chatModel;

    public void addDocument(File file) {
        var document = toDocument(file);
        ingestor.ingest(document);
    }

    public void addDocument(String text) {
        var document = new Document(text);
        ingestor.ingest(document);
    }

    public String generateAnswer(String query) {
        var queryEmbedding = embeddingModel.embed(query).content();
        var context = getContext(queryEmbedding);
        var messages = createMessages(query, context.toString());
        return chatModel.generate(messages).content().text();
    }

    @SneakyThrows
    private Document toDocument(File pdfFile) {
        var pdDocument = Loader.loadPDF(pdfFile);
        var pdfTextStripper = new PDFTextStripper();
        var pdfText = pdfTextStripper.getText(pdDocument);
        return new Document(pdfText);
    }

    private StringBuilder getContext(Embedding queryEmbedding) {
        var relevantMatches = getRelevantMatches(queryEmbedding);
        StringBuilder context = new StringBuilder();
        for (EmbeddingMatch<TextSegment> match : relevantMatches.matches()) {
            context.append(match.embedded().text()).append("\n");
        }
        return context;
    }

    private EmbeddingSearchResult<TextSegment> getRelevantMatches(Embedding queryEmbedding) {
        int maxResults = 3;
        return embeddingStore.search(
                EmbeddingSearchRequest.builder()
                        .queryEmbedding(queryEmbedding)
                        .maxResults(maxResults).build());
    }

    private List<ChatMessage> createMessages(String query, String context) {
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(SystemMessage.from(
                """
                        You are a helpful assistant. Please respond in a friendly and warm manner.
                        Evaluate the information found in our databases and suggest the best option.
                        If you don't know the answer, simply say so.
                        """
        ));
        messages.add(UserMessage.from(createUserMessage(query, context)));
        log.info("Messages: {}", messages);
        return messages;
    }

    private String createUserMessage(String query, String context) {
        return """
                Based on the following information:
                ```
                %s
                ```
                Answer the query: %s
                """.formatted(context, query);
    }
}
