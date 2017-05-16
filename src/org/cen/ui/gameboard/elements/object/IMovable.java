package org.cen.ui.gameboard.elements.object;

import java.awt.geom.Point2D;

public interface IMovable {
	/**
	 * Sets the position of the object
	 * 
	 * @param position
	 *            the position of the object
	 */
	void setPosition(Point2D position);

	/**
	 * Sets the orientation of the object
	 * 
	 * @param angle
	 *            the orientation angle in radians
	 */
	void setOrientation(double angle);
}
