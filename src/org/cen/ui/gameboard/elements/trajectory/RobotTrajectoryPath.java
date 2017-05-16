package org.cen.ui.gameboard.elements.trajectory;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.util.List;

import org.cen.ui.gameboard.IGameBoardTimedElement;

public class RobotTrajectoryPath extends AbstractTrajectoryPath {
	private List<ITrajectoryPath> elements;

	public RobotTrajectoryPath(String name, List<ITrajectoryPath> elements) {
		super(name, new Point2D.Double(), null, null, 0, 0);
		this.elements = elements;
		ITrajectoryPath element = getFirstElement();
		this.initialAngle = element.getRobotInitialAngle();
		element = getLastElement();
		this.finalAngle = element.getRobotFinalAngle();
	}

	protected ITrajectoryPath getFirstElement() {
		if (elements.size() > 0) {
			return elements.get(0);
		} else {
			return null;
		}
	}

	protected ITrajectoryPath getLastElement() {
		int index = elements.size() - 1;
		if (index >= 0) {
			return elements.get(index);
		} else {
			return null;
		}
	}

	@Override
	public double getOrientation(double timestamp) {
		ITrajectoryPath path = getPathForTimestamp(timestamp);
		double start = path.getStartTimestamp();
		return path.getOrientation(timestamp - start);
	}

	@Override
	public Shape getPath() {
		return null;
	}

	private ITrajectoryPath getPathForTimestamp(double timestamp) {
		for (ITrajectoryPath element : elements) {
			double end = element.getEndTimestamp();
			if (timestamp <= end) {
				return element;
			}
		}
		return getLastElement();
	}

	@Override
	public Point2D getPosition(double timestamp) {
		ITrajectoryPath element = getPathForTimestamp(timestamp);
		double start = element.getStartTimestamp();
		return element.getPosition(timestamp - start);
	}

	@Override
	public Point2D[] getTrajectoryControlPoints() {
		return null;
	}

	@Override
	public void paint(Graphics2D g, double timestamp) {
		ITrajectoryPath path = getPathForTimestamp(timestamp);
		if (path instanceof IGameBoardTimedElement) {
			IGameBoardTimedElement element = (IGameBoardTimedElement) path;
			double start = path.getStartTimestamp();
			element.paint(g, timestamp - start);
		}
	}

	@Override
	public void paintUnscaled(Graphics2D g, double timestamp) {
		ITrajectoryPath path = getPathForTimestamp(timestamp);
		if (path instanceof IGameBoardTimedElement) {
			IGameBoardTimedElement element = (IGameBoardTimedElement) path;
			double start = path.getStartTimestamp();
			element.paintUnscaled(g, timestamp - start);
		}
	}

	@Override
	public double getEndTimestamp() {
		ITrajectoryPath path = getLastElement();
		double timestamp = path == null ? 0 : path.getEndTimestamp();
		return timestamp;
	}

	@Override
	public double getStartTimestamp() {
		ITrajectoryPath path = getFirstElement();
		double timestamp = path == null ? 0 : path.getEndTimestamp();
		return timestamp;
	}
}
