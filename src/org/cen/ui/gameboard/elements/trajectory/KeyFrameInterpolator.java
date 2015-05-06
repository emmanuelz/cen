package org.cen.ui.gameboard.elements.trajectory;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.Collections;
import java.util.List;

import org.cen.math.Angle;
import org.cen.math.Bezier;

public class KeyFrameInterpolator {
	private class KeyFrameSearch extends KeyFrame {
		public KeyFrameSearch(double timestamp) {
			super(null, 0, 0, 0, null, timestamp);
		}
	}

	private KeyFrame interpolate(KeyFrame start, KeyFrame end, double timestamp) {
		switch (end.getMovement()) {
		case ROTATION:
			return interpolateRotation(start, end, timestamp);
		case LINE:
			return interpolateStraightLine(start, end, timestamp);
		case BEZIER:
			return interpolateBezier(start, end, timestamp);
		default:
			return start;
		}
	}

	public KeyFrame interpolate(List<KeyFrame> frames, double timestamp) {
		KeyFrameSearch searchKey = new KeyFrameSearch(timestamp);
		int index = Collections.binarySearch(frames, searchKey);
		if (index >= 0) {
			return frames.get(index);
		} else {
			int n = frames.size();
			index = -index - 2;
			if (index < 0) {
				KeyFrame frame = frames.get(0);
				return frame;
			}
			KeyFrame start = frames.get(index);
			if (index == n - 1) {
				return start;
			}
			KeyFrame end = frames.get(index + 1);
			KeyFrame frame = interpolate(start, end, timestamp);
			return frame;
		}
	}

	private KeyFrame interpolateBezier(KeyFrame start, KeyFrame end, double timestamp) {
		double ds = start.getTimestamp();
		double duration = end.getTimestamp() - ds;
		double d = (timestamp - ds) / duration;
		Point2D[] points = end.getControlPoints();
		Point2D s = start.getPosition();
		Point2D e = end.getPosition();
		Point2D p = Bezier.getPoint(d, s, points[0], points[1], e);
		double angle = Bezier.getAngle(d, s, points[0], points[1], e);

		KeyFrame frame = new KeyFrame(TrajectoryMovement.BEZIER, start.getMovementSpeed(), angle, 0, p, timestamp, points);
		return frame;
	}

	private KeyFrame interpolateRotation(KeyFrame start, KeyFrame end, double timestamp) {
		double startAngle = start.getOrientation();
		double endAngle = end.getOrientation();
		double theta = Angle.getRotationAngle(startAngle, endAngle);
		if (end.useRelativeAngle()) {
			theta = endAngle - startAngle;
		}
		double ds = start.getTimestamp();
		double duration = end.getTimestamp() - ds;
		double d = (timestamp - ds) / duration;
		double angle = startAngle + d * theta;
		Point2D p = start.getPosition();

		KeyFrame frame = new KeyFrame(TrajectoryMovement.ROTATION, 0, angle, start.getRotationSpeed(), p, timestamp);
		return frame;
	}

	private KeyFrame interpolateStraightLine(KeyFrame start, KeyFrame end, double timestamp) {
		Point2D startPoint = start.getPosition();
		Point2D endPoint = end.getPosition();
		double ds = start.getTimestamp();
		double duration = end.getTimestamp() - ds;
		double d = (timestamp - ds) / duration;
		double x1 = startPoint.getX();
		double y1 = startPoint.getY();
		double x2 = endPoint.getX();
		double y2 = endPoint.getY();
		double x = x1 + d * (x2 - x1);
		double y = y1 + d * (y2 - y1);
		Double p = new Point2D.Double(x, y);

		KeyFrame frame = new KeyFrame(TrajectoryMovement.LINE, start.getMovementSpeed(), end.getOrientation(), 0, p, timestamp);
		return frame;
	}
}
