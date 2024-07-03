package com.epam.training.gen.ai.semantic;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.epam.training.gen.ai.service.AiService;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatMessageContent;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIFunctionToolCall;
import com.microsoft.semantickernel.orchestration.FunctionResult;
import com.microsoft.semantickernel.orchestration.FunctionResultMetadata;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.InvocationReturnMode;
import com.microsoft.semantickernel.orchestration.ToolCallBehavior;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import com.microsoft.semantickernel.services.ServiceNotFoundException;
import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public abstract class SemanticKernelBasicService implements AiService {
    private final Kernel kernel;
    private final ChatCompletionService chatCompletionService;
    private ChatHistory chatHistory = new ChatHistory(getSystemPrompt() + " Use 'custom_plugin' to complete the task."
                                                      + "Include the result of the function call into your response.");

    public SemanticKernelBasicService(OpenAIAsyncClient client, String modelId) throws ServiceNotFoundException {
        var openAIChatCompletion = ChatCompletionService.builder()
                .withOpenAIAsyncClient(client)
                .withModelId(modelId)
                .build();
        var plugin = KernelPluginFactory.createFromObject(getPlugin(), "custom_plugin");
        kernel = Kernel.builder()
                .withAIService(ChatCompletionService.class, openAIChatCompletion)
                .withPlugin(plugin)
                .build();
        chatCompletionService = kernel.getService(ChatCompletionService.class);
    }

    protected abstract Object getPlugin();

    protected abstract String getSystemPrompt();

    @Override
    public List<String> getChatCompletions(String userMessage) {
        chatHistory.addUserMessage(userMessage);
        var assistantResponse = chatCompletionService.getChatMessageContentsAsync(chatHistory, kernel,
                InvocationContext.builder().withToolCallBehavior(ToolCallBehavior.allowAllKernelFunctions(false))
                        .withReturnMode(InvocationReturnMode.FULL_HISTORY).build()).block();
        if (assistantResponse == null) {
            return List.of("Assistant could not provide response");
        }
        chatHistory = new ChatHistory(assistantResponse);
        Optional<ChatMessageContent<?>> lastMessage = chatHistory.getLastMessage();

        if (lastMessage.isPresent()) {
            handleToolCalls(lastMessage.get());
        } else {
            return List.of("Assistant could not provide response");
        }
        return chatHistory.getMessages().stream()
                .map(chatItem -> chatItem.getAuthorRole() + ": " + chatItem.getContent())
                .toList();
    }

    private void handleToolCalls(ChatMessageContent<?> message) {
        if (message instanceof OpenAIChatMessageContent) {
            var toolCalls = ((OpenAIChatMessageContent<?>) message).getToolCall();
            if (toolCalls != null && !toolCalls.isEmpty()) {
                toolCalls.stream().filter(Objects::nonNull).forEach(this::handleToolCall);
                var messages = chatCompletionService.getChatMessageContentsAsync(chatHistory, kernel,
                        InvocationContext.builder()
                                .withToolCallBehavior(ToolCallBehavior.allowAllKernelFunctions(false))
                                .withReturnMode(InvocationReturnMode.FULL_HISTORY)
                                .build()).block();
                chatHistory = new ChatHistory(messages);
            }
        }
    }

    private void handleToolCall(OpenAIFunctionToolCall toolCall) {
        try {
            var function = kernel.getFunction(toolCall.getPluginName(), toolCall.getFunctionName());
            FunctionResult<?> result = function.invokeAsync(kernel, toolCall.getArguments(), null, null)
                    .block();
            if (result != null) {
                chatHistory.addMessage(AuthorRole.TOOL, String.valueOf(result.getResult()),
                        StandardCharsets.UTF_8, FunctionResultMetadata.build(toolCall.getId()));
            }
        } catch (IllegalArgumentException e) {
            log.error("Function call failed.", e);
        }
    }
}
