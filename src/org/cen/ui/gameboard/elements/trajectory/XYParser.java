package org.cen.ui.gameboard.elements.trajectory;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

import org.cen.math.Angle;

public class XYParser extends AbstractTrajectoryParser {
	private class CommonData {
		double additionalTime = 0;
		double linearSpeed = defaultLinearSpeed;
		double rotationSpeed = defaultRotationSpeed;
	}

	private static final double MIN_ANGLE = Math.toRadians(0.5);
	private static final double MIN_DISTANCE = 0.1;
	private double defaultLinearSpeed = 0.1;
	private double defaultRotationSpeed = 0.1;
	private String delimiter;
	private double finalAngle = 0;
	private ArrayList<KeyFrame> frames = new ArrayList<>();
	private double initialAngle = 0;
	private double lastAngle = 0;
	private double lastx = 0;
	private double lasty = 0;
	private double timestamp = 0;

	public XYParser() {
		this(";");
	}

	public XYParser(String delimiter) {
		super();
		this.delimiter = delimiter;
	}

	private void addAdditionalTime(Point2D position, double angle, double additionalTime) {
		if (additionalTime > 0) {
			timestamp += additionalTime;
			KeyFrame frame = new KeyFrame(TrajectoryMovement.NONE, 0, angle, 0, position, timestamp);
			frames.add(frame);
		}
	}

	@Override
	public ITrajectoryPath getPath(String name) {
		ITrajectoryPath path = new StraightLine(name, initialAngle, finalAngle, frames);
		return path;
	}

	private void parseBezier(Scanner s) {
		// final position
		double x = s.nextDouble();
		double y = s.nextDouble();
		// control point 1
		double cx1 = s.nextDouble();
		double cy1 = s.nextDouble();
		// control point 2
		double cx2 = s.nextDouble();
		double cy2 = s.nextDouble();
		CommonData data = parseCommon(s);

		Point2D p = new Point2D.Double(x, y);
		Point2D cp1 = new Point2D.Double(cx1, cy1);
		Point2D cp2 = new Point2D.Double(cx2, cy2);

		// start angle
		double dx = cx1 - lastx;
		double dy = cy1 - lasty;
		double startAngle = Math.atan2(dy, dx);

		// end angle
		dx = x - cx2;
		dy = y - cy2;
		double endAngle = Math.atan2(dy, dx);

		// initial rotation
		double theta = Math.abs(Angle.getRotationAngle(lastAngle, startAngle));
		if (theta > MIN_ANGLE) {
			timestamp += getRotationDuration(theta, data.rotationSpeed);
			Point2D last = new Point2D.Double(x, y);
			KeyFrame frame = new KeyFrame(TrajectoryMovement.ROTATION, 0, startAngle, data.rotationSpeed, last, timestamp);
			frames.add(frame);
		}

		double distante = p.distance(lastx, lasty);
		timestamp += distante / data.linearSpeed;
		KeyFrame frame = new KeyFrame(TrajectoryMovement.BEZIER, data.linearSpeed, endAngle, data.rotationSpeed, p, timestamp, cp1, cp2);
		frames.add(frame);
	}

	private CommonData parseCommon(Scanner s) {
		CommonData data = new CommonData();
		if (s.hasNextDouble()) {
			data.linearSpeed = s.nextDouble();
		}
		if (s.hasNextDouble()) {
			data.rotationSpeed = s.nextDouble();
		}
		if (s.hasNextDouble()) {
			data.additionalTime = s.nextDouble();
		}
		return data;
	}

	private void parseInitialPosition(Scanner s) throws ParseException {
		if (!frames.isEmpty()) {
			throw new ParseException("the initial position must be the first entry", 0);
		}

		double x = s.nextDouble();
		double y = s.nextDouble();
		double angle = s.nextDouble();
		initialAngle = Math.toRadians(angle);
		defaultLinearSpeed = s.nextDouble();
		defaultRotationSpeed = s.nextDouble();
		double additionalTime = 0;
		if (s.hasNextDouble()) {
			additionalTime = s.nextDouble();
		}

		Double p = new Point2D.Double(x, y);
		KeyFrame frame = new KeyFrame(TrajectoryMovement.START, 0, initialAngle, 0, p, 0);
		frames.add(frame);

		addAdditionalTime(p, initialAngle, additionalTime);

		lastx = x;
		lasty = y;
		lastAngle = initialAngle;
	}

	@Override
	public void parseLine(String line) throws ParseException {
		Scanner s = new Scanner(line);
		try {
			s.useDelimiter(delimiter);

			if (!s.hasNext()) {
				return;
			}

			String type = s.next();
			char c = type.charAt(0);
			switch (c) {
			case '/':
			case '#':
				return;
			case 'i':
				parseInitialPosition(s);
				break;
			case 's':
				parseSegment(s);
				break;
			case 'b':
				parseBezier(s);
				break;
			default:
				throw new ParseException("unexpected value: " + type, 0);
			}
		} finally {
			s.close();
		}
	}

	private void parseSegment(Scanner s) throws ParseException {
		double x = s.nextDouble();
		double y = s.nextDouble();
		CommonData data = parseCommon(s);

		double dx = x - lastx;
		double dy = y - lasty;
		double angle = Math.atan2(dy, dx);

		Point2D p = new Point2D.Double(x, y);

		// initial position
		if (frames.isEmpty()) {
			throw new ParseException("initial position has not been defined", 0);
		}

		double distance = p.distance(lastx, lasty);
		double theta = Math.abs(Angle.getRotationAngle(lastAngle, angle));

		// rotation
		if (theta > MIN_ANGLE && distance > MIN_DISTANCE) {
			timestamp += getRotationDuration(theta, data.rotationSpeed);
			KeyFrame frame = new KeyFrame(TrajectoryMovement.ROTATION, 0, angle, data.rotationSpeed, new Point2D.Double(lastx, lasty), timestamp);
			frames.add(frame);
		}

		// straight line
		timestamp += distance / data.linearSpeed;
		KeyFrame frame = new KeyFrame(TrajectoryMovement.LINE, data.linearSpeed, angle, 0, p, timestamp);
		frames.add(frame);

		// pause
		addAdditionalTime(p, angle, data.additionalTime);

		lastx = x;
		lasty = y;
		lastAngle = angle;
	}

	private double getRotationDuration(double angle, double speed) {
		double duration = angle / (2.0 * Math.PI) / speed;
		return duration;
	}
}
