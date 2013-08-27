package ar.edu.itba.imageprocess.utils;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Checks that the file is valid.
 */
public class ImageFilter extends FileFilter {

	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}

		String extension = FileUtils.getFileExtension(f);
		if (extension.matches("png|jpe?g|gif|w?bmp|tiff?|pgm|ppm|raw")) {
			return true;
		} else {
			return false;
		}
	}

	public String getDescription() {
		return "Just Images";
	}
}
