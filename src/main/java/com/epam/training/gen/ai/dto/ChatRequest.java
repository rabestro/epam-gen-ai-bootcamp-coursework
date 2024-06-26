package com.epam.training.gen.ai.dto;

import com.epam.training.gen.ai.validator.ValidDeploymentName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class ChatRequest {

    @NotNull(message = "Prompt cannot be null")
    @JsonProperty("prompt")
    private String input;

    @Min(value = 1, message = "maxTokens must be at least 1")
    @Max(value = Integer.MAX_VALUE, message = "maxTokens must be less than or equal to " + Integer.MAX_VALUE)
    @JsonProperty("max_tokens")
    private Integer maxTokens = 256;

    @Min(value = 0, message = "temperature must be at least 0.0")
    @Max(value = 2, message = "temperature must be less than or equal to 2.0")
    @JsonProperty("temperature")
    private Double temperature = 1.0;

    @ValidDeploymentName
    @JsonProperty("model")
    private String deploymentName = "gpt-35-turbo";
}
