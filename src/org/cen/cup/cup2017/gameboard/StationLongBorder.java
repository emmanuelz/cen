package org.cen.cup.cup2017.gameboard;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;
import org.cen.ui.gameboard.RALColor;

public class StationLongBorder extends AbstractGameBoardElement {
	private Rectangle area = null;

	// (0,0) au centre du rectangle
	public StationLongBorder(String name, Point2D position) {
		super(name, position);
		area = new Rectangle(-225, -14, 450, 28);
	}

	@Override
	public void paint(Graphics2D g) {
		g.translate(-position.getX(), -position.getY());
		g.setColor(RALColor.RAL_9016);
		g.fill(area);
		g.translate(position.getX(), position.getX());
	}
}
