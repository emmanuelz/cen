package org.cen.util;

import java.awt.geom.Point2D;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Reverse {
	private FileInputStream stream;

	private static final double STEPS_BY_MM = 76.08 / 8.0;

	private static final double STEPS_BY_RADIAN = 44682.0 / Math.PI / 4.0;

	public Reverse(String path) throws FileNotFoundException {
		super();
		stream = new FileInputStream(path);
	}

	@Override
	protected void finalize() throws Throwable {
		if (stream != null) {
			stream.close();
			stream = null;
		}
		super.finalize();
	}

	public static void main(String[] args) {
		try {
			new Reverse("test.txt").run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void run() throws IOException {
		Point2D position = new Point2D.Double();
		double orientation = Math.toRadians(90);

		Point2D lastPosition = position;
		double lastOrientation = orientation;
		double pause = 0;

		StringBuilder output = new StringBuilder();

		try (Scanner scanner = new Scanner(stream, "ISO8859-1")) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				addComment(output, line);

				if (line.isEmpty()) {
					continue;
				} else if (line.startsWith("delay_ms")) {
					double duration = readNumeric(line);
					pause += duration;
					continue;
				} else if (line.startsWith("FCM_XCall_avancer_mm")) {
					double distance = readNumeric(line);
					position = computeNewPosition(position, orientation, distance);
				} else if (line.startsWith("FCM_XCall_tourner_deci_degree")) {
					double angle = readNumeric(line);
					angle = Math.toRadians(angle * 0.1);
					orientation = computeNewOrientation(orientation, angle);
				} else if (line.startsWith("FCM_XCall_avancer_pas")) {
					double distance = readNumeric(line);
					distance /= STEPS_BY_MM;
					position = computeNewPosition(position, orientation, distance);
				} else if (line.startsWith("FCM_XCall_tourner_pas")) {
					double angle = readNumeric(line);
					angle /= STEPS_BY_RADIAN;
					orientation = computeNewOrientation(orientation, angle);
				} else if (line.startsWith("// start at ")) {
					position = getInitialPosition(line);
					addInitialPosition(output, position, orientation);
					lastPosition = position;
				} else if (line.startsWith("FCM_XCall_envoyer_ordre_NXT")) {
					double value = readNumeric(line);
					addNXTCommand(output, (int) value);
				}
				if (pause > 0) {
					addPause(output, (int) pause);
					pause = 0;
				}
				if (lastPosition.distance(position) > 1) {
					addLocation(output, position, orientation);
					lastPosition = position;
				}
				if (Math.abs(orientation - lastOrientation) > Math.toRadians(0.5)) {
					addOrientation(output, lastOrientation, orientation);
					lastOrientation = orientation;
				}
			}
		}
		try (FileWriter fw = new FileWriter("reversed.txt")) {
			String s = output.toString();
			fw.write(s);
		}
		System.out.println(output.toString());
	}

	private void addPause(StringBuilder output, int value) {
		output.append("p;");
		output.append(value);
		output.append("\r\n");
	}

	private void addNXTCommand(StringBuilder output, int value) {
		output.append("x;");
		output.append(value);
		output.append("\r\n");
	}

	private void addInitialPosition(StringBuilder output, Point2D position, double orientation) {
		double x = position.getX();
		double y = position.getY();
		double o = Math.toDegrees(orientation);
		output.append("i;");
		output.append((int) x);
		output.append(";");
		output.append((int) y);
		output.append(";");
		output.append((int) o);
		output.append(";100;0.1;0\r\n");
	}

	private void addComment(StringBuilder output, String line) {
		if (!line.startsWith("//") && !line.isEmpty()) {
			output.append("// command: ");
		}
		output.append(line);
		output.append("\r\n");
	}

	private Point2D getInitialPosition(String line) {
		try (Scanner s = new Scanner(line)) {
			String value = s.findInLine("[0-9]+(\\.[0-9]+)?,");
			value = value.substring(0, value.length() - 1);
			double x = Double.parseDouble(value);
			value = s.findInLine("[0-9]+(\\.[0-9]+)?");
			double y = Double.parseDouble(value);
			Point2D result = new Point2D.Double(x, y);
			return result;
		}
	}

	private void addOrientation(StringBuilder output, double lastOrientation, double orientation) {
		// double angle = Angle.getRotationAngle(lastOrientation, orientation);
		double angle = orientation - lastOrientation;
		angle = Math.toDegrees(angle);
		long a = Math.round(angle);
		output.append("r;");
		output.append(a);
		output.append("\r\n");
	}

	private void addLocation(StringBuilder output, Point2D position, double orientation) {
		double x = position.getX();
		double y = position.getY();
		output.append("s;");
		output.append((int) x);
		output.append(';');
		output.append((int) y);
		output.append("\r\n");
	}

	private double computeNewOrientation(double orientation, double angle) {
		orientation += angle;
		return orientation;
	}

	private Point2D computeNewPosition(Point2D position, double orientation, double distance) {
		double dx = distance * Math.cos(orientation);
		double dy = distance * Math.sin(orientation);
		double x = position.getX() + dx;
		double y = position.getY() + dy;
		return new Point2D.Double(x, y);
	}

	private double readNumeric(String line) {
		try (Scanner s = new Scanner(line)) {
			String value = s.findInLine("\\-?[0-9]+(\\.[0-9]*)?");
			double d = Double.parseDouble(value);
			return d;
		}
	}
}
