package com.example.generatechartsllm.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

@Schema(description = "Request payload containing chart data and configuration")
public class ChartRequest {

    @Schema(description = "Type of chart to generate (e.g., bar, pie, line)", example = "bar")
    private String chartType;

    @Schema(description = "Title of the chart", example = "Sales by Month")
    private String title;

    @Schema(description = "Data for the chart as key-value pairs")
    private Map<String, Double> data;

    @Schema(description = "Labels for the chart data")
    private List<String> labels;

    @Schema(description = "Values corresponding to the labels")
    private List<Double> values;

    public ChartRequest() {
    }

    public ChartRequest(String chartType, String title, Map<String, Double> data, List<String> labels, List<Double> values) {
        this.chartType = chartType;
        this.title = title;
        this.data = data;
        this.labels = labels;
        this.values = values;
    }

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String, Double> getData() {
        return data;
    }

    public void setData(Map<String, Double> data) {
        this.data = data;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<Double> getValues() {
        return values;
    }

    public void setValues(List<Double> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "ChartRequest{" +
                "chartType='" + chartType + '\'' +
                ", title='" + title + '\'' +
                ", data=" + data +
                ", labels=" + labels +
                ", values=" + values +
                '}';
    }
}
