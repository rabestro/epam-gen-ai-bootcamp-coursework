package com.epam.lab7;

import com.epam.lab7.documentreader.Chat;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Lab7Application {
    public static void main(String[] args) {
        SpringApplication.run(Lab7Application.class, args);
    }

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
        return chatClientBuilder.build();
    }

    @Bean
    public Chat chat(ChatClient chatClient, VectorStore vectorStore) {
        return new Chat(chatClient, vectorStore);
    }
}
