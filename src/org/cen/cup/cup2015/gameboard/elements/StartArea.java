package org.cen.cup.cup2015.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

import org.cen.cup.cup2015.gameboard.GameBoard2015;
import org.cen.ui.gameboard.AbstractGameBoardElement;
import org.cen.ui.gameboard.RALColor;

public class StartArea extends AbstractGameBoardElement {
	private Path2D pathIn = new Path2D.Double();
	private Path2D pathOut = new Path2D.Double();
	private Color colorIn;
	private Color colorOut;

	public StartArea(String name, Point2D position, Color colorIn, Color colorOut, boolean symetric) {
		super(name, position, 0, 1);
		this.colorIn = colorIn;
		this.colorOut = colorOut;

		pathIn.moveTo(222, 0);
		pathIn.lineTo(-222, 0);
		pathIn.lineTo(-222, 400);
		pathIn.lineTo(-200, 400);
		pathIn.lineTo(-200, 450);
		pathIn.append(new Arc2D.Double(-200, 250, 400, 400, 180, 180, Arc2D.OPEN), true);
		pathIn.lineTo(200, 450);
		pathIn.lineTo(200, 400);
		pathIn.lineTo(222, 400);
		pathIn.closePath();

		pathOut.moveTo(-600, 0);
		pathOut.lineTo(600, 0);
		pathOut.lineTo(600, 400);
		pathOut.lineTo(-600, 400);
		pathOut.closePath();

		if (symetric) {
			AffineTransform t = GameBoard2015.getSymetricTransform();
			pathIn.transform(t);
			pathOut.transform(t);
		}
	}

	@Override
	public void paint(Graphics2D g) {
		g.setStroke(Strokes.thinStroke);
		g.setColor(colorOut);
		g.fill(pathOut);
		g.setColor(RALColor.RAL_9005);
		g.draw(pathOut);

		g.setColor(colorIn);
		g.fill(pathIn);
		g.setColor(RALColor.RAL_9005);
		g.draw(pathIn);
	}
}
