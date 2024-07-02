# Module 3: Working with Different Models

This module focuses on working with different models using Azure AI Studio and Semantic Kernel. The tasks include configuring the application to use the Azure OpenAI chat completion service, calling different models from the Dial service, and comparing results for the same prompts using different models and PromptExecutionSettings.

## Learning Objectives

- Understand how to use Azure AI Studio
- Understand the compatibility of Hugging Face
- Write an application that uses different models with Semantic Kernel

## Technologies Used

- Java 21
- Spring Boot 3.3.1
- Semantic Kernel 0.2.13-alpha
- Azure AI OpenAI 1.0.0-beta.8
- Spring Cloud 2023.0.2
- Jackson 2.16.1

## Tasks

### Task 1: Configure the Application

The application is configured to use the Azure OpenAI chat completion service. The necessary settings are added to the `application.properties` file.

### Task 2: Call Other Models from Dial Service

The application is modified to call different models from the Dial service by changing the deployment name. The deployment names are retrieved using the provided API.

### Task 3: Compare Results

The results for the same prompts are compared using different models and PromptExecutionSettings. Observations and differences in the results are documented.

### Task 4: Free Practice

The application is modified to use the Semantic Kernel to generate images using the Imagen model.
