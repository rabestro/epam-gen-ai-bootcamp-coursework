package com.epam.training.gen.ai.semantic.plugin;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class BingSearchPlugin {
    private static final String URL_TEMPLATE = "https://www.bing.com/search?q=%s";

    @DefineKernelFunction(name = "getBingSearchUrl", description = "Return URL for to a search request at www.bing.com")
    public String getBingSearchUrl(
            @KernelFunctionParameter(description = "Search query", name = "query") String query
    ) {
        var encoded = URLEncoder.encode(query, StandardCharsets.UTF_8);
        return String.format(URL_TEMPLATE, encoded);
    }
}
