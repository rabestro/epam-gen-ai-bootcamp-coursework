package com.epam.training.gen.ai.config;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAiConfig {
    private final OpenAIProperties aiProperties;
    @Value("${spring.ai.azure.openai.chat.options.deployment-name}")
    private String deploymentName;

    public OpenAiConfig(OpenAIProperties aiProperties) {
        this.aiProperties = aiProperties;
    }

    @Bean
    public OpenAIAsyncClient openAiAsyncClient() {
        return new OpenAIClientBuilder()
                .credential(new AzureKeyCredential(aiProperties.apiKey()))
                .endpoint(aiProperties.endpoint())
                .buildAsyncClient();
    }

    @Bean
    public Kernel ragSemanticKernel() {
        var ragPlugIn = KernelPluginFactory.importPluginFromResourcesDirectory(
                "plugins", "RAG", "AnswerQuestion", null, String.class);
        var chatService = ChatCompletionService.builder()
                .withModelId(deploymentName)
                .withOpenAIAsyncClient(openAiAsyncClient())
                .build();
        return Kernel.builder()
                .withAIService(ChatCompletionService.class, chatService)
                .withPlugin(ragPlugIn)
                .build();
    }
}
