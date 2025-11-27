package com.example.generatechartsllm.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

/**
 * Chat client service for interacting with OpenAI API.
 */
@Service
public class ChatClientService {

    private static final Logger logger = LoggerFactory.getLogger(ChatClientService.class);

    private final WebClient webClient;
    private final String model;
    private final ObjectMapper objectMapper;
    private final boolean apiKeyConfigured;

    public ChatClientService(
            @Value("${openai.api.key:}") String apiKey,
            @Value("${openai.api.base-url:https://api.openai.com}") String baseUrl,
            @Value("${openai.api.model:gpt-4o-mini}") String model) {

        this.model = model;
        this.objectMapper = new ObjectMapper();
        this.apiKeyConfigured = apiKey != null && !apiKey.isEmpty() && !apiKey.equals("your-api-key-here");

        if (!apiKeyConfigured) {
            logger.warn("OpenAI API key is not configured. LLM features will use default fallback behavior.");
        }

        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + (apiKeyConfigured ? apiKey : ""))
                .build();

        logger.info("ChatClientService initialized with model: {}", model);
    }

    /**
     * Checks if the API key is properly configured.
     *
     * @return true if API key is configured, false otherwise
     */
    public boolean isApiKeyConfigured() {
        return apiKeyConfigured;
    }

    /**
     * Sends a prompt to the LLM and returns the response.
     *
     * @param prompt the user prompt
     * @return the LLM response content
     * @throws RuntimeException if API key is not configured or API call fails
     */
    public String chat(String prompt) {
        logger.debug("Sending prompt to LLM");

        if (!apiKeyConfigured) {
            throw new RuntimeException("OpenAI API key is not configured");
        }

        try {
            ChatRequest request = new ChatRequest(
                    model,
                    List.of(new Message("user", prompt)),
                    0.7
            );

            ChatResponse response = webClient.post()
                    .uri("/v1/chat/completions")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(ChatResponse.class)
                    .block();

            if (response != null && response.choices() != null && !response.choices().isEmpty()) {
                String content = response.choices().get(0).message().content();
                logger.debug("LLM response: {}", content);
                return content;
            }

            logger.warn("Empty response from LLM");
            return null;
        } catch (Exception e) {
            logger.error("Error calling LLM API", e);
            throw new RuntimeException("Failed to call LLM API: " + e.getMessage(), e);
        }
    }

    // Request/Response DTOs for OpenAI API
    public record ChatRequest(
            String model,
            List<Message> messages,
            double temperature
    ) {}

    public record Message(
            String role,
            String content
    ) {}

    public record ChatResponse(
            String id,
            String object,
            long created,
            String model,
            List<Choice> choices,
            Usage usage
    ) {}

    public record Choice(
            int index,
            Message message,
            @JsonProperty("finish_reason") String finishReason
    ) {}

    public record Usage(
            @JsonProperty("prompt_tokens") int promptTokens,
            @JsonProperty("completion_tokens") int completionTokens,
            @JsonProperty("total_tokens") int totalTokens
    ) {}
}
