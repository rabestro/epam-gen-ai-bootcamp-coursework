package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.model.ChatRequest;
import com.epam.training.gen.ai.service.AiService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BingController {
    private final AiService bingService;

    @Autowired
    public BingController(AiService bingService) {
        this.bingService = bingService;
    }

    @PostMapping(path = "/bing")
    public @ResponseBody List<String> getBingUrl(@RequestBody @Valid ChatRequest request) {
        return bingService.getChatCompletions(request.query());
    }
}
