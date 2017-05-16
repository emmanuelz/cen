package org.cen.cup.cup2015.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public class Stand extends AbstractGameBoardElement {
	private Color color;
	private static Shape outer = new Ellipse2D.Double(-30, -30, 60, 60);
	private static Shape inner = new Ellipse2D.Double(-17.5, -17.5, 35, 35);

	public Stand(String name, Point2D position, Color color) {
		super(name, position, 0, 5);
		this.color = color;
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(color);
		g.fill(outer);
		g.setColor(Color.BLACK);
		g.draw(outer);
		g.draw(inner);
	}
}
