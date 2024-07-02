package com.epam.training.gen.ai.config;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAIConfig {
    @Value("${client-azureopenai-key}")
    private String azureClientKey;

    @Value("${client-azureopenai-endpoint}")
    private String azureClientEndpoint;

    @Bean
    public OpenAIAsyncClient openAIAsyncClient() {
        return new OpenAIClientBuilder()
                .credential(new AzureKeyCredential(azureClientKey))
                .endpoint(azureClientEndpoint)
                .buildAsyncClient();
    }

    @Bean
    public ChatCompletionService.Builder chatCompletionServiceBuilder(OpenAIAsyncClient openAIAsyncClient) {
        return ChatCompletionService.builder()
                .withOpenAIAsyncClient(openAIAsyncClient);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public PromptExecutionSettings promptExecutionSettings() {
        return PromptExecutionSettings.builder()
                .withMaxTokens(1_000)
                .withTemperature(0.0)
                .build();
    }
}
