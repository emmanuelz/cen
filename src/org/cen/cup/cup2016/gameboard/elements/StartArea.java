package org.cen.cup.cup2016.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.cen.cup.cup2015.gameboard.GameBoard2015;
import org.cen.cup.cup2015.gameboard.elements.Strokes;
import org.cen.ui.gameboard.AbstractGameBoardElement;
import org.cen.ui.gameboard.RALColor;

public class StartArea extends AbstractGameBoardElement {
	private Area bottom;
	private Area top;
	private Color color;

	public StartArea(String name, Point2D position, Color color, boolean symetric) {
		super(name, position, 0, 1);

		this.color = color;

		Shape s = new Rectangle2D.Double(0, 0, 500, 300);
		bottom = new Area(s);

		Path2D p = new Path2D.Double();
		p.append(new Rectangle2D.Double(50, 0, 50, 300), false);
		p.append(new Rectangle2D.Double(400, 0, 50, 300), false);
		top = new Area(p);

		if (symetric) {
			AffineTransform t = GameBoard2015.getSymetricTransform();
			bottom.transform(t);
			top.transform(t);
		}
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(color);
		g.fill(bottom);
		g.setColor(RALColor.RAL_9016);
		g.fill(top);
		g.setColor(RALColor.RAL_9005);
		g.setStroke(Strokes.thinStroke);
		g.draw(bottom);
		g.draw(top);
	}
}
