package ar.edu.itba.imageprocess;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.edu.itba.imageprocess.utils.ExtImageIO;
import ar.edu.itba.imageprocess.utils.FileUtils;
import ar.edu.itba.imageprocess.utils.Log;
import ar.edu.itba.imageprocess.utils.StringUtils;

@SuppressWarnings("serial")
public class ImagePane extends JPanel implements MouseListener, MouseMotionListener {

	public static final Color COLOR_UNSELECTED = new Color(0, 0, 0, 20);
	public static final Color COLOR_SOURCE = new Color(0, 255, 0, 20);

	private MainController mController;
	private int mIndex;
	private BufferedImage mImage;
	private JPanel mImagePane;
	private JLabel mImageLabel;
	private JButton mPrevBtn;
	private JButton mNextBtn;
	private JLabel mPixelLabel;

	public ImagePane(MainController controller, int index) {
		super(new GridBagLayout());

		mController = controller;
		mIndex = index;
		mImage = null;
		GridBagConstraints c;

		mImagePane = new JPanel(new GridBagLayout());
		mImagePane.setPreferredSize(ImageProcess.IMG_DIMENSION);
		mImagePane.setBackground(COLOR_UNSELECTED);
		mImagePane.addMouseListener(this);
		c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		add(mImagePane, c);

		mImageLabel = new JLabel();
		mImageLabel.addMouseMotionListener(this);
		mImagePane.add(mImageLabel);

		mPrevBtn = new JButton("Prev");
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(8, 0, 0, 8);
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		add(mPrevBtn, c);

		mNextBtn = new JButton("Next");
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.insets = new Insets(8, 0, 0, 8);
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		add(mNextBtn, c);

		mPixelLabel = new JLabel("---");
		mPixelLabel.setFont(new Font("Monospaced", Font.PLAIN, 12));
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 1;
		c.insets = new Insets(8, 0, 0, 0);
		c.anchor = GridBagConstraints.LINE_START;
		add(mPixelLabel, c);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getSource() == mImagePane) {
			mController.selectImagePane(this);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (mImage != null) {
			Color color = new Color(mImage.getRGB(e.getX(), e.getY()), true);
			String red = StringUtils.rightPad(String.valueOf(color.getRed()), 3);
			String green = StringUtils.rightPad(String.valueOf(color.getGreen()), 3);
			String blue = StringUtils.rightPad(String.valueOf(color.getBlue()), 3);
			mPixelLabel.setText("R=" + red + " G=" + green + " B=" + blue);
		} else {
			mPixelLabel.setText("---");
		}
	}

	public int getIndex() {
		return mIndex;
	}

	public void setSource(boolean source) {
		if (source) {
			mImagePane.setBackground(COLOR_SOURCE);
		} else {
			mImagePane.setBackground(COLOR_UNSELECTED);
		}
	}

	public BufferedImage getImage() {
		return mImage;
	}

	public void setImage(BufferedImage image) {
		mImage = image;
		if (mImage != null) {
			mImageLabel.setIcon(new ImageIcon(mImage));
		}
	}

	public void loadImage(File file, int width, int height) {
		try {
			String extension = FileUtils.getFileExtension(file);
			BufferedImage image = null;
			if (extension.equals("raw")) {
				image = ExtImageIO.readRaw(file, width, height);
			} else if (extension.equals("pgm")) {
				image = ExtImageIO.readPgm(file);
			} else if (extension.equals("ppm")) {
				image = ExtImageIO.readPpm(file);
			} else {
				image = ImageIO.read(file);
			}
			if (image != null) {
				setImage(image);
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
