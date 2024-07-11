package com.epam.training.gen.ai.semantic;

import java.util.Optional;

@FunctionalInterface
public interface AiService {
    Optional<String> getKernelFunctionalResponse(String message);
}
