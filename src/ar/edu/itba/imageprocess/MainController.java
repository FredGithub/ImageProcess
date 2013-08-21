package ar.edu.itba.imageprocess;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import ar.edu.itba.imageprocess.utils.ExtImageIO;
import ar.edu.itba.imageprocess.utils.FileUtils;
import ar.edu.itba.imageprocess.utils.Log;

public class MainController {

	private MainFrame mMainFrame;
	private ImagePane mImagePaneSource;

	public MainController() {
		mMainFrame = new MainFrame(this);
		mMainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mImagePaneSource = null;
	}

	public void start() {
		mMainFrame.start();
	}

	public void repaintMainFrame() {
		if (mMainFrame != null) {
			mMainFrame.repaint();
		}
	}

	public void selectImagePane(ImagePane imagePane) {
		if (imagePane != null) {
			Log.d("selecting image pane " + imagePane.getIndex());
			if (mImagePaneSource != null) {
				mImagePaneSource.setSource(false);
			}
			mImagePaneSource = imagePane;
			mImagePaneSource.setSource(true);
			repaintMainFrame();
		}
	}

	public void loadImage(File file, int width, int height) {
		if (mImagePaneSource != null) {
			Log.d("opening " + file.getName());
			mImagePaneSource.loadImage(file, width, height);
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
		if (mImagePaneSource != null) {
			int imageSize = 256;
			int radius = 92;
			BufferedImage image = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_BYTE_BINARY);
			Graphics2D g2d = image.createGraphics();
			g2d.setColor(Color.WHITE);
			g2d.fill(new Ellipse2D.Double(imageSize / 2 - radius / 2, imageSize / 2 - radius / 2, radius, radius));
			mImagePaneSource.setImageWithHistory(image);
		}
	}

	public void generateSquare() {
		if (mImagePaneSource != null) {
			int imageSize = 256;
			int size = 92;
			BufferedImage image = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_BYTE_BINARY);
			Graphics2D g2d = image.createGraphics();
			g2d.setColor(Color.WHITE);
			g2d.fill(new Rectangle2D.Double(imageSize / 2 - size / 2, imageSize / 2 - size / 2, size, size));
			mImagePaneSource.setImageWithHistory(image);
		}
	}

	public void generateGradient() {
		if (mImagePaneSource != null) {
			int imageSize = 256;
			BufferedImage image = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_BYTE_GRAY);
			Graphics2D g2d = image.createGraphics();
			g2d.setPaint(new GradientPaint(0, 0, Color.GRAY, imageSize, 0, Color.WHITE));
			g2d.fill(new Rectangle2D.Double(0, 0, imageSize, imageSize));
			mImagePaneSource.setImageWithHistory(image);
		}
	}

	public void generateColorGradient() {
		if (mImagePaneSource != null) {
			int imageSize = 256;
			BufferedImage image = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = image.createGraphics();
			g2d.setPaint(new GradientPaint(0, 0, Color.BLUE, imageSize, 0, Color.WHITE));
			g2d.fill(new Rectangle2D.Double(0, 0, imageSize, imageSize));
			mImagePaneSource.setImageWithHistory(image);
		}
	}
}
