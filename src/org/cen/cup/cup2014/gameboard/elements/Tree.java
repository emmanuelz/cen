package org.cen.cup.cup2014.gameboard.elements;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;
import org.cen.ui.gameboard.RALColor;

public class Tree extends AbstractGameBoardElement {
	private static double FRUITS_RADIUS = 120.0;
	private Shape tree = new Ellipse2D.Double(-150, -150, 300, 300);
	private Shape marker = new Ellipse2D.Double(-FRUITS_RADIUS, -FRUITS_RADIUS, FRUITS_RADIUS * 2.0, FRUITS_RADIUS * 2.0);
	private Shape fruit = new Ellipse2D.Double(-12, -12, 24, 24);
	private Stroke stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1.0f, new float[] { 25.0f, 25.0f }, 0.0f);

	public Tree(String name, Point2D position, double orientation) {
		super(name, position, orientation, 5);
	}

	@Override
	public void paint(Graphics2D g) {
		// tree
		g.setColor(RALColor.RAL_6018);
		g.fill(tree);
		// contour
		g.setColor(Color.BLACK);
		g.draw(tree);
		// fruits line
		g.setStroke(stroke);
		g.draw(marker);
		// fruitmouths
		g.setColor(RALColor.RAL_4008);
		for (int i = 0; i < 6; i++) {
			double a = i * (Math.PI / 3.0);
			double tx = Math.cos(a) * FRUITS_RADIUS;
			double ty = Math.sin(a) * FRUITS_RADIUS;
			g.translate(tx, ty);
			g.fill(fruit);
			g.translate(-tx, -ty);
		}
	}
}
