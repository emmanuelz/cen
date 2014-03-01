package org.cen.math;

public class Angle {
	public static final double PI2 = Math.PI * 2.0;

	public static double getRotationAngle(double start, double end) {
		double theta = end - start;
		if (theta > Math.PI) {
			theta += -PI2;
		} else if (theta < -Math.PI) {
			theta += PI2;
		}
		theta %= 2.0 * Math.PI;
		return theta;
	}
}
