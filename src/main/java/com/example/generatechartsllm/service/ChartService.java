package com.example.generatechartsllm.service;

import com.example.generatechartsllm.model.ChartAnalysis;
import com.example.generatechartsllm.model.ChartRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

@Service
public class ChartService {

    private static final Logger logger = LoggerFactory.getLogger(ChartService.class);

    private final ChatClientService chatClient;
    private final ObjectMapper objectMapper;

    public ChartService(ChatClientService chatClient) {
        this.chatClient = chatClient;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Analyzes the input JSON data using LLM and generates a chart image.
     *
     * @param chartRequest the chart request containing data
     * @return byte array representing the chart image
     */
    public byte[] generateChart(ChartRequest chartRequest) {
        logger.info("Generating chart for request: {}", chartRequest);

        // Get chart analysis from LLM
        ChartAnalysis analysis = analyzeDataWithLLM(chartRequest);
        logger.info("LLM Analysis result: {}", analysis);

        // Generate the chart based on analysis
        JFreeChart chart = createChart(chartRequest, analysis);

        // Convert chart to image bytes
        return convertChartToBytes(chart);
    }

    /**
     * Uses LLM to analyze the chart data and provide recommendations.
     *
     * @param chartRequest the chart request
     * @return ChartAnalysis with LLM recommendations
     */
    public ChartAnalysis analyzeDataWithLLM(ChartRequest chartRequest) {
        String dataDescription = buildDataDescription(chartRequest);

        String prompt = """
                Analyze the following data and provide chart recommendations in JSON format.
                
                Data:
                %s
                
                Please respond with a JSON object containing:
                - recommendedChartType: the best chart type for this data (bar, pie, or line)
                - suggestedTitle: a descriptive title for the chart
                - xAxisLabel: label for x-axis (if applicable)
                - yAxisLabel: label for y-axis (if applicable)
                - insights: brief insights about the data patterns
                
                Respond only with valid JSON, no additional text.
                """.formatted(dataDescription);

        try {
            String response = chatClient.chat(prompt);

            logger.info("LLM Response: {}", response);

            // Parse the JSON response
            return parseAnalysisResponse(response, chartRequest);
        } catch (Exception e) {
            logger.error("Error calling LLM, using defaults", e);
            return getDefaultAnalysis(chartRequest);
        }
    }

    private String buildDataDescription(ChartRequest chartRequest) {
        StringBuilder sb = new StringBuilder();

        if (chartRequest.getChartType() != null) {
            sb.append("Requested chart type: ").append(sanitizeInput(chartRequest.getChartType())).append("\n");
        }
        if (chartRequest.getTitle() != null) {
            sb.append("Title: ").append(sanitizeInput(chartRequest.getTitle())).append("\n");
        }
        if (chartRequest.getData() != null && !chartRequest.getData().isEmpty()) {
            sb.append("Data (key-value pairs):\n");
            chartRequest.getData().forEach((k, v) -> sb.append("  ").append(sanitizeInput(k)).append(": ").append(v).append("\n"));
        }
        if (chartRequest.getLabels() != null && chartRequest.getValues() != null) {
            sb.append("Labels and Values:\n");
            for (int i = 0; i < chartRequest.getLabels().size(); i++) {
                String label = sanitizeInput(chartRequest.getLabels().get(i));
                Double value = i < chartRequest.getValues().size() ? chartRequest.getValues().get(i) : 0.0;
                sb.append("  ").append(label).append(": ").append(value).append("\n");
            }
        }

        return sb.toString();
    }

    /**
     * Sanitizes user input to prevent prompt injection attacks.
     * Removes or escapes potentially dangerous characters and limits length.
     *
     * @param input the user input to sanitize
     * @return sanitized input string
     */
    private String sanitizeInput(String input) {
        if (input == null) {
            return "";
        }
        // Limit input length to prevent excessive data
        String sanitized = input.length() > 100 ? input.substring(0, 100) : input;
        // Remove potentially dangerous characters used in prompt injection
        sanitized = sanitized.replaceAll("[\\r\\n]+", " ");
        sanitized = sanitized.replaceAll("[{}\\[\\]\"'`]", "");
        return sanitized.trim();
    }

    private ChartAnalysis parseAnalysisResponse(String response, ChartRequest chartRequest) {
        try {
            // Clean the response - remove markdown code blocks if present
            String cleanedResponse = response.trim();
            if (cleanedResponse.startsWith("```json")) {
                cleanedResponse = cleanedResponse.substring(7);
            }
            if (cleanedResponse.startsWith("```")) {
                cleanedResponse = cleanedResponse.substring(3);
            }
            if (cleanedResponse.endsWith("```")) {
                cleanedResponse = cleanedResponse.substring(0, cleanedResponse.length() - 3);
            }
            cleanedResponse = cleanedResponse.trim();

            return objectMapper.readValue(cleanedResponse, ChartAnalysis.class);
        } catch (Exception e) {
            logger.warn("Failed to parse LLM response, using defaults: {}", e.getMessage());
            return getDefaultAnalysis(chartRequest);
        }
    }

    private ChartAnalysis getDefaultAnalysis(ChartRequest chartRequest) {
        ChartAnalysis analysis = new ChartAnalysis();
        analysis.setRecommendedChartType(chartRequest.getChartType() != null ? chartRequest.getChartType() : "bar");
        analysis.setSuggestedTitle(chartRequest.getTitle() != null ? chartRequest.getTitle() : "Data Chart");
        analysis.setXAxisLabel("Category");
        analysis.setYAxisLabel("Value");
        analysis.setInsights("Chart generated with default settings");
        return analysis;
    }

    private JFreeChart createChart(ChartRequest chartRequest, ChartAnalysis analysis) {
        String chartType = analysis.getRecommendedChartType() != null
                ? analysis.getRecommendedChartType().toLowerCase()
                : "bar";

        Map<String, Double> dataMap = getDataMap(chartRequest);
        String title = analysis.getSuggestedTitle() != null ? analysis.getSuggestedTitle() : "Chart";

        return switch (chartType) {
            case "pie" -> createPieChart(title, dataMap);
            case "line" -> createLineChart(title, dataMap, analysis);
            default -> createBarChart(title, dataMap, analysis);
        };
    }

    private Map<String, Double> getDataMap(ChartRequest chartRequest) {
        if (chartRequest.getData() != null && !chartRequest.getData().isEmpty()) {
            return chartRequest.getData();
        }

        // Build data map from labels and values
        Map<String, Double> dataMap = new java.util.LinkedHashMap<>();
        List<String> labels = chartRequest.getLabels();
        List<Double> values = chartRequest.getValues();

        if (labels != null && values != null) {
            for (int i = 0; i < labels.size() && i < values.size(); i++) {
                dataMap.put(labels.get(i), values.get(i));
            }
        }

        return dataMap;
    }

    private JFreeChart createBarChart(String title, Map<String, Double> data, ChartAnalysis analysis) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        data.forEach((category, value) -> dataset.addValue(value, "Data", category));

        return ChartFactory.createBarChart(
                title,
                analysis.getXAxisLabel() != null ? analysis.getXAxisLabel() : "Category",
                analysis.getYAxisLabel() != null ? analysis.getYAxisLabel() : "Value",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
    }

    private JFreeChart createPieChart(String title, Map<String, Double> data) {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        data.forEach(dataset::setValue);

        return ChartFactory.createPieChart(
                title,
                dataset,
                true,
                true,
                false
        );
    }

    private JFreeChart createLineChart(String title, Map<String, Double> data, ChartAnalysis analysis) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        data.forEach((category, value) -> dataset.addValue(value, "Data", category));

        return ChartFactory.createLineChart(
                title,
                analysis.getXAxisLabel() != null ? analysis.getXAxisLabel() : "Category",
                analysis.getYAxisLabel() != null ? analysis.getYAxisLabel() : "Value",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
    }

    private byte[] convertChartToBytes(JFreeChart chart) {
        try {
            BufferedImage image = chart.createBufferedImage(800, 600);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ChartUtils.writeBufferedImageAsPNG(baos, image);
            return baos.toByteArray();
        } catch (Exception e) {
            logger.error("Error converting chart to bytes", e);
            throw new RuntimeException("Failed to generate chart image", e);
        }
    }
}
