package org.cen.ui.gameboard;

import java.awt.geom.Point2D;

public class GameBoardMouseMoveEvent implements IGameBoardEvent {
	private Point2D position;

	public GameBoardMouseMoveEvent(Point2D position) {
		super();
		this.position = position;
	}

	public Point2D getPosition() {
		return position;
	}
}
