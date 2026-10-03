package com.smorag.jdbc_hibernate_benchmark.utils;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BenchmarkChartGenerator {

    private static final String CHART_DIR = "benchmark-results/charts";


    /**
     * Generates chart
     *
     * @param framework name of used framework for file name
     * @param entity entity name for file name
     * @param operation used method for file name
     * @param records size of records in benchmark
     * @param times times saved after each iteration in benchmark method
     */
    public static void generate(
            String framework,
            String entity,
            String operation,
            int records,
            List<Long> times
    ) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 0; i < times.size(); i++) {
            dataset.addValue(
                    times.get(i),
                    framework,
                    String.valueOf(i + 1)
            );
        }

        String title = String.format(
                "%s - %s - %s -%d records",
                framework,
                entity,
                operation,
                records
        );

        JFreeChart chart = ChartFactory.createLineChart(
                title,
                "Iteration",
                "Time [ms]",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                true
        );

        File directory = new File(CHART_DIR);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = String.format(
                "%s_%s_%s_%d.png",
                framework.toLowerCase(),
                entity.toLowerCase(),
                operation.toLowerCase(),
                records
        );

        ChartUtilities.saveChartAsJPEG(
                new File(directory, fileName),
                chart,
                1200,
                700
        );
    }
}
