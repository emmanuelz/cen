package org.cen.control;

import org.cen.trajectories.planner.OrientedPosition;

public class PositionFilter {
	private double distanceThreshold;

	public ControlParameters applyPosition(OrientedPosition current, OrientedPosition destination, int speed) {
		double distance = current.distance(destination);
		double angle = current.getOrientation();
		double s = 0;
		if (distance > distanceThreshold) {
			s = speed;
		}
		return new ControlParameters(distance, s, angle);
	}

	public PositionFilter(double distanceThreshold) {
		super();
		this.distanceThreshold = distanceThreshold;
	}
}
