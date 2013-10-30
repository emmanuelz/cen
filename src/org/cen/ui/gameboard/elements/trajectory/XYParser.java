package org.cen.ui.gameboard.elements.trajectory;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.Scanner;


public class XYParser extends AbstractTrajectoryParser {
	private String delimiter;
	private ArrayList<Point2D> points = new ArrayList<Point2D>();

	public XYParser() {
		this(";");
	}

	public XYParser(String delimiter) {
		super();
		this.delimiter = delimiter;
	}

	@Override
	public void parseLine(String line) {
		Scanner s = new Scanner(line);
		try {
			s.useDelimiter(delimiter);
			double x = s.nextDouble();
			double y = s.nextDouble();
			addPoint(x, y);
		} finally {
			s.close();
		}
	}

	private void addPoint(double x, double y) {
		Double p = new Point2D.Double(x, y);
		points.add(p);
	}

	@Override
	public ITrajectoryPath getPath() {
		int n = points.size();
		Point2D[] array = points.toArray(new Point2D[n]);
		ITrajectoryPath path = new StraightLine("path", 0, 0, array);
		return path;
	}
}
