package org.cen;

import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;

import org.cen.ui.gameboard.GameBoardView;

public class Main {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Main();
	}

	public Main() {
		super();
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = frame.getContentPane();

		GameBoardView view = new GameBoardView();
		view.setPreferredSize(new Dimension(320, 640));

		c.add(view);

		frame.pack();
		frame.setVisible(true);
	}
}
