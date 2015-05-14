package org.cen.ui.gameboard;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

public interface IGameBoardTimedElement extends IGameBoardElement {
	/**
	 * Returns the orientation angle of the element in radians.
	 * 
	 * @param timestamp
	 *            the timestamp
	 * @return the orientation angle of the element
	 */
	public double getOrientation(double timestamp);

	/**
	 * Returns the position of the element on the gameboard.
	 * 
	 * @param timestamp
	 *            the timestamp
	 * @return the position of the element on the gameboard
	 */
	public Point2D getPosition(double timestamp);

	/**
	 * Returns the line number in the source file. 0 if not handled.
	 * 
	 * @param timestamp
	 *            the timestamp
	 * @return the line number in the source file
	 */
	double getSourceLine(double timestamp);

	/**
	 * Paints the element on the specified graphic device.
	 * 
	 * @param g
	 *            the graphic device to paint on
	 * @param timestamp
	 *            the timestamp
	 */
	public void paint(Graphics2D g, double timestamp);

	/**
	 * Paints the element using the screen coordinates.
	 * 
	 * @param g
	 *            the graphic device to paint on
	 * @param timestamp
	 *            the timestamp
	 */
	public void paintUnscaled(Graphics2D g, double timestamp);
}
