package ar.edu.itba.imageprocess.utils;

import java.awt.image.BufferedImage;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;

public class ChartUtils {

	public static BufferedImage createHistogramChartImage(int chartWidth, int chartHeight, double[] data, int bins) {
		HistogramDataset dataset = new HistogramDataset();
		dataset.addSeries("values", data, bins);
		JFreeChart chart = ChartFactory.createHistogram("", "", "", dataset, PlotOrientation.VERTICAL, false, false, false);
		return chart.createBufferedImage(chartWidth, chartHeight);
	}

	public static BufferedImage createHistogramChartImage(int chartWidth, int chartHeight, double[] data, int bins, int lowBound, int highBound) {
		HistogramDataset dataset = new HistogramDataset();
		dataset.addSeries("values", data, bins, lowBound, highBound);
		JFreeChart chart = ChartFactory.createHistogram("", "", "", dataset, PlotOrientation.VERTICAL, false, false, false);
		return chart.createBufferedImage(chartWidth, chartHeight);
	}
}
