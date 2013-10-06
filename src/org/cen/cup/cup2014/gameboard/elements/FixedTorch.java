package org.cen.cup.cup2014.gameboard.elements;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;
import org.cen.ui.gameboard.RALColor;

public class FixedTorch extends AbstractGameBoardElement {
	private Shape rect1 = new Rectangle2D.Double(-47 - 22, 0, 22, 22);
	private Shape rect2 = new Rectangle2D.Double(47, 0, 22, 22);

	public FixedTorch(String name, Point2D position, double orientation) {
		super(name, position, orientation, 3);
	}

	@Override
	public void paint(Graphics2D g) {
		// torch
		g.setColor(RALColor.RAL_7032);
		g.fill(rect1);
		g.fill(rect2);
	}
}
