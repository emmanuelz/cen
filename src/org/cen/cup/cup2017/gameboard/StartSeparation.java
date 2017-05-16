package org.cen.cup.cup2017.gameboard;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;
import org.cen.ui.gameboard.RALColor;

public class StartSeparation extends AbstractGameBoardElement {
	private Path2D area = null;

	// point (0,0) en haut à gauche
	public StartSeparation(String name, Point2D position) {
		super(name, position);
		area = new Path2D.Double();
		area.moveTo(position.getX(), position.getY());
		area.lineTo(position.getX() + 22, position.getY());
		area.lineTo(position.getX() + 22, position.getY() - 710);
		area.lineTo(position.getX(), position.getY() - 710);
		area.closePath();
	}

	@Override
	public void paint(Graphics2D g) {
		g.translate(-position.getX(), -position.getY());
		g.setColor(RALColor.RAL_8002);
		g.fill(area);
		g.translate(position.getX(), position.getY());
	}
}
