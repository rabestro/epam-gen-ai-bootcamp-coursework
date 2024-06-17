package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.dto.ChatResponse;
import com.epam.training.gen.ai.semantic.FunctionMessagePromptService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FunctionMessageController {
    private final FunctionMessagePromptService functionMessagePromptService;

    public FunctionMessageController(FunctionMessagePromptService functionMessagePromptService) {
        this.functionMessagePromptService = functionMessagePromptService;
    }

    @GetMapping("/chat/function-message")
    public ResponseEntity<ChatResponse> getFunctionMessagePromptResponse(@RequestParam String input) {
        var response = functionMessagePromptService.getChatCompletionsWithFunction(input);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/chat/function-message-changed")
    public ResponseEntity<ChatResponse> getFunctionMessageWithChangedSettingsPromptResponse(@RequestParam String input) {
        var response = functionMessagePromptService.getChatCompletionsWithFunctionAndChangedSettings(input);
        return ResponseEntity.ok(response);
    }
}
