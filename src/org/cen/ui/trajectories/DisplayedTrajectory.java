package org.cen.ui.trajectories;

import org.cen.ui.gameboard.IGameBoardElement;
import org.cen.ui.gameboard.elements.trajectory.IGauge;
import org.cen.ui.gameboard.elements.trajectory.ITrajectoryPath;

public class DisplayedTrajectory {
	private String label;
	private IGameBoardElement element;

	public DisplayedTrajectory(String label, IGameBoardElement element) {
		super();
		this.label = label;
		this.element = element;
	}

	public String getLabel() {
		return label;
	}

	public IGameBoardElement getElement() {
		return element;
	}

	public ITrajectoryPath getTrajectoryPath() {
		ITrajectoryPath path = (ITrajectoryPath) element;
		return path;
	}

	public IGauge getGauge() {
		ITrajectoryPath path = getTrajectoryPath();
		IGauge gauge = path.getGauge();
		return gauge;
	}
}
