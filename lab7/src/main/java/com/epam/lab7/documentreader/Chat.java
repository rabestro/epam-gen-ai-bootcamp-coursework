package com.epam.lab7.documentreader;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.vectorstore.VectorStore;

public record Chat(ChatClient aiClient, VectorStore vectorStore) {
}
