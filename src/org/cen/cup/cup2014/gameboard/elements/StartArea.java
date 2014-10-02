package org.cen.cup.cup2014.gameboard.elements;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

import org.cen.cup.cup2014.gameboard.GameBoard2014;
import org.cen.ui.gameboard.AbstractGameBoardElement;
import org.cen.ui.gameboard.RALColor;

public class StartArea extends AbstractGameBoardElement {
	private Path2D path = new Path2D.Double();
	private Stroke stroke = new BasicStroke(20, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
	private Color color;

	public StartArea(String name, Point2D position, Color color, boolean symetric) {
		super(name, position, 0, 1);
		this.color = color;

		path.moveTo(-10, GameBoard2014.symetrize(symetric, 390));
		path.lineTo(300, GameBoard2014.symetrize(symetric, 390));
		path.quadTo(690, GameBoard2014.symetrize(symetric, 390), 690, GameBoard2014.symetrize(symetric, -10));
		path.lineTo(-10, GameBoard2014.symetrize(symetric, -10));
		path.lineTo(-10, GameBoard2014.symetrize(symetric, 390));
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(RALColor.RAL_7032);
		g.fill(path);
		g.setColor(color);
		g.setStroke(stroke);
		g.draw(path);
	}
}
