package org.cen.ui.gameboard.elements.object;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;

import org.cen.cup.cup2015.gameboard.elements.Strokes;
import org.cen.ui.gameboard.AbstractGameBoardElement;

public class MovableRobot extends AbstractGameBoardElement implements IMovable {
	private Shape area;

	public MovableRobot(String name) {
		super(name, new Point2D.Double(0, 0), 0, 1000);
		area = new Arc2D.Double(-105, -105, 210, 210, 0, 360, Arc2D.PIE);
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fill(area);
		g.setColor(Color.BLACK);
		g.setStroke(Strokes.thinStroke);
		g.draw(area);
	}

	@Override
	public void setPosition(Point2D position) {
		this.position = position;
	}

	@Override
	public void setOrientation(double angle) {
		this.orientation = angle;
	}
}
