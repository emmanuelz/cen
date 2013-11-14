package org.cen.trajectories;

import java.nio.file.Path;

public class TrajectoryFile implements ITrajectoryFile {
	private Path path;

	public TrajectoryFile(Path path) {
		super();
		this.path = path;
	}

	@Override
	public String getName() {
		Path p = path.getFileName();
		return p.toString();
	}

	@Override
	public Path getPath() {
		return path;
	}

	@Override
	public String toString() {
		return getName();
	}
}
