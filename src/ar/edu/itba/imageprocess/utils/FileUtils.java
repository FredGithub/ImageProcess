package ar.edu.itba.imageprocess.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUtils {

	public static String getFileExtension(File file) {
		return file.getName().substring(file.getName().lastIndexOf(".") + 1).toLowerCase();
	}

	public static byte[] readFileBytes(File file) {
		FileInputStream is = null;
		ByteArrayOutputStream ous = null;
		try {
			is = new FileInputStream(file);
			ous = new ByteArrayOutputStream();
			int i = is.read();
			while (i != -1) {
				ous.write(i);
				i = is.read();
			}
			return ous.toByteArray();
		} catch (IOException e) {
			return null;
		} finally {
			try {
				if (ous != null)
					ous.close();
			} catch (IOException e) {
			}
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
			}
		}
	}
}
