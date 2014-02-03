package org.cen.ui.trajectories;

import org.cen.ui.gameboard.IGameBoardElement;
import org.cen.ui.gameboard.elements.trajectory.IGauge;
import org.cen.ui.gameboard.elements.trajectory.ITrajectoryPath;

public class DisplayedTrajectory {
	private IGameBoardElement element;
	private String gaugeLabel;
	private String pathLabel;
	private boolean visible;

	public DisplayedTrajectory(String pathLabel, String gaugeLabel, IGameBoardElement element) {
		super();
		visible = true;
		this.pathLabel = pathLabel;
		this.gaugeLabel = gaugeLabel;
		this.element = element;
	}

	public IGameBoardElement getElement() {
		return element;
	}

	public IGauge getGauge() {
		ITrajectoryPath path = getTrajectoryPath();
		IGauge gauge = path.getGauge();
		return gauge;
	}

	public String getGaugeLabel() {
		return gaugeLabel;
	}

	public String getPathLabel() {
		return pathLabel;
	}

	public ITrajectoryPath getTrajectoryPath() {
		ITrajectoryPath path = (ITrajectoryPath) element;
		return path;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setElement(IGameBoardElement element) {
		this.element = element;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public String toString() {
		return pathLabel + "-" + gaugeLabel;
	}
}
