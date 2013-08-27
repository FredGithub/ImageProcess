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
}
