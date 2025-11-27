package com.example.generatechartsllm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartAnalysis {
    private String chartType; // BAR, LINE, PIE, SCATTER, etc.
    private String title;
    private String xAxisLabel;
    private String yAxisLabel;
    private List<String> categories;
    private Map<String, List<Double>> series;
    private String reasoning;
}

