package org.cen.models;

import java.util.ArrayList;
import java.util.List;

public class MatchModel implements IModel {
	private String name;
	private List<RobotTrajectoryModel> robots;

	public MatchModel() {
		this("undefined");
	}

	public MatchModel(String name) {
		this(name, new ArrayList<RobotTrajectoryModel>());
	}

	public MatchModel(String name, List<RobotTrajectoryModel> robots) {
		super();
		this.name = name;
		this.robots = robots;
	}

	public String getName() {
		return name;
	}

	public List<RobotTrajectoryModel> getRobots() {
		return robots;
	}
}
