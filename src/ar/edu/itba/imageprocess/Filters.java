package ar.edu.itba.imageprocess;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import ar.edu.itba.imageprocess.utils.ArrayUtils;
import ar.edu.itba.imageprocess.utils.ChartUtils;
import ar.edu.itba.imageprocess.utils.Log;
import ar.edu.itba.imageprocess.utils.RandGenerator;

public class Filters {

	public static final int CHART_WIDTH = 400;
	public static final int CHART_HEIGHT = 300;
	public static final int MASK_FILTER_AVERAGE = 1;
	public static final int MASK_FILTER_GAUSSIAN = 2;
	public static final int MASK_FILTER_HIGH_PASS = 3;

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
		int lowBound = Math.min(0, ArrayUtils.min(values));
		int highBound = Math.max(255, ArrayUtils.max(values));
		return new Image(ChartUtils.createHistogramChartImage(CHART_WIDTH, CHART_HEIGHT, ArrayUtils.intArrayToDoubleArray(values), highBound - lowBound + 1, lowBound, highBound));
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

	public static Image generateGaussianChartImage(double spread, double average) {
		// generate test data and create a chart out of it
		int size = 5000;
		double data[] = new double[size];
		for (int i = 0; i < size; i++) {
			data[i] = RandGenerator.gaussian(spread, average);
		}
		return new Image(ChartUtils.createHistogramChartImage(CHART_WIDTH, CHART_HEIGHT, data, 100));
	}

	public static Image generateRayleighChartImage(double param) {
		// generate test data and create a chart out of it
		int size = 5000;
		double data[] = new double[size];
		for (int i = 0; i < size; i++) {
			data[i] = RandGenerator.rayleigh(param);
		}
		return new Image(ChartUtils.createHistogramChartImage(CHART_WIDTH, CHART_HEIGHT, data, 100));
	}

	public static Image generateExponentialChartImage(double param) {
		// generate test data and create a chart out of it
		int size = 5000;
		double data[] = new double[size];
		for (int i = 0; i < size; i++) {
			data[i] = RandGenerator.exponential(param);
		}
		return new Image(ChartUtils.createHistogramChartImage(CHART_WIDTH, CHART_HEIGHT, data, 100));
	}

	public static Image applyPepperAndSalt(Image image, double p0, double p1) {
		// prepare the new image channel arrays
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] redChannel = new int[width][height];
		int[][] greenChannel = new int[width][height];
		int[][] blueChannel = new int[width][height];

		// apply the noise to each pixel of the image
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				double rand = Math.random();
				if (rand <= p0) {
					redChannel[x][y] = 0;
					greenChannel[x][y] = 0;
					blueChannel[x][y] = 0;
				} else if (rand >= p1) {
					redChannel[x][y] = 255;
					greenChannel[x][y] = 255;
					blueChannel[x][y] = 255;
				} else {
					redChannel[x][y] = image.getRed(x, y);
					greenChannel[x][y] = image.getGreen(x, y);
					blueChannel[x][y] = image.getBlue(x, y);
				}
			}
		}

		return new Image(redChannel, greenChannel, blueChannel);
	}

	public static Image applyFactorMaskFilter(Image image, int maskWidth, int maskHeight, int filterType) {
		// prepare the new image channel arrays
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] redChannel = new int[width][height];
		int[][] greenChannel = new int[width][height];
		int[][] blueChannel = new int[width][height];

		// get the position of the pixel at the center of the mask
		// if one side has an even length, for example maskWidth = 8
		// the center is considered to be 3 (the fourth column)
		int offsetX = (int) (Math.ceil(maskWidth / 2.0) - 1);
		int offsetY = (int) (Math.ceil(maskHeight / 2.0) - 1);

		// setup the mask and the factor
		int[][] mask = new int[maskWidth][maskHeight];
		double factor = 0;
		if (filterType == MASK_FILTER_AVERAGE) {
			factor = 1.0 / (maskWidth * maskHeight);
			for (int x = 0; x < maskWidth; x++) {
				for (int y = 0; y < maskHeight; y++) {
					mask[x][y] = 1;
				}
			}
		} else if (filterType == MASK_FILTER_GAUSSIAN) {

		} else if (filterType == MASK_FILTER_HIGH_PASS) {
			factor = 1.0 / (maskWidth * maskHeight);
			for (int x = 0; x < maskWidth; x++) {
				for (int y = 0; y < maskHeight; y++) {
					if (x == offsetX && y == offsetY) {
						mask[x][y] = 8;
					} else {
						mask[x][y] = -1;
					}
				}
			}
		}

		// apply the mask on each pixel of the image
		for (int pixelX = 0; pixelX < width; pixelX++) {
			for (int pixelY = 0; pixelY < height; pixelY++) {
				double redSum = 0;
				double greenSum = 0;
				double blueSum = 0;

				// iterate over the mask for that pixel
				for (int x = 0; x < maskWidth; x++) {
					for (int y = 0; y < maskHeight; y++) {
						redSum += mask[x][y] * image.getRed(pixelX - offsetX + x, pixelY - offsetY + y);
						greenSum += mask[x][y] * image.getGreen(pixelX - offsetX + x, pixelY - offsetY + y);
						blueSum += mask[x][y] * image.getBlue(pixelX - offsetX + x, pixelY - offsetY + y);
					}
				}

				// set the new image pixels
				redChannel[pixelX][pixelY] = (int) (redSum * factor);
				greenChannel[pixelX][pixelY] = (int) (greenSum * factor);
				blueChannel[pixelX][pixelY] = (int) (blueSum * factor);
			}
		}

		return new Image(redChannel, greenChannel, blueChannel);
	}

	public static Image applyMedianMaskFilter(Image image, int maskWidth, int maskHeight) {
		// prepare the new image channel arrays
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] redChannel = new int[width][height];
		int[][] greenChannel = new int[width][height];
		int[][] blueChannel = new int[width][height];

		// get the position of the pixel at the center of the mask
		// if one side has an even length, for example maskWidth = 8
		// the center is considered to be 3 (the fourth column)
		int offsetX = (int) (Math.ceil(maskWidth / 2.0) - 1);
		int offsetY = (int) (Math.ceil(maskHeight / 2.0) - 1);

		// apply the mask on each pixel of the image
		for (int pixelX = 0; pixelX < width; pixelX++) {
			for (int pixelY = 0; pixelY < height; pixelY++) {
				int[] redValues = new int[maskWidth * maskHeight];
				int[] greenValues = new int[maskWidth * maskHeight];
				int[] blueValues = new int[maskWidth * maskHeight];

				// get all the pixels values under the mask
				for (int x = 0; x < maskWidth; x++) {
					for (int y = 0; y < maskHeight; y++) {
						redValues[x * maskHeight + y] = image.getRed(pixelX - offsetX + x, pixelY - offsetY + y);
						greenValues[x * maskHeight + y] = image.getGreen(pixelX - offsetX + x, pixelY - offsetY + y);
						blueValues[x * maskHeight + y] = image.getBlue(pixelX - offsetX + x, pixelY - offsetY + y);
					}
				}

				// set the new image pixels to the median
				redChannel[pixelX][pixelY] = (int) (ArrayUtils.median(redValues));
				greenChannel[pixelX][pixelY] = (int) (ArrayUtils.median(greenValues));
				blueChannel[pixelX][pixelY] = (int) (ArrayUtils.median(blueValues));
			}
		}

		return new Image(redChannel, greenChannel, blueChannel);
	}
}
