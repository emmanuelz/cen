package org.cen.ui.gameboard.elements.trajectory;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class KeyFrame implements Comparable<KeyFrame> {
	private ArrayList<String> comments;
	private Point2D[] controlPoints;
	private TrajectoryMovement movement;
	private double movementSpeed;

	private double orientation;
	private Point2D position;

	private double rotationSpeed;
	private int sourceLine;
	private double timestamp;

	public KeyFrame(TrajectoryMovement movement, double movementSpeed, double orientation, double rotationSpeed, Point2D position, double timestamp, int sourceLine, Point2D... controlPoints) {
		super();
		this.movement = movement;
		this.movementSpeed = movementSpeed;
		this.orientation = orientation;
		this.rotationSpeed = rotationSpeed;
		this.position = position;
		this.timestamp = timestamp;
		this.sourceLine = sourceLine;
		this.controlPoints = controlPoints;
	}

	public void addComment(String comment) {
		if (comments == null) {
			comments = new ArrayList<String>();
		}
		comments.add(comment);
	}

	@Override
	public int compareTo(KeyFrame k) {
		return Double.compare(timestamp, k.timestamp);
	}

	public ArrayList<String> getComments() {
		return comments;
	}

	public Point2D[] getControlPoints() {
		return controlPoints;
	}

	public TrajectoryMovement getMovement() {
		return movement;
	}

	public double getMovementSpeed() {
		return movementSpeed;
	}

	public double getOrientation() {
		return orientation;
	}

	public Point2D getPosition() {
		return position;
	}

	public double getRotationSpeed() {
		return rotationSpeed;
	}

	public int getSourceLine() {
		return sourceLine;
	}

	public double getTimestamp() {
		return timestamp;
	}

	public boolean hasComments() {
		return (comments != null) && (!comments.isEmpty());
	}

	public boolean useRelativeAngle() {
		return movementSpeed > 0;
	}
}
