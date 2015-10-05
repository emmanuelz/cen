package org.cen.cup.cup2016.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;
import org.cen.ui.gameboard.RALColor;

public class SeaShell extends AbstractGameBoardElement {
	private Color color;
	private Path2D path;

	public SeaShell(String name, Point2D position, Color color) {
		super(name, position);
		order = 5;
		this.color = color;
		path = new Path2D.Double();
		path.append(new Ellipse2D.Double(-36, -36, 72, 72), false);
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(color);
		g.fill(path);
		g.setColor(RALColor.RAL_9005);
		g.draw(path);
	}
}
