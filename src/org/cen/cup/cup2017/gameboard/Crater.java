package org.cen.cup.cup2017.gameboard;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public class Crater extends AbstractGameBoardElement {
	private CraterBorder top = null;
	private CraterBorder ne = null;
	private CraterBorder se = null;
	private CraterBorder south = null;
	private CraterBorder sw = null;
	private CraterBorder nw = null;

	public Crater(String name, Point2D position) {
		super(name, position);
		top = new CraterBorder(this.name + "-b-top", new Point2D.Double(0, 100), Math.PI / 2);
		ne = new CraterBorder(this.name + "-b-ne", new Point2D.Double(50 + 15 * Math.tan(Math.PI / 6) + (50 + 15 * Math.tan(Math.PI / 6)) * Math.cos(Math.PI / 3), -100 + (50 + 15 * Math.tan(Math.PI / 6)) * Math.sin(Math.PI / 3)), Math.PI / 2 - Math.PI / 3);
		se = new CraterBorder(this.name + "-b-se", new Point2D.Double(50 + 15 * Math.tan(Math.PI / 6) + (50 + 15 * Math.tan(Math.PI / 6)) * Math.cos(Math.PI / 3), 100 - (50 + 15 * Math.tan(Math.PI / 6)) * Math.sin(Math.PI / 3)), Math.PI / 2 - 2 * Math.PI / 3);
		south = new CraterBorder(this.name + "-b-south", new Point2D.Double(0, -100), Math.PI / 2);
		sw = new CraterBorder(this.name + "-b-sw", new Point2D.Double(-50 - 15 * Math.tan(Math.PI / 6) - (50 + 15 * Math.tan(Math.PI / 6)) * Math.cos(Math.PI / 3), 100 - (50 + 15 * Math.tan(Math.PI / 6)) * Math.sin(Math.PI / 3)), Math.PI / 2 - Math.PI / 3);
		nw = new CraterBorder(this.name + "-b-nw", new Point2D.Double(-50 - 15 * Math.tan(Math.PI / 6) - (50 + 15 * Math.tan(Math.PI / 6)) * Math.cos(Math.PI / 3), -100 + (50 + 15 * Math.tan(Math.PI / 6)) * Math.sin(Math.PI / 3)), Math.PI / 2 - 2 * Math.PI / 3);
	}

	@Override
	public void paint(Graphics2D g) {
		top.paint(g);
		ne.paint(g);
		se.paint(g);
		south.paint(g);
		sw.paint(g);
		nw.paint(g);
	}
}
