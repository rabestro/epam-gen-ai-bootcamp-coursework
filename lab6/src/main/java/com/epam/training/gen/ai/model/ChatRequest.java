package com.epam.training.gen.ai.model;

import jakarta.validation.constraints.NotBlank;

public record ChatRequest(
        @NotBlank(message = "Query cannot be blank") String query) {
}
