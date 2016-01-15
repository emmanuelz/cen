package org.cen.persistance;

import java.io.File;

import org.cen.models.RobotTrajectoryModel;
import org.cen.trajectories.planner.OrientedPosition;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONMapper {
	private static ObjectMapper mapper;

	private static ObjectMapper getMapper() {
		if (mapper == null) {
			mapper = new ObjectMapper();
			mapper.addMixIn(OrientedPosition.class, OrientedPositionMixIn.class);
			mapper.addMixIn(RobotTrajectoryModel.class, RobotTrajectoryModelMixIn.class);
		}
		return mapper;
	}

	public static <T extends Object> T loadObject(String path, String name, Class<T> type) throws Exception {
		ObjectMapper mapper = getMapper();
		try {
			File file = new File(path, name);
			T model = mapper.readValue(file, type);
			return model;
		} catch (Exception e) {
			throw new Exception("unable to load model " + name, e);
		}
	}

	public static void saveObject(String path, String name, Object object) throws Exception {
		ObjectMapper mapper = getMapper();
		try {
			File file = new File(path, name);
			mapper.writeValue(file, object);
		} catch (Exception e) {
			throw new Exception("unable to save model " + name, e);
		}
	}
}
