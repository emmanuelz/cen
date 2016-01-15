package org.cen.models.actuators;

import org.cen.models.IModel;
import org.cen.trajectories.planner.OrientedPosition;

public class ActuatorStateModel implements IModel {
	@Override
	public String toString() {
		return name;
	}

	private String name;
	private OrientedPosition position;

	public ActuatorStateModel() {
		this("undefined", new OrientedPosition(0, 0, 0));
	}

	public ActuatorStateModel(String name, OrientedPosition position) {
		super();
		this.name = name;
		this.position = position;
	}

	public String getName() {
		return name;
	}

	public OrientedPosition getPosition() {
		return position;
	}
}
