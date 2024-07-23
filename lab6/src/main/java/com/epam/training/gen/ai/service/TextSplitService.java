package com.epam.training.gen.ai.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
public class TextSplitService implements Function<String, List<String>> {
    public static final int MAX_LENGTH = 7500;
    public static final int SEARCH_START_OFFSET = 300;

    private int findSplitIndex(String text) {
        for (int i = MAX_LENGTH - SEARCH_START_OFFSET; i < MAX_LENGTH; i++) {
            if (isPunctuation(text.charAt(i))) return i;
        }
        return MAX_LENGTH;
    }

    private boolean isPunctuation(char c) {
        return ".:;?!".indexOf(c) >= 0;
    }

    @Override
    public List<String> apply(String text) {
        var chunks = new ArrayList<String>();
        while (text.length() > MAX_LENGTH) {
            int splitIndex = findSplitIndex(text);
            chunks.add(text.substring(0, splitIndex));
            text = text.substring(splitIndex);
        }
        chunks.add(text);
        return chunks;
    }
}
