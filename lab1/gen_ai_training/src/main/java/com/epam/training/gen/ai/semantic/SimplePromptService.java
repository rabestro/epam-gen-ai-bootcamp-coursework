package com.epam.training.gen.ai.semantic;

import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatRequestUserMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

@Service
public class SimplePromptService {
    public static final String GREETING_MESSAGE = "Hello!";

    private final Function<ChatCompletionsOptions, Stream<String>> completionsStream;

    public SimplePromptService(Function<ChatCompletionsOptions, Stream<String>> completionsStream) {
        this.completionsStream = completionsStream;
    }

    public List<String> getChatCompletions() {
        var userMessageOptions = new ChatCompletionsOptions(List.of(new ChatRequestUserMessage(GREETING_MESSAGE)));
        return completionsStream.apply(userMessageOptions).toList();
    }
}
