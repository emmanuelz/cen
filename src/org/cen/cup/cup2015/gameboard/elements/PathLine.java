package org.cen.cup.cup2015.gameboard.elements;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

import org.cen.cup.cup2015.gameboard.GameBoard2015;
import org.cen.math.PathUtils;
import org.cen.ui.gameboard.AbstractGameBoardElement;
import org.cen.ui.gameboard.RALColor;

public class PathLine extends AbstractGameBoardElement {
	private Path2D path = new Path2D.Double();

	public PathLine(String name, Point2D position, boolean symetric) {
		super(name, position);
		order = 3;
		path.moveTo(820, 550);
		path.lineTo(1700, 550);
		PathUtils.arcTo(path, 1850, 400, 1850, 550);
		PathUtils.arcTo(path, 2000, 250, 1850, 250);
		path.moveTo(1850, 400);
		path.lineTo(1850, 860);
		PathUtils.arcTo(path, 1700, 1010, 1850, 1010);
		PathUtils.arcTo(path, 1550, 1160, 1550, 1010);
		path.lineTo(1550, 1500);
		path.moveTo(1850, 550);
		path.lineTo(2000, 550);
		path.moveTo(1850, 850);
		path.lineTo(2000, 850);
		if (symetric) {
			AffineTransform t = GameBoard2015.getSymetricTransform();
			path.transform(t);
		}
	}

	@Override
	public void paint(Graphics2D g) {
		g.setStroke(Strokes.thickStroke);
		g.setColor(RALColor.RAL_9005);
		g.draw(path);
	}
}
