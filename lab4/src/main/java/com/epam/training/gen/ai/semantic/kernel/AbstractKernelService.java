package com.epam.training.gen.ai.semantic.kernel;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.epam.training.gen.ai.config.OpenAIProperties;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.orchestration.FunctionResult;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.orchestration.ToolCallBehavior;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.semanticfunctions.KernelFunction;
import com.microsoft.semantickernel.semanticfunctions.KernelFunctionArguments;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public abstract class AbstractKernelService {
    private static final double TEMPERATURE = 0.2;
    private final OpenAIAsyncClient openAIAsyncClient;
    private final OpenAIProperties properties;

    protected abstract KernelPlugin getPlugin();

    protected abstract String getSystemPrompt();

    protected abstract String getPrompt();

    protected abstract String getFunctionName();

    protected abstract String getTemplate();

    public Optional<String> getKernelFunctionalResponse(String message) {
        var chatCompletionService = OpenAIChatCompletion.builder()
                .withModelId(properties.deploymentName())
                .withOpenAIAsyncClient(openAIAsyncClient)
                .build();

        KernelPlugin plugin = getPlugin();

        var kernel = Kernel.builder()
                .withAIService(ChatCompletionService.class, chatCompletionService)
                .withPlugin(plugin)
                .build();

        var getIntent = KernelFunction.<String>createFromPrompt(getPrompt())
                .withTemplateFormat("handlebars")
                .build();

        var arguments = KernelFunctionArguments.builder()
                .withVariable("system", getSystemPrompt())
                .withVariable("request", message)
                .build();

        var promptExecutionSettings = PromptExecutionSettings.builder()
                .withTemperature(TEMPERATURE)
                .build();

        var block = kernel
                .invokeAsync(getIntent)
                .withPromptExecutionSettings(promptExecutionSettings)
                .withArguments(arguments)
                .withToolCallBehavior(ToolCallBehavior.allowOnlyKernelFunctions(true, plugin.get(getFunctionName())))
                .block();

        return Optional.ofNullable(block)
                .map(FunctionResult::getResult);
    }
}
