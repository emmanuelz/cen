package org.cen.cup.cup2014.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;
import org.cen.ui.gameboard.RALColor;

public class Basket extends AbstractGameBoardElement {
	private Shape shape1 = new Rectangle2D.Double(0, 0, 130, 700);
	private Shape shape2 = new Rectangle2D.Double(130, 0, 170, 700);
	private Shape inner = new Rectangle2D.Double(130, 22, 148, 656);
	private Color color;

	public Basket(String name, Point2D position, Color color) {
		super(name, position, 0, 3);
		this.color = color;
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(RALColor.RAL_6018);
		g.fill(shape1);
		g.setColor(color);
		g.fill(shape2);
		g.setColor(Color.BLACK);
		g.draw(shape1);
		g.draw(shape2);
		g.draw(inner);
	}
}
