package org.cen.cup.cup2017.gameboard;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public class StartZone extends AbstractGameBoardElement {
	private Color color = null;
	private Path2D area = null;

	// point (0,0) en haut à gauche
	public StartZone(String name, Point2D position, Color color) {
		super(name, position);
		this.color = color;
		area = new Path2D.Double();
		area.moveTo(position.getX(), position.getY());
		area.lineTo(position.getX() + 360, position.getY());
		area.lineTo(position.getX() + 360, position.getY() - 1070);
		area.lineTo(position.getX(), position.getY() - 1070);
		area.closePath();
	}

	@Override
	public void paint(Graphics2D g) {
		g.translate(-position.getX(), -position.getY());
		g.setColor(color);
		g.fill(area);
		g.translate(position.getX(), position.getY());
	}
}
