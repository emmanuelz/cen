package org.cen.cup.cup2015.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public class PopCornBasket extends AbstractGameBoardElement {
	private static Shape outer = new Ellipse2D.Double(-47.5, -47.5, 95, 95);
	private static Shape inner = new Ellipse2D.Double(-27, -27, 54, 54);

	public PopCornBasket(String name, Point2D position) {
		super(name, position, 0, 5);
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.fill(outer);
		g.setColor(Color.BLACK);
		g.draw(outer);
		g.draw(inner);
	}
}
