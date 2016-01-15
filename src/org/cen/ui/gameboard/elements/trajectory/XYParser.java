package org.cen.ui.gameboard.elements.trajectory;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.cen.math.Angle;

public class XYParser extends AbstractTrajectoryParser {
	private class CommonData {
		double additionalTime = 0;
		double linearSpeed = defaultLinearSpeed;
		double rotationSpeed = defaultRotationSpeed;
	}

	private static final String KEY_ORIENTATION = "orientation";

	private static final Object KEY_PAUSE = "pause";

	private static final double MIN_ANGLE = Math.toRadians(0.5);
	private static final double MIN_DISTANCE = 0.1;
	private double angleScale = 1;
	private double angleTranslation = 0;
	private double defaultLinearSpeed = 0.1;
	private double defaultRotationSpeed = 0.1;
	private String delimiter;
	private double finalAngle = 0;
	private ArrayList<KeyFrame> frames = new ArrayList<>();
	private double initialAngle = 0;
	private double lastAngle = 0;
	private double lastx = 0;
	private double lasty = 0;
	private int sourceLine = 0;
	private double timestamp = 0;
	private double xScale = 1;
	private double xTranslation = 0;
	private double yScale = 1;
	private double yTranslation = 0;

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
			KeyFrame frame = new KeyFrame(TrajectoryMovement.NONE, 0, angle, 0, 0, position, timestamp, sourceLine);
			frames.add(frame);
		}
	}

	@Override
	public ITrajectoryPath getPath(String name) {
		ITrajectoryPath path = new StraightLine(name, initialAngle, finalAngle, frames);
		return path;
	}

	private double getRotationDuration(double angle, double speed) {
		double duration = angle / (2.0 * Math.PI) / speed;
		return duration;
	}

	private void handleXYCoordinates(double x, double y, CommonData data) throws ParseException {
		double dx = x - lastx;
		double dy = y - lasty;
		double angle = Math.atan2(dy, dx);

		// if linear speed is negative, move backward
		if (data.linearSpeed < 0) {
			angle += Math.PI;
		}

		Point2D p = new Point2D.Double(x, y);

		// initial position
		if (frames.isEmpty()) {
			throw new ParseException("initial position has not been defined", 0);
		}

		double distance = p.distance(lastx, lasty);
		if (distance == 0) {
			return;
		}
		double theta = Math.abs(Angle.getRotationAngle(lastAngle, angle));

		// rotation
		if (theta > MIN_ANGLE && distance > MIN_DISTANCE) {
			timestamp += getRotationDuration(theta, data.rotationSpeed);
			KeyFrame frame = new KeyFrame(TrajectoryMovement.ROTATION, 0, angle, 0, data.rotationSpeed, new Point2D.Double(lastx, lasty), timestamp, sourceLine);
			frames.add(frame);
		}

		// straight line
		timestamp += distance / Math.abs(data.linearSpeed);
		KeyFrame frame = new KeyFrame(TrajectoryMovement.LINE, data.linearSpeed, angle, 0, 0, p, timestamp, sourceLine);
		frames.add(frame);

		// pause
		addAdditionalTime(p, angle, data.additionalTime);

		lastx = x;
		lasty = y;
		lastAngle = angle;
	}

	private void parseBezier(Scanner s) {
		// final position
		double x = readX(s);
		double y = readY(s);
		// control point 1
		double cx1 = readX(s);
		double cy1 = readY(s);
		// control point 2
		double cx2 = readX(s);
		double cy2 = readY(s);
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
			Point2D last = new Point2D.Double(lastx, lasty);
			KeyFrame frame = new KeyFrame(TrajectoryMovement.ROTATION, 0, startAngle, 0, data.rotationSpeed, last, timestamp, sourceLine);
			frames.add(frame);
		}

		double distante = p.distance(lastx, lasty);
		timestamp += distante / data.linearSpeed;
		KeyFrame frame = new KeyFrame(TrajectoryMovement.BEZIER, data.linearSpeed, endAngle, 0, data.rotationSpeed, p, timestamp, sourceLine, cp1, cp2);
		frames.add(frame);
	}

	private void parseComment(String line) {
		int index = frames.size() - 1;
		if (index < 0) {
			return;
		}
		KeyFrame frame = frames.get(index);
		line = line.substring(2);
		frame.addComment(line);
		parseCommentContent(line, frame);
	}

	private void parseCommentContent(String line, KeyFrame frame) {
		Pattern p = Pattern.compile("FCM_envoyer_ordre_NXT\\(([0-9]+)\\);");
		Matcher m = p.matcher(line);
		if (m.matches()) {
			String s = m.group(1);
			int id = Integer.parseInt(s);
			int delay = 0;
			switch (id) {
			case 1:
			case 10:
			case 13:
				delay = 200;
				break;
			case 2:
				delay = 600;
				break;
			case 3:
			case 9:
				delay = 50;
				break;
			case 4:
				delay = 800;
				break;
			case 5:
			case 6:
			case 7:
			case 8:
				delay = 300;
				break;
			case 12:
				delay = 6000;
				break;
			}
			while (delay > 255) {
				frame.addComment("delay_ms(255);");
				delay -= 255;
			}
			if (delay > 0) {
				frame.addComment(String.format("delay_ms(%d);", delay));
			}
			timestamp += 1e-3 * (delay + 300);
		}
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

		double x = readX(s);
		double y = readY(s);
		initialAngle = readAngle(s);
		defaultLinearSpeed = s.nextDouble();
		defaultRotationSpeed = s.nextDouble();
		timestamp = s.nextDouble();
		double additionalTime = 0;
		if (s.hasNextDouble()) {
			additionalTime = s.nextDouble();
		}

		Double p = new Point2D.Double(x, y);
		KeyFrame frame = new KeyFrame(TrajectoryMovement.START, 0, initialAngle, 0, 0, p, timestamp, sourceLine);
		frames.add(frame);

		addAdditionalTime(p, initialAngle, additionalTime);

		lastx = x;
		lasty = y;
		lastAngle = initialAngle;
	}

	@Override
	public void parseLine(String line) throws ParseException {
		sourceLine++;
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
				break;
			case 'c':
				parseComment(line);
				break;
			case 'i':
				parseInitialPosition(s);
				break;
			case 's':
				parseSegment(s);
				break;
			case 'b':
				parseBezier(s);
				break;
			case 't':
				parseTransform(s);
				break;
			case 'r':
				parseRotation(s);
				break;
			case 'd':
				parseStraightDistance(s);
				break;
			case 'o':
				parseOrientation(s);
				break;
			case 'p':
				parsePause(s);
				break;
			case 'a':
				parseActuator(s);
				break;
			default:
				throw new ParseException("unexpected value: " + type, 0);
			}
		} finally {
			s.close();
		}
	}

	private void parseActuator(Scanner s) {
		// TODO
	}

	private void parseOrientation(Scanner s) {
		double finalAngle = readAngle(s);
		double minAngle = Math.toRadians(s.nextDouble());
		double signum = Math.signum(minAngle);
		double theta = Angle.getRotationAngle(lastAngle, finalAngle);
		if (Math.abs(theta) < Math.abs(minAngle)) {
			theta += Angle.PI2 * signum;
		}
		if (Math.signum(theta) != signum) {
			theta += 2 * Angle.PI2 * signum;
		}

		double opposite = Angle.getRotationAngle(-lastAngle, -finalAngle);
		if (Math.abs(opposite) < Math.abs(minAngle)) {
			opposite += Angle.PI2 * signum;
		}
		if (Math.signum(opposite) != signum) {
			opposite += 2 * Angle.PI2 * signum;
		}

		CommonData data = parseCommon(s);

		double angle = lastAngle + theta;

		Point2D p = new Point2D.Double(lastx, lasty);
		timestamp += getRotationDuration(Math.abs(theta), data.rotationSpeed);
		KeyFrame frame = new KeyFrame(TrajectoryMovement.ROTATION, 0, angle, theta, data.rotationSpeed, p, timestamp, sourceLine);
		NumberFormat format = NumberFormat.getNumberInstance(Locale.ROOT);
		frame.addComment(String.format("%s=%s", KEY_ORIENTATION, format.format(opposite)));
		frames.add(frame);

		addAdditionalTime(p, angle, data.additionalTime);

		lastAngle = angle;// % Angle.PIx2;
	}

	private void parsePause(Scanner s) {
		double duration = s.nextDouble();
		Point2D p = new Double(lastx, lasty);
		timestamp += duration;
		KeyFrame frame = new KeyFrame(TrajectoryMovement.NONE, 0, lastAngle, 0, 0, p, timestamp, sourceLine);
		frame.addComment(String.format("%s=%.0f", KEY_PAUSE, duration * 1000));
		frames.add(frame);
	}

	private void parseRotation(Scanner s) {
		double theta = Math.toRadians(s.nextDouble()) * angleScale;
		CommonData data = parseCommon(s);

		double angle = lastAngle + theta;

		Point2D p = new Point2D.Double(lastx, lasty);
		timestamp += getRotationDuration(Math.abs(theta), data.rotationSpeed);
		KeyFrame frame = new KeyFrame(TrajectoryMovement.ROTATION, 0, angle, theta, data.rotationSpeed, p, timestamp, sourceLine);
		frames.add(frame);

		addAdditionalTime(p, angle, data.additionalTime);

		lastAngle = angle;// % Angle.PIx2;
	}

	private void parseSegment(Scanner s) throws ParseException {
		double x = readX(s);
		double y = readY(s);
		CommonData data = parseCommon(s);

		handleXYCoordinates(x, y, data);
	}

	private void parseStraightDistance(Scanner s) throws ParseException {
		double distance = s.nextDouble();
		CommonData data = parseCommon(s);

		double angle = lastAngle;
		double x = lastx + distance * Math.cos(angle);
		double y = lasty + distance * Math.sin(angle);

		data.linearSpeed = Math.copySign(data.linearSpeed, Math.signum(distance));

		handleXYCoordinates(x, y, data);
	}

	private void parseTransform(Scanner s) {
		xScale = s.nextDouble();
		xTranslation = s.nextDouble();
		yScale = s.nextDouble();
		yTranslation = s.nextDouble();
		angleScale = s.nextDouble();
		angleTranslation = Math.toRadians(s.nextDouble());
	}

	private double readAngle(Scanner s) {
		double angle = Math.toRadians(s.nextDouble());
		angle = transformCoordinate(angle, angleScale, angleTranslation);
		return angle;
	}

	private double readCoordinate(Scanner s, double scale, double translation) {
		double c = s.nextDouble();
		c = transformCoordinate(c, scale, translation);
		return c;
	}

	private double readX(Scanner s) {
		return readCoordinate(s, xScale, xTranslation);
	}

	private double readY(Scanner s) {
		return readCoordinate(s, yScale, yTranslation);
	}

	private double transformCoordinate(double value, double scale, double translation) {
		value *= scale;
		value += translation;
		return value;
	}
}
