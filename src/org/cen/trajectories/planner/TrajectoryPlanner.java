package org.cen.trajectories.planner;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.cen.math.Bezier;

public class TrajectoryPlanner {
	public static void main(String[] args) {
		TrajectoryPlanner p = new TrajectoryPlanner(10, 100);
		OrientedPosition[] points = p.getTrajectory(new OrientedPosition(0, 0, Math.toRadians(90)), new OrientedPosition(1000, 1000, Math.toRadians(90)));
		for (OrientedPosition point : points) {
			System.out.println(point);
		}
	}

	private double interpolationDistance;

	private double mmPerRadian;

	public TrajectoryPlanner(double mmPerDegree, double interpolationDistance) {
		super();
		this.mmPerRadian = Math.toDegrees(mmPerDegree);
		this.interpolationDistance = interpolationDistance;
	}

	private Point2D getControlPoint(OrientedPosition p, double angleIncrement, boolean forward) {
		Point2D l = p.getLocation();
		double angle = p.getOrientation();
		double signum = forward ? 1.0 : -1.0;
		double x = l.getX() + Math.cos(angle) * mmPerRadian * angleIncrement * signum;
		double y = l.getY() + Math.sin(angle) * mmPerRadian * angleIncrement * signum;
		l = new Point2D.Double(x, y);
		return l;
	}

	public OrientedPosition[] getTrajectory(OrientedPosition start, OrientedPosition end) {
		List<OrientedPosition> points = new ArrayList<OrientedPosition>();

		Point2D s = start.getLocation();
		Point2D e = end.getLocation();
		double d = s.distance(e);
		double dx = e.getX() - s.getX();
		double dy = e.getY() - s.getY();

		double angle = Math.atan2(dy, dx);
		double da = Math.abs(start.getOrientation() - angle);
		Point2D cp1 = getControlPoint(start, da, true);
		da = Math.abs(end.getOrientation() - angle);
		Point2D cp2 = getControlPoint(end, da, false);

		int n = (int) (d / interpolationDistance);
		for (int i = 0; i < n; i++) {
			double t = (double) i / n;
			Point2D p = Bezier.getPoint(t, s, cp1, cp2, e);
			double o = Bezier.getAngle(t, s, cp1, cp2, e);
			points.add(new OrientedPosition(p, o));
		}
		points.add(end);

		OrientedPosition[] array = new OrientedPosition[points.size()];
		return points.toArray(array);
	}
}
