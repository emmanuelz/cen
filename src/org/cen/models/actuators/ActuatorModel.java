package org.cen.models.actuators;

import java.util.HashMap;
import java.util.Map;

import org.cen.models.IModel;

public class ActuatorModel implements IModel {
	private String name;
	private Map<String, ActuatorStateModel> states;

	public ActuatorModel() {
		this("undefined", new HashMap<String, ActuatorStateModel>());
	}

	public ActuatorModel(String name) {
		this(name, new HashMap<String, ActuatorStateModel>());
	}

	public ActuatorModel(String name, Map<String, ActuatorStateModel> states) {
		super();
		this.name = name;
		this.states = states;
	}

	public void addState(ActuatorStateModel state) {
		String name = state.getName();
		states.put(name, state);
	}

	public String getName() {
		return name;
	}

	public ActuatorStateModel getState(String name) {
		return states.get(name);
	}

	public Map<String, ActuatorStateModel> getStates() {
		return states;
	}

	@Override
	public String toString() {
		return name;
	}
}
