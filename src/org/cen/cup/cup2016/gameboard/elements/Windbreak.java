package org.cen.cup.cup2016.gameboard.elements;

import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;
import java.awt.geom.Path2D.Double;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.cen.cup.cup2015.gameboard.elements.Strokes;
import org.cen.ui.gameboard.AbstractGameBoardElement;
import org.cen.ui.gameboard.RALColor;

public class Windbreak extends AbstractGameBoardElement {
	private Double outline;
	private Arc2D arcA;
	private Arc2D arcB;

	public Windbreak(String name, Point2D position) {
		super(name, position);

		outline = new Path2D.Double();
		outline.append(new Rectangle2D.Double(0, -600, 22, 1200), false);
		outline.append(new Rectangle2D.Double(0, -24, 600, 48), false);

		arcA = new Arc2D.Double(-590, -590, 1180, 1180, 0, 90, Arc2D.OPEN);
		arcB = new Arc2D.Double(-590, -590, 1180, 1180, -90, 90, Arc2D.OPEN);
	}

	@Override
	public void paint(Graphics2D g) {
		g.setStroke(Strokes.thickStroke);
		g.setColor(RALColor.RAL_4008);
		g.draw(arcA);
		g.setColor(RALColor.RAL_6001);
		g.draw(arcB);
		g.setColor(RALColor.RAL_1007);
		g.fill(outline);
		g.setColor(RALColor.RAL_9005);
		g.setStroke(Strokes.thinStroke);
		g.draw(outline);
	}
}
