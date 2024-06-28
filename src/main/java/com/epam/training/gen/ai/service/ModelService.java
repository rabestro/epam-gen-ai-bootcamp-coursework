package com.epam.training.gen.ai.service;


import com.epam.training.gen.ai.client.AzureOpenAiClient;
import com.epam.training.gen.ai.dto.Deployment;
import com.epam.training.gen.ai.exception.OpenAiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModelService {
    private final AzureOpenAiClient azureOpenAiClient;
    @Value("${client-azureopenai-key}")
    private String clientAzureOpenAiKey;

    public List<String> getModels() {
        var response = azureOpenAiClient.getDeployments(clientAzureOpenAiKey);

        if (response == null) {
            throw new OpenAiException("Failed to get model list");
        }

        return response.getData()
                .stream()
                .map(Deployment::getModel)
                .toList();
    }
}
