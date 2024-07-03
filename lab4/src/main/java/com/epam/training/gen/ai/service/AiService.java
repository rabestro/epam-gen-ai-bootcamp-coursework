package com.epam.training.gen.ai.service;

import java.util.List;

@FunctionalInterface
public interface AiService {
    List<String> getChatCompletions(String userMessage);
}
