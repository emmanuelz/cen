package org.cen.cup.cup2017.gameboard;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;
import org.cen.ui.gameboard.RALColor;

public class Soute extends AbstractGameBoardElement {
	private Rectangle area = null;

	// (0,0) au milieu du rectangle
	public Soute(String name, Point2D position) {
		super(name, position);
		area = new Rectangle(-111, -200, 222, 400);
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(RALColor.RAL_7000);
		g.fill(area);
		g.setColor(RALColor.RAL_9005);
		g.draw(area);
	}
}
