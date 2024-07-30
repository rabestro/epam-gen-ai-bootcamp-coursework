package com.epam.lab7.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DocumentServiceImpl implements DocumentService {
    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    @Value("classpath:/prompts/search-prompt.txt")
    private Resource searchPromptResource;

    public DocumentServiceImpl(ChatClient.Builder builder, VectorStore vectorStore) {
        this.chatClient = builder.build();
        this.vectorStore = vectorStore;
    }

    @Override
    public void addDocument(MultipartFile file) {
        var reader = new PagePdfDocumentReader(file.getResource());
        vectorStore.add(reader.read());
    }

    @Override
    public String queryDocument(String query) {
        var context = getDocuments(query);
        var systemMessage = new SystemPromptTemplate(searchPromptResource)
                .createMessage(Map.of("context", context, "query", query));
        var prompt = new Prompt(List.of(systemMessage));
        return chatClient.prompt(prompt).call().content();
    }

    private String getDocuments(String query) {
        return getContext(vectorStore.similaritySearch(SearchRequest.query(query).withTopK(3)));
    }

    private String getContext(List<Document> documents) {
        return documents.stream()
                .map(Document::getContent)
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
