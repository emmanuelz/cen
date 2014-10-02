package org.cen.ui.gameboard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Abstract base class of IGameboard implementations.
 */
public abstract class AbstractGameBoard implements IGameBoardService {
	@Override
	public List<IGameBoardElement> findElements(String elementName) {
		List<IGameBoardElement> found = new ArrayList<IGameBoardElement>();
		List<IGameBoardElement> elements = getElements();
		Iterator<IGameBoardElement> i = elements.iterator();
		while (i.hasNext()) {
			IGameBoardElement e = i.next();
			if (e.getName().equals(elementName)) {
				found.add(e);
			}
		}
		return found;
	}

	@Override
	public void removeElements(String elementName) {
		List<IGameBoardElement> elements = getElements();
		List<IGameBoardElement> found = findElements(elementName);
		for (IGameBoardElement e : found) {
			elements.remove(e);
		}
	}
}
