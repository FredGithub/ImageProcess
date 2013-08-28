package ar.edu.itba.imageprocess.utils;

public class ArrayUtils {

	public static double[] intArrayToDoubleArray(int[] source) {
		double[] dest = new double[source.length];
		for (int i = 0; i < source.length; i++) {
			dest[i] = source[i];
		}
		return dest;
	}

	public static int[] intArray2Dto1D(int[][] source) {
		int[] dest = new int[source.length * source[0].length];
		for (int j = 0; j < source[0].length; j++) {
			for (int i = 0; i < source.length; i++) {
				dest[i * source[0].length + j] = source[i][j];
			}
		}
		return dest;
	}

	public static int max(int[] source) {
		int max = source[0];
		for (int i = 0; i < source.length; i++) {
			if (max < source[i]) {
				max = source[i];
			}
		}
		return max;
	}

	public static int max(int[][] source) {
		int max = source[0][0];
		for (int i = 0; i < source.length; i++) {
			for (int j = 0; j < source[0].length; j++) {
				if (max < source[i][j]) {
					max = source[i][j];
				}
			}
		}
		return max;
	}

	public static int min(int[] source) {
		int min = source[0];
		for (int i = 0; i < source.length; i++) {
			if (min > source[i]) {
				min = source[i];
			}
		}
		return min;
	}

	public static int min(int[][] source) {
		int min = source[0][0];
		for (int i = 0; i < source.length; i++) {
			for (int j = 0; j < source[0].length; j++) {
				if (min > source[i][j]) {
					min = source[i][j];
				}
			}
		}
		return min;
	}
}
