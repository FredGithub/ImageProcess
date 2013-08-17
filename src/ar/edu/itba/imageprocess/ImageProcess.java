package ar.edu.itba.imageprocess;

import javax.swing.JFrame;

public class ImageProcess {

	public static final String APPLICATION_NAME = "ImageProcess";
	public static final String IMG_PATH = "res/img/";

	public static void main(String[] args) {
		MainFrame frame = new MainFrame();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
