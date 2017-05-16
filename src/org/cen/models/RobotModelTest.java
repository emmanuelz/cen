package org.cen.models;

import java.awt.geom.Point2D;

import org.cen.models.actuators.ActuatorModel;
import org.cen.models.actuators.ActuatorStateModel;
import org.cen.persistance.OrientedPositionMixIn;
import org.cen.trajectories.planner.OrientedPosition;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RobotModelTest {
	public static void main(String[] args) {
		ActuatorStateModel state = new ActuatorStateModel("default", new OrientedPosition(new Point2D.Double(0, 0), 0));
		ActuatorModel actuator = new ActuatorModel("engine");
		actuator.addState(state);
		RobotModel model = new RobotModel();
		model.addActuator(actuator);

		ObjectMapper mapper = new ObjectMapper();
		mapper.addMixIn(OrientedPosition.class, OrientedPositionMixIn.class);
		try {
			String json = mapper.writeValueAsString(model);
			System.out.println(json);
			model = mapper.readValue(json, RobotModel.class);
			json = mapper.writeValueAsString(model);
			System.out.println(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
