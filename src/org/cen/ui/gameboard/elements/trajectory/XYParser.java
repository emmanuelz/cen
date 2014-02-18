package org.cen.ui.gameboard.elements.trajectory;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Scanner;

public class XYParser extends AbstractTrajectoryParser {
	private static final double MIN_ANGLE = Math.toRadians(0.5);
	private String delimiter;
	private ArrayList<KeyFrame> frames = new ArrayList<>();
	private double initialAngle = 0;
	private double finalAngle = 0;
	private double lastx = 0;
	private double lasty = 0;
	private double lastAngle = 0;
	private double timestamp = 0;

	public XYParser() {
		this(";");
	}

	public XYParser(String delimiter) {
		super();
		this.delimiter = delimiter;
	}

	@Override
	public void parseLine(String line) {
		Scanner s = new Scanner(line);
		try {
			s.useDelimiter(delimiter);
			double x = s.nextDouble();
			double y = s.nextDouble();
			double linearSpeed = s.nextDouble();
			double rotationSpeed = s.nextDouble();
			addKeyFrame(x, y, linearSpeed, rotationSpeed);
		} finally {
			s.close();
		}
	}

	private void addKeyFrame(double x, double y, double linearSpeed, double rotationSpeed) {
		double dx = x - lastx;
		double dy = y - lasty;
		double angle = Math.atan2(dy, dx);

		Point2D p = new Point2D.Double(x, y);

		// initial position
		if (frames.isEmpty()) {
			KeyFrame frame = new KeyFrame(TrajectoryMovement.START, 0, initialAngle, 0, p, 0);
			frames.add(frame);

			lastAngle = initialAngle;
			lastx = x;
			lasty = y;

			return;
		}

		double distance = p.distance(lastx, lasty);
		double theta = Math.abs(angle - lastAngle) % Math.PI;

		// rotation
		if (theta > MIN_ANGLE) {
			timestamp += theta / (2.0 * Math.PI) / rotationSpeed;
			KeyFrame frame = new KeyFrame(TrajectoryMovement.ROTATION, 0, angle, rotationSpeed, new Point2D.Double(lastx, lasty), timestamp);
			frames.add(frame);
		}

		// straight line
		timestamp += distance / linearSpeed;
		KeyFrame frame = new KeyFrame(TrajectoryMovement.LINE, linearSpeed, angle, 0, p, timestamp);
		frames.add(frame);

		lastx = x;
		lasty = y;
		lastAngle = angle;
	}

	@Override
	public ITrajectoryPath getPath(String name) {
		ITrajectoryPath path = new StraightLine(name, initialAngle, finalAngle, frames);
		return path;
	}
}
