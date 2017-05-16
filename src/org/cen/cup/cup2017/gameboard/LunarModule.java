package org.cen.cup.cup2017.gameboard;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;
import org.cen.ui.gameboard.RALColor;

public class LunarModule extends AbstractGameBoardElement {
	private Arc2D area1 = null;
	private Arc2D area2 = null;
	private Color color1 = null;
	private Color color2 = null;

	public LunarModule(String name, Point2D position, Color color1, Color color2) {
		super(name, position);
		this.color1 = color1;
		this.color2 = color2;
		if (color2 == null)
			area1 = new Arc2D.Double(-32, -32, 64, 64, 0, 360, Arc2D.OPEN);
		else {
			area1 = new Arc2D.Double(-32, -32, 64, 64, 90, 180, Arc2D.OPEN);
			area2 = new Arc2D.Double(-32, -32, 64, 64, -90, 180, Arc2D.OPEN);
		}
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(color1);
		g.fill(area1);
		g.setColor(RALColor.RAL_9005);
		g.draw(area1);
		if (color2 != null) {
			g.setColor(color2);
			g.fill(area2);
			g.setColor(RALColor.RAL_9005);
			g.draw(area2);
		}
	}
}
