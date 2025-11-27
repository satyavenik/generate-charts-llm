package com.example.generatechartsllm.controller;

import com.example.generatechartsllm.model.ChartAnalysis;
import com.example.generatechartsllm.model.ChartRequest;
import com.example.generatechartsllm.service.ChartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/charts")
@Tag(name = "Chart Generator", description = "API for generating charts from JSON data using LLM")
public class ChartController {

    private static final Logger logger = LoggerFactory.getLogger(ChartController.class);

    private final ChartService chartService;

    public ChartController(ChartService chartService) {
        this.chartService = chartService;
    }

    @Operation(
            summary = "Generate a chart image from JSON data",
            description = "Accepts JSON data and uses LLM to analyze the data and generate an appropriate chart image"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Chart image generated successfully",
                    content = @Content(mediaType = MediaType.IMAGE_PNG_VALUE)
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @PostMapping(value = "/generate", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateChart(@RequestBody ChartRequest chartRequest) {
        logger.info("Received chart generation request: {}", chartRequest);

        try {
            byte[] chartImage = chartService.generateChart(chartRequest);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(chartImage);
        } catch (Exception e) {
            logger.error("Error generating chart", e);
            throw new RuntimeException("Failed to generate chart: " + e.getMessage(), e);
        }
    }

    @Operation(
            summary = "Analyze JSON data with LLM",
            description = "Uses LLM to analyze the provided data and return chart recommendations without generating the image"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Analysis completed successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ChartAnalysis.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @PostMapping(value = "/analyze", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChartAnalysis> analyzeData(@RequestBody ChartRequest chartRequest) {
        logger.info("Received data analysis request: {}", chartRequest);

        ChartAnalysis analysis = chartService.analyzeDataWithLLM(chartRequest);
        return ResponseEntity.ok(analysis);
    }
}
