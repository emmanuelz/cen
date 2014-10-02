package org.cen.ui.gameboard;

import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 * Interface describing the game board.
 */
public interface IGameBoardService {
	/**
	 * Search for the gameboard elements with the specified name
	 * 
	 * @param elementName
	 *            the name of the elements
	 * @return a list containing the gameboard elements that match the specified
	 *         name
	 */
	public List<IGameBoardElement> findElements(String elementName);

	/**
	 * Returns the ordered list of the elements of the game board.
	 * 
	 * @return a list of game board elements sorted by painting order
	 */
	public List<IGameBoardElement> getElements();

	/**
	 * Returns the playable bounds of the game board.
	 * 
	 * @return the playable bounds of the game board
	 */
	public Rectangle2D getGameplayBounds();

	/**
	 * Returns the visible bounds of the game board.
	 * 
	 * @return the visible bounds of the game board
	 */
	public Rectangle2D getVisibleBounds();

	/**
	 * Removes all elements with the specified name from the gameboard.
	 * 
	 * @param elementName
	 *            the name of the elements to remove
	 * @since 2010
	 */
	public void removeElements(String elementName);
}
