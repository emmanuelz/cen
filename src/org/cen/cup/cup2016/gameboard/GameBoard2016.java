package org.cen.cup.cup2016.gameboard;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cen.cup.cup2014.gameboard.elements.Beacon;
import org.cen.cup.cup2016.gameboard.elements.Hut;
import org.cen.cup.cup2016.gameboard.elements.Rock;
import org.cen.cup.cup2016.gameboard.elements.Sea;
import org.cen.cup.cup2016.gameboard.elements.SeaShell;
import org.cen.cup.cup2016.gameboard.elements.StartArea;
import org.cen.cup.cup2016.gameboard.elements.Windbreak;
import org.cen.ui.gameboard.AbstractGameBoard;
import org.cen.ui.gameboard.GameBoardElementsComparator;
import org.cen.ui.gameboard.IGameBoardElement;
import org.cen.ui.gameboard.RALColor;
import org.cen.ui.gameboard.elements.Board;
import org.cen.ui.gameboard.elements.Border;

/**
 * Gameboard for the cup 2016.
 */
public class GameBoard2016 extends AbstractGameBoard {
	public static final double BOARD_WIDTH = 2000d;

	public static final double BOARD_HEIGHT = 3000d;

	public static final double BOARD_MIDDLE_HEIGHT = BOARD_HEIGHT / 2.0d;

	public static final double BOARD_MIDDLE_WIDTH = BOARD_WIDTH / 2.0d;

	public static final double BORDER_WIDTH = 22d;

	public static final Color COLOR_A = RALColor.RAL_4008;
	public static final Color COLOR_B = RALColor.RAL_6001;
	public static final Color COLOR_N = RALColor.RAL_9016;

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
	private final Color COLOR_BOARD = RALColor.RAL_1023;

	private final List<IGameBoardElement> elements;

	private final Rectangle2D gameplayBounds;

	private final Rectangle2D visibleBounds;

	public GameBoard2016() {
		super();
		gameplayBounds = new Rectangle2D.Double(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
		visibleBounds = new Rectangle2D.Double(-150, -200, BOARD_WIDTH + 400, BOARD_HEIGHT + 400);
		elements = new ArrayList<IGameBoardElement>();

		addElements();
	}

	private void addElements() {
		elements.add(new Board(COLOR_BOARD, BOARD_WIDTH, BOARD_HEIGHT));

		elements.add(new Sea("sea", new Point2D.Double(1200, 0)));
		elements.add(new Windbreak("winbreak", new Point2D.Double(750, 1500)));

		elements.add(new Border("border", BOARD_HEIGHT + BORDER_WIDTH * 2, BORDER_WIDTH, RALColor.RAL_1023, new Point2D.Double(0, -BORDER_WIDTH), Math.PI / 2, 0));
		elements.add(new Border("border", BOARD_WIDTH, BORDER_WIDTH, RALColor.RAL_1023, new Point2D.Double(0, -BORDER_WIDTH), 0, 0));
		elements.add(new Border("border", BOARD_HEIGHT + BORDER_WIDTH * 2, BORDER_WIDTH, RALColor.RAL_1023, new Point2D.Double(BOARD_WIDTH + BORDER_WIDTH, -BORDER_WIDTH), Math.PI / 2, 0));
		elements.add(new Border("border", BOARD_WIDTH, BORDER_WIDTH, RALColor.RAL_1023, new Point2D.Double(0, BOARD_HEIGHT), 0, 0));
		elements.add(new Border("border", 200, BORDER_WIDTH, RALColor.RAL_1023, new Point2D.Double(0, 800), 0, 0));
		elements.add(new Border("border", 200, BORDER_WIDTH, RALColor.RAL_1023, new Point2D.Double(0, BOARD_HEIGHT - 800 - BORDER_WIDTH), 0, 0));

		elements.add(new Beacon("beacon-a-1", new Point2D.Double(-62, BOARD_HEIGHT + 62), COLOR_A));
		elements.add(new Beacon("beacon-a-2", new Point2D.Double(BOARD_WIDTH + 62, BOARD_HEIGHT + 62), COLOR_A));
		elements.add(new Beacon("beacon-a-3", new Point2D.Double(BOARD_WIDTH / 2, -62), COLOR_A));

		elements.add(new Beacon("beacon-b-1", new Point2D.Double(-62, -62), COLOR_B));
		elements.add(new Beacon("beacon-b-2", new Point2D.Double(BOARD_WIDTH + 62, -62), COLOR_B));
		elements.add(new Beacon("beacon-b-3", new Point2D.Double(BOARD_WIDTH / 2, BOARD_HEIGHT + 62), COLOR_B));

		elements.add(new StartArea("start-a", new Point2D.Double(600, 0), COLOR_A, false));
		elements.add(new StartArea("start-b", new Point2D.Double(600, 0), COLOR_B, true));

		elements.add(new Hut("hut-a-1", new Point2D.Double(0, 300)));
		elements.add(new Hut("hut-a-2", new Point2D.Double(0, 600)));
		elements.add(new Hut("hut-b-2", new Point2D.Double(0, 2400)));
		elements.add(new Hut("hut-b-1", new Point2D.Double(0, 2700)));

		elements.add(new SeaShell("shell-1", new Point2D.Double(1250, 200), COLOR_A));
		elements.add(new SeaShell("shell-2", new Point2D.Double(1550, 200), COLOR_N));
		elements.add(new SeaShell("shell-1", new Point2D.Double(1250, 700), COLOR_N));
		elements.add(new SeaShell("shell-2", new Point2D.Double(1550, 700), COLOR_N));
		elements.add(new SeaShell("shell-3", new Point2D.Double(1450, 900), COLOR_A));
		elements.add(new SeaShell("shell-4", new Point2D.Double(1650, 1200), COLOR_A));

		elements.add(new SeaShell("shell-1", new Point2D.Double(1250, 2800), COLOR_B));
		elements.add(new SeaShell("shell-2", new Point2D.Double(1550, 2800), COLOR_N));
		elements.add(new SeaShell("shell-1", new Point2D.Double(1250, 2300), COLOR_N));
		elements.add(new SeaShell("shell-2", new Point2D.Double(1550, 2300), COLOR_N));
		elements.add(new SeaShell("shell-3", new Point2D.Double(1450, 2100), COLOR_B));
		elements.add(new SeaShell("shell-4", new Point2D.Double(1650, 1800), COLOR_B));

		elements.add(new SeaShell("shell-3", new Point2D.Double(1550, 1500), COLOR_N));
		elements.add(new SeaShell("shell-4", new Point2D.Double(1850, 1500), COLOR_N));

		elements.add(new SeaShell("shell-4", new Point2D.Double(1947, 53), COLOR_N));
		elements.add(new SeaShell("shell-3", new Point2D.Double(1819, 85), COLOR_A));
		elements.add(new SeaShell("shell-3", new Point2D.Double(1915, 181), COLOR_N));

		elements.add(new SeaShell("shell-4", new Point2D.Double(1947, 2947), COLOR_N));
		elements.add(new SeaShell("shell-4", new Point2D.Double(1819, 2915), COLOR_B));
		elements.add(new SeaShell("shell-4", new Point2D.Double(1915, 2819), COLOR_N));

		elements.add(new Rock("rock-a", new Point2D.Double(BOARD_WIDTH, 0), false));
		elements.add(new Rock("rock-b", new Point2D.Double(BOARD_WIDTH, BOARD_HEIGHT), true));

		// elements.add(new FilmEditingArea("editing", new
		// Point2D.Double(BOARD_WIDTH, BOARD_MIDDLE_HEIGHT)));
		// elements.add(new CentralArea("central", new Point2D.Double(1070,
		// BOARD_MIDDLE_HEIGHT)));
		//
		// elements.add(new PathLine("path-a", new Point2D.Double(0, 0),
		// false));
		// elements.add(new PathLine("path-b", new Point2D.Double(0, 0), true));
		//
		// elements.add(new StepsArea("steps-a", new Point2D.Double(0,
		// BOARD_MIDDLE_HEIGHT - BORDER_WIDTH / 2 - StepsArea.HALF_HEIGHT),
		// COLOR_A));
		// elements.add(new StepsArea("steps-b", new Point2D.Double(0,
		// BOARD_MIDDLE_HEIGHT + BORDER_WIDTH / 2 + StepsArea.HALF_HEIGHT),
		// COLOR_B));
		//
		// elements.add(new PopCornBasket("popcorn-a-left", new
		// Point2D.Double(1750, 250)));
		// elements.add(new PopCornBasket("popcorn-a-right", new
		// Point2D.Double(830, 910)));
		// elements.add(new PopCornBasket("popcorn-b-left", new
		// Point2D.Double(1750, 2750)));
		// elements.add(new PopCornBasket("popcorn-b-right", new
		// Point2D.Double(830, 2090)));
		// elements.add(new PopCornBasket("popcorn-center", new
		// Point2D.Double(1650, 1500)));
		//
		// elements.add(new Stand("stand-a-1", new Point2D.Double(200, 90),
		// COLOR_A));
		// elements.add(new Stand("stand-a-2", new Point2D.Double(1750, 90),
		// COLOR_A));
		// elements.add(new Stand("stand-a-3", new Point2D.Double(1850, 90),
		// COLOR_A));
		// elements.add(new Stand("stand-a-4", new Point2D.Double(100, 850),
		// COLOR_A));
		// elements.add(new Stand("stand-a-5", new Point2D.Double(200, 850),
		// COLOR_A));
		// elements.add(new Stand("stand-a-6", new Point2D.Double(1355, 870),
		// COLOR_A));
		// elements.add(new Stand("stand-a-7", new Point2D.Double(1770, 1100),
		// COLOR_A));
		// elements.add(new Stand("stand-a-8", new Point2D.Double(1400, 1300),
		// COLOR_A));
		// elements.add(new Stand("stand-a-1", new Point2D.Double(200, 2910),
		// COLOR_B));
		// elements.add(new Stand("stand-a-2", new Point2D.Double(1750, 2910),
		// COLOR_B));
		// elements.add(new Stand("stand-a-3", new Point2D.Double(1850, 2910),
		// COLOR_B));
		// elements.add(new Stand("stand-a-4", new Point2D.Double(100, 2150),
		// COLOR_B));
		// elements.add(new Stand("stand-a-5", new Point2D.Double(200, 2150),
		// COLOR_B));
		// elements.add(new Stand("stand-a-6", new Point2D.Double(1355, 2130),
		// COLOR_B));
		// elements.add(new Stand("stand-a-7", new Point2D.Double(1770, 1900),
		// COLOR_B));
		// elements.add(new Stand("stand-a-8", new Point2D.Double(1400, 1700),
		// COLOR_B));
		//
		// elements.add(new PopCornDispenser("dispenser-a-1", new
		// Point2D.Double(0, 300)));
		// elements.add(new PopCornDispenser("dispenser-a-2", new
		// Point2D.Double(0, 600)));
		// elements.add(new PopCornDispenser("dispenser-b-2", new
		// Point2D.Double(0, 2400)));
		// elements.add(new PopCornDispenser("dispenser-b-1", new
		// Point2D.Double(0, 2700)));

		// elements.add(new MovableRobot("robot"));

		// PlannedTrajectory pt = new PlannedTrajectory("planned");
		// pt.planTrajectory(new OrientedPosition(0, 0, Math.toRadians(15)), new
		// OrientedPosition(1000, 1000, Math.toRadians(90)));
		// elements.add(pt);

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
