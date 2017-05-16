package org.cen.cup.cup2017.gameboard;

import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;
import org.cen.ui.gameboard.RALColor;

public class Rocket extends AbstractGameBoardElement {
	private Arc2D circle = null;

	public Rocket(String name, Point2D position) {
		super(name, position);
		circle = new Arc2D.Double(-40, -40, 80, 80, 0, 360, Arc2D.PIE);
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(RALColor.RAL_8002);
		g.fill(circle);
	}
}
