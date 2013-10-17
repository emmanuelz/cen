package org.cen.ui.gameboard;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;

import org.cen.cup.cup2014.gameboard.GameBoard2014;

public class GameBoardView extends JPanel implements ComponentListener {
	private static final long serialVersionUID = 1L;
	private GameBoardPainter painter;

	public GameBoardView() {
		super();
		addComponentListener(this);
		painter = new GameBoardPainter();
		GameBoard2014 gameBoard = new GameBoard2014();
		painter.setGameBoard(gameBoard);
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void componentResized(ComponentEvent e) {
		Dimension size = getSize();
		painter.setSize(size);
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		painter.paint(g);
	}
}
