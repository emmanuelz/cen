package org.cen.ui.gameboard;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;

import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

import org.cen.cup.cup2014.gameboard.GameBoard2014;

public class GameBoardView extends JPanel implements ComponentListener, MouseListener, MouseMotionListener, MouseWheelListener {
	private static final long serialVersionUID = 1L;
	private int buttonPressed;
	private Point lastClick;
	private EventListenerList listeners = new EventListenerList();
	private GameBoardPainter painter;

	public GameBoardView(GameBoard2014 gameBoard) {
		super();
		addComponentListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		painter = new GameBoardPainter();
		painter.setGameBoard(gameBoard);
	}

	public void addGameBoardEventListener(IGameBoardEventListener listener) {
		listeners.add(IGameBoardEventListener.class, listener);
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
		repaint();
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (buttonPressed == MouseEvent.BUTTON3) {
			Point click = e.getPoint();
			Point p = new Point(click);
			p.translate(-lastClick.x, -lastClick.y);
			lastClick = click;
			painter.adjustPosition(p.x, p.y);
			repaint();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Point pos = e.getPoint();
		Point2D p = painter.getRealCoordinates(pos);
		GameBoardMouseMoveEvent event = new GameBoardMouseMoveEvent(p);
		notifyListeners(event);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		lastClick = e.getPoint();
		buttonPressed = e.getButton();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		Point position = e.getPoint();
		double factor = e.getWheelRotation();
		if (factor == 0) {
			factor = e.getPreciseWheelRotation();
		}
		painter.adjustZoom(factor, position);
		repaint();
	}

	protected void notifyListeners(IGameBoardEvent event) {
		IGameBoardEventListener[] listeners = this.listeners.getListeners(IGameBoardEventListener.class);
		for (IGameBoardEventListener l : listeners) {
			l.onGameBoardEvent(event);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		painter.paint(g);
	}

	public void setTimestamp(double timestamp) {
		painter.setTimestamp(timestamp);
	}
}
