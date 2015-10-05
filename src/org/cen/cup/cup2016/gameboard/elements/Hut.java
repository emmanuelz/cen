package org.cen.cup.cup2016.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public class Hut extends AbstractGameBoardElement {
	private Path2D path;

	public Hut(String name, Point2D position) {
		super(name, position);
		path = new Path2D.Double();
		path.append(new Rectangle(-80, -50, 80, 100), false);
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(Color.BLUE);
		g.fill(path);
		g.setColor(Color.BLACK);
		g.draw(path);
	}
}
