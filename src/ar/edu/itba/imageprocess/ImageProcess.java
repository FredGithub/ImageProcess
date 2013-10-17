package ar.edu.itba.imageprocess;

import java.awt.Dimension;

public class ImageProcess {

	public static final String APPLICATION_NAME = "ImageProcess";
	public static final String IMG_PATH = "res/img/";
	public static final String VIDEOS_PATH = "res/videos/";
	public static final Dimension IMG_DIMENSION = new Dimension(512, 350);

	public static void main(String[] args) {
		MainController controller = new MainController();
		controller.start();
	}
}
