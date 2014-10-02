package org.cen.ui.gameboard.elements.trajectory;

import java.awt.Color;
import java.awt.Shape;

public class Gauge implements IGauge {
	private Color color;
	private Shape shape;

	public Gauge(Shape shape) {
		this(shape, Color.LIGHT_GRAY);
	}

	public Gauge(Shape shape, Color color) {
		super();
		this.shape = shape;
		this.color = color;
	}

	@Override
	public Color getGaugeColor() {
		return color;
	}

	@Override
	public Shape getGaugeShape() {
		return shape;
	}
}
