package com.epam.training.gen.ai.semantic;

import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatRequestUserMessage;
import com.epam.training.gen.ai.dto.ChatResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

@Service
public class SinglePromptService {
    private final Function<ChatCompletionsOptions, Stream<String>> completionsStream;

    public SinglePromptService(Function<ChatCompletionsOptions, Stream<String>> completionsStream) {
        this.completionsStream = completionsStream;
    }

    public ChatResponse getSinglePromptChatCompletions(String input) {
        var chatCompletionsOptions = new ChatCompletionsOptions(List.of(new ChatRequestUserMessage(input)));
        var messages = completionsStream.apply(chatCompletionsOptions).toList();
        return new ChatResponse(messages);
    }
}
