package com.epam.training.gen.ai.semantic;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.models.ChatChoice;
import com.azure.ai.openai.models.ChatCompletions;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatRequestUserMessage;
import com.azure.ai.openai.models.ChatResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
public class SimplePromptService {
    public static final String GREETING_MESSAGE = "Say hi";

    private final OpenAIAsyncClient aiClientService;
    @Value("${client-azureopenai-deployment-name}")
    private String deploymentOrModelName;

    public SimplePromptService(OpenAIAsyncClient aiClientService) {
        this.aiClientService = aiClientService;
    }

    public List<String> getChatCompletions() {
        var chatCompletionsOptions = new ChatCompletionsOptions(List.of(new ChatRequestUserMessage(GREETING_MESSAGE)));
        var completions = aiClientService
                .getChatCompletions(deploymentOrModelName, chatCompletionsOptions)
                .block();
        var messages = Stream.ofNullable(completions)
                .map(ChatCompletions::getChoices)
                .flatMap(List::stream)
                .map(ChatChoice::getMessage)
                .map(ChatResponseMessage::getContent)
                .toList();
        log.info(messages.toString());
        return messages;
    }
}
