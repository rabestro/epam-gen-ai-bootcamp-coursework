package com.epam.lab7.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class TreasureController {
    private final ChatClient chatClient;

    /**
     * The system message can be used to specify the persona used by the model in its replies.
     */
    public TreasureController(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultSystem("Please respond to any question in the voice of a pirate.")
                .build();
    }

    /**
     * Respond to inquiries about pirate treasure
     */
    @GetMapping("/treasure")
    public String treasureFacts() {
        return chatClient.prompt()
                .user("Tell me a really interesting fact about famous pirate treasures. Please keep your answer to 1 or 2 sentences.")
                .call()
                .content();
    }
}
