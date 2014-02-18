package org.cen.ui.gameboard.elements.trajectory;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class StraightLine extends AbstractTrajectoryPath {
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

		path = new Path2D.Double();
		path.moveTo(start.getX(), start.getY());
		for (int i = 1; i < n; i++) {
			Point2D p = frames.get(i).getPosition();
			path.lineTo(p.getX(), p.getY());
		}
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
		return getOrientation(0d);
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
