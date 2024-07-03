package com.epam.training.gen.ai.semantic;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.models.ChatChoice;
import com.azure.ai.openai.models.ChatCompletions;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatRequestFunctionMessage;
import com.azure.ai.openai.models.ChatRequestSystemMessage;
import com.azure.ai.openai.models.ChatRequestUserMessage;
import com.azure.ai.openai.models.ChatResponseMessage;
import com.epam.training.gen.ai.dto.ChatResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
public class FunctionMessagePromptService {
    private final OpenAIAsyncClient aiClientService;
    @Value("${client-azureopenai-deployment-name}")
    private String deploymentOrModelName;

    public FunctionMessagePromptService(OpenAIAsyncClient aiClientService) {
        this.aiClientService = aiClientService;
    }

    public ChatResponse getChatCompletionsWithFunction(String userInput) {
        var functionName = "ContextMessage";
        var functionContent = "Focus on historical accuracy";
        var functionMessage = new ChatRequestFunctionMessage(functionName, functionContent);
        var systemMessage = new ChatRequestSystemMessage("""
                Include only the required numbered list of titles and authors in your response.
                Do not add any comments or other information.""");

        var chatCompletionsOptions = new ChatCompletionsOptions(List.of(
                new ChatRequestUserMessage(userInput),
                functionMessage,
                systemMessage
        ));
        var completions = aiClientService
                .getChatCompletions(deploymentOrModelName, chatCompletionsOptions)
                .block();

        return getChatResponse(userInput, completions);
    }

    public ChatResponse getChatCompletionsWithFunctionAndChangedSettings(String userInput) {
        var functionName = "ContextMessage";
        var functionContent = "Focus on historical accuracy";
        var functionMessage = new ChatRequestFunctionMessage(functionName, functionContent);
        var systemMessage = new ChatRequestSystemMessage("Include only the required numbered list of titles and authors in your response. Do not add any comments or other information.");

        ChatCompletions completions = aiClientService.getChatCompletions(
                deploymentOrModelName,
                new ChatCompletionsOptions(List.of(
                        new ChatRequestUserMessage(userInput),
                        functionMessage,
                        systemMessage
                ))
                        .setMaxTokens(300)
                        .setTemperature(0.2)
                        .setTopP(0.9)
                        .setFrequencyPenalty(0.5)
                        .setPresencePenalty(0.5)
        ).block();

        return getChatResponse(userInput, completions);
    }

    private ChatResponse getChatResponse(String userInput, ChatCompletions completions) {
        var content = Stream.ofNullable(completions)
                .map(ChatCompletions::getChoices)
                .flatMap(List::stream)
                .findFirst()
                .map(ChatChoice::getMessage)
                .map(ChatResponseMessage::getContent)
                .orElse("No content available");

        var messages = content.lines().toList();
        var validationStatus = validateResponse(content, userInput);

        log.info("Received messages: {}", messages);
        log.info("Validation status: {}", validationStatus);
        return new ChatResponse(messages);
    }

    private String validateResponse(String content, String originalRequest) {
        var hasTenItems = (content.split("\\n").length == 10);
        var aiValidation = requestAIValidation(content, originalRequest);

        log.info("AI validation. Is valid: {}", aiValidation);
        log.info("Item number validation. Is valid: {}", hasTenItems);

        if (hasTenItems && aiValidation) {
            return "Valid";
        } else if (hasTenItems || aiValidation) {
            return "Partially valid";
        } else {
            return "Invalid";
        }
    }

    private boolean requestAIValidation(String content, String originalRequest) {
        var userInput = "Is this response valid? Request: ```" + originalRequest + "```. Response: ```" + content + "```";
        var systemMessage = new ChatRequestSystemMessage("Include only `Yes` or `No`");

        var completions = aiClientService.getChatCompletions(
                        deploymentOrModelName,
                        new ChatCompletionsOptions(
                                List.of(
                                        new ChatRequestUserMessage(userInput),
                                        systemMessage
                                ))
                )
                .block();
        var answer = completions.getChoices().getFirst().getMessage().getContent();
        log.info("AI validation response: {}", answer);
        return answer.contains("Yes");
    }
}
