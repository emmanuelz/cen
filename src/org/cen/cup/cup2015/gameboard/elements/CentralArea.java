package org.cen.cup.cup2015.gameboard.elements;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;
import org.cen.ui.gameboard.RALColor;

public class CentralArea extends AbstractGameBoardElement {
	private Area area;

	public CentralArea(String name, Point2D position) {
		super(name, position);

		AffineTransform t = new AffineTransform();
		Rectangle2D rect = new Rectangle2D.Double(-250, -590, 500, 1180);
		area = new Area(rect);
		Arc2D circle = new Arc2D.Double(-90, -90, 180, 180, 0, 360, Arc2D.PIE);
		compose(t, -250, -590, circle);
		compose(t, 500, 0, circle);
		compose(t, 0, 1180, circle);
		compose(t, -500, 0, circle);
	}

	private void compose(AffineTransform t, double dx, double dy, Arc2D circle) {
		t.translate(dx, dy);
		Shape s = t.createTransformedShape(circle);
		area.subtract(new Area(s));
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(RALColor.RAL_3020);
		g.fill(area);
		g.setColor(RALColor.RAL_9005);
		g.setStroke(Strokes.thickStroke);
		g.draw(area);
	}
}
