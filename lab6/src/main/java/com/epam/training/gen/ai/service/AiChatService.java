package com.epam.training.gen.ai.service;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.FunctionResult;
import com.microsoft.semantickernel.semanticfunctions.KernelFunctionArguments;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AiChatService implements Function<String, Optional<String>> {
    private final Function<String, Stream<Document>> searchService;
    private final Kernel kernel;

    public AiChatService(Function<String, Stream<Document>> searchService, Kernel kernel) {
        this.searchService = searchService;
        this.kernel = kernel;
    }

    @Override
    public Optional<String> apply(String question) {
        var context = searchService
                .apply(question)
                .map(Document::getContent)
                .collect(Collectors.joining());

        var kernelFunctionArguments = KernelFunctionArguments
                .builder()
                .withVariable("context", context)
                .withVariable("question", question)
                .build();

        var answerExecutionContext = kernel
                .invokeAsync("RAG", "AnswerQuestion")
                .withArguments(kernelFunctionArguments)
                .withResultType(String.class)
                .block();

        return Optional.ofNullable(answerExecutionContext)
                .map(FunctionResult::getResult);
    }
}
