package ar.edu.itba.imageprocess.utils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ExtImageIO {

	public static BufferedImage readRaw(File file, int width, int height) {
		// create an empty image and copy the image data into its data buffer
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		byte[] fileData = FileUtils.readFileBytes(file);
		byte[] imgData = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(fileData, 0, imgData, 0, width * height);
		return image;
	}

	public static BufferedImage readPgm(File file) throws IOException {
		// get the pixmap image width, height and data
		PixmapData pixmapData = extractPixmapData(file);

		// create an empty image and copy the image data into its data buffer
		BufferedImage image = new BufferedImage(pixmapData.width, pixmapData.height, BufferedImage.TYPE_BYTE_GRAY);
		byte[] imgData = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(pixmapData.data, 0, imgData, 0, pixmapData.data.length);
		return image;
	}

	public static BufferedImage readPpm(File file) throws IOException {
		// get the pixmap image width, height and data
		PixmapData pixmapData = extractPixmapData(file);

		// create an empty image and copy the image data into its data buffer
		BufferedImage image = new BufferedImage(pixmapData.width, pixmapData.height, BufferedImage.TYPE_INT_RGB);
		int[] fileData = bytesToRGBInt(pixmapData.data);
		int[] imgData = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(fileData, 0, imgData, 0, fileData.length);
		return image;
	}

	public static void write(BufferedImage image, File file) throws IOException {
		// write the image data to a file
		String extension = FileUtils.getFileExtension(file);
		FileOutputStream out = new FileOutputStream(file);
		if (extension.equals("raw")) {
			out.write(((DataBufferByte) image.getData().getDataBuffer()).getData());
		} else if (extension.equals("pgm")) {
			// TODO
		} else if (extension.equals("ppm")) {
			// TODO
		}
		out.close();
	}

	private static PixmapData extractPixmapData(File file) throws IOException {
		PixmapData headerData = new PixmapData();
		InputStream is = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		// skip the first line
		br.readLine();

		// extract the image dimension from the second line
		String[] dimensions = br.readLine().split(" ");
		headerData.width = Integer.parseInt(dimensions[0]);
		headerData.height = Integer.parseInt(dimensions[1]);

		// skip the third line
		br.readLine();

		// read the rest of the file
		String data = "";
		String line;
		while ((line = br.readLine()) != null) {
			data += line;
		}
		is.close();
		headerData.data = data.getBytes();
		return headerData;
	}

	private static int[] bytesToRGBInt(byte[] buf) {
		// convert a byte array to an array of 3 byte integers
		int[] intArr = new int[buf.length / 3];
		int offset = 0;
		for (int i = 0; i < intArr.length; i++) {
			intArr[i] = (buf[2 + offset] & 0xFF) | ((buf[1 + offset] & 0xFF) << 8) | ((buf[0 + offset] & 0xFF) << 16);
			offset += 3;
		}
		return intArr;
	}

	private static class PixmapData {
		int width;
		int height;
		byte[] data;
	}
}
