package com.example.generatechartsllm.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "LLM analysis result for chart generation")
public class ChartAnalysis {

    @Schema(description = "Recommended chart type based on data analysis")
    private String recommendedChartType;

    @Schema(description = "Chart title suggested by LLM")
    private String suggestedTitle;

    @Schema(description = "X-axis label")
    private String xAxisLabel;

    @Schema(description = "Y-axis label")
    private String yAxisLabel;

    @Schema(description = "Additional insights from LLM about the data")
    private String insights;

    public ChartAnalysis() {
    }

    public ChartAnalysis(String recommendedChartType, String suggestedTitle, String xAxisLabel, String yAxisLabel, String insights) {
        this.recommendedChartType = recommendedChartType;
        this.suggestedTitle = suggestedTitle;
        this.xAxisLabel = xAxisLabel;
        this.yAxisLabel = yAxisLabel;
        this.insights = insights;
    }

    public String getRecommendedChartType() {
        return recommendedChartType;
    }

    public void setRecommendedChartType(String recommendedChartType) {
        this.recommendedChartType = recommendedChartType;
    }

    public String getSuggestedTitle() {
        return suggestedTitle;
    }

    public void setSuggestedTitle(String suggestedTitle) {
        this.suggestedTitle = suggestedTitle;
    }

    public String getXAxisLabel() {
        return xAxisLabel;
    }

    public void setXAxisLabel(String xAxisLabel) {
        this.xAxisLabel = xAxisLabel;
    }

    public String getYAxisLabel() {
        return yAxisLabel;
    }

    public void setYAxisLabel(String yAxisLabel) {
        this.yAxisLabel = yAxisLabel;
    }

    public String getInsights() {
        return insights;
    }

    public void setInsights(String insights) {
        this.insights = insights;
    }

    @Override
    public String toString() {
        return "ChartAnalysis{" +
                "recommendedChartType='" + recommendedChartType + '\'' +
                ", suggestedTitle='" + suggestedTitle + '\'' +
                ", xAxisLabel='" + xAxisLabel + '\'' +
                ", yAxisLabel='" + yAxisLabel + '\'' +
                ", insights='" + insights + '\'' +
                '}';
    }
}
