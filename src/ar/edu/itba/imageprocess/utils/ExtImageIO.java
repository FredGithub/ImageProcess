package ar.edu.itba.imageprocess.utils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ExtImageIO {

	public static int[][] readRawImageChannel(File file, int width, int height) {
		int[][] grayChannel = new int[width][height];
		byte[] fileData = FileUtils.readFileBytes(file);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				grayChannel[x][y] = fileData[y * width + x] & 0xFF;
			}
		}
		return grayChannel;
	}

	public static int[][] readPgmImageChannel(File file) throws IOException {
		PixmapData pixmapData = extractPixmapData(file);
		int[][] grayChannel = new int[pixmapData.width][pixmapData.height];
		for (int x = 0; x < pixmapData.width; x++) {
			for (int y = 0; y < pixmapData.height; y++) {
				grayChannel[x][y] = pixmapData.data[y * pixmapData.width + x] & 0xFF;
			}
		}
		return grayChannel;
	}

	public static int[][][] readPpmImageChannels(File file) throws IOException {
		PixmapData pixmapData = extractPixmapData(file);
		int[][] redChannel = new int[pixmapData.width][pixmapData.height];
		int[][] greenChannel = new int[pixmapData.width][pixmapData.height];
		int[][] blueChannel = new int[pixmapData.width][pixmapData.height];
		for (int x = 0; x < pixmapData.width; x++) {
			for (int y = 0; y < pixmapData.height; y++) {
				redChannel[x][y] = pixmapData.data[(y * pixmapData.width + x) * 3] & 0xFF;
				greenChannel[x][y] = pixmapData.data[(y * pixmapData.width + x) * 3 + 1] & 0xFF;
				blueChannel[x][y] = pixmapData.data[(y * pixmapData.width + x) * 3 + 2] & 0xFF;
			}
		}
		return new int[][][] { redChannel, greenChannel, blueChannel };
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

	private static class PixmapData {
		int width;
		int height;
		byte[] data;
	}
}
