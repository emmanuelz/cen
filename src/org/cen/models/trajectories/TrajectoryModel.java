package org.cen.models.trajectories;

import java.util.ArrayList;
import java.util.List;

import org.cen.models.IModel;

public class TrajectoryModel implements IModel {
	private List<TrajectoryElementModel> elements;
	private String name;

	public TrajectoryModel() {
		this("undefined");
	}

	public TrajectoryModel(String name) {
		this(name, new ArrayList<TrajectoryElementModel>());
	}

	public TrajectoryModel(String name, List<TrajectoryElementModel> elements) {
		super();
		this.name = name;
		this.elements = elements;
	}

	public List<TrajectoryElementModel> getElements() {
		return elements;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}
}
