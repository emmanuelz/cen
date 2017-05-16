package org.cen.cup.cup2017.gameboard;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;
import org.cen.ui.gameboard.RALColor;

public class Bascule extends AbstractGameBoardElement {
	private Rectangle area = null;

	// (0,0) au centre du rectangle
	public Bascule(String name, Point2D position) {
		super(name, position);
		area = new Rectangle(-180, -175, 360, 350);
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(RALColor.RAL_7032);
		g.fill(area);
	}
}
