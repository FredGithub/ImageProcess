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
		int[][] redChannel = new int[width][height];
		int[][] greenChannel = new int[width][height];
		int[][] blueChannel = new int[width][height];
		for(int x=0; x<width; x++){
			for(int y=0; y<width; y++){
				redChannel[x][y] = 255;
				greenChannel[x][y] = 255;
				blueChannel[x][y] = 255;
			}
		}
		return new Image(redChannel, greenChannel, blueChannel);
	}
	
	public static Image generateCircle(int radius, int imageSize) {
		BufferedImage bufferedImage = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_BYTE_GRAY);
		Graphics2D g2d = bufferedImage.createGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fill(new Ellipse2D.Double(imageSize / 2 - radius / 2, imageSize / 2 - radius / 2, radius, radius));
		return new Image(bufferedImage);
	}

	public static Image generateSquare(int size, int imageSize) {
		BufferedImage bufferedImage = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_BYTE_GRAY);
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
	
	public static Image addImages(Image image1, Image image2){
		if(image1.getWidth() != image2.getWidth() || image1.getHeight() != image2.getHeight()){
			Log.d("images must be the same size");
			return null;
		}
		
		int width = image1.getWidth();
		int height = image1.getHeight();
		int[][] redChannel = new int[width][height];
		int[][] greenChannel = new int[width][height];
		int[][] blueChannel = new int[width][height];
		
		for(int x=0; x<width; x++){
			for(int y=0; y<height; y++){
				redChannel[x][y] = image1.getRed(x, y) + image2.getRed(x, y);
				greenChannel[x][y] = image1.getGreen(x, y) + image2.getGreen(x, y);
				blueChannel[x][y] = image1.getBlue(x, y) + image2.getBlue(x, y);
			}
		}
		
		return new Image(redChannel, greenChannel, blueChannel);
	}
}
