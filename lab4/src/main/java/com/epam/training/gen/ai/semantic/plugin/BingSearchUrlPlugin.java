package com.epam.training.gen.ai.semantic.plugin;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class BingSearchUrlPlugin {

    public static final String BING_SEARCH_URL_PLUGIN = "getBingSearchUrl";
    public static final String BING_URL_TEMPLATE = "https://www.bing.com/search?q=%s";

    @DefineKernelFunction(name = BING_SEARCH_URL_PLUGIN, description = "Return URL for Bing search query.")
    public String getBingSearchUrl(
            @KernelFunctionParameter(description = "Text to search for", name = "query") String query) {
        String encoded = URLEncoder.encode(query, StandardCharsets.UTF_8);
        return String.format(BING_URL_TEMPLATE, encoded);
    }
}