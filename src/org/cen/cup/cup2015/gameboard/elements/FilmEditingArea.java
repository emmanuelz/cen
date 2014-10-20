package org.cen.cup.cup2015.gameboard.elements;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

import org.cen.math.PathUtils;
import org.cen.ui.gameboard.AbstractGameBoardElement;
import org.cen.ui.gameboard.RALColor;

public class FilmEditingArea extends AbstractGameBoardElement {
	private Path2D path = new Path2D.Double();

	public FilmEditingArea(String name, Point2D position) {
		super(name, position, Math.PI / 2, 1);

		path.moveTo(-400, 0);
		path.lineTo(-400, 100);
		PathUtils.arcTo(path, -300, 200, -400, 200);
		path.lineTo(300, 200);
		PathUtils.arcTo(path, 400, 100, 400, 200);
		path.lineTo(400, 0);
		path.closePath();
	}

	@Override
	public void paint(Graphics2D g) {
		g.setStroke(Strokes.thinStroke);
		g.setColor(RALColor.RAL_3020);
		g.fill(path);
		g.setColor(RALColor.RAL_9005);
		g.draw(path);
	}
}
