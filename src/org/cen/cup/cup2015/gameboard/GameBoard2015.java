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
import org.cen.cup.cup2015.gameboard.elements.StartArea;
import org.cen.cup.cup2015.gameboard.elements.StepsArea;
import org.cen.ui.gameboard.AbstractGameBoard;
import org.cen.ui.gameboard.GameBoardElementsComparator;
import org.cen.ui.gameboard.IGameBoardElement;
import org.cen.ui.gameboard.RALColor;
import org.cen.ui.gameboard.elements.Board;
import org.cen.ui.gameboard.elements.Border;

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
		elements.add(new CentralArea("central", new Point2D.Double(BOARD_MIDDLE_WIDTH, BOARD_MIDDLE_HEIGHT)));

		elements.add(new PathLine("path-a", new Point2D.Double(0, 0), false));
		elements.add(new PathLine("path-b", new Point2D.Double(0, 0), true));

		elements.add(new StepsArea("steps-a", new Point2D.Double(0, BOARD_MIDDLE_HEIGHT - BORDER_WIDTH / 2 - StepsArea.HALF_HEIGHT), COLOR_A));
		elements.add(new StepsArea("steps-b", new Point2D.Double(0, BOARD_MIDDLE_HEIGHT + BORDER_WIDTH / 2 + StepsArea.HALF_HEIGHT), COLOR_B));

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
