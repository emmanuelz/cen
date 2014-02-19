package org.cen.ui.gameboard.elements.trajectory;

import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;

public class TrajectoryStroke implements Stroke {
	private static final float FLATNESS = 5;
	private Area gauge;

	public TrajectoryStroke(Shape gauge) {
		super();
		this.gauge = new Area(gauge);
	}

	public Shape createStrokedShape(Shape shape) {
		Area area = new Area();
		PathIterator it = new FlatteningPathIterator(shape.getPathIterator(null), FLATNESS);
		double points[] = new double[6];
		double lastX = 0, lastY = 0;
		double thisX = 0, thisY = 0;
		double lastAngle = 0;
		int type = 0;

		while (!it.isDone()) {
			type = it.currentSegment(points);
			switch (type) {
			case PathIterator.SEG_MOVETO:
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
				if (distance == 0) {
					it.next();
					continue;
				}
				AffineTransform t = new AffineTransform();
				double start = Math.min(angle, lastAngle);
				double end = Math.max(angle, lastAngle);
				for (double i = start; i < end; i += .03) {
					t.setToTranslation(lastX, lastY);
					t.rotate(i);
					Area s = gauge.createTransformedArea(t);
					area.add(s);
				}
				for (int i = 0; i < distance; i += 50) {
					t.setToTranslation(lastX, lastY);
					t.rotate(angle);
					t.translate(i, 0);
					Area s = gauge.createTransformedArea(t);
					area.add(s);
				}
				lastX = thisX;
				lastY = thisY;
				lastAngle = angle;
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
			case PathIterator.SEG_CLOSE:
				result.closePath();
			case PathIterator.SEG_LINETO:
				thisX = points[0];
				thisY = points[1];
				double dx = thisX - lastX;
				double dy = thisY - lastY;
				double distance = Math.sqrt(dx * dx + dy * dy);
				if (distance > 10.0) {
					result.lineTo(points[0], points[1]);
					lastX = thisX;
					lastY = thisY;
				}
			}
			it.next();
		}
		return result;
	}
}
