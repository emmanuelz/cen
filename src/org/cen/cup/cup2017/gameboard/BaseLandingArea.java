package org.cen.cup.cup2017.gameboard;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public class BaseLandingArea extends AbstractGameBoardElement {
	private BaseLandingWood wood1 = null;
	private BaseLandingWood wood2 = null;
	private double orientation = 0;

	// (0,0) au centre de rotation
	public BaseLandingArea(String name, Point2D position, double orientation) {
		super(name, position);
		this.orientation = orientation;
		wood1 = new BaseLandingWood(this.name + "-wood-1", new Point2D.Double(500, 54), 0);
		wood2 = new BaseLandingWood(this.name + "-wood-2", new Point2D.Double(500, -54), 0);
	}

	@Override
	public void paint(Graphics2D g) {
		g.rotate(-orientation);
		wood1.paint(g);
		wood2.paint(g);
		g.rotate(orientation);
	}
}
