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
import ar.edu.itba.imageprocess.utils.Log;

public class Filters {

	public static Image generateWhiteImage(int width, int height) {
		int[][] grayChannel = new int[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < width; y++) {
				grayChannel[x][y] = 255;
			}
		}
		return new Image(grayChannel);
	}

	public static Image generateCircle(int radius, int imageSize) {
		BufferedImage bufferedImage = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fill(new Ellipse2D.Double(imageSize / 2 - radius / 2, imageSize / 2 - radius / 2, radius, radius));
		return new Image(bufferedImage);
	}

	public static Image generateSquare(int size, int imageSize) {
		BufferedImage bufferedImage = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fill(new Rectangle2D.Double(imageSize / 2 - size / 2, imageSize / 2 - size / 2, size, size));
		return new Image(bufferedImage);
	}

	public static Image generateGradient(int imageSize) {
		BufferedImage bufferedImage = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB);
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
		int lowBound = Math.min(0, ArrayUtils.min(values));
		int highBound = Math.max(255, ArrayUtils.max(values));
		dataset.addSeries("values", ArrayUtils.intArrayToDoubleArray(values), highBound - lowBound + 1, lowBound, highBound);
		JFreeChart chart = ChartFactory.createHistogram("", "", "", dataset, PlotOrientation.VERTICAL, false, false, false);
		return new Image(chart.createBufferedImage(400, 300));
	}

	public static Image addImages(Image image1, Image image2) {
		// images must be the same size
		if (image1.getWidth() != image2.getWidth() || image1.getHeight() != image2.getHeight()) {
			Log.d("images must be the same size");
			return null;
		}

		// prepare the new image channel arrays
		int width = image1.getWidth();
		int height = image1.getHeight();
		int[][] redChannel = new int[width][height];
		int[][] greenChannel = new int[width][height];
		int[][] blueChannel = new int[width][height];

		// add each pixel one by one
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				redChannel[x][y] = image1.getRed(x, y) + image2.getRed(x, y);
				greenChannel[x][y] = image1.getGreen(x, y) + image2.getGreen(x, y);
				blueChannel[x][y] = image1.getBlue(x, y) + image2.getBlue(x, y);
			}
		}

		return new Image(redChannel, greenChannel, blueChannel);
	}

	public static Image subtractImages(Image image1, Image image2) {
		// images must be the same size
		if (image1.getWidth() != image2.getWidth() || image1.getHeight() != image2.getHeight()) {
			Log.d("images must be the same size");
			return null;
		}

		// prepare the new image channel arrays
		int width = image1.getWidth();
		int height = image1.getHeight();
		int[][] redChannel = new int[width][height];
		int[][] greenChannel = new int[width][height];
		int[][] blueChannel = new int[width][height];

		// subtract each pixel one by one
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				redChannel[x][y] = image1.getRed(x, y) - image2.getRed(x, y);
				greenChannel[x][y] = image1.getGreen(x, y) - image2.getGreen(x, y);
				blueChannel[x][y] = image1.getBlue(x, y) - image2.getBlue(x, y);
			}
		}

		return new Image(redChannel, greenChannel, blueChannel);
	}

	public static Image multiplyScalar(Image image, double scalar) {
		// prepare the new image channel arrays
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] redChannel = new int[width][height];
		int[][] greenChannel = new int[width][height];
		int[][] blueChannel = new int[width][height];

		// multiply each pixel one by one
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				redChannel[x][y] = (int) (image.getRed(x, y) * scalar);
				greenChannel[x][y] = (int) (image.getGreen(x, y) * scalar);
				blueChannel[x][y] = (int) (image.getBlue(x, y) * scalar);
			}
		}

		return new Image(redChannel, greenChannel, blueChannel);
	}

	public static Image compress(Image image) {
		int[][] grayChannel = image.getGrayChannel();
		int[][] newGrayChannel = new int[image.getWidth()][image.getHeight()];

		// get the maximum gray level and the factor of compression
		int max = ArrayUtils.max(grayChannel);
		double c = 255 / Math.log(max);

		// apply the filter to all pixels
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				// only apply the filter if the max gray level is above 255
				if (max > 255) {
					newGrayChannel[x][y] = (int) (c * Math.log(grayChannel[x][y] + 1));
				} else {
					newGrayChannel[x][y] = grayChannel[x][y];
				}
				// now apply a simple trim if the gray level is under 0
				newGrayChannel[x][y] = Math.max(0, newGrayChannel[x][y]);
			}
		}

		return new Image(newGrayChannel);
	}
}
