package org.cen.control;

public class DecelerationFilter {
	private double decelerationFactor;

	public DecelerationFilter(double decelerationFactor) {
		super();
		this.decelerationFactor = decelerationFactor;
	}

	public double applyFilter(ControlParameters parameters, double currentSpeed) {
		double speed = parameters.getSpeed();
		double maxSpeed = parameters.getDistance() * decelerationFactor / currentSpeed;
		if (speed > maxSpeed) {
			return maxSpeed;
		}
		return speed;
	}
}
