package org.cen.ui.gameboard.elements.trajectory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.cen.persistance.FileLocations;

public abstract class AbstractTrajectoryFactory {
	public abstract ITrajectoryPath getTrajectoryPath(String name, InputStream stream);

	public ITrajectoryPath loadFromFile(String filePath) throws FileNotFoundException {
		File dir = new File(FileLocations.DATA, FileLocations.TRAJECTORIES);
		File file = new File(dir, filePath);
		String name = file.getName();
		FileInputStream fis = new FileInputStream(file);
		return getTrajectoryPath(name, fis);
	}
}
