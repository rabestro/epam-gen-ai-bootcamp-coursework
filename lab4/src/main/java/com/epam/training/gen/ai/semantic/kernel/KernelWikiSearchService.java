package com.epam.training.gen.ai.semantic.kernel;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.epam.training.gen.ai.config.OpenAIProperties;
import com.epam.training.gen.ai.semantic.plugin.WikiSearchUrlPlugin;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import org.springframework.stereotype.Service;

import static com.epam.training.gen.ai.semantic.plugin.WikiSearchUrlPlugin.WIKIPEDIA_SEARCH_URL_PLUGIN;
import static com.epam.training.gen.ai.semantic.plugin.WikiSearchUrlPlugin.WIKIPEDIA_URL_TEMPLATE;

@Service
public class KernelWikiSearchService extends AbstractKernelService {

    private static final String PROMPT = """
            <message role="system">{{system}}</message>
            <message role="user">{{request}}</message>
            """;

    public KernelWikiSearchService(OpenAIAsyncClient openAIAsyncClient, OpenAIProperties properties) {
        super(openAIAsyncClient, properties);
    }

    @Override
    protected KernelPlugin getPlugin() {
        return KernelPluginFactory.createFromObject(new WikiSearchUrlPlugin(), getPluginName());
    }

    @Override
    protected String getSystemPrompt() {
        return String.format("""
                You are a virtual assistant that helps users create Wikipedia search links.
                The input will be a search query.
                Use the 'getWikipediaSearchUrl' function to generate a valid Wikipedia search URL.
                Use the provided template for the response: %s.
                """, getTemplate());
    }

    @Override
    protected String getPrompt() {
        return PROMPT;
    }

    @Override
    protected String getPluginName() {
        return WIKIPEDIA_SEARCH_URL_PLUGIN;
    }

    @Override
    protected String getTemplate() {
        return WIKIPEDIA_URL_TEMPLATE;
    }
}
