package org.cen.ui.gameboard.elements.trajectory;

import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;

import org.cen.math.Angle;
import org.cen.math.Bezier;

public class TrajectoryStroke implements Stroke {
	private static final double MIN_SEGMENT_DISTANCE = 10.0;
	private static final int STEP_DISTANCE = 50;
	private static final double STEP_ANGLE = .03;
	private static final float FLATNESS = 5;
	private Area gauge;
	private double initialAngle;

	public TrajectoryStroke(Shape gauge, double initialAngle) {
		super();
		this.gauge = new Area(gauge);
		this.initialAngle = initialAngle;
	}

	private void addArea(Area area, AffineTransform t, double x, double y, double angle, double dx, int dy) {
		t.setToTranslation(x, y);
		t.rotate(angle);
		t.translate(dx, dy);
		Area s = gauge.createTransformedArea(t);
		area.add(s);
	}

	public Shape createStrokedShape(Shape shape) {
		Area area = new Area();
		// PathIterator it = new FlatteningPathIterator(shape.getPathIterator(null), FLATNESS);
		PathIterator it = shape.getPathIterator(null);
		double points[] = new double[6];
		double lastX = 0, lastY = 0;
		double thisX = 0, thisY = 0;
		double lastAngle = initialAngle;
		int type = 0;
		boolean backward = false;

		while (!it.isDone()) {
			type = it.currentSegment(points);
			switch (type) {
			case PathIterator.SEG_MOVETO:
				// extra moveto are used for backward moves
				if (lastX == points[0] && lastY == points[1]) {
					lastAngle += Math.PI;
					backward = !backward;
				}
				lastX = points[0];
				lastY = points[1];
				break;

			case PathIterator.SEG_LINETO:
				thisX = points[0];
				thisY = points[1];
				double dx = thisX - lastX;
				double dy = thisY - lastY;
				double angle = Math.atan2(dy, dx);
				double distance = Math.sqrt(dx * dx + dy * dy);

				AffineTransform t = new AffineTransform();
				double theta = Angle.getRotationAngle(lastAngle, angle);
				double start;
				double end;
				if (theta < 0) {
					start = lastAngle + theta;
					end = lastAngle;
				} else {
					start = lastAngle;
					end = lastAngle + theta;
				}

				if (backward) {
					start += Math.PI;
					end += Math.PI;
				}

				for (double i = start; i < end; i += STEP_ANGLE) {
					addArea(area, t, lastX, lastY, i, 0, 0);
				}

				lastAngle = angle;

				if (distance == 0) {
					it.next();
					continue;
				}

				double signum = 1.0;
				if (backward) {
					angle += Math.PI;
					signum = -1.0;
				}

				for (int i = 0; i < distance; i += STEP_DISTANCE) {
					addArea(area, t, lastX, lastY, angle, signum * i, 0);
				}
				addArea(area, t, lastX, lastY, angle, signum * distance, 0);

				lastX = thisX;
				lastY = thisY;
				break;

			case PathIterator.SEG_CUBICTO:
				angle = lastAngle;
				double cx1 = points[0];
				double cy1 = points[1];
				double cx2 = points[2];
				double cy2 = points[3];
				thisX = points[4];
				thisY = points[5];
				Point2D s = new Point2D.Double(lastX, lastY);
				Point2D cp1 = new Point2D.Double(cx1, cy1);
				Point2D cp2 = new Point2D.Double(cx2, cy2);
				Point2D e = new Point2D.Double(thisX, thisY);

				t = new AffineTransform();
				distance = s.distance(e);
				double step = STEP_DISTANCE / distance;
				for (double u = 0; u <= 1.0001; u += step) {
					Point2D p = Bezier.getPoint(u, s, cp1, cp2, e);
					angle = Bezier.getAngle(u, s, cp1, cp2, e);
					addArea(area, t, p.getX(), p.getY(), angle, 0, 0);
				}

				lastX = thisX;
				lastY = thisY;
				lastAngle = angle;
				break;
			}
			it.next();
		}

		GeneralPath result = new GeneralPath();
		it = new FlatteningPathIterator(area.getPathIterator(null), FLATNESS);
		while (!it.isDone()) {
			type = it.currentSegment(points);
			switch (type) {
			case PathIterator.SEG_MOVETO:
				result.moveTo(points[0], points[1]);
				break;
			case PathIterator.SEG_CLOSE:
				result.closePath();
				break;
			case PathIterator.SEG_LINETO:
				thisX = points[0];
				thisY = points[1];
				double dx = thisX - lastX;
				double dy = thisY - lastY;
				double distance = Math.sqrt(dx * dx + dy * dy);
				if (distance > MIN_SEGMENT_DISTANCE) {
					result.lineTo(points[0], points[1]);
					lastX = thisX;
					lastY = thisY;
				}
				break;
			}
			it.next();
		}
		return result;
	}
}
