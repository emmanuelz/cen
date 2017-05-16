package org.cen.models;

import org.cen.persistance.FileLocations;
import org.cen.persistance.JSONMapper;

public class MatchModelFactory {
	public MatchModel loadModel(String name) throws Exception {
		String path = FileLocations.getPath(FileLocations.DATA, FileLocations.MATCHES);
		MatchModel model = JSONMapper.loadObject(path, name, MatchModel.class);
		return model;
	}

	public void saveModel(MatchModel model) throws Exception {
		String path = FileLocations.getPath(FileLocations.DATA, FileLocations.MATCHES);
		String name = model.getName();
		JSONMapper.saveObject(path, name, model);
	}
}
