package org.cen.ui.gameboard.elements.trajectory;

import java.awt.Color;
import java.awt.Shape;

/**
 * Represents the gauge of an object. The gauge is used to compute the footprint
 * of a trajectory involving the object.
 * 
 * @author Emmanuel ZURMELY
 */
public interface IGauge {
	public Shape getGaugeShape();

	public Color getGaugeColor();
}
