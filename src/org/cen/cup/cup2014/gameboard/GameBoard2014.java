package org.cen.cup.cup2014.gameboard;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cen.cup.cup2014.gameboard.elements.Basket;
import org.cen.cup.cup2014.gameboard.elements.Beacon;
import org.cen.cup.cup2014.gameboard.elements.CentralFireplace;
import org.cen.cup.cup2014.gameboard.elements.Fire;
import org.cen.cup.cup2014.gameboard.elements.FixedTorch;
import org.cen.cup.cup2014.gameboard.elements.FrescoPath;
import org.cen.cup.cup2014.gameboard.elements.MobileTorch;
import org.cen.cup.cup2014.gameboard.elements.SideFireplace;
import org.cen.cup.cup2014.gameboard.elements.StartArea;
import org.cen.cup.cup2014.gameboard.elements.Tree;
import org.cen.ui.gameboard.AbstractGameBoard;
import org.cen.ui.gameboard.GameBoardElementsComparator;
import org.cen.ui.gameboard.IGameBoardElement;
import org.cen.ui.gameboard.RALColor;
import org.cen.ui.gameboard.elements.Board;
import org.cen.ui.gameboard.elements.Border;

/**
 * Gameboard for the cup 2012.
 */
public class GameBoard2014 extends AbstractGameBoard {
	public static final double BOARD_WIDTH = 2000d;

	public static final double BOARD_HEIGHT = 3000d;

	public static final double BOARD_MIDDLE_HEIGHT = BOARD_HEIGHT / 2.0d;

	public static final double BOARD_MIDDLE_WIDTH = BOARD_WIDTH / 2.0d;

	public static final double BORDER_WIDTH = 22d;

	public static double symetrize(boolean symetric, double value) {
		if (symetric) {
			value = BOARD_HEIGHT - value;
		}
		return value;
	}

	/** Color of board. */
	private final Color COLOR_BOARD = RALColor.RAL_6018;

	private final List<IGameBoardElement> elements;

	private final Rectangle2D gameplayBounds;

	private final Rectangle2D visibleBounds;

	public GameBoard2014() {
		super();
		gameplayBounds = new Rectangle2D.Double(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
		visibleBounds = new Rectangle2D.Double(-150, -150, BOARD_WIDTH + 400, BOARD_HEIGHT + 400);
		elements = new ArrayList<IGameBoardElement>();
		addElements();
	}

	private void addElements() {
		elements.add(new Board(COLOR_BOARD, BOARD_WIDTH, BOARD_HEIGHT));
		elements.add(new Border("border", BOARD_HEIGHT, BORDER_WIDTH, Color.WHITE, new Point2D.Double(BOARD_WIDTH + BORDER_WIDTH, 0), Math.PI / 2, 0));

		elements.add(new Tree("tree-red1", new Point2D.Double(1300, 0), 0));
		elements.add(new Tree("tree-red2", new Point2D.Double(BOARD_WIDTH, 700), Math.PI / 2));
		elements.add(new Tree("tree-yellow1", new Point2D.Double(1300, BOARD_HEIGHT), 0));
		elements.add(new Tree("tree-yellow2", new Point2D.Double(BOARD_WIDTH, BOARD_HEIGHT - 700), Math.PI / 2));

		elements.add(new CentralFireplace("fireplace-centre", new Point2D.Double(BOARD_WIDTH / 2, BOARD_HEIGHT / 2)));
		elements.add(new SideFireplace("fireplace-red", new Point2D.Double(BOARD_WIDTH, 0), 0));
		elements.add(new SideFireplace("fireplace-yellow", new Point2D.Double(BOARD_WIDTH, BOARD_HEIGHT), Math.PI / 2));

		elements.add(new Basket("basket-red", new Point2D.Double(0, BOARD_HEIGHT - 400 - 700), RALColor.RAL_3020));
		elements.add(new Basket("basket-yellow", new Point2D.Double(0, 400), RALColor.RAL_1023));

		elements.add(new MobileTorch("torch-mobile-red", new Point2D.Double(1100, 900)));
		elements.add(new MobileTorch("torch-mobile-yellow", new Point2D.Double(1100, BOARD_HEIGHT - 900)));
		elements.add(new FixedTorch("torch-fixed-red1", new Point2D.Double(800, 0), 0));
		elements.add(new FixedTorch("torch-fixed-red2", new Point2D.Double(BOARD_WIDTH, 1300), Math.PI / 2));
		elements.add(new FixedTorch("torch-fixed-yellow1", new Point2D.Double(800, BOARD_HEIGHT), Math.PI));
		elements.add(new FixedTorch("torch-fixed-yellow2", new Point2D.Double(BOARD_WIDTH, BOARD_HEIGHT - 1300), Math.PI / 2));

		elements.add(new Fire("fire-torch-fixed-red1", new Point2D.Double(800, Fire.HEIGHT / 2), 0));
		elements.add(new Fire("fire-torch-fixed-red2", new Point2D.Double(BOARD_WIDTH - Fire.HEIGHT / 2, 1300), Math.PI / 2));
		elements.add(new Fire("fire-torch-fixed-yellow1", new Point2D.Double(800, BOARD_HEIGHT - Fire.HEIGHT / 2), Math.PI));
		elements.add(new Fire("fire-torch-fixed-yellow2", new Point2D.Double(BOARD_WIDTH - Fire.HEIGHT / 2, BOARD_HEIGHT - 1300), Math.PI / 2));

		elements.add(new Fire("fire-free-red1", new Point2D.Double(1100, 400), Math.PI / 2));
		elements.add(new Fire("fire-free-red2", new Point2D.Double(1600, 900), 0));
		elements.add(new Fire("fire-free-red3", new Point2D.Double(600, 900), 0));
		elements.add(new Fire("fire-free-yellow1", new Point2D.Double(1100, BOARD_HEIGHT - 400), Math.PI / 2));
		elements.add(new Fire("fire-free-yellow2", new Point2D.Double(1600, BOARD_HEIGHT - 900), 0));
		elements.add(new Fire("fire-free-yellow3", new Point2D.Double(600, BOARD_HEIGHT - 900), 0));

		elements.add(new FrescoPath("fresco-red", new Point2D.Double(0, 0), false));
		elements.add(new FrescoPath("fresco-yellow", new Point2D.Double(0, 0), true));

		elements.add(new StartArea("start-red", new Point2D.Double(0, 0), RALColor.RAL_3020, false));
		elements.add(new StartArea("start-yellow", new Point2D.Double(0, 0), RALColor.RAL_1023, true));

		elements.add(new Border("border", 700, BORDER_WIDTH, RALColor.RAL_3020, new Point2D.Double(0, -BORDER_WIDTH), 0, 3));
		elements.add(new Border("border", 700, BORDER_WIDTH, RALColor.RAL_1023, new Point2D.Double(0, BOARD_HEIGHT), 0, 3));
		elements.add(new Border("border", BOARD_WIDTH - 700 + BORDER_WIDTH, BORDER_WIDTH, Color.WHITE, new Point2D.Double(700, -BORDER_WIDTH), 0, 3));
		elements.add(new Border("border", BOARD_WIDTH - 700 + BORDER_WIDTH, BORDER_WIDTH, Color.WHITE, new Point2D.Double(700, BOARD_HEIGHT), 0, 3));
		elements.add(new Border("border", BORDER_WIDTH, 400 + BORDER_WIDTH, RALColor.RAL_3020, new Point2D.Double(-BORDER_WIDTH, -BORDER_WIDTH), 0, 3));
		elements.add(new Border("border", BORDER_WIDTH, 400 + BORDER_WIDTH, RALColor.RAL_1023, new Point2D.Double(-BORDER_WIDTH, BOARD_HEIGHT - 400), 0, 3));
		elements.add(new Border("border", BORDER_WIDTH, 800, Color.WHITE, new Point2D.Double(-BORDER_WIDTH, 1100), 0, 3));
		elements.add(new Border("border", BORDER_WIDTH, 700, RALColor.RAL_8002, new Point2D.Double(-BORDER_WIDTH, 400), 0, 3));
		elements.add(new Border("border", BORDER_WIDTH, 700, RALColor.RAL_8002, new Point2D.Double(-BORDER_WIDTH, 1900), 0, 3));

		elements.add(new Beacon("beacon-red-1", new Point2D.Double(-62, -62), RALColor.RAL_3020));
		elements.add(new Beacon("beacon-red-2", new Point2D.Double(BOARD_WIDTH + 62, -62), RALColor.RAL_3020));
		elements.add(new Beacon("beacon-red-3", new Point2D.Double(BOARD_WIDTH / 2, BOARD_HEIGHT + 62), RALColor.RAL_3020));

		elements.add(new Beacon("beacon-yellow-1", new Point2D.Double(-62, BOARD_HEIGHT + 62), RALColor.RAL_1023));
		elements.add(new Beacon("beacon-yellow-2", new Point2D.Double(BOARD_WIDTH + 62, BOARD_HEIGHT + 62), RALColor.RAL_1023));
		elements.add(new Beacon("beacon-yellow-3", new Point2D.Double(BOARD_WIDTH / 2, -62), RALColor.RAL_1023));

		Collections.sort(elements, new GameBoardElementsComparator());
	}

	private void addOpponentLocation(Point2D coordinates) {
		// Opponent opponent = RobotUtils.getRobotAttribute(Opponent.class,
		// servicesProvider);
		// opponent.addLocation(coordinates);
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
