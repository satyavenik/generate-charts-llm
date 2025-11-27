package com.example.generatechartsllm.controller;

import com.example.generatechartsllm.model.ChartAnalysis;
import com.example.generatechartsllm.model.ChartRequest;
import com.example.generatechartsllm.service.ChatClientService;
import com.example.generatechartsllm.service.ChartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/charts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Chart Generation", description = "APIs for generating charts from JSON data using LLM analysis")
public class ChartController {

    private final ChatClientService chatClientService;
    private final ChartService chartService;

    @PostMapping(value = "/generate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.IMAGE_PNG_VALUE)
    @Operation(summary = "Generate a chart from JSON data",
               description = "Accepts JSON data, uses LLM to analyze and recommend the best chart type, then generates and returns the chart as PNG image")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Chart generated successfully",
                     content = @Content(mediaType = MediaType.IMAGE_PNG_VALUE)),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<byte[]> generateChart(@RequestBody ChartRequest request) {
        try {
            log.info("Received chart generation request");

            // Step 1: Call LLM to analyze data and recommend chart type
            ChartAnalysis analysis = chatClientService.analyzeDataForChart(
                    request.getData(),
                    request.getTitle(),
                    request.getDescription()
            );

            log.info("LLM recommended chart type: {} - Reasoning: {}",
                     analysis.getChartType(), analysis.getReasoning());

            // Step 2: Generate the chart based on LLM analysis
            byte[] chartImage = chartService.generateChart(analysis);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setContentLength(chartImage.length);
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=chart.png");

            return new ResponseEntity<>(chartImage, headers, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error generating chart: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(value = "/analyze", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Analyze JSON data for chart recommendation",
               description = "Uses LLM to analyze JSON data and recommend the best chart type without generating the chart")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Analysis completed successfully",
                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                      schema = @Schema(implementation = ChartAnalysis.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ChartAnalysis> analyzeData(@RequestBody ChartRequest request) {
        try {
            log.info("Received chart analysis request");

            ChartAnalysis analysis = chatClientService.analyzeDataForChart(
                    request.getData(),
                    request.getTitle(),
                    request.getDescription()
            );

            return ResponseEntity.ok(analysis);

        } catch (Exception e) {
            log.error("Error analyzing data: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/health")
    @Operation(summary = "Health check endpoint")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Chart Generation Service is running");
    }
}

