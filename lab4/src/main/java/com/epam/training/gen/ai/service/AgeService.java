package com.epam.training.gen.ai.service;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.epam.training.gen.ai.semantic.SemanticKernelBasicService;
import com.epam.training.gen.ai.semantic.plugin.AgeCalculatorPlugin;
import com.microsoft.semantickernel.services.ServiceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AgeService extends SemanticKernelBasicService {

    public AgeService(OpenAIAsyncClient client,
                      @Value("${client-azureopenai-deployment-name}") String modelId) throws ServiceNotFoundException {
        super(client, modelId);
    }

    @Override
    protected Object getPlugin() {
        return new AgeCalculatorPlugin();
    }

    @Override
    protected String getSystemPrompt() {
        return """
                You will be provided with a date.
                Convert year, month and day into an integer.
                Your task is to calculate the age
                """;
    }
}
