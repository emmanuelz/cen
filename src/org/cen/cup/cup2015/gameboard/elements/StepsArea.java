package org.cen.cup.cup2015.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.cen.cup.cup2015.gameboard.GameBoard2015;
import org.cen.ui.gameboard.AbstractGameBoardElement;
import org.cen.ui.gameboard.RALColor;

public class StepsArea extends AbstractGameBoardElement {
	public static final double HALF_HEIGHT = 250;
	Path2D coloredArea = new Path2D.Double();
	Path2D carpetArea = new Path2D.Double();
	Path2D steps = new Path2D.Double();
	Path2D borders = new Path2D.Double();
	private Color color;

	public StepsArea(String name, Point2D position, Color color) {
		super(name, position);

		this.color = color;

		coloredArea.append(new Rectangle2D.Double(0, -HALF_HEIGHT, 300, HALF_HEIGHT * 2), true);
		coloredArea.append(new Rectangle2D.Double(300, -150, 280, 300), true);

		carpetArea.append(new Rectangle2D.Double(300, -HALF_HEIGHT, 280, 100), false);
		carpetArea.append(new Rectangle2D.Double(300, HALF_HEIGHT - 100, 280, 100), false);

		steps.moveTo(370, -HALF_HEIGHT - GameBoard2015.BORDER_WIDTH);
		steps.lineTo(370, HALF_HEIGHT + GameBoard2015.BORDER_WIDTH);
		steps.moveTo(440, -HALF_HEIGHT);
		steps.lineTo(440, HALF_HEIGHT);
		steps.moveTo(510, -HALF_HEIGHT);
		steps.lineTo(510, HALF_HEIGHT);
		steps.moveTo(580, -HALF_HEIGHT + 100);
		steps.lineTo(580, HALF_HEIGHT - 100);

		borders.append(new Rectangle2D.Double(0, HALF_HEIGHT, 580, GameBoard2015.BORDER_WIDTH), false);
		borders.append(new Rectangle2D.Double(0, -HALF_HEIGHT - GameBoard2015.BORDER_WIDTH, 580, GameBoard2015.BORDER_WIDTH), false);
		borders.append(new Rectangle2D.Double(0, -HALF_HEIGHT, GameBoard2015.BORDER_WIDTH, HALF_HEIGHT * 2), false);
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(color);
		g.fill(coloredArea);
		g.setColor(RALColor.RAL_7032);
		g.fill(carpetArea);
		g.setColor(RALColor.RAL_5015);
		g.fill(borders);
		g.setColor(RALColor.RAL_9005);
		g.setStroke(Strokes.thinStroke);
		g.draw(steps);
		g.draw(carpetArea);
		g.draw(borders);
	}
}
