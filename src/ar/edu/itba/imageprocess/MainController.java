package ar.edu.itba.imageprocess;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Scrollbar;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
			mImagePaneDest.loadImage(file, width, height);
			repaintMainFrame();
		}
	}

	public void saveImage(File file) {
		if (mImagePaneSource != null && mImagePaneSource.getImage() != null) {
			Log.d("saving " + file.getName());
			String extension = FileUtils.getFileExtension(file);
			try {
				if (extension.matches("gif|png|jpe?g")) {
					ImageIO.write(mImagePaneSource.getImage(), extension, file);
				} else if (extension.matches("raw|pgm|ppm")) {
					ExtImageIO.write(mImagePaneSource.getImage(), file);
				} else {
					Log.d("unsupported format " + extension);
				}
			} catch (IOException e) {
				Log.d("couldn't save file! " + e);
			}
		}
	}

	public void generateCircle() {
		if (mImagePaneDest != null) {
			int imageSize = 256;
			int radius = 92;
			BufferedImage image = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_BYTE_BINARY);
			Graphics2D g2d = image.createGraphics();
			g2d.setColor(Color.WHITE);
			g2d.fill(new Ellipse2D.Double(imageSize / 2 - radius / 2, imageSize / 2 - radius / 2, radius, radius));
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	public void generateSquare() {
		if (mImagePaneDest != null) {
			int imageSize = 256;
			int size = 92;
			BufferedImage image = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_BYTE_BINARY);
			Graphics2D g2d = image.createGraphics();
			g2d.setColor(Color.WHITE);
			g2d.fill(new Rectangle2D.Double(imageSize / 2 - size / 2, imageSize / 2 - size / 2, size, size));
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	public void generateGradient() {
		if (mImagePaneDest != null) {
			int imageSize = 256;
			BufferedImage image = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_BYTE_GRAY);
			Graphics2D g2d = image.createGraphics();
			g2d.setPaint(new GradientPaint(0, 0, Color.GRAY, imageSize, 0, Color.WHITE));
			g2d.fill(new Rectangle2D.Double(0, 0, imageSize, imageSize));
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	public void generateColorGradient() {
		if (mImagePaneDest != null) {
			int imageSize = 256;
			BufferedImage image = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = image.createGraphics();
			g2d.setPaint(new GradientPaint(0, 0, Color.BLUE, imageSize, 0, Color.WHITE));
			g2d.fill(new Rectangle2D.Double(0, 0, imageSize, imageSize));
			mImagePaneDest.setImageWithHistory(image);
		}
	}

	/**
	 * TP1-2
	 * Creates a negative of the image.
	 */
	public void filterNegative() {
		if (mImagePaneDest != null) {
			BufferedImage image = mImagePaneSource.getImage();
			int width = image.getWidth();
			int height = image.getHeight();
			BufferedImage imageDest = new BufferedImage(width, height, image.getType());
			
			for (int i=0; i<image.getWidth(); i++) {
				for (int j=0; j<image.getHeight(); j++) {
					imageDest.setRGB(i, j, 0xFFFFFF-image.getRGB(i, j));
				}
			}
			mImagePaneDest.setImageWithHistory(imageDest);
		}
	}

	/**
	 * TP1-5
	 * Creates a threshold version of the image.
	 */
	public void filterThreshold() {
		if (mImagePaneDest != null) {
			BufferedImage image = mImagePaneSource.getImage();
			int width = image.getWidth();
			int height = image.getHeight();
			BufferedImage imageDest = new BufferedImage(width, height, image.getType());
			
			// TODO - Make sure that you can set the threshold yourself
			int threshold = 0x888888;
			for (int i=0; i<image.getWidth(); i++) {
				for (int j=0; j<image.getHeight(); j++) {
					// TODO - Why minus..? It works, but why minus?
					int rgb = -image.getRGB(i, j);
					if(rgb < threshold) {
						imageDest.setRGB(i, j, 0x000000);
					} else {
						imageDest.setRGB(i, j, 0xFFFFFF);
					}
				}
			}
			
			mImagePaneDest.setImageWithHistory(imageDest);
		}		
	}
}
