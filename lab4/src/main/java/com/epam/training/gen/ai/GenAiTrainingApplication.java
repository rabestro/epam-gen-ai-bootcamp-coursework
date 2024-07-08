package com.epam.training.gen.ai;

import com.epam.training.gen.ai.config.OpenAIProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableConfigurationProperties(OpenAIProperties.class)
@PropertySource("classpath:/config/application.properties")
public class GenAiTrainingApplication {

    public static void main(String[] args) {
        SpringApplication.run(GenAiTrainingApplication.class, args);
    }
}
