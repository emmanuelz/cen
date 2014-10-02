package org.cen.ui.gameboard.elements.trajectory;

import java.io.InputStream;
import java.text.ParseException;
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
				try {
					parser.parseLine(line);
				} catch (ParseException e) {
					System.out.println(e.getMessage());
					System.out.println(line);
				}
			}
		} finally {
			scanner.close();
		}
		return parser.getPath(name);
	}

	protected abstract AbstractTrajectoryParser createParser();
}
