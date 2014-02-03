package org.cen.trajectories;

import java.io.InputStream;
import java.nio.file.Path;

public interface IInputFile {
	public String getName();

	public Path getPath();

	public InputFileType getType();

	public InputStream getInputStream() throws Exception;
}
