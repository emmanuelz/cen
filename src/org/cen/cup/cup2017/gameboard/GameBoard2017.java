package org.cen.cup.cup2017.gameboard;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.cen.ui.gameboard.AbstractGameBoard;
import org.cen.ui.gameboard.IGameBoardElement;
import org.cen.ui.gameboard.RALColor;
import org.cen.ui.gameboard.elements.Board;
import org.cen.ui.gameboard.elements.Border;

public class GameBoard2017 extends AbstractGameBoard {
	public static final double BOARD_WIDTH = 2000d;
	public static final double BOARD_HEIGHT = 3000d;
	public static final double BOARD_MIDDLE_HEIGHT = BOARD_HEIGHT / 2.0d;
	public static final double BOARD_MIDDLE_WIDTH = BOARD_WIDTH / 2.0d;
	public static final double BORDER_WIDTH = 22d;

	public static final Color COLOR_A = RALColor.RAL_1023;
	public static final Color COLOR_B = RALColor.RAL_5015;

	/** Color of board. */
	private final Color COLOR_BOARD = RALColor.RAL_7032;

	private final List<IGameBoardElement> elements;

	private final Rectangle2D gameplayBounds;

	private final Rectangle2D visibleBounds;

	public GameBoard2017() {
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

		elements.add(new Tower("tower-up-left", new Point2D.Double(-62, 3062)));
		elements.add(new Tower("tower-up-middle", new Point2D.Double(1000, 3062)));
		elements.add(new Tower("tower-up-right", new Point2D.Double(2062, 3062)));
		elements.add(new Tower("tower-bt-left", new Point2D.Double(-62, -62)));
		elements.add(new Tower("tower-bt-middle", new Point2D.Double(1000, -62)));
		elements.add(new Tower("tower-bt-right", new Point2D.Double(2062, -62)));

		elements.add(new TableSeparation("table_separation_1", new Point2D.Double(1000, 1000)));
		elements.add(new TableSeparation("table_separation_1", new Point2D.Double(1000, 2000)));

		elements.add(new StartZone("sz-top", new Point2D.Double(0, 3000), COLOR_A));
		elements.add(new StartZone("sz-bottom", new Point2D.Double(0, 1070), COLOR_B));

		elements.add(new StartSeparation("ssp-top", new Point2D.Double(360, 3000)));
		elements.add(new StartSeparation("ssp-bottom", new Point2D.Double(360, 710)));

		elements.add(new Crater("top-crater", new Point2D.Double(540, 2350)));
		elements.add(new Crater("bottom-crater", new Point2D.Double(540, 650)));
		elements.add(new Crater("top-station", new Point2D.Double(1870, 1930)));
		elements.add(new Crater("bottom-station", new Point2D.Double(1870, 1070)));

		elements.add(new CraterQuarter("big-crater-q1", new Point2D.Double(2000, 0), 0));
		elements.add(new CraterQuarter("big-crater-q2", new Point2D.Double(2000, 0), Math.PI / 4));
		elements.add(new CraterQuarter("big-crater-q3", new Point2D.Double(2000, 3000), -Math.PI / 4));
		elements.add(new CraterQuarter("big-crater-q4", new Point2D.Double(2000, 3000), -Math.PI / 2));

		elements.add(new CentralBase("central-base", new Point2D.Double(2000, 1500)));

		elements.add(new Rocket("rkt-south", new Point2D.Double(1350, 40)));
		elements.add(new Rocket("rkt-msouth", new Point2D.Double(40, 1150)));
		elements.add(new Rocket("rkt-mnorth", new Point2D.Double(40, 1850)));
		elements.add(new Rocket("rkt-north", new Point2D.Double(1350, 2960)));

		elements.add(new Bascule("bascule-south", new Point2D.Double(180, 535)));
		elements.add(new Bascule("bascule-north", new Point2D.Double(180, 2465)));

		elements.add(new Soute("soute-south", new Point2D.Double(-111, 250)));
		elements.add(new Soute("soute-north", new Point2D.Double(-111, 2750)));

		elements.add(new StationBorder("station-north", new Point2D.Double(925, 3000), 0d));
		elements.add(new StationBorder("station-south", new Point2D.Double(925, 0), Math.PI));

		elements.add(new LunarModule("lm-a1", new Point2D.Double(600, 2800), COLOR_A, null));
		elements.add(new LunarModule("lm-n2", new Point2D.Double(1100, 2500), COLOR_A, COLOR_B));
		elements.add(new LunarModule("lm-a3", new Point2D.Double(1850, 2200), COLOR_A, null));
		elements.add(new LunarModule("lm-n3", new Point2D.Double(1400, 2100), COLOR_A, COLOR_B));
		elements.add(new LunarModule("lm-n5", new Point2D.Double(600, 2000), COLOR_A, COLOR_B));
		elements.add(new LunarModule("lm-n6", new Point2D.Double(600, 1000), COLOR_B, COLOR_A));
		elements.add(new LunarModule("lm-n6", new Point2D.Double(1400, 900), COLOR_B, COLOR_A));
		elements.add(new LunarModule("lm-b8", new Point2D.Double(1850, 800), COLOR_B, null));
		elements.add(new LunarModule("lm-n9", new Point2D.Double(1100, 500), COLOR_B, COLOR_A));
		elements.add(new LunarModule("lm-b0", new Point2D.Double(600, 200), COLOR_B, null));
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
