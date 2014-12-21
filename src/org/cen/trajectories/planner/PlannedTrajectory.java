package org.cen.trajectories.planner;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

import org.cen.cup.cup2015.gameboard.elements.Strokes;
import org.cen.ui.gameboard.AbstractGameBoardElement;

public class PlannedTrajectory extends AbstractGameBoardElement {
	private Path2D line;

	public PlannedTrajectory(String name) {
		super(name, new Point2D.Double(0, 0), 0, 2000);
		line = new Path2D.Double();
	}

	public void planTrajectory(OrientedPosition start, OrientedPosition end) {
		TrajectoryPlanner planner = new TrajectoryPlanner(10, 100);
		OrientedPosition[] points = planner.getTrajectory(start, end);
		line.reset();
		Point2D p = start.getLocation();
		line.moveTo(p.getX(), p.getY());
		for (OrientedPosition point : points) {
			line.lineTo(point.getX(), point.getY());
		}
	}

	@Override
	public void paint(Graphics2D g) {
		g.setStroke(Strokes.thickStroke);
		g.setColor(Color.MAGENTA);
		g.draw(line);
	}
}
