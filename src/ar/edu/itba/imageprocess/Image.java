package ar.edu.itba.imageprocess;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Image class This class holds both 3 array of ints that represent its colors
 * and a BufferedImage. The BufferedImage colors are always trimmed between 0
 * and 255 with the method trimColor. The 3 arrays however are allowed to have
 * values lower than 0 or higher than 255. This allows to apply algorithm on the
 * image that will give values outside of the [0, 255] range, and only after
 * compress these values with the compress method
 */

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
		drawBufferedImage(bufferedImage);
	}

	public Image(int[][] redChannel, int[][] greenChannel, int[][] blueChannel) {
		drawChannels(redChannel, greenChannel, blueChannel);
	}

	public Image(int[][] grayChannel) {
		drawGrayChannel(grayChannel);
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
		// if the pixel is outside of the image give the one of the border
		x = Math.max(0, Math.min(mWidth - 1, x));
		y = Math.max(0, Math.min(mHeight - 1, y));
		return mRedChannel[x][y];
	}

	public int[][] getGreenChannel() {
		return mGreenChannel;
	}

	public int getGreen(int x, int y) {
		// if the pixel is outside of the image give the one of the border
		x = Math.max(0, Math.min(mWidth - 1, x));
		y = Math.max(0, Math.min(mHeight - 1, y));
		return mGreenChannel[x][y];
	}

	public int[][] getBlueChannel() {
		return mBlueChannel;
	}

	public int getBlue(int x, int y) {
		// if the pixel is outside of the image give the one of the border
		x = Math.max(0, Math.min(mWidth - 1, x));
		y = Math.max(0, Math.min(mHeight - 1, y));
		return mBlueChannel[x][y];
	}

	public int getRGB(int x, int y) {
		return getRed(x, y) * 256 * 256 + getGreen(x, y) * 256 + getBlue(x, y);
	}

	public int getGray(int x, int y) {
		// if the pixel is outside of the image give the one of the border
		x = Math.max(0, Math.min(mWidth - 1, x));
		y = Math.max(0, Math.min(mHeight - 1, y));
		return (int) (0.2126 * mRedChannel[x][y] + 0.7152 * mGreenChannel[x][y] + 0.0722 * mBlueChannel[x][y]);
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

	/**
	 * Calculates the histogram of the specified channel. The histogram size is
	 * at least 256. If the image has pixels with a value higher than 255 or
	 * lower than 0, the histogram will be bigger
	 */
	public int[] getHistogram(int channel) {
		// get the bounds
		int[] bounds = getRange(channel);

		// make sure the histogram size is at least 256
		bounds[0] = Math.min(0, bounds[0]);
		bounds[1] = Math.max(255, bounds[1]);

		// get the size and prepare the histogram array
		int size = bounds[1] - bounds[0] + 1;
		int[] histogram = new int[size];

		// fill the histogram
		for (int x = 0; x < mWidth; x++) {
			for (int y = 0; y < mHeight; y++) {
				int index = getChannelColor(x, y, channel) + bounds[0];
				histogram[index]++;
			}
		}
		return histogram;
	}

	/**
	 * Get the lower and the higher value of the channel
	 * 
	 * @return an array formed as [lowerValue, higherValue]
	 */
	public int[] getRange(int channel) {
		int[] bounds = new int[] { getChannelColor(0, 0, channel), getChannelColor(0, 0, channel) };
		for (int x = 0; x < mWidth; x++) {
			for (int y = 0; y < mHeight; y++) {
				if (bounds[0] > getChannelColor(x, y, channel)) {
					bounds[0] = getChannelColor(x, y, channel);
				}
				if (bounds[1] < getChannelColor(x, y, channel)) {
					bounds[1] = getChannelColor(x, y, channel);
				}
			}
		}
		return bounds;
	}

	public void drawBufferedImage(BufferedImage bufferedImage) {
		mWidth = bufferedImage.getWidth();
		mHeight = bufferedImage.getHeight();
		mRedChannel = new int[mWidth][mHeight];
		mGreenChannel = new int[mWidth][mHeight];
		mBlueChannel = new int[mWidth][mHeight];
		mBufferedImage = new BufferedImage(mWidth, mHeight, BufferedImage.TYPE_INT_RGB);

		for (int x = 0; x < mWidth; x++) {
			for (int y = 0; y < mHeight; y++) {
				Color color = new Color(bufferedImage.getRGB(x, y));
				mRedChannel[x][y] = color.getRed();
				mGreenChannel[x][y] = color.getGreen();
				mBlueChannel[x][y] = color.getBlue();
				mBufferedImage.setRGB(x, y, color.getRGB());
			}
		}
	}

	public void drawChannels(int[][] redChannel, int[][] greenChannel, int[][] blueChannel) {
		mWidth = redChannel.length;
		mHeight = redChannel[0].length;
		mRedChannel = new int[mWidth][mHeight];
		mGreenChannel = new int[mWidth][mHeight];
		mBlueChannel = new int[mWidth][mHeight];
		mBufferedImage = new BufferedImage(mWidth, mHeight, BufferedImage.TYPE_INT_RGB);

		for (int x = 0; x < mWidth; x++) {
			for (int y = 0; y < mHeight; y++) {
				Color color = new Color(trimColor(redChannel[x][y]), trimColor(greenChannel[x][y]), trimColor(blueChannel[x][y]));
				mRedChannel[x][y] = redChannel[x][y];
				mGreenChannel[x][y] = greenChannel[x][y];
				mBlueChannel[x][y] = blueChannel[x][y];
				mBufferedImage.setRGB(x, y, color.getRGB());
			}
		}
	}

	public void drawGrayChannel(int[][] grayChannel) {
		mWidth = grayChannel.length;
		mHeight = grayChannel[0].length;
		mRedChannel = new int[mWidth][mHeight];
		mGreenChannel = new int[mWidth][mHeight];
		mBlueChannel = new int[mWidth][mHeight];
		mBufferedImage = new BufferedImage(mWidth, mHeight, BufferedImage.TYPE_INT_RGB);

		for (int x = 0; x < mWidth; x++) {
			for (int y = 0; y < mHeight; y++) {
				Color color = new Color(trimColor(grayChannel[x][y]), trimColor(grayChannel[x][y]), trimColor(grayChannel[x][y]));
				mRedChannel[x][y] = grayChannel[x][y];
				mGreenChannel[x][y] = grayChannel[x][y];
				mBlueChannel[x][y] = grayChannel[x][y];
				mBufferedImage.setRGB(x, y, color.getRGB());
			}
		}
	}

	private int trimColor(int value) {
		return Math.min(255, Math.max(0, value));
	}
}
