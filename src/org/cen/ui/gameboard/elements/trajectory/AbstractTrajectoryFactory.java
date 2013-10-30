package org.cen.ui.gameboard.elements.trajectory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public abstract class AbstractTrajectoryFactory {
	public abstract ITrajectoryPath getTrajectoryPath(InputStream stream);

	public ITrajectoryPath loadFromFile(String filePath) throws FileNotFoundException {
		File file = new File(filePath);
		FileInputStream fis = new FileInputStream(file);
		return getTrajectoryPath(fis);
	}
}
