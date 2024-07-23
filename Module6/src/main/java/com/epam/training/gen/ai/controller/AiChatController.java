package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.model.ChatRequest;
import com.epam.training.gen.ai.model.ChatResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.function.Function;

@RestController
@RequestMapping("/api/v1/chat")
public class AiChatController {
    private final Function<String, Optional<String>> chatService;

    public AiChatController(Function<String, Optional<String>> chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<ChatResponse> sendMessage(@RequestBody ChatRequest request) {
        return chatService.apply(request.question())
                .or("I don't know"::describeConstable)
                .map(ChatResponse::new)
                .map(ResponseEntity::ok)
                .orElseThrow();
    }
}
