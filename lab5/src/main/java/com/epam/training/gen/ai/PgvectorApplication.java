package com.epam.training.gen.ai;

import com.epam.training.gen.ai.config.OpenAIProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(OpenAIProperties.class)
public class PgvectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(PgvectorApplication.class, args);
    }

}