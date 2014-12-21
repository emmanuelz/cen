package org.cen.control;

public class AccelerationFilter {
	private double maxAcceleration;
	private double maxDeceleration;

	public AccelerationFilter(double maxAcceleration, double maxDeceleration) {
		super();
		this.maxAcceleration = maxAcceleration;
		this.maxDeceleration = maxDeceleration;
	}

	public double applyFilter(double currentSpeed, double newSpeed) {
		if (Math.signum(newSpeed) == -Math.signum(currentSpeed)) {
			// passage par zéro
			newSpeed = 0;
		}
		double a = newSpeed - currentSpeed;
		boolean acceleration = Math.abs(newSpeed) > Math.abs(currentSpeed);
		if (acceleration) {
			if (Math.abs(a) > maxAcceleration) {
				currentSpeed += maxAcceleration * Math.signum(a);
			} else {
				currentSpeed = newSpeed;
			}
		} else {
			if (Math.abs(a) > maxDeceleration) {
				currentSpeed += maxDeceleration * Math.signum(a);
			} else {
				currentSpeed = newSpeed;
			}
		}
		return currentSpeed;
	}
}
