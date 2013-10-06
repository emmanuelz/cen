package org.cen.cup.cup2014.gameboard.elements;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;
import org.cen.ui.gameboard.RALColor;

public class SideFireplace extends AbstractGameBoardElement {
	private Shape shape = new Arc2D.Double(-250, -250, 500, 500, -90, -90, Arc2D.PIE);

	public SideFireplace(String name, Point2D position, double orientation) {
		super(name, position, orientation, 3);
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(RALColor.RAL_8002);
		g.fill(shape);
	}
}
