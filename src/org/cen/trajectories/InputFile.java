package org.cen.trajectories;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
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
	public InputStream getInputStream() throws Exception {
		URI uri = path.toUri();
		URL url = uri.toURL();
		InputStream is = url.openStream();
		return is;
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
