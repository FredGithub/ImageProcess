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
					if(bufferedImage != null){
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

	public void multiplyScalar() {
		if (mImagePaneDest != null) {

		}
	}

	public void compress() {
		if (mImagePaneDest != null && mImagePaneSource != null && mImagePaneSource.getImage() != null) {
			Image image = Filters.compress(mImagePaneSource.getImage());
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	public void desaturate() {
		if (mImagePaneSource != null && mImagePaneSource.getImage() != null) {
			Image image = new Image(mImagePaneSource.getImage().getGrayChannel());
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	public void displayHistogram() {
		if (mImagePaneSource != null && mImagePaneSource.getImage() != null) {
			int[] values = ArrayUtils.intArray2Dto1D(mImagePaneSource.getImage().getGrayChannel());
			Image image = Filters.generateHistogramImage(values);
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	/**
	 * TP1-2 Creates a negative of the image.
	 */
	public void filterNegative() {
		if (mImagePaneDest != null && mImagePaneSource.getImage() != null) {
			Image image = mImagePaneSource.getImage();
			int width = image.getWidth();
			int height = image.getHeight();
			BufferedImage imageDest = new BufferedImage(width, height, image.getBufferedImage().getType());

			for (int i = 0; i < image.getWidth(); i++) {
				for (int j = 0; j < image.getHeight(); j++) {
					imageDest.setRGB(i, j, 0xFFFFFF - image.getRGB(i, j));
				}
			}
			mImagePaneDest.setImageWithHistory(new Image(imageDest));
		}
	}

	/**
	 * TP1-5 Creates a threshold version of the image.
	 */
	public void filterThreshold() {
		if (mImagePaneDest != null) {
			Image image = mImagePaneSource.getImage();
			int width = image.getWidth();
			int height = image.getHeight();
			BufferedImage imageDest = new BufferedImage(width, height, image.getBufferedImage().getType());

			// TODO - Make sure that you can set the threshold yourself
			int threshold = 0x888888;
			for (int i = 0; i < image.getWidth(); i++) {
				for (int j = 0; j < image.getHeight(); j++) {
					if (image.getRGB(i, j) < threshold) {
						imageDest.setRGB(i, j, 0x000000);
					} else {
						imageDest.setRGB(i, j, 0xFFFFFF);
					}
				}
			}

			mImagePaneDest.setImageWithHistory(new Image(imageDest));
		}
	}
}
