package org.cen.control;

public class ControlParameters {
	private double angle;
	private double distance;
	private double speed;

	public ControlParameters(double distance, double speed, double angle) {
		super();
		this.distance = distance;
		this.speed = speed;
		this.angle = angle;
	}

	public double getAngle() {
		return angle;
	}

	public double getDistance() {
		return distance;
	}

	public double getSpeed() {
		return speed;
	}
}
