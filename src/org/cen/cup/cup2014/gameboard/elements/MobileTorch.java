package org.cen.cup.cup2014.gameboard.elements;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;
import org.cen.ui.gameboard.RALColor;

public class MobileTorch extends AbstractGameBoardElement {
	private static double BOTTOM_RADIUS = 65.0;
	private static double RECT_RADIUS = 80 - 17;
	private Shape torch = new Ellipse2D.Double(-80, -80, 160, 160);
	private Shape marker = new Ellipse2D.Double(-BOTTOM_RADIUS, -BOTTOM_RADIUS, BOTTOM_RADIUS * 2.0, BOTTOM_RADIUS * 2.0);
	private Shape rect = new Rectangle2D.Double(-18, -25, 18, 50);
	private Stroke stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1.0f, new float[] { 25.0f, 25.0f }, 0.0f);

	public MobileTorch(String name, Point2D position) {
		super(name, position, 0, 5);
	}

	@Override
	public void paint(Graphics2D g) {
		// torch
		g.setColor(RALColor.RAL_7032);
		g.fill(torch);
		// contour
		g.setColor(Color.BLACK);
		g.draw(torch);
		// bottom line
		g.setStroke(stroke);
		g.draw(marker);
		// fruitmouths
		g.setColor(RALColor.RAL_4008);
		for (int i = 0; i < 3; i++) {
			double a = i * (2.0 * Math.PI / 3.0);
			double tx = Math.cos(a) * RECT_RADIUS;
			double ty = Math.sin(a) * RECT_RADIUS;
			g.translate(tx, ty);
			g.rotate(a);
			g.fill(rect);
			g.rotate(-a);
			g.translate(-tx, -ty);
		}
	}
}
