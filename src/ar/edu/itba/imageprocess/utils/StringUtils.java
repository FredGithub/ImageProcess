package ar.edu.itba.imageprocess.utils;

public class StringUtils {

	public static String rightPad(String str, int length) {
		return rightPad(str, length, " ");
	}

	public static String rightPad(String str, int length, String padChar) {
		while (str.length() < length) {
			str += padChar;
		}
		return str;
	}
}
