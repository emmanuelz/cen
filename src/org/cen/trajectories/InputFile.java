package org.cen.trajectories;

import java.nio.file.Path;

public class InputFile implements IInputFile {
	private Path path;
	private InputFileType type;

	public InputFile(Path path, InputFileType type) {
		super();
		this.path = path;
		this.type = type;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IInputFile) {
			Path path2 = ((IInputFile) obj).getPath();
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
	public InputFileType getType() {
		return type;
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
