package com.epam.training.gen.ai.semantic.controller;

import com.epam.training.gen.ai.semantic.AgeService;
import com.epam.training.gen.ai.semantic.BingService;
import com.epam.training.gen.ai.semantic.WikiUrlService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PromptController {

    private final AgeService promptService;
    private final WikiUrlService urlService;
    private final BingService bingService;

    @Autowired
    public PromptController(AgeService promptService, WikiUrlService urlService, BingService bingService) {
        this.promptService = promptService;
        this.urlService = urlService;
        this.bingService = bingService;
    }

    @PostMapping(path = "/age", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> getAge(@RequestBody PromptRequest request) {
        return promptService.getChatCompletions(request.prompt);
    }

    @PostMapping(path = "/wiki", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> getWikiUrl(@RequestBody PromptRequest request) {
        return urlService.getChatCompletions(request.prompt);
    }

    @PostMapping(path = "/bing", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> getBingUrl(@RequestBody PromptRequest request) {
        return bingService.getChatCompletions(request.prompt);
    }

    public static class PromptRequest {

        private String prompt;

        public String getPrompt() {
            return prompt;
        }

        public void setPrompt(String prompt) {
            this.prompt = prompt;
        }
    }
}
