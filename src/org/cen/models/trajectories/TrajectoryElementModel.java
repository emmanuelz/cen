package org.cen.models.trajectories;

import org.cen.models.IModel;

public class TrajectoryElementModel implements IModel {
	private String name;

	public TrajectoryElementModel() {
		this("undefined");
	}

	public TrajectoryElementModel(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
