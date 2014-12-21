package org.cen.control;

import org.cen.trajectories.planner.OrientedPosition;

public class Control {
	public static void main(String[] args) {
		new Control().start();
	}

	private void start() {
		PositionFilter pf = new PositionFilter(5);
		AccelerationFilter af = new AccelerationFilter(15, 15);
		DecelerationFilter df = new DecelerationFilter(60);

		double speed = 0;
		double currentSpeed = speed;
		OrientedPosition current = new OrientedPosition(0, 0, Math.toRadians(0));
		OrientedPosition destination = new OrientedPosition(1000, 0, Math.toRadians(90));
		do {
			ControlParameters p = pf.applyPosition(current, destination, 100);
			speed = p.getSpeed();
			speed = df.applyFilter(p, currentSpeed);
			speed = af.applyFilter(currentSpeed, speed);
			System.out.println(speed);
			current = getNewPosition(current, speed);
			currentSpeed = speed;
		} while (speed > 1);
		System.out.println(current);
	}

	private OrientedPosition getNewPosition(OrientedPosition current, double speed) {
		double x = current.getX() + speed / 5;
		double y = current.getY();
		return new OrientedPosition(x, y, current.getOrientation());
	}
}
