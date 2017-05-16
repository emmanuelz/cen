package org.cen.persistance;

import java.io.File;

public class FileLocations {
	public static final String TRAJECTORIES = "trajectories";
	public static final String GAUGES = "gauges";
	public static final String ROBOTS = "robots";
	public static final String MATCHES = "matches";
	public static final String DATA = "data";

	public static String getPath(String parent, String child) {
		return parent + File.separatorChar + child;
	}
}
