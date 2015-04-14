package org.cen.ui.gameboard.elements.trajectory;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.cen.math.Angle;

public class StraightLine extends AbstractTrajectoryPath {
	@Override
	public String getTrajectoryDescription() {
		StringBuilder sb = new StringBuilder();
		Point2D last = null;
		double lastOrientation = 0;
		for (KeyFrame frame : frames) {
			TrajectoryMovement movement = frame.getMovement();
			switch (movement) {
			case START:
				last = frame.getPosition();
				lastOrientation = frame.getOrientation();
				sb.append(String.format("// start at %s\r\n", last.toString()));
				break;
			case BEZIER:
				break;
			case CLOTHOID:
				break;
			case LINE:
				Point2D p = frame.getPosition();
				double distance = p.distance(last);
				double speed = frame.getMovementSpeed();
				String direction = speed > 0 ? "forward" : "backward";
				sb.append(String.format("// move %s of %.0f mm (%.0f)\r\n", direction, distance, 9.557 * distance * Math.signum(speed)));
				sb.append(String.format("FCM_avancer(%.0f);\r\n", 9.557 * distance * Math.signum(speed)));
				last = p;
				break;
			case NONE:
				break;
			case ROTATION:
				double o = frame.getOrientation();
				double angle = Angle.getRotationAngle(lastOrientation, o);
				angle = Math.toDegrees(angle);
				sb.append(String.format("// rotation of %.0f° (%.0f)\r\n", angle, 22527.5d / 360d * angle));
				sb.append(String.format("FCM_tourner(%.0f);\r\n", 22527.5d / 360d * angle));
				lastOrientation = o;
				break;
			default:
				break;
			}
			if (frame.hasComments()) {
				ArrayList<String> comments = frame.getComments();
				for (String s : comments) {
					sb.append(s);
					sb.append("\r\n");
				}
			}
		}
		return sb.toString();
	}

	private ArrayList<KeyFrame> frames;
	private KeyFrame keyFrame;
	private Path2D path;

	public StraightLine(String name, double initialAngle, double finalAngle, ArrayList<KeyFrame> frames) {
		super(name, new Point2D.Double(), null, null, initialAngle, finalAngle);
		this.frames = frames;
		order = 1000;

		int n = frames.size();
		start = frames.get(0).getPosition();
		end = frames.get(n - 1).getPosition();

		boolean forward = true;

		path = new Path2D.Double();
		path.moveTo(start.getX(), start.getY());
		for (int i = 1; i < n; i++) {
			KeyFrame frame = frames.get(i);
			TrajectoryMovement movement = frame.getMovement();
			switch (movement) {
			case LINE:
				// handles backward moves by adding an extra moveto instruction
				if (forward == frame.getMovementSpeed() < 0) {
					Point2D p = path.getCurrentPoint();
					path.moveTo(p.getX(), p.getY());
					forward = !forward;
				}
				Point2D p = frame.getPosition();
				path.lineTo(p.getX(), p.getY());
				break;
			case BEZIER:
				p = frame.getPosition();
				Point2D[] cp = frame.getControlPoints();
				path.curveTo(cp[0].getX(), cp[0].getY(), cp[1].getX(), cp[1].getY(), p.getX(), p.getY());
				break;
			default:
				break;
			}
		}

		String description = getTrajectoryDescription();
		setProperty(KEY_DESCRIPTION, description);
	}

	private KeyFrame getKeyFrame(double timestamp) {
		if (keyFrame == null || keyFrame.getTimestamp() != timestamp) {
			KeyFrameInterpolator interpolator = new KeyFrameInterpolator();
			keyFrame = interpolator.interpolate(frames, timestamp);
		}
		return keyFrame;
	}

	@Override
	public double getOrientation() {
		return 0;
	}

	@Override
	public double getOrientation(double timestamp) {
		KeyFrame frame = getKeyFrame(timestamp);
		return frame.getOrientation();
	}

	@Override
	public Shape getPath() {
		return path;
	}

	@Override
	public Point2D getPosition(double timestamp) {
		KeyFrame frame = getKeyFrame(timestamp);
		return frame.getPosition();
	}

	@Override
	public Point2D[] getTrajectoryControlPoints() {
		return null;
	}

	@Override
	public void paint(Graphics2D g, double timestamp) {
		super.paint(g);

		Point2D p = getPosition(timestamp);
		double x = p.getX() - position.getX();
		double y = p.getY() - position.getY();
		g.translate(x, y);

		double angle = getOrientation(timestamp) - orientation;
		g.rotate(angle);

		paintGauge(g);
	}

	@Override
	public void paintUnscaled(Graphics2D g) {
		paintUnscaled(g, 0d);
	}

	@Override
	public void paintUnscaled(Graphics2D g, double timestamp) {
		if (timestamp == 0d) {
			super.paintUnscaled(g);
		}
	}
}
