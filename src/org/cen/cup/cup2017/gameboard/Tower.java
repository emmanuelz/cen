package org.cen.cup.cup2017.gameboard;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public class Tower extends AbstractGameBoardElement {

	private Path2D outer = null;
	private Path2D inner = null;

	public Tower(String name, Point2D position) {
		super(name, position);
		outer = new Path2D.Double();
		outer.moveTo(position.getX() - 40, position.getY() - 40);
		outer.lineTo(position.getX() - 40, position.getY() + 40);
		outer.lineTo(position.getX() + 40, position.getY() + 40);
		outer.lineTo(position.getX() + 40, position.getY() - 40);
		outer.closePath();
		inner = new Path2D.Double();
		inner.moveTo(position.getX() - 12, position.getY() - 35);
		inner.lineTo(position.getX() + 12, position.getY() - 35);
		inner.lineTo(position.getX() + 12, position.getY() - 12);
		inner.lineTo(position.getX() + 35, position.getY() - 12);
		inner.lineTo(position.getX() + 35, position.getY() + 12);
		inner.lineTo(position.getX() + 12, position.getY() + 12);
		inner.lineTo(position.getX() + 12, position.getY() + 35);
		inner.lineTo(position.getX() - 12, position.getY() + 35);
		inner.lineTo(position.getX() - 12, position.getY() + 12);
		inner.lineTo(position.getX() - 35, position.getY() + 12);
		inner.lineTo(position.getX() - 35, position.getY() - 12);
		inner.lineTo(position.getX() - 12, position.getY() - 12);
		inner.closePath();
	}

	@Override
	public void paint(Graphics2D g) {
		g.translate(-position.getX(), -position.getY());
		g.setColor(Color.BLACK);
		g.fill(outer);
		g.setColor(Color.WHITE);
		g.fill(inner);
		g.translate(position.getX(), position.getY());
	}

}
