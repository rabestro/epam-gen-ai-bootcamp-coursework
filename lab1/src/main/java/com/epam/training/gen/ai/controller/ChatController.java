package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.dto.ChatBookResponse;
import com.epam.training.gen.ai.dto.ChatRequest;
import com.epam.training.gen.ai.service.SemanticKernelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatController {

    private final SemanticKernelService semanticKernelService;

    @PostMapping("/generate-text")
    public ResponseEntity<String> getResponseWithSettings(@Valid @RequestBody ChatRequest chatRequest) {
        var response = semanticKernelService.getResponseWithSettings(chatRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/generate-json")
    public ResponseEntity<ChatBookResponse> getJsonResponseWithSettings(@Valid @RequestBody ChatRequest chatRequest) {
        var response = semanticKernelService.getJsonResponseWithSettings(chatRequest);
        return ResponseEntity.ok(response);
    }
}
