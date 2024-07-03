package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.dto.PromptRequest;
import com.epam.training.gen.ai.service.WikiUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class WiKiController {
    private final WikiUrlService wikiUrlService;

    @Autowired
    public WiKiController(WikiUrlService wikiUrlService) {
        this.wikiUrlService = wikiUrlService;
    }

    @PostMapping(path = "/wiki")
    public @ResponseBody List<String> getAge(@RequestBody PromptRequest request) {
        return wikiUrlService.getChatCompletions(request.prompt());
    }
}
