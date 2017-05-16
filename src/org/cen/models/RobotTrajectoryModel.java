package org.cen.models;

import java.util.HashMap;
import java.util.Map;

import org.cen.models.trajectories.TrajectoryModel;

public class RobotTrajectoryModel implements IModel {
	private RobotModel robot;
	private String name;
	private Map<String, TrajectoryModel> trajectories;

	public RobotTrajectoryModel() {
		this(new RobotModel());
	}

	public RobotTrajectoryModel(RobotModel model) {
		this(model, new HashMap<String, TrajectoryModel>());
	}

	public RobotTrajectoryModel(RobotModel robot, Map<String, TrajectoryModel> trajectories) {
		super();
		this.robot = robot;
		this.name = robot.getName();
		this.trajectories = trajectories;
	}

	@Override
	public String getName() {
		return name;
	}

	public RobotModel getRobot() {
		return robot;
	}

	public Map<String, TrajectoryModel> getTrajectories() {
		return trajectories;
	}

	public void setRobot(RobotModel robot) {
		this.robot = robot;
		this.name = robot.getName();
	}
}
