package ar.edu.itba.imageprocess;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.edu.itba.imageprocess.utils.ExtImageIO;

public class MainFrame extends JFrame {

	private JPanel mMainPanel;

	public MainFrame() {
		super(ImageProcess.APPLICATION_NAME);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(new Dimension((int) (screenSize.width * 0.8), (int) (screenSize.width * 0.8 / 1.6)));
		setResizable(false);
		setLocationRelativeTo(null);

		mMainPanel = new JPanel(new GridBagLayout());
		getContentPane().add(mMainPanel);

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
			if (image != null)
				mMainPanel.add(new JLabel(new ImageIcon(image)));
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
