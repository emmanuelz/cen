package org.cen.cup.cup2014.gameboard.elements;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;
import org.cen.ui.gameboard.RALColor;

public class CentralFireplace extends AbstractGameBoardElement {
	private Shape shape = new Ellipse2D.Double(-150, -150, 300, 300);

	public CentralFireplace(String name, Point2D position) {
		super(name, position, 0, 3);
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(RALColor.RAL_8002);
		g.fill(shape);
	}
}
