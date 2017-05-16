package org.cen.trajectories;

import java.net.URI;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.DefaultListModel;

public class InputFilesFactory {
	private DefaultListModel<IInputFile> model = new DefaultListModel<IInputFile>();

	public void addSource(URI uri, InputFileType type) {
		Path path = Paths.get(uri);
		try {
			DirectoryStream<Path> stream = Files.newDirectoryStream(path);
			try {
				for (Path p : stream) {
					InputFile file = new InputFile(p, type);
					model.add(0, file);
				}
			} finally {
				stream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public DefaultListModel<IInputFile> getListModel() {
		return model;
	}
}
