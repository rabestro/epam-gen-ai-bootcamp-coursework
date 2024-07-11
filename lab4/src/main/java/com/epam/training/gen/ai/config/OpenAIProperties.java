package com.epam.training.gen.ai.config;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "client-azureopenai")
public record OpenAIProperties(String deploymentName, String key, String endpoint) {
}
