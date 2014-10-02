package org.cen.cup.cup2014.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public class Beacon extends AbstractGameBoardElement {
	private Shape rect = new Rectangle2D.Double(-40, -40, 80, 80);
	private Color color;

	public Beacon(String name, Point2D position, Color color) {
		super(name, position, 0, 5);
		this.color = color;
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(color);
		g.fill(rect);
	}
}
