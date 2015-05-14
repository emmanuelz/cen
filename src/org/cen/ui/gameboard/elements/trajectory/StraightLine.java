package org.cen.ui.gameboard.elements.trajectory;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.cen.math.Angle;

public class StraightLine extends AbstractTrajectoryPath {
	private static final String KEY_ANGLE = "angle";
	private static final String KEY_PRECOMMENTS = "preComments";
	private static final String KEY_POSTCOMMENTS = "postComments";
	private static final String KEY_DELAY = "delay";
	private static final String KEY_DISTANCE = "distance";
	private static final String KEY_NXT = "nxt";
	private static final String KEY_ORIENTATION = "orientation";

	private ArrayList<KeyFrame> frames;

	private KeyFrame keyFrame;

	private Path2D path;

	public StraightLine(String name, double initialAngle, double finalAngle, ArrayList<KeyFrame> frames) {
		super(name, new Point2D.Double(), null, null, initialAngle, finalAngle);
		this.frames = frames;
		order = 1000;

		int n = frames.size();
		start = frames.get(0).getPosition();
		end = frames.get(n - 1).getPosition();

		boolean forward = true;

		path = new Path2D.Double();
		path.moveTo(start.getX(), start.getY());
		for (int i = 1; i < n; i++) {
			KeyFrame frame = frames.get(i);
			TrajectoryMovement movement = frame.getMovement();
			switch (movement) {
			case LINE:
				// handles backward moves by adding an extra moveto instruction
				if (forward == frame.getMovementSpeed() < 0) {
					Point2D p = path.getCurrentPoint();
					path.moveTo(p.getX(), p.getY());
					forward = !forward;
				}
				Point2D p = frame.getPosition();
				path.lineTo(p.getX(), p.getY());
				break;
			case BEZIER:
				p = frame.getPosition();
				Point2D[] cp = frame.getControlPoints();
				path.curveTo(cp[0].getX(), cp[0].getY(), cp[1].getX(), cp[1].getY(), p.getX(), p.getY());
				break;
			default:
				break;
			}
		}

		String description = getTrajectoryDescription();
		setProperty(KEY_DESCRIPTION, description);
	}

	private void addComments(Map<String, String> params, String values, boolean preComments) {
		String c;
		if (preComments) {
			c = params.get(KEY_PRECOMMENTS);
		} else {
			c = params.get(KEY_POSTCOMMENTS);
		}

		if (c != null) {
			c += values;
		} else {
			c = values;
		}
		c += "\r\n";

		if (preComments) {
			params.put(KEY_PRECOMMENTS, c);
		} else {
			params.put(KEY_POSTCOMMENTS, c);
		}
	}

	private void addParameters(Map<String, String> params, String values) {
		if (values.startsWith("//")) {
			addComments(params, values, false);
			return;
		}

		Pattern p = Pattern.compile("((\\w+)=([\\w0-9\\-,\\.]+))*");
		Matcher m = p.matcher(values);
		boolean found = false;
		while (m.find()) {
			String key = m.group(2);
			if (key == null) {
				continue;
			}
			String value = m.group(3);
			params.put(key, value);
			found = true;
		}

		if (!found) {
			addComments(params, values, false);
		}
	}

	private void addValue(Map<String, String> params, String key, double d) {
		params.put(key, String.format("%.0f", d));
	}

	private void clear(Map<String, String> params, String key) {
		params.remove(key);
	}

	private KeyFrame getKeyFrame(double timestamp) {
		if (keyFrame == null || keyFrame.getTimestamp() != timestamp) {
			KeyFrameInterpolator interpolator = new KeyFrameInterpolator();
			keyFrame = interpolator.interpolate(frames, timestamp);
		}
		return keyFrame;
	}

	@Override
	public double getOrientation() {
		return 0;
	}

	@Override
	public double getOrientation(double timestamp) {
		KeyFrame frame = getKeyFrame(timestamp);
		return frame.getOrientation();
	}

	@Override
	public Shape getPath() {
		return path;
	}

	@Override
	public Point2D getPosition(double timestamp) {
		KeyFrame frame = getKeyFrame(timestamp);
		return frame.getPosition();
	}

	@Override
	public Point2D[] getTrajectoryControlPoints() {
		return null;
	}

	@Override
	public String getTrajectoryDescription() {
		StringBuilder sb = new StringBuilder();
		Point2D last = null;
		double lastOrientation = 0;
		double lastTimestamp = 0;
		Map<String, String> params = new HashMap<String, String>();
		for (KeyFrame frame : frames) {
			if (frame.hasComments()) {
				ArrayList<String> comments = frame.getComments();
				for (String s : comments) {
					addParameters(params, s);
				}
			}

			TrajectoryMovement movement = frame.getMovement();
			switch (movement) {
			case START:
				last = frame.getPosition();
				lastOrientation = frame.getOrientation();
				sb.append(String.format("// start at %s\r\n", last.toString()));
				break;
			case BEZIER:
				break;
			case CLOTHOID:
				break;
			case LINE:
				Point2D p = frame.getPosition();
				double distance = p.distance(last);
				double speed = frame.getMovementSpeed();
				String direction = speed > 0 ? "forward" : "backward";
				addComments(params, String.format("// move %s of %.0f mm (%.0f)", direction, distance, 9.557 * distance * Math.signum(speed)), true);
				addValue(params, KEY_DISTANCE, 9.557 * distance * Math.signum(speed));
				last = p;
				break;
			case NONE:
				double delay = frame.getTimestamp() - lastTimestamp;
				addComments(params, String.format("delay_ms(%.0f);", delay * 1000), true);
				break;
			case ROTATION:
				double o = frame.getOrientation();
				double angle = Angle.getRotationAngle(lastOrientation, o);
				if (frame.useRelativeAngle()) {
					// the angle is a relative angle that can be > 180°
					angle = o - lastOrientation;
				}
				angle = Math.toDegrees(angle);
				addComments(params, String.format("// rotation of %.0f° (%.0f)", angle, 22527.5d / 360d * angle), true);
				addValue(params, KEY_ANGLE, 22527.5d / 360d * angle);
				if (params.containsKey(KEY_ORIENTATION)) {
					double opposite = getDoubleValue(params, KEY_ORIENTATION);
					opposite = Math.toDegrees(opposite);
					addValue(params, KEY_ORIENTATION, 22527.5d / 360d * opposite);
				}
				lastOrientation = o;
				break;
			default:
				break;
			}
			lastTimestamp = frame.getTimestamp();
			writeCommands(sb, params);
		}
		return sb.toString();
	}

	private double getDoubleValue(Map<String, String> params, String key) {
		String value = params.get(key);
		double d = Double.parseDouble(value);
		return d;
	}

	@Override
	public void paint(Graphics2D g, double timestamp) {
		super.paint(g);

		Point2D p = getPosition(timestamp);
		double x = p.getX() - position.getX();
		double y = p.getY() - position.getY();
		g.translate(x, y);

		double angle = getOrientation(timestamp) - orientation;
		g.rotate(angle);

		paintGauge(g);
	}

	@Override
	public void paintUnscaled(Graphics2D g) {
		paintUnscaled(g, 0d);
	}

	@Override
	public void paintUnscaled(Graphics2D g, double timestamp) {
		if (timestamp == 0d) {
			super.paintUnscaled(g);
		}
	}

	private void writeCommands(StringBuilder sb, Map<String, String> params) {
		String comments = params.get(KEY_PRECOMMENTS);
		if (comments != null) {
			sb.append(comments);
			clear(params, KEY_PRECOMMENTS);
		}

		if (params.containsKey(KEY_ORIENTATION)) {
			String v = params.get(KEY_ANGLE);
			String o = params.get(KEY_ORIENTATION);
			addComments(params, String.format("FCM_tourner_sens_couleur(%s, %s);", v, o), true);
			clear(params, KEY_ANGLE);
			clear(params, KEY_ORIENTATION);
		} else if (params.containsKey(KEY_ANGLE)) {
			String v = params.get(KEY_ANGLE);
			sb.append(String.format("FCM_tourner(%s);\r\n", v));
			clear(params, KEY_ANGLE);
		} else if (params.containsKey(KEY_DISTANCE)) {
			String distance = params.get(KEY_DISTANCE);
			if (params.containsKey(KEY_NXT)) {
				String cmd = params.get(KEY_NXT);
				String delay = params.get(KEY_DELAY);
				if (delay == null) {
					delay = "0";
				}
				sb.append(String.format("FCM_avancer_et_envoyer_ordre(%s, %s, %s);\r\n", distance, cmd, delay));
			} else {
				sb.append(String.format("FCM_avancer(%s);\r\n", distance));
			}
			clear(params, KEY_DISTANCE);
			clear(params, KEY_NXT);
			clear(params, KEY_DELAY);
		}

		comments = params.get(KEY_POSTCOMMENTS);
		if (comments != null) {
			sb.append(comments);
			clear(params, KEY_POSTCOMMENTS);
		}
	}
}
