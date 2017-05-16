package org.cen.models;

import java.util.List;

import org.cen.models.trajectories.TrajectoryElementModel;

public class ModelFactory {
	public IModel load(ModelType type, String name) throws Exception {
		IModel result;
		switch (type) {
		case MATCH:
			result = loadMatch(name);
			break;
		case ROBOT:
			result = loadRobot(name);
			break;
		case TRAJECTORY_ELEMENT:
			result = loadTrajectoryElement(name);
			break;
		default:
			result = null;
			break;
		}
		return result;
	}

	private MatchModel loadMatch(String name) throws Exception {
		MatchModelFactory factory = new MatchModelFactory();
		MatchModel model = factory.loadModel(name);
		loadRobots(model);
		return model;
	}

	private RobotModel loadRobot(String name) throws Exception {
		RobotModelFactory factory = new RobotModelFactory();
		RobotModel model = factory.loadModel(name);
		return model;
	}

	private void loadRobots(MatchModel model) throws Exception {
		List<RobotTrajectoryModel> robots = model.getRobots();
		for (RobotTrajectoryModel robot : robots) {
			String name = robot.getName();
			RobotModel robotModel = loadRobot(name);
			robot.setRobot(robotModel);
		}
	}

	private IModel loadTrajectoryElement(String name) {
		TrajectoryElementModel model = new TrajectoryElementModel(name);
		return model;
	}

	public void save(IModel model) throws Exception {
		if (model instanceof MatchModel) {
			saveMatch((MatchModel) model);
		}
	}

	private void saveMatch(MatchModel model) throws Exception {
		MatchModelFactory factory = new MatchModelFactory();
		factory.saveModel(model);
	}
}
