package com.epam.training.gen.ai.service.text;

import org.springframework.stereotype.Service;

import java.util.function.UnaryOperator;

@Service
public class TextCleanupService implements UnaryOperator<String> {
    @Override
    public String apply(String text) {
        return text
                .replace("\n", " ")
                .replaceAll("\\s{2,}", " ");
    }
}
