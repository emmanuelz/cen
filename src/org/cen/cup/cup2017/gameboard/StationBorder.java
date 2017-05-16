package org.cen.cup.cup2017.gameboard;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public class StationBorder extends AbstractGameBoardElement {
	private StationSmallBorder smwest = null;
	private StationSmallBorder smest = null;
	private StationLongBorder smsouth = null;

	// (0,0) dans le vide au milieu des 2 morceaux sur les cotés, contre la bordure
	public StationBorder(String name, Point2D position, double angle) {
		super(name, position, angle);
		smwest = new StationSmallBorder(this.name + "smwest", new Point2D.Double(-237, 40));
		smest = new StationSmallBorder(this.name + "smest", new Point2D.Double(237, 40));
		smsouth = new StationLongBorder(this.name + "smsouth", new Point2D.Double(0, 94));
	}

	@Override
	public void paint(Graphics2D g) {
		smwest.paint(g);
		smest.paint(g);
		smsouth.paint(g);
	}
}
