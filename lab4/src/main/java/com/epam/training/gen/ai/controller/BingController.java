package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.model.ChatRequest;
import com.epam.training.gen.ai.model.ChatResponse;
import com.epam.training.gen.ai.semantic.kernel.KernelBingSearchService;
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
public class BingController {
    private final KernelBingSearchService bingService;

    @Autowired
    public BingController(KernelBingSearchService bingService) {
        this.bingService = bingService;
    }

    @PostMapping(path = "/search-bing")
    public @ResponseBody ResponseEntity<ChatResponse> getBingUrl(@RequestBody @Valid ChatRequest request) {
        return bingService.getKernelFunctionalResponse(request.query())
                .map(ChatResponse::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.unprocessableEntity().build());
    }
}
