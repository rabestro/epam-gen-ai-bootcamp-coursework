package com.epam.training.gen.ai.core;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.models.ChatChoice;
import com.azure.ai.openai.models.ChatCompletions;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatResponseMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

@Component
public class ChatCompletionsProvider implements Function<ChatCompletionsOptions, Stream<String>> {
    private final OpenAIAsyncClient openAIClient;

    @Value("${client-azureopenai-deployment-name}")
    private String modelName;

    public ChatCompletionsProvider(OpenAIAsyncClient openAIClient) {
        this.openAIClient = openAIClient;
    }

    /**
     * Apply method generates chat completions based on the given options.
     * It uses the OpenAIAsyncClient service to interact with the OpenAI Chat Completions API.
     *
     * @param options The chat completions options.
     * @return A stream of chat completions generated based on the options.
     */
    @Override
    public Stream<String> apply(ChatCompletionsOptions options) {
        var completions = openAIClient
                .getChatCompletions(modelName, options)
                .block();
        return Stream.ofNullable(completions)
                .map(ChatCompletions::getChoices)
                .flatMap(List::stream)
                .map(ChatChoice::getMessage)
                .map(ChatResponseMessage::getContent);
    }
}
