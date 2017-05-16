package org.cen.cup.cup2017.gameboard;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;
import org.cen.ui.gameboard.RALColor;

public class TableSeparation extends AbstractGameBoardElement {
	private Color color = null;
	private Rectangle2D outer = null;

	public TableSeparation(String name, Point2D positionCentrale) {
		super(name, positionCentrale);
		color = RALColor.RAL_9005;
		outer = new Rectangle2D.Double(positionCentrale.getX() - 1000, positionCentrale.getY(), 2000, 1);
	}

	@Override
	public void paint(Graphics2D g) {
		g.translate(-position.getX(), -position.getY());
		g.setColor(color);
		g.fill(outer);
		g.translate(position.getX(), position.getY());
	}
}
