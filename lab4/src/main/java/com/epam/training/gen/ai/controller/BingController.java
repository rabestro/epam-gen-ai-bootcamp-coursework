package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.dto.PromptRequest;
import com.epam.training.gen.ai.service.BingService;
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
    private final BingService bingService;

    @Autowired
    public BingController(BingService bingService) {
        this.bingService = bingService;
    }

    @PostMapping(path = "/bing")
    public @ResponseBody List<String> getBingUrl(@RequestBody PromptRequest request) {
        return bingService.getChatCompletions(request.prompt());
    }
}
