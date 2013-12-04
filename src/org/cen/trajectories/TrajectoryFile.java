package org.cen.trajectories;

import java.nio.file.Path;

public class TrajectoryFile implements ITrajectoryFile {
	private Path path;

	public TrajectoryFile(Path path) {
		super();
		this.path = path;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ITrajectoryFile) {
			Path path2 = ((ITrajectoryFile) obj).getPath();
			return path.equals(path2);
		}
		return false;
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
	public int hashCode() {
		return path.hashCode();
	}

	@Override
	public String toString() {
		return getName();
	}
}
