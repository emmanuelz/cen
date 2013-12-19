package org.cen.ui.gameboard.elements.trajectory;

import java.awt.Shape;

public class Gauge implements IGauge {
	private Shape shape;

	public Gauge(Shape shape) {
		super();
		this.shape = shape;
	}

	@Override
	public Shape getGaugeShape() {
		return shape;
	}
}
