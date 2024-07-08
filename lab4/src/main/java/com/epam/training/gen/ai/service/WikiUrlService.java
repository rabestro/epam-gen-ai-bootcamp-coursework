package com.epam.training.gen.ai.service;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.epam.training.gen.ai.semantic.SemanticKernelBasicService;
import com.epam.training.gen.ai.semantic.plugin.SearchUrlPlugin;
import com.microsoft.semantickernel.services.ServiceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WikiUrlService extends SemanticKernelBasicService {

    public WikiUrlService(
            OpenAIAsyncClient client,
            @Value("${client-azureopenai.deployment-name}") String modelId
    ) throws ServiceNotFoundException {
        super(client, modelId);
    }

    @Override
    protected Object getPlugin() {
        return new SearchUrlPlugin();
    }

    @Override
    protected String getSystemPrompt() {
        return "You will be given a search query. Your task is to create URL for Wikipedia search";
    }
}
