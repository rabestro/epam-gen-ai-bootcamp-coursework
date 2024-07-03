package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.service.AgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AgeController {
    private final AgeService ageService;

    @Autowired
    public AgeController(AgeService ageService) {
        this.ageService = ageService;
    }

    @PostMapping(path = "/age", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> getAge(@RequestBody PromptRequest request) {
        return ageService.getChatCompletions(request.prompt);
    }

     public record PromptRequest(String prompt) {
    }
}
