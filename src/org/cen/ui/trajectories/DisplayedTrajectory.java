package org.cen.ui.trajectories;

import org.cen.trajectories.IInputFile;
import org.cen.ui.gameboard.IGameBoardElement;
import org.cen.ui.gameboard.elements.trajectory.IGauge;
import org.cen.ui.gameboard.elements.trajectory.ITrajectoryPath;

public class DisplayedTrajectory {
	private IGameBoardElement element;

	private IInputFile gaugeFile;

	private IInputFile pathFile;
	private boolean visible;

	public DisplayedTrajectory(IInputFile pathFile, IInputFile gaugeFile, IGameBoardElement element) {
		super();
		visible = true;
		this.pathFile = pathFile;
		this.gaugeFile = gaugeFile;
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

	public IInputFile getGaugeFile() {
		return gaugeFile;
	}

	public String getGaugeLabel() {
		return gaugeFile.getName();
	}

	public IInputFile getPathFile() {
		return pathFile;
	}

	public String getPathLabel() {
		return pathFile.getName();
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
		return getPathLabel() + "-" + getGaugeLabel();
	}
}
