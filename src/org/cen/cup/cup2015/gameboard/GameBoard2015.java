package org.cen.cup.cup2015.gameboard;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cen.cup.cup2015.gameboard.elements.CentralArea;
import org.cen.cup.cup2015.gameboard.elements.FilmEditingArea;
import org.cen.cup.cup2015.gameboard.elements.PathLine;
import org.cen.cup.cup2015.gameboard.elements.PopCornBasket;
import org.cen.cup.cup2015.gameboard.elements.Stand;
import org.cen.cup.cup2015.gameboard.elements.StartArea;
import org.cen.cup.cup2015.gameboard.elements.StepsArea;
import org.cen.trajectories.planner.OrientedPosition;
import org.cen.trajectories.planner.PlannedTrajectory;
import org.cen.ui.gameboard.AbstractGameBoard;
import org.cen.ui.gameboard.GameBoardElementsComparator;
import org.cen.ui.gameboard.IGameBoardElement;
import org.cen.ui.gameboard.RALColor;
import org.cen.ui.gameboard.elements.Board;
import org.cen.ui.gameboard.elements.Border;
import org.cen.ui.gameboard.elements.object.MovableRobot;

/**
 * Gameboard for the cup 2015.
 */
public class GameBoard2015 extends AbstractGameBoard {
	public static final double BOARD_WIDTH = 2000d;

	public static final double BOARD_HEIGHT = 3000d;

	public static final double BOARD_MIDDLE_HEIGHT = BOARD_HEIGHT / 2.0d;

	public static final double BOARD_MIDDLE_WIDTH = BOARD_WIDTH / 2.0d;

	public static final double BORDER_WIDTH = 22d;

	public static final Color COLOR_A = RALColor.RAL_1023;
	public static final Color COLOR_B = RALColor.RAL_6018;

	public static double symetrize(boolean symetric, double value) {
		if (symetric) {
			value = BOARD_HEIGHT - value;
		}
		return value;
	}

	private static AffineTransform symetricTransform;

	public static AffineTransform getSymetricTransform() {
		if (symetricTransform == null) {
			AffineTransform t = new AffineTransform();
			t.translate(0, BOARD_HEIGHT);
			t.scale(1, -1);
			symetricTransform = t;
		}
		return symetricTransform;
	}

	/** Color of board. */
	private final Color COLOR_BOARD = RALColor.RAL_5015;

	private final List<IGameBoardElement> elements;

	private final Rectangle2D gameplayBounds;

	private final Rectangle2D visibleBounds;

	public GameBoard2015() {
		super();
		gameplayBounds = new Rectangle2D.Double(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
		visibleBounds = new Rectangle2D.Double(-150, -200, BOARD_WIDTH + 400, BOARD_HEIGHT + 400);
		elements = new ArrayList<IGameBoardElement>();

		addElements();
	}

	private void addElements() {
		elements.add(new Board(COLOR_BOARD, BOARD_WIDTH, BOARD_HEIGHT));

		elements.add(new Border("border", BOARD_HEIGHT + BORDER_WIDTH * 2, BORDER_WIDTH, RALColor.RAL_9005, new Point2D.Double(0, -BORDER_WIDTH), Math.PI / 2, 0));
		elements.add(new Border("border", BOARD_WIDTH, BORDER_WIDTH, RALColor.RAL_9005, new Point2D.Double(0, -BORDER_WIDTH), 0, 0));
		elements.add(new Border("border", BOARD_HEIGHT + BORDER_WIDTH * 2, BORDER_WIDTH, RALColor.RAL_9005, new Point2D.Double(BOARD_WIDTH + BORDER_WIDTH, -BORDER_WIDTH), Math.PI / 2, 0));
		elements.add(new Border("border", BOARD_WIDTH, BORDER_WIDTH, RALColor.RAL_9005, new Point2D.Double(0, BOARD_HEIGHT), 0, 0));

		elements.add(new StartArea("start-a", new Point2D.Double(BOARD_MIDDLE_WIDTH, 0), COLOR_A, RALColor.RAL_6018, false));
		elements.add(new StartArea("start-b", new Point2D.Double(BOARD_MIDDLE_WIDTH, 0), COLOR_B, RALColor.RAL_1023, true));

		elements.add(new FilmEditingArea("editing", new Point2D.Double(BOARD_WIDTH, BOARD_MIDDLE_HEIGHT)));
		elements.add(new CentralArea("central", new Point2D.Double(1070, BOARD_MIDDLE_HEIGHT)));

		elements.add(new PathLine("path-a", new Point2D.Double(0, 0), false));
		elements.add(new PathLine("path-b", new Point2D.Double(0, 0), true));

		elements.add(new StepsArea("steps-a", new Point2D.Double(0, BOARD_MIDDLE_HEIGHT - BORDER_WIDTH / 2 - StepsArea.HALF_HEIGHT), COLOR_A));
		elements.add(new StepsArea("steps-b", new Point2D.Double(0, BOARD_MIDDLE_HEIGHT + BORDER_WIDTH / 2 + StepsArea.HALF_HEIGHT), COLOR_B));

		elements.add(new PopCornBasket("popcorn-a-left", new Point2D.Double(1750, 250)));
		elements.add(new PopCornBasket("popcorn-a-right", new Point2D.Double(830, 910)));
		elements.add(new PopCornBasket("popcorn-b-left", new Point2D.Double(1750, 2750)));
		elements.add(new PopCornBasket("popcorn-b-right", new Point2D.Double(830, 2090)));
		elements.add(new PopCornBasket("popcorn-center", new Point2D.Double(1650, 1500)));

		elements.add(new Stand("stand-a-1", new Point2D.Double(200, 90), COLOR_A));
		elements.add(new Stand("stand-a-2", new Point2D.Double(1750, 90), COLOR_A));
		elements.add(new Stand("stand-a-3", new Point2D.Double(1850, 90), COLOR_A));
		elements.add(new Stand("stand-a-4", new Point2D.Double(100, 850), COLOR_A));
		elements.add(new Stand("stand-a-5", new Point2D.Double(200, 850), COLOR_A));
		elements.add(new Stand("stand-a-6", new Point2D.Double(1355, 870), COLOR_A));
		elements.add(new Stand("stand-a-7", new Point2D.Double(1770, 1100), COLOR_A));
		elements.add(new Stand("stand-a-8", new Point2D.Double(1400, 1300), COLOR_A));
		elements.add(new Stand("stand-a-1", new Point2D.Double(200, 2910), COLOR_B));
		elements.add(new Stand("stand-a-2", new Point2D.Double(1750, 2910), COLOR_B));
		elements.add(new Stand("stand-a-3", new Point2D.Double(1850, 2910), COLOR_B));
		elements.add(new Stand("stand-a-4", new Point2D.Double(100, 2150), COLOR_B));
		elements.add(new Stand("stand-a-5", new Point2D.Double(200, 2150), COLOR_B));
		elements.add(new Stand("stand-a-6", new Point2D.Double(1355, 2130), COLOR_B));
		elements.add(new Stand("stand-a-7", new Point2D.Double(1770, 1900), COLOR_B));
		elements.add(new Stand("stand-a-8", new Point2D.Double(1400, 1700), COLOR_B));

		elements.add(new MovableRobot("robot"));

		PlannedTrajectory pt = new PlannedTrajectory("planned");
		pt.planTrajectory(new OrientedPosition(0, 0, Math.toRadians(15)), new OrientedPosition(1000, 1000, Math.toRadians(90)));
		elements.add(pt);

		Collections.sort(elements, new GameBoardElementsComparator());
	}

	@Override
	public List<IGameBoardElement> getElements() {
		return elements;
	}

	@Override
	public Rectangle2D getGameplayBounds() {
		return gameplayBounds;
	}

	@Override
	public Rectangle2D getVisibleBounds() {
		return visibleBounds;
	}
}
