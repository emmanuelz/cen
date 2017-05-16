package org.cen.trajectories;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.cen.models.RobotModel;
import org.cen.models.trajectories.TrajectoryElementModel;
import org.cen.models.trajectories.TrajectoryModel;
import org.cen.ui.gameboard.elements.trajectory.AbstractTrajectoryFactory;
import org.cen.ui.gameboard.elements.trajectory.ITrajectoryPath;
import org.cen.ui.gameboard.elements.trajectory.RobotTrajectoryPath;
import org.cen.ui.gameboard.elements.trajectory.TextTrajectoryFactory;
import org.cen.ui.gameboard.elements.trajectory.XYParser;

public class TrajectoryFactory {
	private static final String FIELD_DELIMITER = ";";
	private RobotModel robot;

	public TrajectoryFactory(RobotModel robot) {
		super();
		this.robot = robot;
	}

	public RobotTrajectoryPath build(TrajectoryModel trajectory) throws FileNotFoundException {
		List<TrajectoryElementModel> elements = trajectory.getElements();
		List<ITrajectoryPath> paths = new ArrayList<>();
		for (TrajectoryElementModel element : elements) {
			ITrajectoryPath path = buildPart(element);
			paths.add(path);
		}
		String name = robot.getName();
		RobotTrajectoryPath result = new RobotTrajectoryPath(name, paths);
		return result;
	}

	private ITrajectoryPath buildPart(TrajectoryElementModel element) throws FileNotFoundException {
		String name = element.getName();
		AbstractTrajectoryFactory factory = getFactory(name);
		String filePath = name;
		ITrajectoryPath part = factory.loadFromFile(filePath);
		return part;
	}

	private AbstractTrajectoryFactory getFactory(String name) {
		XYParser parser = new XYParser(FIELD_DELIMITER);
		TextTrajectoryFactory factory = new TextTrajectoryFactory(parser);
		return factory;
	}
}
