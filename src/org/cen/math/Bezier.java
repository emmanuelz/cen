package org.cen.math;

import java.awt.geom.Point2D;

public class Bezier {
	private static double getCombination(int k, int n) {
		if (k == 0 || n == 0 || k == n) {
			return 1;
		}
		int a = 1;
		int b = n;
		for (int i = 2; i < k; i++) {
			a *= i;
			b *= n - i;
		}
		return b / a;
	}

	private static double getFactor(double t, int k, int n) {
		double result = getCombination(k, n) * Math.pow(t, k) * Math.pow(1 - t, n - k);
		return result;
	}

	public static Point2D getPoint(double t, Point2D... points) {
		double x = 0;
		double y = 0;
		int n = points.length;
		for (int i = 0; i < n; i++) {
			double factor = getFactor(t, i, n - 1);
			x += points[i].getX() * factor;
			y += points[i].getY() * factor;
		}
		Point2D p = new Point2D.Double(x, y);
		return p;
	}

	public static double getAngle(double t, Point2D... points) {
		int n = points.length;

		// Computes the derivate
		Point2D[] points1 = new Point2D[n - 1];
		Point2D[] points2 = new Point2D[n - 1];

		System.arraycopy(points, 1, points1, 0, n - 1);
		System.arraycopy(points, 0, points2, 0, n - 1);

		Point2D p1 = getPoint(t, points1);
		Point2D p2 = getPoint(t, points2);

		// angle
		double x = n * (p1.getX() - p2.getX());
		double y = n * (p1.getY() - p2.getY());
		double angle = Math.atan2(y, x);

		return angle;
	}
}
