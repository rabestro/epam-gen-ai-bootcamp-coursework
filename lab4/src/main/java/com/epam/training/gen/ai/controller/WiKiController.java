package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.model.ChatRequest;
import com.epam.training.gen.ai.model.ChatResponse;
import com.epam.training.gen.ai.semantic.kernel.KernelWikiSearchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class WiKiController {
    private final KernelWikiSearchService wikiUrlService;

    @Autowired
    public WiKiController(KernelWikiSearchService wikiUrlService) {
        this.wikiUrlService = wikiUrlService;
    }

    @PostMapping(path = "/search-wiki")
    public @ResponseBody ResponseEntity<ChatResponse> getWikiUrl(@Valid @RequestBody ChatRequest request) {
        return wikiUrlService.getKernelFunctionalResponse(request.query())
                .map(ChatResponse::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.unprocessableEntity().build());
    }
}
