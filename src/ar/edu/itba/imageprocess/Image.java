package ar.edu.itba.imageprocess;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Image {

	private int[][] mRedChannel;
	private int[][] mGreenChannel;
	private int[][] mBlueChannel;
	private BufferedImage mBufferedImage;

	public Image(BufferedImage bufferedImage) {
		setBufferedImage(bufferedImage);
	}

	public int[][] getRedChannel() {
		return mRedChannel;
	}

	public int getRed(int x, int y) {
		return mRedChannel[x][y];
	}

	public int[][] getGreenChannel() {
		return mGreenChannel;
	}

	public int getGreen(int x, int y) {
		return mGreenChannel[x][y];
	}

	public int[][] getBlueChannel() {
		return mBlueChannel;
	}

	public int getBlue(int x, int y) {
		return mBlueChannel[x][y];
	}

	public BufferedImage getBufferedImage() {
		return mBufferedImage;
	}

	public void setBufferedImage(BufferedImage bufferedImage) {
		mRedChannel = new int[bufferedImage.getWidth()][bufferedImage.getHeight()];
		mGreenChannel = new int[bufferedImage.getWidth()][bufferedImage.getHeight()];
		mBlueChannel = new int[bufferedImage.getWidth()][bufferedImage.getHeight()];
		mBufferedImage = bufferedImage;

		for (int x = 0; x < bufferedImage.getWidth(); x++) {
			for (int y = 0; y < bufferedImage.getHeight(); y++) {
				Color color = new Color(bufferedImage.getRGB(x, y));
				mRedChannel[x][y] = color.getRed();
				mGreenChannel[x][y] = color.getGreen();
				mBlueChannel[x][y] = color.getBlue();
			}
		}
	}
}
