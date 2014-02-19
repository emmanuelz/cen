package org.cen.ui.gameboard.elements.trajectory;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;
import org.cen.ui.gameboard.IGameBoardTimedElement;

public abstract class AbstractTrajectoryPath extends AbstractGameBoardElement implements ITrajectoryPath, IGauge, IGameBoardTimedElement {
	protected static final Color GAUGE_COLOR = new Color(0x200000ff, true);
	protected static final Stroke OUTLINE_STROKE = new BasicStroke(2, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10, new float[] { 15, 15 }, 0);
	protected Point2D end;
	protected double finalAngle;
	private IGauge gauge;
	protected double initialAngle;
	protected Point2D start;
	private BasicStroke stroke = new BasicStroke(3);
	private Shape trajectoryGauge;

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
	public IGauge getGauge() {
		return gauge;
	}

	@Override
	public Shape getGaugeShape() {
		if (trajectoryGauge == null) {
			if (gauge == null) {
				return null;
			}
			Shape p = getPath();
			Shape g = gauge.getGaugeShape();
			TrajectoryStroke ts = new TrajectoryStroke(g);
			trajectoryGauge = ts.createStrokedShape(p);
		}
		return trajectoryGauge;
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

	@Override
	public Point2D getTrajectoryStart() {
		return start;
	}

	@Override
	public void paint(Graphics2D g) {
		Shape gauge = getGaugeShape();
		if (gauge != null) {
			g.setColor(GAUGE_COLOR);
			g.fill(gauge);
			g.setColor(Color.WHITE);
			g.setStroke(OUTLINE_STROKE);
			g.draw(gauge);
		}

		Stroke stroke = getStroke();
		Shape path = getPath();
		Color color = getColor();
		g.setStroke(stroke);
		g.setColor(color);
		g.draw(path);
	}

	protected void paintGauge(Graphics2D g) {
		IGauge gauge = getGauge();
		if (gauge == null) {
			return;
		}
		Shape gaugeShape = gauge.getGaugeShape();
		if (gaugeShape == null) {
			return;
		}
		g.setColor(Color.LIGHT_GRAY);
		g.fill(gaugeShape);
		Stroke stroke = getStroke();
		g.setStroke(stroke);
		g.setColor(Color.BLACK);
		g.draw(gaugeShape);
	}

	@Override
	public void setGauge(IGauge gauge) {
		if (gauge == this.gauge) {
			return;
		}
		trajectoryGauge = null;
		this.gauge = gauge;
	}
}
