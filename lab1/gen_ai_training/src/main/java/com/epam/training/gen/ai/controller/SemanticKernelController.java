package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.dto.ChatBookResponse;
import com.epam.training.gen.ai.dto.ChatKernelRequest;
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
    public ResponseEntity<ChatResponse> getSimplePromptKernelResponse(@RequestBody ChatKernelRequest chatKernelRequest) {
        var response = semanticKernelService.getKernelResponseUsingSimplePrompt(chatKernelRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/kernel/json")
    public ResponseEntity<ChatBookResponse> getJsonPromptKernelResponse(@RequestBody ChatKernelRequest chatKernelRequest) {
        var response = semanticKernelService.getKernelJsonResponse(chatKernelRequest);
        return ResponseEntity.ok(response);
    }
}
