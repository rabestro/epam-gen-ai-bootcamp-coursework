package com.epam.training.gen.ai.service;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.epam.training.gen.ai.semantic.SemanticKernelBasicService;
import com.epam.training.gen.ai.semantic.plugin.BingSearchPlugin;
import com.microsoft.semantickernel.services.ServiceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BingService extends SemanticKernelBasicService {

    public BingService(
            OpenAIAsyncClient client,
            @Value("${client-azureopenai.deployment-name}") String modelId
    ) throws ServiceNotFoundException {
        super(client, modelId);
    }

    @Override
    protected Object getPlugin() {
        return new BingSearchPlugin();
    }

    @Override
    protected String getSystemPrompt() {
        return """
                You will be given a search query.
                Your task is to create a URL for given search query at www.bing.com.
                """;
    }
}
