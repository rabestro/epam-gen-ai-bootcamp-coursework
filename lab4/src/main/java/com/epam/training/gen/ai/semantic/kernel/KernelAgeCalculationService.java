package com.epam.training.gen.ai.semantic.kernel;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.epam.training.gen.ai.config.OpenAIProperties;
import com.epam.training.gen.ai.semantic.plugin.AgeCalculatorPlugin;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import org.springframework.stereotype.Service;

import static com.epam.training.gen.ai.semantic.plugin.AgeCalculatorPlugin.AGE_TEMPLATE;
import static com.epam.training.gen.ai.semantic.plugin.AgeCalculatorPlugin.AGE_TEMPLATE_PLUGIN;

@Service
public class KernelAgeCalculationService extends AbstractKernelService {

    private static final String PROMPT = """
            <message role="system">{{system}}</message>
            <message role="user">{{request}}</message>
            """;

    public KernelAgeCalculationService(OpenAIAsyncClient openAIAsyncClient, OpenAIProperties properties) {
        super(openAIAsyncClient, properties);
    }

    @Override
    protected KernelPlugin getPlugin() {
        return KernelPluginFactory.createFromObject(new AgeCalculatorPlugin(), getFunctionName());
    }

    @Override
    protected String getSystemPrompt() {
        return String.format("""
                You are a helpful virtual assistant specialized in calculating people's age.
                The input will provide the birth year, month, and day.
                Use the 'calculateAge' function to return the age in years, months, and days.
                Use the provided template for the response: %s.
                """, getTemplate());
    }

    @Override
    protected String getPrompt() {
        return PROMPT;
    }

    @Override
    protected String getFunctionName() {
        return AGE_TEMPLATE_PLUGIN;
    }

    @Override
    protected String getTemplate() {
        return AGE_TEMPLATE;
    }
}
