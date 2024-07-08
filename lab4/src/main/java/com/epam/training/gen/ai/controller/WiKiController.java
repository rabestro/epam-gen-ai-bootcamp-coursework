package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.model.ChatRequest;
import com.epam.training.gen.ai.model.ChatResponse;
import com.epam.training.gen.ai.semantic.AiService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class WiKiController {
    private final AiService aiService;

    @Autowired
    public WiKiController(@Qualifier("kernelWikiSearchService") AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping(path = "/search-wiki")
    public @ResponseBody ResponseEntity<ChatResponse> getWikiUrl(@Valid @RequestBody ChatRequest request) {
        return aiService.getKernelFunctionalResponse(request.query())
                .map(ChatResponse::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.unprocessableEntity().build());
    }
}
