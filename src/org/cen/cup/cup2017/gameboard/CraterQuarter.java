package org.cen.cup.cup2017.gameboard;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public class CraterQuarter extends AbstractGameBoardElement {
	private CraterBorder _1 = null;
	private CraterBorder _2 = null;
	private CraterBorder _3 = null;
	private CraterBorder _4 = null;
	private double orientation = 0;

	// (0,0) au centre du cercle inscrit
	public CraterQuarter(String name, Point2D position, double orientation) {
		super(name, position);
		this.orientation = orientation;
		double l = 15 * Math.sin(Math.PI / 32);
		double alpha = 0;
		double x1 = (510 + 15) * Math.cos(alpha);
		double y1 = (510 + 15) * Math.sin(alpha);
		double x2 = (l + 50) * Math.sin(alpha);
		double y2 = (l + 50) * Math.cos(alpha);
		_1 = new CraterBorder(this.name + "-bcs-1", new Point2D.Double(x1 - x2, -(y1 + y2)), Math.PI / 32);
		double beta = 2 * Math.PI / 32;
		double x3 = (510 + 15) * Math.cos(beta);
		double y3 = (510 + 15) * Math.sin(beta);
		double x4 = (l + 50) * Math.sin(beta);
		double y4 = (l + 50) * Math.cos(beta);
		_2 = new CraterBorder(this.name + "-bcs-2", new Point2D.Double(x3 - x4, -(y3 + y4)), 3 * Math.PI / 32);
		double gamma = 4 * Math.PI / 32;
		double x5 = (510 + 15) * Math.cos(gamma);
		double y5 = (510 + 15) * Math.sin(gamma);
		double x6 = (l + 50) * Math.sin(gamma);
		double y6 = (l + 50) * Math.cos(gamma);
		_3 = new CraterBorder(this.name + "-bcs-3", new Point2D.Double(x5 - x6, -(y5 + y6)), 5 * Math.PI / 32);
		double delta = 6 * Math.PI / 32;
		double x7 = (510 + 15) * Math.cos(delta);
		double y7 = (510 + 15) * Math.sin(delta);
		double x8 = (l + 50) * Math.sin(delta);
		double y8 = (l + 50) * Math.cos(delta);
		_4 = new CraterBorder(this.name + "-bcs-4", new Point2D.Double(x7 - x8, -(y7 + y8)), 7 * Math.PI / 32);
	}

	@Override
	public void paint(Graphics2D g) {
		g.rotate(-orientation);
		_1.paint(g);
		_2.paint(g);
		_3.paint(g);
		_4.paint(g);
		g.rotate(orientation);
	}
}
