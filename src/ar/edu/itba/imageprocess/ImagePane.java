package ar.edu.itba.imageprocess;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.edu.itba.imageprocess.utils.ExtImageIO;

@SuppressWarnings("serial")
public class ImagePane extends JPanel {
	public ImagePane() {
		super(new GridBagLayout());

		GridBagConstraints c;

		JPanel imagePane = new JPanel(new GridBagLayout());
		imagePane.setBackground(new Color(0, 0, 0, 20));
		c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		add(imagePane, c);

		try {
			BufferedImage image = ImageIO.read(new File(ImageProcess.IMG_PATH + "wdg3.gif"));
			//image = ExtImageIO.readRaw(new File(ImageProcess.IMG_PATH + "girl.raw"), 389, 164);
			//image = ExtImageIO.readRaw(new File(ImageProcess.IMG_PATH + "girl2.raw"), 256, 256);
			//image = ExtImageIO.readRaw(new File(ImageProcess.IMG_PATH + "barco.raw"), 290, 207);
			//image = ExtImageIO.readRaw(new File(ImageProcess.IMG_PATH + "lena.raw"), 256, 256);
			//image = ExtImageIO.readRaw(new File(ImageProcess.IMG_PATH + "lenax.raw"), 256, 256);
			//image = ExtImageIO.readRaw(new File(ImageProcess.IMG_PATH + "fractal.raw"), 200, 200);
			//image = ExtImageIO.readPgm(new File(ImageProcess.IMG_PATH + "test.pgm"));
			image = ExtImageIO.readPpm(new File(ImageProcess.IMG_PATH + "tree.ppm"));
			//image = ImageIO.read(new File(ImageProcess.IMG_PATH + "wallpaper.jpg"));
			if (image != null) {
				imagePane.add(new JLabel(new ImageIcon(image)));
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		JButton prevButton = new JButton("Prev");
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(8, 0, 0, 8);
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		add(prevButton, c);

		JButton nextButton = new JButton("Next");
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.insets = new Insets(8, 0, 0, 0);
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		add(nextButton, c);
	}
}
