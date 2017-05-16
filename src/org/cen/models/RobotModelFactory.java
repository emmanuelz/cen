package org.cen.models;

import java.io.File;
import java.io.FileInputStream;

import org.cen.models.actuators.ActuatorStateModel;
import org.cen.persistance.FileLocations;
import org.cen.persistance.JSONMapper;
import org.cen.ui.gameboard.elements.trajectory.Gauge;
import org.cen.ui.gameboard.elements.trajectory.GaugeFactory;
import org.cen.ui.gameboard.elements.trajectory.IGauge;

public class RobotModelFactory {
	public RobotModel loadModel(String name) throws Exception {
		String path = FileLocations.getPath(FileLocations.DATA, FileLocations.ROBOTS);
		RobotModel model = JSONMapper.loadObject(path, name, RobotModel.class);
		return model;
	}

	private IGauge loadGauge(String name) {
		GaugeFactory factory = new GaugeFactory();
		File file = new File(FileLocations.GAUGES, name);
		try (FileInputStream fis = new FileInputStream(file)) {
			Gauge gauge = factory.getGauge(fis);
			return gauge;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	public IGauge getGauge(RobotModel model) {
		String name = model.getName();
		IGauge gauge = loadGauge(name);
		return gauge;
	}

	public IGauge getActuatorGauge(ActuatorStateModel model) {
		String name = model.getName();
		IGauge gauge = loadGauge(name);
		return gauge;
	}
}
