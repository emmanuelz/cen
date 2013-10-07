package org.cen.ui.gameboard;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import org.cen.cup.cup2014.gameboard.GameBoard2014;

public class GameBoardView extends JPanel {
	private static final long serialVersionUID = 1L;
	private GameBoardPainter painter;

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		painter.paint(g);
	}

	public GameBoardView() {
		super();
		Dimension d = new Dimension(640, 1280);
		setPreferredSize(d);
		painter = new GameBoardPainter();
		GameBoard2014 gameBoard = new GameBoard2014();
		painter.setGameBoard(gameBoard);
		painter.setSize(d);
	}
}
