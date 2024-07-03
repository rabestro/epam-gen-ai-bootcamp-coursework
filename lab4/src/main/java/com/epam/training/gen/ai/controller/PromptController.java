package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.semantic.AgeService;
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


    @Autowired
    public PromptController(AgeService promptService) {
        this.promptService = promptService;
    }

    @PostMapping(path = "/age", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> getAge(@RequestBody PromptRequest request) {
        return promptService.getChatCompletions(request.prompt);
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
