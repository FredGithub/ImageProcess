package ar.edu.itba.imageprocess;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;

import ar.edu.itba.imageprocess.utils.ArrayUtils;

public class Filters {

	public static Image generateCircle(int radius, int imageSize) {
		BufferedImage bufferedImage = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_BYTE_BINARY);
		Graphics2D g2d = bufferedImage.createGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fill(new Ellipse2D.Double(imageSize / 2 - radius / 2, imageSize / 2 - radius / 2, radius, radius));
		return new Image(bufferedImage);
	}

	public static Image generateSquare(int size, int imageSize) {
		BufferedImage bufferedImage = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_BYTE_BINARY);
		Graphics2D g2d = bufferedImage.createGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fill(new Rectangle2D.Double(imageSize / 2 - size / 2, imageSize / 2 - size / 2, size, size));
		return new Image(bufferedImage);
	}

	public static Image generateGradient(int imageSize) {
		BufferedImage bufferedImage = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_BYTE_GRAY);
		Graphics2D g2d = bufferedImage.createGraphics();
		g2d.setPaint(new GradientPaint(0, 0, Color.GRAY, imageSize, 0, Color.WHITE));
		g2d.fill(new Rectangle2D.Double(0, 0, imageSize, imageSize));
		return new Image(bufferedImage);
	}

	public static Image generateColorGradient(int imageSize) {
		BufferedImage bufferedImage = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		g2d.setPaint(new GradientPaint(0, 0, Color.BLUE, imageSize, 0, Color.WHITE));
		g2d.fill(new Rectangle2D.Double(0, 0, imageSize, imageSize));
		return new Image(bufferedImage);
	}

	public static Image generateHistogramImage(int[] values) {
		HistogramDataset dataset = new HistogramDataset();
		dataset.addSeries("values", ArrayUtils.intArrayToDoubleArray(values), 256, 0, 255);
		JFreeChart chart = ChartFactory.createHistogram("", "", "", dataset, PlotOrientation.VERTICAL, false, false, false);
		return new Image(chart.createBufferedImage(400, 300));
	}
}
