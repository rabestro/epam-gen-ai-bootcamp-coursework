package com.epam.training.gen.ai.semantic.kernel;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.epam.training.gen.ai.config.OpenAIProperties;
import com.epam.training.gen.ai.semantic.plugin.BingSearchUrlPlugin;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import org.springframework.stereotype.Service;

import static com.epam.training.gen.ai.semantic.plugin.BingSearchUrlPlugin.BING_SEARCH_URL_PLUGIN;
import static com.epam.training.gen.ai.semantic.plugin.BingSearchUrlPlugin.BING_URL_TEMPLATE;

@Service
public class KernelBingSearchService extends AbstractKernelService {

    private static final String PROMPT = """
            <message role="system">{{system}}</message>
            <message role="user">{{request}}</message>
            """;

    public KernelBingSearchService(OpenAIAsyncClient openAIAsyncClient, OpenAIProperties properties) {
        super(openAIAsyncClient, properties);
    }

    @Override
    protected KernelPlugin getPlugin() {
        return KernelPluginFactory.createFromObject(new BingSearchUrlPlugin(), getPluginName());
    }

    @Override
    protected String getSystemPrompt() {
        return String.format("""
                You are a virtual assistant designed to help users create Bing search links.
                The input will be a search query.
                Use the 'getBingSearchUrl' function to generate a valid Bing search URL.
                Use the provided template for the response: %s.
                """, getTemplate());
    }

    @Override
    protected String getPrompt() {
        return PROMPT;
    }

    @Override
    protected String getPluginName() {
        return BING_SEARCH_URL_PLUGIN;
    }

    @Override
    protected String getTemplate() {
        return BING_URL_TEMPLATE;
    }
}
