package org.cen.ui.gameboard.elements.trajectory;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.io.FileNotFoundException;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public abstract class AbstractTrajectoryPath extends AbstractGameBoardElement implements ITrajectoryPath {
	private static final Color GAUGE_COLOR = new Color(0x200000ff, true);
	private static final Stroke OUTLINE_STROKE = new BasicStroke(2, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10, new float[] { 15, 15 }, 0);
	protected Point2D end;
	protected double finalAngle;
	private Shape gauge;

	protected double initialAngle;

	protected Point2D start;
	private BasicStroke stroke = new BasicStroke(3);

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

	@Override
	public Point2D getTrajectoryEnd() {
		return end;
	}

	protected Shape getTrajectoryGauge() {
		if (gauge == null) {
			Shape p = getPath();
			TrajectoryStroke ts;
			try {
				ts = new TrajectoryStroke(new GaugeFactory().getGauge("gauges/robin.txt"));
				gauge = ts.createStrokedShape(p);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return gauge;
	}

	@Override
	public Point2D getTrajectoryStart() {
		return start;
	}

	@Override
	public void paint(Graphics2D g) {
		Shape gauge = getTrajectoryGauge();
		g.setColor(GAUGE_COLOR);
		g.fill(gauge);
		g.setColor(Color.WHITE);
		g.setStroke(OUTLINE_STROKE);
		g.draw(gauge);

		Stroke stroke = getStroke();
		Shape path = getPath();
		Color color = getColor();
		g.setStroke(stroke);
		g.setColor(color);
		g.draw(path);
	}
}
