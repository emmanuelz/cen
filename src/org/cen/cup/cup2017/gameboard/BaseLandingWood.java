package org.cen.cup.cup2017.gameboard;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;
import org.cen.ui.gameboard.RALColor;

public class BaseLandingWood extends AbstractGameBoardElement {
	private Rectangle area = null;
	private double theta = 0;

	// (0, 0) au centre du rectangle
	public BaseLandingWood(String name, Point2D position, double theta) {
		super(name, position);
		this.theta = theta;
		this.area = new Rectangle(-300, -14, 600, 28);
	}

	@Override
	public void paint(Graphics2D g) {
		g.translate(-position.getX(), -position.getY());
		g.rotate(-theta);
		g.setColor(RALColor.RAL_9016);
		g.fill(area);
		g.rotate(theta);
		g.translate(position.getX(), position.getY());
	}

}
