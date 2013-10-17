package ar.edu.itba.imageprocess;

import java.awt.Point;
import java.util.LinkedList;

public class LevelSetState {

	public int[][] phi;
	public LinkedList<Point> lin;
	public LinkedList<Point> lout;
	public double[] averageIn;
	public double[] averageOut;

	public LevelSetState(int[][] phi, LinkedList<Point> lin, LinkedList<Point> lout, double[] averageIn, double[] averageOut) {
		this.phi = phi;
		this.lin = lin;
		this.lout = lout;
		this.averageIn = averageIn;
		this.averageOut = averageOut;
	}
}
