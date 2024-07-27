package com.epam.lab7.documentreader;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.epam.lab7.documentreader.DocumentSearchSystemPrompt.SYSTEM_PROMPT;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private final Chat chat;

    @Override
    public void addDocument(MultipartFile file) {
        var reader = new PagePdfDocumentReader(file.getResource());
        chat.vectorStore().add(reader.read());
    }

    @Override
    public String queryDocument(String query) {
        var context = getDocuments(query);
        var systemPromptTemplate = new SystemPromptTemplate(SYSTEM_PROMPT);
        var systemMessage = systemPromptTemplate.createMessage(Map.of("context", context, "query", query));
        var prompt = new Prompt(List.of(systemMessage));
        return chat.aiClient().prompt(prompt).call().content();
    }

    private String getDocuments(String query) {
        return getContext(chat.vectorStore().similaritySearch(SearchRequest.query(query).withTopK(3)));
    }

    private String getContext(List<Document> documents) {
        return documents.stream()
                .map(Document::getContent)
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
