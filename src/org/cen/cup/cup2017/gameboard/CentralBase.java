package org.cen.cup.cup2017.gameboard;

import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public class CentralBase extends AbstractGameBoardElement {
	private BaseLandingArea blnorth = null;
	private BaseLandingArea blmiddle = null;
	private BaseLandingArea blsouth = null;
	private Arc2D semicirclebase = null;

	// (0,0) au centre du cercle de la base centrale
	public CentralBase(String name, Point2D position) {
		super(name, position);
		blnorth = new BaseLandingArea(this.name + "-bln", new Point2D.Double(0, 0), Math.PI / 4);
		blmiddle = new BaseLandingArea(this.name + "blm", new Point2D.Double(0, 0), 0);
		blsouth = new BaseLandingArea(this.name + "-bls", new Point2D.Double(0, 0), -Math.PI / 4);
		semicirclebase = new Arc2D.Double(-200, -200, 400, 400, 90, 180, Arc2D.PIE);
	}

	@Override
	public void paint(Graphics2D g) {
		blnorth.paint(g);
		blmiddle.paint(g);
		blsouth.paint(g);
		g.fill(semicirclebase);
	}

}
