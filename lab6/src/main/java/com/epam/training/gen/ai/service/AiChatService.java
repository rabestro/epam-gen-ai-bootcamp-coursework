package com.epam.training.gen.ai.service;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.FunctionResult;
import com.microsoft.semantickernel.semanticfunctions.KernelFunctionArguments;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AiChatService implements Function<String, Optional<String>> {
    private final EmbeddingService embeddingService;
    private final Kernel kernel;

    public AiChatService(EmbeddingService embeddingService, Kernel kernel) {
        this.embeddingService = embeddingService;
        this.kernel = kernel;
    }

    @Override
    public Optional<String> apply(String question) {
        var context = embeddingService
                .getDocumentsFromVectorDB(question)
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
