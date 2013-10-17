package ar.edu.itba.imageprocess;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

import ar.edu.itba.imageprocess.utils.ArrayUtils;
import ar.edu.itba.imageprocess.utils.ChartUtils;
import ar.edu.itba.imageprocess.utils.Log;
import ar.edu.itba.imageprocess.utils.RandGenerator;

public class Filters {

	public static final int CHART_WIDTH = 400;
	public static final int CHART_HEIGHT = 300;
	public static final int MASK_FILTER_AVERAGE = 1;
	public static final int MASK_FILTER_HIGH_PASS = 2;

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

	public static Image compressLinear(Image image) {
		// prepare the new image gray channel
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] grayChannel = image.getGrayChannel();
		int[][] newGrayChannel = new int[width][height];

		// get the bounds and calculate the linear transform parameters
		int[] range = image.getRange(Image.CHANNEL_GRAY);
		double factor = (double) 255 / (range[1] - range[0]);
		double b = -factor * range[0];

		// apply the filter to all pixels
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				newGrayChannel[x][y] = (int) (grayChannel[x][y] * factor + b);
			}
		}

		return new Image(newGrayChannel);
	}

	public static Image compress(Image image) {
		// prepare the new image gray channel
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] grayChannel = image.getGrayChannel();
		int[][] newGrayChannel = new int[width][height];

		// get the maximum gray level and the factor of compression
		int max = ArrayUtils.max(grayChannel);
		double c = 255 / Math.log(max);

		// apply the filter to all pixels
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
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

	public static Image filterNegative(Image image) {
		// prepare the new image channel arrays
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] redChannel = new int[width][height];
		int[][] greenChannel = new int[width][height];
		int[][] blueChannel = new int[width][height];

		// inverse the color of each pixel
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				redChannel[x][y] = 255 - image.getRed(x, y);
				greenChannel[x][y] = 255 - image.getGreen(x, y);
				blueChannel[x][y] = 255 - image.getBlue(x, y);
			}
		}

		return new Image(redChannel, greenChannel, blueChannel);
	}

	public static Image filterThreshold(Image image, int threshold) {
		return filterThreshold(image, threshold, threshold, threshold);
	}

	public static Image filterThreshold(Image image, int thresholdRed, int thresholdGreen, int thresholdBlue) {
		// prepare the new image channel arrays
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] redChannel = new int[width][height];
		int[][] greenChannel = new int[width][height];
		int[][] blueChannel = new int[width][height];

		// apply the threshold to each pixel
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				redChannel[x][y] = image.getRed(x, y) < thresholdRed ? 0 : 255;
				greenChannel[x][y] = image.getGreen(x, y) < thresholdGreen ? 0 : 255;
				blueChannel[x][y] = image.getBlue(x, y) < thresholdBlue ? 0 : 255;
			}
		}

		return new Image(redChannel, greenChannel, blueChannel);
	}

	public static Image filterContrast(Image image, int r1, int r2, int s1, int s2) {
		// prepare the new image channel arrays
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] grayChannel = new int[width][height];

		// calculate the three linear transforms parameters
		double factor1 = (double) (s1 - 0) / (r1 - 0);
		double b1 = 0;
		double factor2 = (double) (s2 - s1) / (r2 - r1);
		double b2 = -factor2 * r1 + s1;
		double factor3 = (double) (255 - s2) / (255 - r2);
		double b3 = -factor3 * r2 + s2;

		// apply the transforms to each pixels
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int gray = image.getGray(x, y);
				if (gray <= r1) {
					grayChannel[x][y] = (int) (gray * factor1 + b1);
				} else if (gray <= r2) {
					grayChannel[x][y] = (int) (gray * factor2 + b2);
				} else {
					grayChannel[x][y] = (int) (gray * factor3 + b3);
				}
			}
		}

		return new Image(grayChannel);
	}

	public static Image filterEqualize(Image image) {
		// prepare the new image gray channel
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] grayChannel = new int[width][height];

		// let ni be the number of occurrences of gray level i
		int[] ni = image.getHistogram(Image.CHANNEL_GRAY);

		// cumulative frequency distribution
		int[] cuf = new int[ni.length];
		cuf[0] = ni[0];
		for (int i = 1; i < cuf.length; i++) {
			cuf[i] = cuf[i - 1] + ni[i];
		}

		int[] cuFeq = new int[ni.length];
		for (int i = 0; i < cuFeq.length; i++) {
			cuFeq[i] = (int) (i * cuf[cuf.length - 1] / cuf.length);
		}

		int[] output = new int[ni.length];
		for (int i = 0; i < ni.length; i++) {
			output[i] = equalize(cuf[i], cuFeq);
		}

		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				grayChannel[x][y] = (int) (output[image.getGray(x, y)]);
			}
		}

		return new Image(grayChannel);
	}

	private static int equalize(int n, int[] cuFeq) {
		int min = Math.abs(n - cuFeq[0]);
		int minindex = 0;
		for (int i = 1; i < cuFeq.length; i++) {
			if (Math.abs(n - cuFeq[i]) < min) {
				min = Math.abs(n - cuFeq[i]);
				minindex = i;
			}
		}
		return minindex;
	}

	public static Image applyAddGaussianNoise(Image image, double sigma, double average, double percentage) {
		// prepare the new image gray channel
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] newGrayChannel = new int[width][height];
		int[][] grayChannel = image.getGrayChannel();

		// apply the gaussian noise to each pixel
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				double rand = Math.random();
				if (rand <= percentage) {
					newGrayChannel[x][y] = grayChannel[x][y] + (int) (RandGenerator.gaussian(sigma, average));
				} else {
					newGrayChannel[x][y] = grayChannel[x][y];
				}
			}
		}

		return new Image(newGrayChannel);
	}

	public static Image applyMulRayleighNoise(Image image, double p, double percentage) {
		// prepare the new image gray channel
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] newGrayChannel = new int[width][height];
		int[][] grayChannel = image.getGrayChannel();

		// apply the rayleigh noise to each pixel
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				double rand = Math.random();
				if (rand <= percentage) {
					newGrayChannel[x][y] = (int) (grayChannel[x][y] * (RandGenerator.rayleigh(p)));
				} else {
					newGrayChannel[x][y] = grayChannel[x][y];
				}
			}
		}

		return new Image(newGrayChannel);
	}

	public static Image applyMulExponentialNoise(Image image, double p, double percentage) {
		// prepare the new image gray channel
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] newGrayChannel = new int[width][height];
		int[][] grayChannel = image.getGrayChannel();

		// apply the exponential noise to each pixel
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				double rand = Math.random();
				if (rand <= percentage) {
					newGrayChannel[x][y] = (int) (grayChannel[x][y] * (RandGenerator.exponential(p)));
				} else {
					newGrayChannel[x][y] = grayChannel[x][y];
				}
			}
		}

		return new Image(newGrayChannel);
	}

	public static Image generateGaussianChartImage(double sigma, double average) {
		// generate test data and create a chart out of it
		int size = 5000;
		double data[] = new double[size];
		for (int i = 0; i < size; i++) {
			data[i] = RandGenerator.gaussian(sigma, average);
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
		double[][] mask = new double[maskWidth][maskHeight];
		double factor = 0;
		if (filterType == MASK_FILTER_AVERAGE) {
			factor = 1.0 / (maskWidth * maskHeight);
			for (int x = 0; x < maskWidth; x++) {
				for (int y = 0; y < maskHeight; y++) {
					mask[x][y] = 1;
				}
			}
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

		// apply the mask
		applyFactorMask(image, mask, factor, redChannel, greenChannel, blueChannel);

		return new Image(redChannel, greenChannel, blueChannel);
	}

	public static Image applyGaussianMaskFilter(Image image, int maskWidth, int maskHeight, double sigma) {
		// prepare the new image channel arrays
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] redChannel = new int[width][height];
		int[][] greenChannel = new int[width][height];
		int[][] blueChannel = new int[width][height];

		// if the provided size for the mask is 0, we use automatic mask size
		// based on sigma
		if (maskWidth == 0 || maskHeight == 0) {
			maskWidth = maskSize(sigma);
			maskHeight = maskWidth;
		}

		// get the position of the pixel at the center of the mask
		// if one side has an even length, for example maskWidth = 8
		// the center is considered to be 3 (the fourth column)
		int offsetX = (int) (Math.ceil(maskWidth / 2.0) - 1);
		int offsetY = (int) (Math.ceil(maskHeight / 2.0) - 1);

		// setup the mask and the factor
		double[][] mask = new double[maskWidth][maskHeight];
		double sigma2 = sigma * sigma;
		double factor = 1.0 / (2 * Math.PI * sigma2);

		double total = 0;
		for (int x = 0; x < maskWidth; x++) {
			for (int y = 0; y < maskHeight; y++) {
				double dist = (x - offsetX) * (x - offsetX) + (y - offsetY) * (y - offsetY);
				double exp = Math.exp((-1 * dist) / sigma2);
				mask[x][y] = exp;
				total += exp;
			}
		}

		// the original factor doesn't work properly. use this one instead
		factor = 1 / total;

		// apply the mask
		applyFactorMask(image, mask, factor, redChannel, greenChannel, blueChannel);

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

	public static Image robertsBorderDetection(Image image) {
		// prepare the new image channel arrays
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] redChannel = new int[width][height];
		int[][] greenChannel = new int[width][height];
		int[][] blueChannel = new int[width][height];

		// build the masks
		double[][] maskX = { { 1, 0 }, { 0, -1 } };
		double[][] maskY = { { 0, 1 }, { -1, 0 } };
		ArrayList<double[][]> masks = new ArrayList<double[][]>();
		masks.add(maskX);
		masks.add(maskY);

		// apply the masks
		applyGradientMask(image, masks, redChannel, greenChannel, blueChannel);

		return new Image(redChannel, greenChannel, blueChannel);
	}

	public static Image prewittBorderDetection(Image image) {
		// prepare the new image gray channel
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] redChannel = new int[width][height];
		int[][] greenChannel = new int[width][height];
		int[][] blueChannel = new int[width][height];

		// build the masks
		double[][] maskX = { { -1, 0, 1 }, { -1, 0, 1 }, { -1, 0, 1 } };
		double[][] maskY = { { -1, -1, -1 }, { 0, 0, 0 }, { 1, 1, 1 } };
		ArrayList<double[][]> masks = new ArrayList<double[][]>();
		masks.add(maskX);
		masks.add(maskY);

		// apply the masks
		applyGradientMask(image, masks, redChannel, greenChannel, blueChannel);

		return new Image(redChannel, greenChannel, blueChannel);
	}

	public static Image sobelBorderDetection(Image image) {
		// prepare the new image gray channel
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] redChannel = new int[width][height];
		int[][] greenChannel = new int[width][height];
		int[][] blueChannel = new int[width][height];

		// build the masks
		double[][] maskX = { { -1, 0, 1 }, { -2, 0, 2 }, { -1, 0, 1 } };
		double[][] maskY = { { -1, -2, -1 }, { 0, 0, 0 }, { 1, 2, 1 } };
		ArrayList<double[][]> masks = new ArrayList<double[][]>();
		masks.add(maskX);
		masks.add(maskY);

		// apply the masks
		applyGradientMask(image, masks, redChannel, greenChannel, blueChannel);

		return new Image(redChannel, greenChannel, blueChannel);
	}

	public static Image simpleBorderDetection(Image image) {
		// prepare the new image gray channel
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] redChannel = new int[width][height];
		int[][] greenChannel = new int[width][height];
		int[][] blueChannel = new int[width][height];

		// build the masks
		double[][] mask = { { 1, 1, 1 }, { 1, -2, 1 }, { -1, -1, -1 } };
		ArrayList<double[][]> masks = new ArrayList<double[][]>();
		masks.add(mask);

		// apply the masks
		applyGradientMask(image, masks, redChannel, greenChannel, blueChannel);

		return new Image(redChannel, greenChannel, blueChannel);
	}

	public static Image kirshBorderDetection(Image image) {
		// prepare the new image gray channel
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] redChannel = new int[width][height];
		int[][] greenChannel = new int[width][height];
		int[][] blueChannel = new int[width][height];

		// build the masks
		double[][] mask = { { 5, 5, 5 }, { -3, 0, -3 }, { -3, -3, -3 } };
		ArrayList<double[][]> masks = new ArrayList<double[][]>();
		masks.add(mask);

		// apply the masks
		applyGradientMask(image, masks, redChannel, greenChannel, blueChannel);

		return new Image(redChannel, greenChannel, blueChannel);
	}

	public static Image laplacianBorderDetection(Image image, int threshold) {
		// prepare the new image gray channel
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] redChannel = new int[width][height];
		int[][] greenChannel = new int[width][height];
		int[][] blueChannel = new int[width][height];

		// apply the masks and get a temporary image out of it
		double[][] mask = { { 0, -1, 0 }, { -1, 4, -1 }, { 0, -1, 0 } };
		applyFactorMask(image, mask, 1, redChannel, greenChannel, blueChannel);
		Image tmpImage = new Image(redChannel, greenChannel, blueChannel);

		// find the zero crossings
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// get the previous value. if the pixel to the left of the
				// current pixel is zero
				// take the one even before
				int prevVal = tmpImage.getRed(x - 1, y) != 0 ? tmpImage.getRed(x - 1, y) : tmpImage.getRed(x - 2, y);

				// get the slope if there is a zero crossing
				int slopeRed = 0;
				if (tmpImage.getRed(x, y) > 0 && prevVal < 0 || tmpImage.getRed(x, y) < 0 && prevVal > 0) {
					slopeRed = tmpImage.getRed(x, y) - prevVal;
				}

				// apply the pixel value based on the slope
				if (Math.abs(slopeRed) > threshold) {
					redChannel[x][y] = 255;
				} else {
					redChannel[x][y] = 0;
				}
			}
		}

		return new Image(redChannel);
	}

	public static Image laplacianGaussianBorderDetection(Image image, int maskWidth, int maskHeight, double sigma, int threshold) {
		// prepare the new image gray channel
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] redChannel = new int[width][height];
		int[][] greenChannel = new int[width][height];
		int[][] blueChannel = new int[width][height];

		// if the provided size for the mask is 0, we use automatic mask size
		// based on sigma
		if (maskWidth == 0 || maskHeight == 0) {
			maskWidth = maskSize(sigma);
			maskHeight = maskWidth;
		}
		int offsetX = (int) (Math.ceil(maskWidth / 2.0) - 1);
		int offsetY = (int) (Math.ceil(maskHeight / 2.0) - 1);

		// setup the mask and the factor
		double[][] mask = new double[maskWidth][maskHeight];
		double c = -1 / (Math.sqrt(2 * Math.PI) * sigma * sigma * sigma);

		double total = 0;
		for (int x = 0; x < maskWidth; x++) {
			for (int y = 0; y < maskHeight; y++) {
				double dist = (x - offsetX) * (x - offsetX) + (y - offsetY) * (y - offsetY);
				double a = 2 - dist / (sigma * sigma);
				double exp = Math.exp((-1 * dist) / (2 * sigma * sigma));
				double val = c * a * exp;
				mask[x][y] = val;
				total += val;
			}
		}
		double factor = 1 / total;

		// apply the masks and get a temporary image out of it
		applyFactorMask(image, mask, factor, redChannel, greenChannel, blueChannel);
		Image tmpImage = new Image(redChannel, greenChannel, blueChannel);

		// find the zero crossings
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// get the previous value. if the pixel to the left of the
				// current pixel is zero
				// take the one even before
				int prevVal = tmpImage.getRed(x - 1, y) != 0 ? tmpImage.getRed(x - 1, y) : tmpImage.getRed(x - 2, y);

				// get the slope if there is a zero crossing
				int slopeRed = 0;
				if (tmpImage.getRed(x, y) > 0 && prevVal < 0 || tmpImage.getRed(x, y) < 0 && prevVal > 0) {
					slopeRed = tmpImage.getRed(x, y) - prevVal;
				}

				// apply the pixel value based on the slope
				if (Math.abs(slopeRed) > threshold) {
					redChannel[x][y] = 255;
				} else {
					redChannel[x][y] = 0;
				}
			}
		}

		return new Image(redChannel);
	}

	public static Image isotropicFilter(Image image, int maskWidth, int maskHeight, double sigma) {
		// prepare the new image channel arrays
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] redChannel = new int[width][height];
		int[][] greenChannel = new int[width][height];
		int[][] blueChannel = new int[width][height];

		// if the provided size for the mask is 0, we use automatic mask size
		// based on sigma
		if (maskWidth == 0 || maskHeight == 0) {
			maskWidth = maskSize(sigma);
			maskHeight = maskWidth;
		}

		// get the position of the pixel at the center of the mask
		// if one side has an even length, for example maskWidth = 8
		// the center is considered to be 3 (the fourth column)
		int offsetX = (int) (Math.ceil(maskWidth / 2.0) - 1);
		int offsetY = (int) (Math.ceil(maskHeight / 2.0) - 1);

		// setup the mask and the factor
		double[][] mask = new double[maskWidth][maskHeight];
		double c = 1 / (4 * Math.PI * sigma);
		double total = 0;

		for (int x = 0; x < maskWidth; x++) {
			for (int y = 0; y < maskHeight; y++) {
				double dist = (x - offsetX) * (x - offsetX) + (y - offsetY) * (y - offsetY);
				double exp = Math.exp((-1 * dist) / 4 * sigma);
				double val = c * exp;
				mask[x][y] = val;
				total += val;
			}
		}
		double factor = 1 / total;
		factor = 1;

		// apply the mask
		applyFactorMask(image, mask, factor, redChannel, greenChannel, blueChannel);

		return new Image(redChannel, greenChannel, blueChannel);
	}

	public static Image anisotropicFilter(Image image, int steps, double sigma, int method) {
		// prepare the new image gray channel
		int width = image.getWidth();
		int height = image.getHeight();
		int[][][] channels = new int[3][width][height];

		// copy the original image
		Image tmpImage = new Image(image.getRedChannel(), image.getGreenChannel(), image.getBlueChannel());

		// perform the anisotropic filter
		for (int i = 0; i < steps; i++) {
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					for (int channelIndex = 0; channelIndex < 3; channelIndex++) {
						int channel = channelIndex == 0 ? Image.CHANNEL_RED : channelIndex == 1 ? Image.CHANNEL_GREEN : Image.CHANNEL_BLUE;
						double dn = tmpImage.getChannelColor(x, y - 1, channel) - tmpImage.getChannelColor(x, y, channel);
						double ds = tmpImage.getChannelColor(x, y + 1, channel) - tmpImage.getChannelColor(x, y, channel);
						double de = tmpImage.getChannelColor(x - 1, y, channel) - tmpImage.getChannelColor(x, y, channel);
						double dw = tmpImage.getChannelColor(x - 1, y, channel) - tmpImage.getChannelColor(x, y, channel);
						double cn = method == 1 ? leclerc(dn, sigma) : lorentziano(dn, sigma);
						double cs = method == 1 ? leclerc(ds, sigma) : lorentziano(ds, sigma);
						double ce = method == 1 ? leclerc(de, sigma) : lorentziano(de, sigma);
						double cw = method == 1 ? leclerc(dw, sigma) : lorentziano(dw, sigma);
						channels[channelIndex][x][y] = (int) (tmpImage.getChannelColor(x, y, channel) + 0.25 * (dn * cn + ds * cs + de * ce + dw * cw));
					}
				}
			}
			tmpImage = new Image(channels[0], channels[1], channels[2]);
		}

		return new Image(channels[0], channels[1], channels[2]);
	}

	public static int globalThreshold(Image image, double delta) {
		int width = image.getWidth();
		int height = image.getHeight();
		int threshold = 128;
		boolean done = false;
		int iterations = 0;

		while (!done) {
			iterations++;

			Image tmpImage = filterThreshold(image, threshold);
			double blackSum = 0;
			int blackCount = 0;
			double whiteSum = 0;
			int whiteCount = 0;

			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					if (tmpImage.getGray(x, y) == 0) {
						blackSum += image.getGray(x, y);
						blackCount++;
					} else {
						whiteSum += image.getGray(x, y);
						whiteCount++;
					}
				}
			}

			int prevThreshold = threshold;
			threshold = (int) ((blackSum / blackCount + whiteSum / whiteCount) / 2);
			if (Math.abs(threshold - prevThreshold) < delta) {
				done = true;
			}
		}

		Log.d("global threshold of " + threshold + " in " + iterations + " iterations");

		return threshold;
	}

	public static int otsuThreshold(Image image, int channel) {
		double maxValue = 0;
		int threshold = 0;
		int L = 256;

		// get the histogram and probabilities of each gray level
		int[] histogram = image.getHistogram(channel);
		double[] proba = new double[L];
		for (int i = 0; i < L; i++) {
			proba[i] = histogram[i] / L;
		}

		// find the threshold that get the highest variance
		for (int t = 0; t < L; t++) {
			// compute the probabilities
			double w1 = 0;
			double w2 = 0;
			for (int i = 0; i < L; i++) {
				if (i <= t) {
					w1 += proba[i];
				} else {
					w2 += proba[i];
				}
			}

			// compute the means
			double u1 = 0;
			double u2 = 0;
			for (int i = 0; i < L; i++) {
				if (i <= t) {
					u1 += ((i + 1) * proba[i]) / w1;
				} else {
					u2 += ((i + 1) * proba[i]) / w2;
				}
			}

			// calculate sigma
			double si = w1 * w2 * (u1 - u2) * (u1 - u2);

			// update the threshold if necessary
			if (maxValue < si) {
				maxValue = si;
				threshold = t;
			}
		}

		Log.d("otsu threshold of " + threshold + " found");

		return threshold;
	}

	public static Image nonMaximum(Image image) {
		int width = image.getWidth();
		int height = image.getHeight();

		// get the gradients X and Y
		double[][] maskX = { { -1, -2, -1 }, { 0, 0, 0 }, { 1, 2, 1 } };
		double[][] maskY = { { -1, 0, 1 }, { -2, 0, 2 }, { -1, 0, 1 } };
		int[][] gx = grayGradient(image, maskX);
		int[][] gy = grayGradient(image, maskY);

		// get the angles and the gradient
		int[][] angles = new int[width][height];
		int[][] g = new int[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				double angle = 0;
				if (gx[x][y] != 0) {
					angle = Math.atan2(gy[x][y], gx[x][y]) * 180 / Math.PI;
				}
				angle = (angle + 180) % 180;
				angles[x][y] = toDiscreteAngle(angle);
				g[x][y] = (int) Math.sqrt(gx[x][y] * gx[x][y] + gy[x][y] * gy[x][y]);
			}
		}

		// remove the non maximum
		int[][] newG = new int[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int moveX = angles[x][y] == 90 ? 0 : angles[x][y] == 135 ? -1 : 1;
				int moveY = angles[x][y] == 0 ? 0 : 1;
				int adj1 = getValueInBounds(g, x + moveX, y + moveY);
				int adj2 = getValueInBounds(g, x - moveX, y - moveY);
				if (adj1 > g[x][y] || adj2 > g[x][y]) {
					newG[x][y] = 0;
				} else {
					newG[x][y] = g[x][y];
				}
			}
		}

		return compressLinear(new Image(newG));
	}

	public static Image histeresisThreshold(Image image, int thresholdLow, int thresholdHigh) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] newGrayChannel = new int[width][height];

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (image.getGray(x, y) < thresholdLow) {
					newGrayChannel[x][y] = 0;
				} else if (image.getGray(x, y) > thresholdHigh) {
					newGrayChannel[x][y] = 255;
				} else {
					boolean borderLeft = image.getGray(x - 1, y) > thresholdHigh;
					boolean borderRight = image.getGray(x + 1, y) > thresholdHigh;
					boolean borderTop = image.getGray(x, y - 1) > thresholdHigh;
					boolean borderBottom = image.getGray(x, y + 1) > thresholdHigh;
					if (borderLeft || borderRight || borderTop || borderBottom) {
						newGrayChannel[x][y] = 255;
					}
				}
			}
		}

		return new Image(newGrayChannel);
	}

	public static Image canny(Image image, double sigma, int thresholdLow, int thresholdHigh) {
		Image blurred = applyGaussianMaskFilter(image, 0, 0, sigma);
		Image detected = histeresisThreshold(nonMaximum(blurred), thresholdLow, thresholdHigh);
		return detected;
	}

	public static Image susan(Image image, int threshold) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] redChannel = new int[width][height];
		int[][] greenChannel = new int[width][height];
		int[][] blueChannel = new int[width][height];
		int halfSize = 3;

		// apply the mask to each pixel
		for (int pixelX = 0; pixelX < width; pixelX++) {
			for (int pixelY = 0; pixelY < height; pixelY++) {
				int sum = 0;
				int maskSize = 0;

				// run through the circle mask
				for (int y = -halfSize; y <= halfSize; y++) {
					for (int x = -halfSize; x <= halfSize; x++) {
						maskSize++;
						double dist2 = x * x + y * y;
						// if we are not at the center and inside the circle
						if (dist2 > 0 && dist2 * 0.8 < halfSize * halfSize) {
							int diff = Math.abs(image.getGray(pixelX, pixelY) - image.getGray(pixelX + x, pixelY + y));
							if (diff < threshold) {
								sum++;
							}
						}
					}
				}

				// add the border
				double s = 1 - (double) sum / maskSize;
				if (s > 0.75) {
					greenChannel[pixelX][pixelY] = 255;
				} else if (s > 0.5) {
					blueChannel[pixelX][pixelY] = 255;
				}
			}
		}

		return new Image(redChannel, greenChannel, blueChannel);
	}

	public static void drawLineNormal(Graphics g, double angle, double dist) {
		double dirX = Math.cos(angle);
		double dirY = Math.sin(angle);
		double norX = dirY;
		double norY = -dirX;
		double x1 = dirX * dist - norX * 10000;
		double y1 = dirY * dist - norY * 10000;
		double x2 = dirX * dist + norX * 10000;
		double y2 = dirY * dist + norY * 10000;
		g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
	}

	public static Image houghLines(Image image, int angleCount, int distCount, int amount) {
		int width = image.getWidth();
		int height = image.getHeight();

		// create the buffered image
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = bufferedImage.getGraphics();
		g.setColor(Color.GREEN);

		// get the hough lines and draw them
		ArrayList<double[]> lines = getHoughLines(image, angleCount, distCount, amount);
		for (double[] line : lines) {
			drawLineNormal(g, line[1], line[2]);
		}

		return new Image(bufferedImage);
	}

	private static ArrayList<double[]> getHoughLines(Image image, int angleCount, int distCount, int amount) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] grayChannel = image.getGrayChannel();

		double angleStep = Math.PI / angleCount;
		double distStep = Math.max(width, height) / distCount;
		double[][] votes = new double[angleCount][distCount];
		ArrayList<double[]> lines = new ArrayList<double[]>();

		// get the votes for each line
		for (int angleIter = 0; angleIter < angleCount; angleIter++) {
			double angle = angleIter * angleStep;
			Log.d("progress " + (100.0 * (angleIter + 1) / angleCount) + "%");

			for (int distIter = 0; distIter < distCount; distIter++) {
				double dist = distIter * distStep;
				double val = getHoughVotes(grayChannel, angle, dist);
				votes[angleIter][distIter] = val;
				lines.add(new double[] { val, angle, dist });
			}
		}

		ArrayList<double[]> limitedLines = filterMostVoted(lines, amount);
		Log.d("number of lines kept: " + limitedLines.size());

		return limitedLines;
	}

	private static double getHoughVotes(int[][] channel, double angle, double dist) {
		int width = channel.length;
		int height = channel[0].length;
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		double epsilon = 1;
		double votes = 0;

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (channel[x][y] >= 128) {
					double space = Math.abs(dist - x * cos - y * sin);
					if (space == 0) {
						votes += 1;
					} else if (space <= epsilon) {
						votes += 1 / (1 + space);
					}
				}
			}
		}

		return votes;
	}

	public static Image houghCircles(Image image, int radius1, int radius2, int amount) {
		int width = image.getWidth();
		int height = image.getHeight();

		// create the buffered image
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = bufferedImage.getGraphics();
		g.setColor(Color.GREEN);

		// get the hough circles and draw them
		ArrayList<double[]> circles = new ArrayList<double[]>();
		circles.add(new double[] { 0, 54, 70, 32 });
		circles = getHoughCircles(image, radius1, radius2, amount);
		for (double[] circle : circles) {
			int x = (int) (circle[1] - circle[3]);
			int y = (int) (circle[2] - circle[3]);
			int size = (int) (circle[3] * 2);
			g.drawOval(x, y, size, size);
			Log.d("circle (" + circle[1] + ", " + circle[2] + ", " + circle[3] + ")");
		}

		return new Image(bufferedImage);
	}

	private static ArrayList<double[]> getHoughCircles(Image image, int radius1, int radius2, int amount) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] grayChannel = image.getGrayChannel();
		ArrayList<double[]> circles = new ArrayList<double[]>();

		// get the votes for each circle
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Log.d("progress " + (100.0 * (x * width + y) / (width * height + 1)) + "%");
				for (int radius = radius1; radius <= radius2; radius++) {
					double val = getHoughVotesCircle(grayChannel, x, y, radius);
					circles.add(new double[] { val, x, y, radius });
				}
			}
		}

		ArrayList<double[]> limitedCircles = filterMostVoted(circles, amount);
		Log.d("number of circles kept: " + limitedCircles.size());

		return limitedCircles;
	}

	private static double getHoughVotesCircle(int[][] channel, int centerX, int centerY, double radius) {
		int width = channel.length;
		int height = channel[0].length;
		double epsilon = 10;
		double votes = 0;

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (channel[x][y] >= 128) {
					double space = Math.abs(radius * radius - (x - centerX) * (x - centerX) - (y - centerY) * (y - centerY));
					if (space == 0) {
						votes += 1;
					} else if (space <= epsilon) {
						votes += 1 / (1 + space);
					}
				}
			}
		}

		return votes;
	}

	private static ArrayList<double[]> filterMostVoted(ArrayList<double[]> lines, int amount) {
		// sort the lines
		Collections.sort(lines, new Comparator<double[]>() {
			@Override
			public int compare(double[] o1, double[] o2) {
				if (o1[0] > o2[0]) {
					return -1;
				} else if (o1[0] < o2[0]) {
					return 1;
				} else {
					return 0;
				}
			}
		});

		// get the amount first lines
		ArrayList<double[]> limitedLines = new ArrayList<double[]>();
		for (double[] obj : lines) {
			if (amount > 0 && limitedLines.size() >= amount) {
				break;
			}
			limitedLines.add(obj);
		}

		return limitedLines;
	}

	public static LevelSetState levelSetStateFromRect(Image image, Rectangle initRect) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] phi = new int[width][height];
		LinkedList<Point> lin = new LinkedList<Point>();
		LinkedList<Point> lout = new LinkedList<Point>();
		double[] averageIn = new double[3];
		int numIn = 0;
		double[] averageOut = new double[3];
		int numOut = 0;

		// transform the rectangle into phi, lin and lout
		Rectangle insideRect = new Rectangle(initRect.x + 1, initRect.y + 1, initRect.width - 2, initRect.height - 2);
		Rectangle lOutRect = new Rectangle(initRect.x - 1, initRect.y - 1, initRect.width + 2, initRect.height + 2);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (insideRect.contains(x, y)) {
					phi[x][y] = -3;
					averageIn[0] += image.getRed(x, y);
					averageIn[1] += image.getGreen(x, y);
					averageIn[2] += image.getBlue(x, y);
					numIn++;
				} else if (initRect.contains(x, y)) {
					phi[x][y] = -1;
					lin.add(new Point(x, y));
				} else if (lOutRect.contains(x, y)) {
					phi[x][y] = 1;
					lout.add(new Point(x, y));
				} else {
					phi[x][y] = 3;
					averageOut[0] += image.getRed(x, y);
					averageOut[1] += image.getGreen(x, y);
					averageOut[2] += image.getBlue(x, y);
					numOut++;
				}
			}
		}

		// calculate the average colors
		averageIn[0] = averageIn[0] / numIn;
		averageIn[1] = averageIn[1] / numIn;
		averageIn[2] = averageIn[2] / numIn;
		averageOut[0] = averageOut[0] / numOut;
		averageOut[1] = averageOut[1] / numOut;
		averageOut[2] = averageOut[2] / numOut;
		//Log.d("in=" + Arrays.toString(averageIn));
		//Log.d("out=" + Arrays.toString(averageOut));

		return new LevelSetState(phi, lin, lout, averageIn, averageOut);
	}

	public static Image levelSet(Image image, LevelSetState state, int mode, int maxIterCycle1, int maxIterCycle2) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] phi = state.phi;
		double[][] fd = new double[width][height];
		LinkedList<Point> lin = state.lin;
		LinkedList<Point> lout = state.lout;
		LinkedList<Point> copy;
		double[] averageIn = state.averageIn;
		double[] averageOut = state.averageOut;
		int iter;

		// cycle one
		for (iter = 0; iter < maxIterCycle1; iter++) {
			boolean done = true;

			// compute the speed fd for lout and lin
			for (Point p : lout) {
				fd[p.x][p.y] = levelSetCalcSpeed(image, p.x, p.y, averageIn, averageOut);
			}
			for (Point p : lin) {
				fd[p.x][p.y] = levelSetCalcSpeed(image, p.x, p.y, averageIn, averageOut);
			}

			// switch in the pixels with a positive speed
			copy = new LinkedList<Point>(lout);
			for (Point p : copy) {
				if (!isBorder(p.x, p.y, width, height) && fd[p.x][p.y] > 0) {
					done = false;
					levelSetSwitchIn(p, lin, lout, phi);
				}
			}

			// remove pixels from lin that are now inside
			levelSetCheckInside(lin, phi, width, height);

			// switch out the pixels with a negative speed
			copy = new LinkedList<Point>(lin);
			for (Point p : copy) {
				if (!isBorder(p.x, p.y, width, height) && fd[p.x][p.y] < 0) {
					done = false;
					levelSetSwitchOut(p, lin, lout, phi);
				}
			}

			// remove pixels from lout that are now outside
			levelSetCheckOutside(lout, phi, width, height);

			// stop if we didn't find any pixel to switch
			if (done) {
				break;
			}
		}

		Log.d("stopped cycle one at iteration " + iter);

		// cycle two
		for (iter = 0; iter < maxIterCycle2; iter++) {
			// apply gauss smoothing to lout
			copy = new LinkedList<Point>(lout);
			for (Point p : copy) {
				if (!isBorder(p.x, p.y, width, height) && levelSetGauss(p.x, p.y, phi) < 0) {
					levelSetSwitchIn(p, lin, lout, phi);
				}
			}

			// remove pixels from lin that are now inside
			levelSetCheckInside(lin, phi, width, height);

			// apply gauss smoothing to lin
			copy = new LinkedList<Point>(lin);
			for (Point p : copy) {
				if (!isBorder(p.x, p.y, width, height) && levelSetGauss(p.x, p.y, phi) > 0) {
					levelSetSwitchOut(p, lin, lout, phi);
				}
			}

			// remove pixels from lout that are now outside
			levelSetCheckOutside(lout, phi, width, height);
		}

		return levelSetDrawPhi(image, phi, mode);
	}

	private static boolean isBorder(int x, int y, int width, int height) {
		return x == 0 || x == width - 1 || y == 0 || y == height - 1;
	}

	private static boolean isOutOfBounds(int x, int y, int width, int height) {
		return x < 0 || x >= width || y < 0 || y >= height;
	}

	private static double levelSetGauss(int x, int y, int[][] phi) {
		double value = 0;
		int maskWidth = 5;
		int maskHeight = 5;
		double sigma = 0.5;
		double c = 1 / (4 * Math.PI * sigma);

		for (int i = -maskWidth / 2; i <= maskWidth / 2; i++) {
			for (int j = -maskHeight / 2; j <= maskHeight / 2; j++) {
				double dist = i * i + j * j;
				double exp = Math.exp((-1 * dist) / 4 * sigma);
				double val = c * exp;
				value += getValueInBounds(phi, x + i, y + j) * val;
			}
		}

		return value;
	}

	private static double levelSetCalcSpeed(Image image, int x, int y, double[] averageIn, double[] averageOut) {
		int red = image.getRed(x, y);
		int green = image.getGreen(x, y);
		int blue = image.getBlue(x, y);

		double p1 = Math.sqrt(Math.pow((averageIn[0] - red), 2) + Math.pow((averageIn[1] - green), 2) + Math.pow((averageIn[2] - blue), 2));
		double p2 = Math.sqrt(Math.pow((averageOut[0] - red), 2) + Math.pow((averageOut[1] - green), 2) + Math.pow((averageOut[2] - blue), 2));
		double psigma1 = 1 - p1 / (Math.sqrt(3) * 255);
		double psigma2 = 1 - p2 / (3 * 255);

		return Math.log(psigma1 / psigma2);
	}

	private static void levelSetSwitchIn(Point p, LinkedList<Point> lin, LinkedList<Point> lout, int[][] phi) {
		lout.remove(p);
		lin.add(p);
		phi[p.x][p.y] = -1;
		for (int x = -1; x < 2; x++) {
			for (int y = -1; y < 2; y++) {
				if (Math.abs(x - y) == 1 && phi[p.x + x][p.y + y] == 3) {
					lout.add(new Point(p.x + x, p.y + y));
					phi[p.x + x][p.y + y] = 1;
				}
			}
		}
	}

	private static void levelSetSwitchOut(Point p, LinkedList<Point> lin, LinkedList<Point> lout, int[][] phi) {
		lin.remove(p);
		lout.add(p);
		phi[p.x][p.y] = 1;
		for (int x = -1; x < 2; x++) {
			for (int y = -1; y < 2; y++) {
				if (Math.abs(x - y) == 1 && phi[p.x + x][p.y + y] == -3) {
					lin.add(new Point(p.x + x, p.y + y));
					phi[p.x + x][p.y + y] = -1;
				}
			}
		}
	}

	private static void levelSetCheckInside(LinkedList<Point> lin, int[][] phi, int width, int height) {
		Iterator<Point> it = lin.iterator();
		while (it.hasNext()) {
			Point p = it.next();
			boolean toRemove = true;
			for (int x = -1; x < 2; x++) {
				for (int y = -1; y < 2; y++) {
					if (Math.abs(x - y) == 1 && !isOutOfBounds(p.x + x, p.y + y, width, height) && phi[p.x + x][p.y + y] > 0) {
						toRemove = false;
					}
				}
			}
			if (toRemove) {
				it.remove();
				phi[p.x][p.y] = -3;
			}
		}
	}

	private static void levelSetCheckOutside(LinkedList<Point> lout, int[][] phi, int width, int height) {
		Iterator<Point> it = lout.iterator();
		while (it.hasNext()) {
			Point p = it.next();
			boolean toRemove = true;
			for (int x = -1; x < 2; x++) {
				for (int y = -1; y < 2; y++) {
					if (Math.abs(x - y) == 1 && !isOutOfBounds(p.x + x, p.y + y, width, height) && phi[p.x + x][p.y + y] < 0) {
						toRemove = false;
					}
				}
			}
			if (toRemove) {
				it.remove();
				phi[p.x][p.y] = 3;
			}
		}
	}

	private static Image levelSetDrawPhi(Image image, int[][] phi, int mode) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] redChannel = new int[width][height];
		int[][] greenChannel = new int[width][height];
		int[][] blueChannel = new int[width][height];

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (phi[x][y] == -1) {
					redChannel[x][y] = 255;
				} else if (phi[x][y] == 1) {
					blueChannel[x][y] = 255;
				} else if (mode == 0) {
					redChannel[x][y] = image.getRed(x, y);
					greenChannel[x][y] = image.getGreen(x, y);
					blueChannel[x][y] = image.getBlue(x, y);
				}
			}
		}

		return new Image(redChannel, greenChannel, blueChannel);
	}

	private static int toDiscreteAngle(double angle) {
		if (angle < 22.5) {
			return 0;
		} else if (angle < 67.5) {
			return 45;
		} else if (angle < 112.5) {
			return 90;
		} else if (angle < 157.5) {
			return 135;
		} else {
			return 0;
		}
	}

	private static int getValueInBounds(int[][] array, int x, int y) {
		x = Math.max(0, Math.min(array.length - 1, x));
		y = Math.max(0, Math.min(array[x].length - 1, y));
		return array[x][y];
	}

	private static double leclerc(double x, double sigma) {
		return Math.exp((-1 * x * x) / (sigma * sigma));
	}

	private static double lorentziano(double x, double sigma) {
		return 1 / (1 + (x * x) / (sigma * sigma));
	}

	private static int maskSize(double sigma) {
		int size = (int) (3 * sigma);
		if (size % 2 == 0) {
			size++;
		}
		return size;
	}

	private static void applyFactorMask(Image image, double[][] mask, double factor, int[][] redChannel, int[][] greenChannel, int[][] blueChannel) {
		int width = image.getWidth();
		int height = image.getHeight();
		int maskWidth = mask.length;
		int maskHeight = mask[0].length;
		int offsetX = (int) (Math.ceil(maskWidth / 2.0) - 1);
		int offsetY = (int) (Math.ceil(maskHeight / 2.0) - 1);

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
	}

	private static void applyGradientMask(Image image, ArrayList<double[][]> masks, int[][] redChannel, int[][] greenChannel, int[][] blueChannel) {
		int width = image.getWidth();
		int height = image.getHeight();

		for (int pixelX = 0; pixelX < width; pixelX++) {
			for (int pixelY = 0; pixelY < height; pixelY++) {
				double totalRedSum = 0;
				double totalGreenSum = 0;
				double totalBlueSum = 0;

				// iterate over all the masks
				for (int i = 0; i < masks.size(); i++) {
					// get the mask information
					double[][] mask = masks.get(i);
					int maskWidth = mask.length;
					int maskHeight = mask[0].length;
					int offsetX = (int) (Math.ceil(maskWidth / 2.0) - 1);
					int offsetY = (int) (Math.ceil(maskHeight / 2.0) - 1);

					// iterate over the mask for that pixel
					double redSum = 0;
					double greenSum = 0;
					double blueSum = 0;
					for (int x = 0; x < maskWidth; x++) {
						for (int y = 0; y < maskHeight; y++) {
							redSum += mask[x][y] * image.getRed(pixelX - offsetX + x, pixelY - offsetY + y);
							greenSum += mask[x][y] * image.getGreen(pixelX - offsetX + x, pixelY - offsetY + y);
							blueSum += mask[x][y] * image.getBlue(pixelX - offsetX + x, pixelY - offsetY + y);
						}
					}

					totalRedSum += redSum * redSum;
					totalGreenSum += greenSum * greenSum;
					totalBlueSum += blueSum * blueSum;
				}

				// set the new image pixels
				redChannel[pixelX][pixelY] = (int) Math.min(255, Math.max(0, Math.sqrt(totalRedSum)));
				greenChannel[pixelX][pixelY] = (int) Math.min(255, Math.max(0, Math.sqrt(totalGreenSum)));
				blueChannel[pixelX][pixelY] = (int) Math.min(255, Math.max(0, Math.sqrt(totalBlueSum)));
			}
		}
	}

	private static int[][] grayGradient(Image image, double[][] mask) {
		int width = image.getWidth();
		int height = image.getHeight();
		int maskWidth = mask.length;
		int maskHeight = mask[0].length;
		int offsetX = (int) (Math.ceil(maskWidth / 2.0) - 1);
		int offsetY = (int) (Math.ceil(maskHeight / 2.0) - 1);
		int[][] gradient = new int[width][height];

		for (int pixelX = 0; pixelX < width; pixelX++) {
			for (int pixelY = 0; pixelY < height; pixelY++) {
				double sum = 0;
				for (int x = 0; x < maskWidth; x++) {
					for (int y = 0; y < maskHeight; y++) {
						sum += mask[x][y] * image.getGray(pixelX - offsetX + x, pixelY - offsetY + y);
					}
				}
				gradient[pixelX][pixelY] = (int) sum;
			}
		}

		return gradient;
	}
}
