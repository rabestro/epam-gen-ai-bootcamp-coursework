package com.epam.training.gen.ai.config;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.azure.openai.AzureOpenAiEmbeddingModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class OpenAIConfig {
    private final OpenAIProperties properties;

    @Bean
    public OpenAIClient openAIClient() {
        return new OpenAIClientBuilder()
                .credential(new AzureKeyCredential(properties.apiKey()))
                .endpoint(properties.endpoint())
                .buildClient();
    }

    @Bean
    public EmbeddingModel embeddingModel() {
        return new AzureOpenAiEmbeddingModel(openAIClient());
    }
}
