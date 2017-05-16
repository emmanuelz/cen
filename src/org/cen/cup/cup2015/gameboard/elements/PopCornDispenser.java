package org.cen.cup.cup2015.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;
import org.cen.ui.gameboard.RALColor;

public class PopCornDispenser extends AbstractGameBoardElement {
	Shape outer = new Rectangle2D.Double(0, -35, 70, 70);
	Shape inner = new Ellipse2D.Double(10, -25, 50, 50);

	public PopCornDispenser(String name, Point2D position) {
		super(name, position, 0, 5);
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(RALColor.RAL_5015);
		g.fill(outer);
		g.setColor(Color.WHITE);
		g.fill(inner);
		g.setColor(Color.BLACK);
		g.draw(outer);
		g.draw(inner);
	}
}
