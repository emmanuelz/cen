package org.cen.math;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class PathUtils {
	public static void arcTo(Path2D path, double x, double y, double cx, double cy) {
		Point2D p = path.getCurrentPoint();
		double sx = p.getX();
		double sy = p.getY();
		double dx1 = cx - sx;
		double dy1 = cy - sy;
		double a1 = Math.atan2(dy1, dx1);
		double dx2 = x - cx;
		double dy2 = y - cy;
		double a2 = Math.atan2(dy2, dx2);
		double a = Math.abs(a2 - a1);
		if (a > Math.PI / 2) {
			// TODO: split the curve
		}
		a = Math.tan(a / 4) * 4 / 3;
		a = Math.abs(a);
		path.curveTo(sx + a * dx1, sy + a * dy1, x - a * dx2, y - a * dy2, x, y);
	}
}
