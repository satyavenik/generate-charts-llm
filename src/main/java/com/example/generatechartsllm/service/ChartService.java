package com.example.generatechartsllm.service;

import com.example.generatechartsllm.model.ChartAnalysis;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChartService {

    public byte[] generateChart(ChartAnalysis analysis) throws IOException {
        log.info("Generating {} chart: {}", analysis.getChartType(), analysis.getTitle());

        JFreeChart chart;

        switch (analysis.getChartType().toUpperCase()) {
            case "BAR":
                chart = createBarChart(analysis);
                break;
            case "LINE":
                chart = createLineChart(analysis);
                break;
            case "PIE":
                chart = createPieChart(analysis);
                break;
            case "SCATTER":
                chart = createScatterChart(analysis);
                break;
            default:
                log.warn("Unknown chart type: {}, defaulting to BAR", analysis.getChartType());
                chart = createBarChart(analysis);
        }

        return convertChartToBytes(chart);
    }

    private JFreeChart createBarChart(ChartAnalysis analysis) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        Map<String, List<Double>> series = analysis.getSeries();
        List<String> categories = analysis.getCategories();

        series.forEach((seriesName, values) -> {
            for (int i = 0; i < values.size() && i < categories.size(); i++) {
                dataset.addValue(values.get(i), seriesName, categories.get(i));
            }
        });

        return ChartFactory.createBarChart(
                analysis.getTitle(),
                analysis.getXAxisLabel(),
                analysis.getYAxisLabel(),
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
    }

    private JFreeChart createLineChart(ChartAnalysis analysis) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        Map<String, List<Double>> series = analysis.getSeries();
        List<String> categories = analysis.getCategories();

        series.forEach((seriesName, values) -> {
            for (int i = 0; i < values.size() && i < categories.size(); i++) {
                dataset.addValue(values.get(i), seriesName, categories.get(i));
            }
        });

        return ChartFactory.createLineChart(
                analysis.getTitle(),
                analysis.getXAxisLabel(),
                analysis.getYAxisLabel(),
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
    }

    private JFreeChart createPieChart(ChartAnalysis analysis) {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        List<String> categories = analysis.getCategories();
        Map<String, List<Double>> series = analysis.getSeries();

        // For pie chart, use the first series
        if (!series.isEmpty()) {
            List<Double> values = series.values().iterator().next();
            for (int i = 0; i < categories.size() && i < values.size(); i++) {
                dataset.setValue(categories.get(i), values.get(i));
            }
        }

        return ChartFactory.createPieChart(
                analysis.getTitle(),
                dataset,
                true,
                true,
                false
        );
    }

    private JFreeChart createScatterChart(ChartAnalysis analysis) {
        XYSeriesCollection dataset = new XYSeriesCollection();

        Map<String, List<Double>> series = analysis.getSeries();

        series.forEach((seriesName, values) -> {
            XYSeries xySeries = new XYSeries(seriesName);
            for (int i = 0; i < values.size(); i++) {
                xySeries.add(i, values.get(i));
            }
            dataset.addSeries(xySeries);
        });

        return ChartFactory.createScatterPlot(
                analysis.getTitle(),
                analysis.getXAxisLabel(),
                analysis.getYAxisLabel(),
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
    }

    private byte[] convertChartToBytes(JFreeChart chart) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ChartUtils.writeChartAsPNG(baos, chart, 800, 600);
        return baos.toByteArray();
    }
}

