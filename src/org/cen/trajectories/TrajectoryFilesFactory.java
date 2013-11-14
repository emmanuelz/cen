package org.cen.trajectories;

import java.net.URI;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.DefaultListModel;

public class TrajectoryFilesFactory {
	private DefaultListModel<ITrajectoryFile> model = new DefaultListModel<ITrajectoryFile>();

	public void addSource(URI uri) {
		Path path = Paths.get(uri);
		try {
			DirectoryStream<Path> stream = Files.newDirectoryStream(path);
			try {
				for (Path p : stream) {
					TrajectoryFile file = new TrajectoryFile(p);
					model.add(0, file);
				}
			} finally {
				stream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public DefaultListModel<ITrajectoryFile> getListModel() {
		return model;
	}
}
