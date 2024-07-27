package com.epam.lab7.v1.chat;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.vectorstore.VectorStore;

public record Chat(ChatClient aiClient, VectorStore vectorStore) {
}
