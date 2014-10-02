package org.cen.cup.cup2014.gameboard.elements;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

import org.cen.cup.cup2014.gameboard.GameBoard2014;
import org.cen.ui.gameboard.AbstractGameBoardElement;

public class FrescoPath extends AbstractGameBoardElement {
	private Path2D path = new Path2D.Double();
	private Stroke stroke = new BasicStroke(20, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);

	public FrescoPath(String name, Point2D position, boolean symetric) {
		super(name, position, 0, 2);
		path.moveTo(600, GameBoard2014.symetrize(symetric, 0));
		path.lineTo(600, GameBoard2014.symetrize(symetric, 1200));
		path.quadTo(600, GameBoard2014.symetrize(symetric, 1350), 450, GameBoard2014.symetrize(symetric, 1350));
		path.lineTo(0, GameBoard2014.symetrize(symetric, 1350));
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.setStroke(stroke);
		g.draw(path);
	}
}
