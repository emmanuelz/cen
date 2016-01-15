package org.cen.models;

import java.util.HashMap;
import java.util.Map;

import org.cen.models.actuators.ActuatorModel;

public class RobotModel implements IModel {
	private Map<String, ActuatorModel> actuators;
	private String name;

	public RobotModel() {
		this("undefined", new HashMap<String, ActuatorModel>());
	}

	public RobotModel(String name, Map<String, ActuatorModel> actuators) {
		super();
		this.name = name;
		this.actuators = actuators;
	}

	public void addActuator(ActuatorModel actuator) {
		String name = actuator.getName();
		actuators.put(name, actuator);
	}

	public Map<String, ActuatorModel> getActuators() {
		return actuators;
	}

	public String getName() {
		return name;
	}
}
