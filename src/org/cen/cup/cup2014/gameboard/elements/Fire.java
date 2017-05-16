package org.cen.cup.cup2014.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public class Fire extends AbstractGameBoardElement {
	public static final double WIDTH = 140 - (30 / Math.cos(Math.PI / 6) / 2);
	public static final double HEIGHT = 30;
	private Shape rect = new Rectangle2D.Double(-WIDTH / 2, -HEIGHT / 2, WIDTH, HEIGHT);

	public Fire(String name, Point2D position, double orientation) {
		super(name, position, orientation, 5);
	}

	@Override
	public void paint(Graphics2D g) {
		// torch
		g.setColor(Color.BLACK);
		g.fill(rect);
	}
}
