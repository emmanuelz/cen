package org.cen.ui.gameboard.elements.trajectory;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public abstract class AbstractTrajectoryPath extends AbstractGameBoardElement implements ITrajectoryPath {
	protected Point2D end;
	protected double finalAngle;
	protected double initialAngle;
	protected Point2D start;
	private BasicStroke stroke = new BasicStroke(5);

	public AbstractTrajectoryPath(String name, Point2D position) {
		this(name, position, null, null, 0.0, 0.0);
	}

	public AbstractTrajectoryPath(String name, Point2D position, Point2D start, Point2D end, double initialAngle, double finalAngle) {
		super(name, position);
		this.start = start;
		this.end = end;
		this.initialAngle = initialAngle;
		this.finalAngle = finalAngle;
	}

	private Color getColor() {
		return Color.BLUE;
	}

	@Override
	public double getRobotFinalAngle() {
		return finalAngle;
	}

	@Override
	public double getRobotInitialAngle() {
		return initialAngle;
	}

	private Stroke getStroke() {
		return stroke;
	}

	protected abstract Shape getTrajectory();

	@Override
	public Point2D getTrajectoryEnd() {
		return end;
	}

	@Override
	public Point2D getTrajectoryStart() {
		return start;
	}

	@Override
	public void paint(Graphics2D g) {
		Stroke stroke = getStroke();
		Shape trajectory = getTrajectory();
		Color color = getColor();
		g.setStroke(stroke);
		g.setColor(color);
		g.draw(trajectory);
	}
}
