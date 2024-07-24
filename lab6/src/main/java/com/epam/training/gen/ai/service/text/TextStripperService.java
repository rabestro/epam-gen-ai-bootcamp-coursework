package com.epam.training.gen.ai.service.text;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class TextStripperService implements Function<PDDocument, Stream<String>> {

    @Override
    public Stream<String> apply(PDDocument pdDocument) {
        var textStripper = new PDFTextStripper();
        var numberOfPages = pdDocument.getNumberOfPages();
        return IntStream.rangeClosed(1, numberOfPages).mapToObj(
                pageNumber -> {
                    try {
                        textStripper.setStartPage(pageNumber);
                        textStripper.setEndPage(pageNumber);
                        return textStripper.getText(pdDocument);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
