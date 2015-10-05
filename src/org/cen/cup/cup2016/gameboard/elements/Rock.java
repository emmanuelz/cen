package org.cen.cup.cup2016.gameboard.elements;

import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;
import org.cen.ui.gameboard.IGameBoardElement;
import org.cen.ui.gameboard.RALColor;

public class Rock extends AbstractGameBoardElement implements IGameBoardElement {
	private Path2D path;

	public Rock(String name, Point2D position, boolean symetric) {
		super(name, position);
		path = new Path2D.Double();
		double angle = symetric ? -90 : 90;
		path.append(new Arc2D.Double(-250, -250, 500, 500, 180, angle, Arc2D.PIE), false);
		path.append(new Arc2D.Double(-150, -150, 300, 300, 180, angle, Arc2D.PIE), false);
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(RALColor.RAL_7032);
		g.fill(path);
		g.setColor(RALColor.RAL_9005);
		g.draw(path);
	}

}
