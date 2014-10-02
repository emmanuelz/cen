package org.cen.ui.gameboard.elements.trajectory;

public class TextTrajectoryFactory extends AbstractTextTrajectoryFactory {
	private AbstractTrajectoryParser parser;

	public TextTrajectoryFactory(AbstractTrajectoryParser parser) {
		super();
		this.parser = parser;
	}

	@Override
	protected AbstractTrajectoryParser createParser() {
		return parser;
	}
}
