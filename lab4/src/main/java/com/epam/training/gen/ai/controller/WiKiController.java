package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.dto.PromptRequest;
import com.epam.training.gen.ai.service.AiService;
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
    private final AiService wikiUrlService;

    @Autowired
    public WiKiController(AiService wikiUrlService) {
        this.wikiUrlService = wikiUrlService;
    }

    @PostMapping(path = "/wiki")
    public @ResponseBody List<String> getWikiUrl(@RequestBody PromptRequest request) {
        return wikiUrlService.getChatCompletions(request.prompt());
    }
}
