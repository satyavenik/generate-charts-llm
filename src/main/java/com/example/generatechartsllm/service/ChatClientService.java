package com.example.generatechartsllm.service;

import com.example.generatechartsllm.model.ChartAnalysis;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class ChatClientService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${openai.model:gpt-4}")
    private String model;

    public ChatClientService(@Value("${openai.api.url}") String apiUrl,
                             @Value("${openai.api.key}") String apiKey) {
        this.webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public ChartAnalysis analyzeDataForChart(JsonNode data, String userTitle, String userDescription) {
        String prompt = buildPrompt(data, userTitle, userDescription);

        log.info("Sending request to LLM for chart analysis");

        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            requestBody.put("messages", List.of(
                    Map.of("role", "system", "content", "You are a data visualization expert. Analyze the given JSON data and recommend the best chart type with configuration in valid JSON format."),
                    Map.of("role", "user", "content", prompt)
            ));
            requestBody.put("temperature", 0.7);

            String response = webClient.post()
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.debug("LLM Response: {}", response);

            return parseResponse(response);
        } catch (Exception e) {
            log.error("Error calling LLM API: {}", e.getMessage(), e);
            // Return a default chart analysis as fallback
            return createFallbackAnalysis(data, userTitle);
        }
    }

    private String buildPrompt(JsonNode data, String userTitle, String userDescription) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Analyze the following JSON data and recommend the best chart type to visualize it.\n\n");
        prompt.append("Data:\n").append(data.toPrettyString()).append("\n\n");

        if (userTitle != null && !userTitle.isEmpty()) {
            prompt.append("User Title: ").append(userTitle).append("\n");
        }
        if (userDescription != null && !userDescription.isEmpty()) {
            prompt.append("User Description: ").append(userDescription).append("\n");
        }

        prompt.append("\nProvide your response as a JSON object with the following structure:\n");
        prompt.append("{\n");
        prompt.append("  \"chartType\": \"BAR\" or \"LINE\" or \"PIE\" or \"SCATTER\",\n");
        prompt.append("  \"title\": \"Chart title\",\n");
        prompt.append("  \"xAxisLabel\": \"X-axis label\",\n");
        prompt.append("  \"yAxisLabel\": \"Y-axis label\",\n");
        prompt.append("  \"categories\": [\"category1\", \"category2\", ...],\n");
        prompt.append("  \"series\": {\"series1\": [value1, value2, ...], \"series2\": [...]},\n");
        prompt.append("  \"reasoning\": \"Brief explanation of why this chart type is recommended\"\n");
        prompt.append("}\n\n");
        prompt.append("Extract numerical data from the JSON and organize it appropriately for the chart.");

        return prompt.toString();
    }

    private ChartAnalysis parseResponse(String response) throws Exception {
        JsonNode rootNode = objectMapper.readTree(response);
        String content = rootNode.path("choices").get(0).path("message").path("content").asText();

        log.debug("LLM Content: {}", content);

        // Extract JSON from markdown code blocks if present
        String jsonContent = extractJson(content);

        JsonNode analysisNode = objectMapper.readTree(jsonContent);

        ChartAnalysis analysis = new ChartAnalysis();
        analysis.setChartType(analysisNode.path("chartType").asText("BAR"));
        analysis.setTitle(analysisNode.path("title").asText("Chart"));
        analysis.setXAxisLabel(analysisNode.path("xAxisLabel").asText("X Axis"));
        analysis.setYAxisLabel(analysisNode.path("yAxisLabel").asText("Y Axis"));
        analysis.setReasoning(analysisNode.path("reasoning").asText(""));

        // Parse categories
        List<String> categories = new ArrayList<>();
        JsonNode categoriesNode = analysisNode.path("categories");
        if (categoriesNode.isArray()) {
            categoriesNode.forEach(cat -> categories.add(cat.asText()));
        }
        analysis.setCategories(categories);

        // Parse series
        Map<String, List<Double>> series = new HashMap<>();
        JsonNode seriesNode = analysisNode.path("series");
        if (seriesNode.isObject()) {
            seriesNode.fields().forEachRemaining(entry -> {
                List<Double> values = new ArrayList<>();
                entry.getValue().forEach(val -> values.add(val.asDouble()));
                series.put(entry.getKey(), values);
            });
        }
        analysis.setSeries(series);

        return analysis;
    }

    private String extractJson(String content) {
        // Try to extract JSON from markdown code blocks
        Pattern pattern = Pattern.compile("```(?:json)?\\s*([\\s\\S]*?)```");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }

        // If no code block, try to find JSON object directly
        int startIdx = content.indexOf('{');
        int endIdx = content.lastIndexOf('}');
        if (startIdx >= 0 && endIdx > startIdx) {
            return content.substring(startIdx, endIdx + 1);
        }

        return content;
    }

    private ChartAnalysis createFallbackAnalysis(JsonNode data, String userTitle) {
        log.warn("Using fallback chart analysis");

        ChartAnalysis analysis = new ChartAnalysis();
        analysis.setChartType("BAR");
        analysis.setTitle(userTitle != null ? userTitle : "Data Visualization");
        analysis.setXAxisLabel("Categories");
        analysis.setYAxisLabel("Values");
        analysis.setReasoning("Fallback analysis due to LLM service unavailability");

        // Simple parsing attempt
        List<String> categories = new ArrayList<>();
        Map<String, List<Double>> series = new HashMap<>();
        List<Double> values = new ArrayList<>();

        if (data.isArray()) {
            int index = 0;
            for (JsonNode item : data) {
                categories.add("Item " + (++index));
                if (item.isNumber()) {
                    values.add(item.asDouble());
                } else if (item.isObject()) {
                    item.fields().forEachRemaining(field -> {
                        if (field.getValue().isNumber()) {
                            values.add(field.getValue().asDouble());
                        }
                    });
                }
            }
        } else if (data.isObject()) {
            data.fields().forEachRemaining(field -> {
                categories.add(field.getKey());
                if (field.getValue().isNumber()) {
                    values.add(field.getValue().asDouble());
                }
            });
        }

        series.put("Series 1", values);
        analysis.setCategories(categories);
        analysis.setSeries(series);

        return analysis;
    }
}

