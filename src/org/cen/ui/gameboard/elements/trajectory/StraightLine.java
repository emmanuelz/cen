package org.cen.ui.gameboard.elements.trajectory;

import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class StraightLine extends AbstractTrajectoryPath {
	private Path2D path;

	public StraightLine(String name, double initialAngle, double finalAngle, Point2D... points) {
		super(name, new Point2D.Double(), null, null, initialAngle, finalAngle);
		order = 1000;

		int n = points.length;
		start = points[0];
		end = points[n - 1];

		path = new Path2D.Double();
		path.moveTo(start.getX(), start.getY());
		for (int i = 1; i < n; i++) {
			Point2D p = points[i];
			path.lineTo(p.getX(), p.getY());
		}
	}

	@Override
	public Shape getPath() {
		return path;
	}

	@Override
	public Point2D[] getTrajectoryControlPoints() {
		return null;
	}

	@Override
	protected Shape getTrajectory() {
		return path;
	}
}
