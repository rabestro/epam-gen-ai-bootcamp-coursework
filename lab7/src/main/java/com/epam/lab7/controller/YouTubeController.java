package com.epam.lab7.v1.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/youtube")
public class YouTubeController {
    private final ChatClient chatClient;
    @Value("classpath:/prompts/youtube.txt")
    private Resource ytPromptResource;

    public YouTubeController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/popular")
    public String findPopularYouTubersStepOne(@RequestParam(value = "genre", defaultValue = "tech") String genre) {
        var message = """
            List 10 of the most popular YouTubers in {genre} along with their current subscriber counts. If you don't know
            the answer, just say "I don't know".
            """;

        return chatClient.prompt()
                .user(u -> u.text(message).param("genre",genre))
                .call()
                .content();
    }

    @GetMapping("/popular-resource")
    public String findPopularYouTubers(@RequestParam(value = "genre", defaultValue = "tech") String genre) {
        return chatClient.prompt()
                .user(u -> u.text(ytPromptResource).param("genre",genre))
                .call()
                .content();
    }
}
