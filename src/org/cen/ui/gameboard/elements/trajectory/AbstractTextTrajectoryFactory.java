package org.cen.ui.gameboard.elements.trajectory;

import java.io.InputStream;
import java.util.Scanner;

public abstract class AbstractTextTrajectoryFactory extends AbstractTrajectoryFactory {
	@Override
	public ITrajectoryPath getTrajectoryPath(String name, InputStream stream) {
		AbstractTrajectoryParser parser = createParser();
		Scanner scanner = new Scanner(stream);
		try {
			scanner.useDelimiter("\r\n?");
			while (scanner.hasNext()) {
				String line = scanner.next();
				parser.parseLine(line);
			}
		} finally {
			scanner.close();
		}
		return parser.getPath(name);
	}

	protected abstract AbstractTrajectoryParser createParser();
}
