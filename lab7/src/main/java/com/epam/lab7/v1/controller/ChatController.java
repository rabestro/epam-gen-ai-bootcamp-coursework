package com.epam.lab7.v1.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ChatController {
    private final ChatClient chatClient;
    private final ChatClient chatWithHistoryClient;

    public ChatController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
        this.chatWithHistoryClient = builder
                .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
                .build();
    }

    @GetMapping("/chat")
    public String joke(@RequestParam(defaultValue = "Tell me a dad joke about Dog") String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content(); // short for getResult().getOutput().getContent();
    }

    @GetMapping("/jokes-by-topic")
    public ResponseEntity<String> jokesByTopic(@RequestParam(value = "topic", defaultValue = "Dog") String topic) {
        var response = chatClient.prompt()
                .user(u -> u.text("Tell me a joke about {topic}").param("topic", topic))
                .call()
                .content();
        return ResponseEntity.ok(response);
    }

    @GetMapping("chat-with-response")
    public ChatResponse jokeWithResponse(
            @RequestParam(defaultValue = "Tell me a dad joke about computers") String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .chatResponse();
    }

    @GetMapping("chat-with-history")
    public String jokeWithHistory(@RequestParam(defaultValue = "Tell me a dad joke about computers") String message) {
        return chatWithHistoryClient.prompt()
                .user(message)
                .call()
                .content();
    }
}
