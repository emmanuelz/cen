package org.cen.cup.cup2016.gameboard.elements;

import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

import org.cen.cup.cup2015.gameboard.elements.Strokes;
import org.cen.ui.gameboard.AbstractGameBoardElement;
import org.cen.ui.gameboard.RALColor;

public class Sea extends AbstractGameBoardElement {
	private Path2D path;
	private Area area;

	public Sea(String name, Point2D position) {
		super(name, position);

		path = new Path2D.Double();
		path.moveTo(0, 0);
		path.lineTo(0, 600);
		path.quadTo(0, 691, 47, 775);
		path.quadTo(300, 1150, 300, 1500);
		path.quadTo(300, 1850, 47, 2225);
		path.quadTo(0, 2309, 0, 2400);
		path.lineTo(0, 3000);

		Path2D outline = new Path2D.Double(path);
		outline.lineTo(800, 3000);
		outline.lineTo(800, 0);
		outline.closePath();

		area = new Area(outline);
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(RALColor.RAL_5015);
		g.fill(area);
		g.setStroke(Strokes.thinStroke);
		g.setColor(RALColor.RAL_9005);
		g.draw(path);
	}
}
