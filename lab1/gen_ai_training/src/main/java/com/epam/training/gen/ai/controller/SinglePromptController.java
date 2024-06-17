package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.dto.ChatResponse;
import com.epam.training.gen.ai.semantic.SinglePromptService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SinglePromptController {
    private final SinglePromptService singlePromptService;

    public SinglePromptController(SinglePromptService singlePromptService) {
        this.singlePromptService = singlePromptService;
    }

    @GetMapping("/chat/simple")
    public ResponseEntity<ChatResponse> getSimplePromptResponse(@RequestParam String input) {
        var response = singlePromptService.getSinglePromptChatCompletions(input);
        return ResponseEntity.ok(response);
    }
}
