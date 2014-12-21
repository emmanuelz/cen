package org.cen.trajectories.planner;

import java.awt.geom.Point2D;

public class OrientedPosition {
	private Point2D location;
	private double orientation;

	public OrientedPosition(double x, double y, double orientation) {
		this(new Point2D.Double(x, y), orientation);
	}

	public OrientedPosition(Point2D location, double orientation) {
		super();
		this.location = location;
		this.orientation = orientation;
	}

	public Point2D getLocation() {
		return location;
	}

	public double getOrientation() {
		return orientation;
	}

	public double getX() {
		return location.getX();
	}

	public double getY() {
		return location.getY();
	}

	@Override
	public String toString() {
		return String.format("%s(%.1f; %.1f; %.1f°)", getClass().getSimpleName(), location.getX(), location.getY(), Math.toDegrees(orientation));
	}

	public double distance(OrientedPosition point) {
		return location.distance(point.location);
	}
}
