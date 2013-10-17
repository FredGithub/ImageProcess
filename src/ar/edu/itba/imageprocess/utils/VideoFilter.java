package ar.edu.itba.imageprocess.utils;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Checks that the file is valid.
 */
public class VideoFilter extends FileFilter {

	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}

		String extension = FileUtils.getFileExtension(f);
		if (extension.matches("avi")) {
			return true;
		} else {
			return false;
		}
	}

	public String getDescription() {
		return "Just AVI Videos";
	}
}
