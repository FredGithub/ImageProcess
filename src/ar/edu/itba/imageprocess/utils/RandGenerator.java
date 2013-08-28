package ar.edu.itba.imageprocess.utils;

public class RandGenerator {

	public static double gaussian(double spread, double average) {
		double r1 = Math.random();
		double r2 = Math.random();
		double y = Math.sqrt(-2 * Math.log(r1)) * Math.cos(2 * Math.PI * r2);
		return spread * y + average;
	}

	public static double rayleigh(double p) {
		double r = Math.random();
		double y = p * Math.sqrt(-2 * Math.log(1 - r));
		return y;
	}

	public static double exponential(double p) {
		double r = Math.random();
		double y = (-1 / p) * Math.log(r);
		return y;
	}
}
