package ar.edu.itba.imageprocess;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import ar.edu.itba.imageprocess.utils.ArrayUtils;
import ar.edu.itba.imageprocess.utils.ExtImageIO;
import ar.edu.itba.imageprocess.utils.FileUtils;
import ar.edu.itba.imageprocess.utils.Log;

public class MainController {

	private MainFrame mMainFrame;
	private ImagePane mImagePaneSource;
	private ImagePane mImagePaneDest;

	public MainController() {
		mMainFrame = new MainFrame(this);
		mMainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mImagePaneSource = null;
		mImagePaneDest = null;
	}

	public void start() {
		mMainFrame.start();
	}

	public void repaintMainFrame() {
		if (mMainFrame != null) {
			mMainFrame.repaint();
		}
	}

	public void selectSourceImagePane(ImagePane imagePane) {
		if (imagePane != null && imagePane != mImagePaneSource) {
			Log.d("selecting source image pane " + imagePane.getIndex());
			if (mImagePaneSource != null) {
				mImagePaneSource.setSource(false);
			}
			mImagePaneSource = imagePane;
			mImagePaneSource.setSource(true);
			repaintMainFrame();
		}
	}

	public void selectDestImagePane(ImagePane imagePane) {
		if (imagePane != null && imagePane != mImagePaneDest) {
			Log.d("selecting dest image pane " + imagePane.getIndex());
			if (mImagePaneDest != null) {
				mImagePaneDest.setDest(false);
			}
			mImagePaneDest = imagePane;
			mImagePaneDest.setDest(true);
			repaintMainFrame();
		}
	}

	public void loadImage(File file, int width, int height) {
		if (mImagePaneDest != null) {
			Log.d("opening " + file.getName());
			try {
				String extension = FileUtils.getFileExtension(file);
				Image image = null;
				if (extension.equals("raw")) {
					image = new Image(ExtImageIO.readRawImageChannel(file, width, height));
				} else if (extension.equals("pgm")) {
					image = new Image(ExtImageIO.readPgmImageChannel(file));
				} else if (extension.equals("ppm")) {
					int[][][] channels = ExtImageIO.readPpmImageChannels(file);
					image = new Image(channels[0], channels[1], channels[2]);
				} else {
					BufferedImage bufferedImage = ImageIO.read(file);
					if (bufferedImage != null) {
						image = new Image(bufferedImage);
					}
				}
				if (image != null) {
					mImagePaneDest.setImageWithHistory(image);
					repaintMainFrame();
				} else {
					Log.d("couldn't load image " + file.getName());
				}
			} catch (IOException e) {
				Log.d("couldn't open file! " + e);
			} catch (Exception e) {
				Log.d("unknown error! " + e);
			}
		}
	}

	public void saveImage(File file) {
		if (mImagePaneSource != null && mImagePaneSource.getImage() != null) {
			Log.d("saving " + file.getName());
			String extension = FileUtils.getFileExtension(file);
			try {
				if (extension.matches("gif|png|jpe?g")) {
					ImageIO.write(mImagePaneSource.getImage().getBufferedImage(), extension, file);
				} else if (extension.matches("raw|pgm|ppm")) {
					ExtImageIO.write(mImagePaneSource.getImage().getBufferedImage(), file);
				} else {
					Log.d("unsupported format " + extension);
				}
			} catch (IOException e) {
				Log.d("couldn't save file! " + e);
			}
		}
	}

	public void generateWhiteImage() {
		if (mImagePaneDest != null) {
			Image image = Filters.generateWhiteImage(256, 256);
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	public void generateCircle() {
		if (mImagePaneDest != null) {
			Image image = Filters.generateCircle(92, 256);
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	public void generateSquare() {
		if (mImagePaneDest != null) {
			Image image = Filters.generateSquare(92, 256);
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	public void generateGradient() {
		if (mImagePaneDest != null) {
			Image image = Filters.generateGradient(256);
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	public void generateColorGradient() {
		if (mImagePaneDest != null) {
			Image image = Filters.generateColorGradient(256);
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	public void addImages() {
		ImagePane[] imagePanes = mMainFrame.getImagePanes();
		if (mImagePaneDest != null && imagePanes[0].getImage() != null && imagePanes[1].getImage() != null) {
			Image image = Filters.addImages(imagePanes[0].getImage(), imagePanes[1].getImage());
			if (image != null) {
				mImagePaneDest.setImageWithHistory(image);
			}
		}
	}

	public void subtractImages() {
		ImagePane[] imagePanes = mMainFrame.getImagePanes();
		if (mImagePaneDest != null && imagePanes[0].getImage() != null && imagePanes[1].getImage() != null) {
			Image image = Filters.subtractImages(imagePanes[0].getImage(), imagePanes[1].getImage());
			if (image != null) {
				mImagePaneDest.setImageWithHistory(image);
			}
		}
	}

	public void multiplyScalar(double scalar) {
		if (mImagePaneDest != null) {
			Image image = Filters.multiplyScalar(mImagePaneSource.getImage(), scalar);
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	public void compressLinear() {
		if (mImagePaneDest != null && mImagePaneSource != null && mImagePaneSource.getImage() != null) {
			Image image = Filters.compressLinear(mImagePaneSource.getImage());
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	public void compress() {
		if (mImagePaneDest != null && mImagePaneSource != null && mImagePaneSource.getImage() != null) {
			Image image = Filters.compress(mImagePaneSource.getImage());
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	public void desaturate() {
		if (mImagePaneDest != null && mImagePaneSource != null && mImagePaneSource.getImage() != null) {
			Image image = new Image(mImagePaneSource.getImage().getGrayChannel());
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	public void displayHistogram() {
		if (mImagePaneDest != null && mImagePaneSource != null && mImagePaneSource.getImage() != null) {
			int[] values = ArrayUtils.intArray2Dto1D(mImagePaneSource.getImage().getGrayChannel());
			Image image = Filters.generateHistogramImage(values);
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	/**
	 * TP1-2 Creates a negative of the image.
	 */
	public void filterNegative() {
		if (mImagePaneDest != null && mImagePaneSource != null && mImagePaneSource.getImage() != null) {
			Image image = Filters.filterNegative(mImagePaneSource.getImage());
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	/**
	 * TP1-5 Creates a threshold version of the image.
	 */
	public void filterThreshold(int threshold) {
		if (mImagePaneDest != null && mImagePaneSource != null && mImagePaneSource.getImage() != null) {
			Image image = Filters.filterThreshold(mImagePaneSource.getImage(), threshold);
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	public void filterContrast(int r1, int r2, int s1, int s2) {
		if (mImagePaneDest != null && mImagePaneSource != null && mImagePaneSource.getImage() != null) {
			Image image = Filters.filterContrast(mImagePaneSource.getImage(), r1, r2, s1, s2);
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	/**
	 * TP1-6 histogram equalization
	 * 
	 * http://www.mee.tcd.ie/~ack/teaching/1e8/histogram_equalisation_slides.pdf
	 */
	public void filterEqualize() {
		if (mImagePaneDest != null && mImagePaneSource != null && mImagePaneSource.getImage() != null) {
			Image image = Filters.filterEqualize(mImagePaneSource.getImage());
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	/**
	 * Adds gaussian noise to an image
	 */

	public void applyAddGaussianNoise(double spread, double average, double percentage) {
		if (mImagePaneDest != null && mImagePaneSource != null && mImagePaneSource.getImage() != null) {
			Image image = Filters.applyAddGaussianNoise(mImagePaneSource.getImage(), spread, average, percentage);
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	/**
	 * Adds rayleigh noise to an image
	 */

	public void applyMulRayleighNoise(double p, double percentage) {
		if (mImagePaneDest != null && mImagePaneSource != null && mImagePaneSource.getImage() != null) {
			Image image = Filters.applyMulRayleighNoise(mImagePaneSource.getImage(), p, percentage);
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	/**
	 * Adds exponential noise to an image
	 */
	public void applyMulExponentialNoise(double p, double percentage) {
		if (mImagePaneDest != null && mImagePaneSource != null && mImagePaneSource.getImage() != null) {
			Image image = Filters.applyMulExponentialNoise(mImagePaneSource.getImage(), p, percentage);
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	public void displayGaussianChart(double spread, double average) {
		if (mImagePaneDest != null) {
			Image image = Filters.generateGaussianChartImage(spread, average);
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	public void displayRayleighChart(double param) {
		if (mImagePaneDest != null) {
			Image image = Filters.generateRayleighChartImage(param);
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	public void displayExponentialChart(double param) {
		if (mImagePaneDest != null) {
			Image image = Filters.generateExponentialChartImage(param);
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	public void applyPepperAndSalt(double p0, double p1) {
		if (mImagePaneDest != null && mImagePaneSource != null && mImagePaneSource.getImage() != null) {
			Image image = Filters.applyPepperAndSalt(mImagePaneSource.getImage(), p0, p1);
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	public void applyFactorMaskFilter(int maskWidth, int maskHeight, int filterType) {
		if (mImagePaneDest != null && mImagePaneSource != null && mImagePaneSource.getImage() != null) {
			Image image = Filters.applyFactorMaskFilter(mImagePaneSource.getImage(), maskWidth, maskHeight, filterType);
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	public void applyGaussianMaskFilter(int maskWidth, int maskHeight, double spread) {
		if (mImagePaneDest != null && mImagePaneSource != null && mImagePaneSource.getImage() != null) {
			Image image = Filters.applyGaussianMaskFilter(mImagePaneSource.getImage(), maskWidth, maskHeight, spread);
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	public void applyMedianMaskFilter(int maskWidth, int maskHeight) {
		if (mImagePaneDest != null && mImagePaneSource != null && mImagePaneSource.getImage() != null) {
			Image image = Filters.applyMedianMaskFilter(mImagePaneSource.getImage(), maskWidth, maskHeight);
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	public void robertsBordersDetection() {
		if (mImagePaneDest != null && mImagePaneSource != null && mImagePaneSource.getImage() != null) {
			Image image = Filters.robertsBorderDetection(mImagePaneSource.getImage());
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	public void prewittBordersDetection() {
		if (mImagePaneDest != null && mImagePaneSource != null && mImagePaneSource.getImage() != null) {
			Image image = Filters.prewittBorderDetection(mImagePaneSource.getImage());
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	public void sobelBordersDetection() {
		if (mImagePaneDest != null && mImagePaneSource != null && mImagePaneSource.getImage() != null) {
			Image image = Filters.sobelBorderDetection(mImagePaneSource.getImage());
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	public void simpleBordersDetection() {
		if (mImagePaneDest != null && mImagePaneSource != null && mImagePaneSource.getImage() != null) {
			Image image = Filters.simpleBorderDetection(mImagePaneSource.getImage());
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	public void kirshBordersDetection() {
		if (mImagePaneDest != null && mImagePaneSource != null && mImagePaneSource.getImage() != null) {
			Image image = Filters.kirshBorderDetection(mImagePaneSource.getImage());
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	public void laplacianBordersDetection(int threshold) {
		if (mImagePaneDest != null && mImagePaneSource != null && mImagePaneSource.getImage() != null) {
			Image image = Filters.laplacianBorderDetection(mImagePaneSource.getImage(), threshold);
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	public void laplacianGaussianBordersDetection(int maskWidth, int maskHeight, double sigma, int threshold) {
		if (mImagePaneDest != null && mImagePaneSource != null && mImagePaneSource.getImage() != null) {
			Image image = Filters.laplacianGaussianBorderDetection(mImagePaneSource.getImage(), maskWidth, maskHeight, sigma, threshold);
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	public void isotropicFilter(int maskWidth, int maskHeight, double sigma) {
		if (mImagePaneDest != null && mImagePaneSource != null && mImagePaneSource.getImage() != null) {
			Image image = Filters.isotropicFilter(mImagePaneSource.getImage(), maskWidth, maskHeight, sigma);
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	public void anisotropicFilter(int steps, double sigma, int method) {
		if (mImagePaneDest != null && mImagePaneSource != null && mImagePaneSource.getImage() != null) {
			Image image = Filters.anisotropicFilter(mImagePaneSource.getImage(), steps, sigma, method);
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	public void globalThreshold(double delta) {
		if (mImagePaneDest != null && mImagePaneSource != null && mImagePaneSource.getImage() != null) {
			int threshold = Filters.globalThreshold(mImagePaneSource.getImage(), delta);
			Image image = Filters.filterThreshold(mImagePaneSource.getImage(), threshold);
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	public void otsuThreshold() {
		if (mImagePaneDest != null && mImagePaneSource != null && mImagePaneSource.getImage() != null) {
			int threshold = Filters.otsuThreshold(mImagePaneSource.getImage(), Image.CHANNEL_GRAY);
			Image image = Filters.filterThreshold(mImagePaneSource.getImage(), threshold);
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	public void otsuColorThreshold() {
		if (mImagePaneDest != null && mImagePaneSource != null && mImagePaneSource.getImage() != null) {
			int thresholdRed = Filters.otsuThreshold(mImagePaneSource.getImage(), Image.CHANNEL_RED);
			int thresholdGreen = Filters.otsuThreshold(mImagePaneSource.getImage(), Image.CHANNEL_GREEN);
			int thresholdBlue = Filters.otsuThreshold(mImagePaneSource.getImage(), Image.CHANNEL_BLUE);
			Image image = Filters.filterThreshold(mImagePaneSource.getImage(), thresholdRed, thresholdGreen, thresholdBlue);
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	public void nonMaximum() {
		if (mImagePaneDest != null && mImagePaneSource != null && mImagePaneSource.getImage() != null) {
			Image image = Filters.nonMaximum(mImagePaneSource.getImage());
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	public void histeresisThreshold(int thresholdLow, int thresholdHigh) {
		if (mImagePaneDest != null && mImagePaneSource != null && mImagePaneSource.getImage() != null) {
			Image image = Filters.histeresisThreshold(mImagePaneSource.getImage(), thresholdLow, thresholdHigh);
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	public void canny(double sigma, int thresholdLow, int thresholdHigh) {
		if (mImagePaneDest != null && mImagePaneSource != null && mImagePaneSource.getImage() != null) {
			Image image = Filters.canny(mImagePaneSource.getImage(), sigma, thresholdLow, thresholdHigh);
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	public void susan(int threshold) {
		if (mImagePaneDest != null && mImagePaneSource != null && mImagePaneSource.getImage() != null) {
			Image image = Filters.susan(mImagePaneSource.getImage(), threshold);
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	public void houghLines(int angleCount, int distCount, int amount) {
		if (mImagePaneDest != null && mImagePaneSource != null && mImagePaneSource.getImage() != null) {
			Image image = Filters.houghLines(mImagePaneSource.getImage(), angleCount, distCount, amount);
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	public void levelSet() {
		if (mImagePaneDest != null && mImagePaneSource != null && mImagePaneSource.getImage() != null) {
			Image image = Filters.levelSet(mImagePaneSource.getImage(), mImagePaneSource.getRect());
			mImagePaneDest.setImageWithHistory(image);
		}
	}
}
