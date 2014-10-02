package org.cen.ui.gameboard.elements.trajectory;

import java.text.ParseException;

public abstract class AbstractTrajectoryParser {
	public abstract void parseLine(String line) throws ParseException;

	public abstract ITrajectoryPath getPath(String name);
}
