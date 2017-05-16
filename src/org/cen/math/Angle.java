package org.cen.math;

public class Angle {
	public static final double PI2 = Math.PI * 2.0;

	/**
	 * Returns the shortest relative rotation angle in the range [-Pi, Pi].
	 * 
	 * @param start
	 *            the starting angle
	 * @param end
	 *            the ending angle
	 * @return the shortest relative rotation angle
	 */
	public static double getRotationAngle(double start, double end) {
		double theta = end - start;
		if (theta > Math.PI) {
			theta -= PI2;
		} else if (theta < -Math.PI) {
			theta += PI2;
		}
		theta %= PI2;
		return theta;
	}
}
