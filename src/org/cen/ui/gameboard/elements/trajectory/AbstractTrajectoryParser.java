package org.cen.ui.gameboard.elements.trajectory;

public abstract class AbstractTrajectoryParser {
	public abstract void parseLine(String line);

	public abstract ITrajectoryPath getPath(String name);
}
