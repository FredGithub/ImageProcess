package ar.edu.itba.imageprocess;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Image {

	public static final int CHANNEL_GRAY = 0;
	public static final int CHANNEL_RED = 1;
	public static final int CHANNEL_GREEN = 2;
	public static final int CHANNEL_BLUE = 3;

	private int mWidth;
	private int mHeight;
	private int[][] mRedChannel;
	private int[][] mGreenChannel;
	private int[][] mBlueChannel;
	private BufferedImage mBufferedImage;

	public Image(BufferedImage bufferedImage) {
		setBufferedImage(bufferedImage);
	}

	public int getWidth() {
		return mWidth;
	}

	public int getHeight() {
		return mHeight;
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

	public int getGray(int x, int y) {
		return (mRedChannel[x][y] + mRedChannel[x][y] + mRedChannel[x][y]) / 3;
	}

	public int[][] getGrayChannel() {
		int[][] grayChannel = new int[mWidth][mHeight];
		for (int x = 0; x < mWidth; x++) {
			for (int y = 0; y < mHeight; y++) {
				grayChannel[x][y] = getGray(x, y);
			}
		}
		return grayChannel;
	}

	public int[][] getChannel(int channel) {
		if (channel == CHANNEL_GRAY) {
			return getGrayChannel();
		} else if (channel == CHANNEL_RED) {
			return mRedChannel;
		} else if (channel == CHANNEL_GREEN) {
			return mGreenChannel;
		} else if (channel == CHANNEL_BLUE) {
			return mBlueChannel;
		} else {
			return null;
		}
	}

	public int getChannelColor(int x, int y, int channel) {
		if (channel == CHANNEL_GRAY) {
			return getGray(x, y);
		} else if (channel == CHANNEL_RED) {
			return mRedChannel[x][y];
		} else if (channel == CHANNEL_GREEN) {
			return mGreenChannel[x][y];
		} else if (channel == CHANNEL_BLUE) {
			return mBlueChannel[x][y];
		} else {
			return 0;
		}
	}

	public BufferedImage getBufferedImage() {
		return mBufferedImage;
	}

	public void setBufferedImage(BufferedImage bufferedImage) {
		mRedChannel = new int[bufferedImage.getWidth()][bufferedImage.getHeight()];
		mGreenChannel = new int[bufferedImage.getWidth()][bufferedImage.getHeight()];
		mBlueChannel = new int[bufferedImage.getWidth()][bufferedImage.getHeight()];
		mBufferedImage = bufferedImage;
		mWidth = bufferedImage.getWidth();
		mHeight = bufferedImage.getHeight();

		for (int x = 0; x < mWidth; x++) {
			for (int y = 0; y < mHeight; y++) {
				Color color = new Color(bufferedImage.getRGB(x, y));
				mRedChannel[x][y] = color.getRed();
				mGreenChannel[x][y] = color.getGreen();
				mBlueChannel[x][y] = color.getBlue();
			}
		}
	}

	public int[] getHistogram(int channel) {
		int[] histogram = new int[256];
		for (int x = 0; x < mWidth; x++) {
			for (int y = 0; y < mHeight; y++) {
				histogram[getChannelColor(x, y, channel)]++;
			}
		}
		return histogram;
	}
}
