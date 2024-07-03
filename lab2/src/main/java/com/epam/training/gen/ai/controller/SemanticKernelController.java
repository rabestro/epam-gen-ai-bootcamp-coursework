package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.dto.ChatBookResponse;
import com.epam.training.gen.ai.dto.ChatRequest;
import com.epam.training.gen.ai.dto.ChatResponse;
import com.epam.training.gen.ai.semantic.SemanticKernelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SemanticKernelController {
    private final SemanticKernelService semanticKernelService;

    public SemanticKernelController(SemanticKernelService semanticKernelService) {
        this.semanticKernelService = semanticKernelService;
    }

    @PostMapping("/kernel/simple")
    public ResponseEntity<ChatResponse> getSimplePromptResponse(@RequestBody ChatRequest chatRequest) {
        var response = semanticKernelService.getSimplePromptResponse(chatRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/kernel/json")
    public ResponseEntity<ChatBookResponse> getJsonResponse(@RequestBody ChatRequest chatRequest) {
        var response = semanticKernelService.getJsonResponse(chatRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/kernel/json-with-settings")
    public ResponseEntity<ChatBookResponse> getJsonResponseWithSettings(@RequestBody ChatRequest chatRequest) {
        var response = semanticKernelService.getJsonResponseWithSettings(chatRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/kernel/json-with-history")
    public ResponseEntity<ChatBookResponse> getJsonResponseWithHistory(@RequestBody ChatRequest chatRequest) {
        var response = semanticKernelService.getJsonResponseWithHistory(chatRequest);
        return ResponseEntity.ok(response);
    }
}
