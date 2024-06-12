package com.epam.training.gen.ai.semantic;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.models.ChatChoice;
import com.azure.ai.openai.models.ChatCompletions;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatRequestUserMessage;
import com.azure.ai.openai.models.ChatResponseMessage;
import com.epam.training.gen.ai.dto.ChatResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class SinglePromptService {
    private final OpenAIAsyncClient aiClientService;
    @Value("${client-azureopenai-deployment-name}")
    private String modelName;

    public SinglePromptService(OpenAIAsyncClient aiClientService) {
        this.aiClientService = aiClientService;
    }

    public ChatResponse getSinglePromptChatCompletions(String input) {
        var chatCompletionsOptions = new ChatCompletionsOptions(List.of(new ChatRequestUserMessage(input)));
        var completions = aiClientService
                .getChatCompletions(modelName, chatCompletionsOptions)
                .block();
        var messages = Stream.ofNullable(completions)
                .map(ChatCompletions::getChoices)
                .flatMap(List::stream)
                .map(ChatChoice::getMessage)
                .map(ChatResponseMessage::getContent)
                .toList();
        return new ChatResponse(messages);
    }
}
