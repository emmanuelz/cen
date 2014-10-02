package org.cen.ui;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class CheckListController<E> extends MouseAdapter implements ListSelectionListener, ActionListener {
	int hotspot;
	private JList<E> list;
	private ListSelectionModel selectionModel = new DefaultListSelectionModel();

	public CheckListController(JList<E> list) {
		this.list = list;
		ListCellRenderer<? super E> oldRenderer = list.getCellRenderer();
		CheckListCellRenderer<? super E> newRenderer = new CheckListCellRenderer<E>(oldRenderer, selectionModel);
		list.setCellRenderer(newRenderer);
		list.registerKeyboardAction(this, KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), JComponent.WHEN_FOCUSED);
		list.addMouseListener(this);
		selectionModel.addListSelectionListener(this);
		hotspot = newRenderer.getHotspotWidth();
	}

	public void actionPerformed(ActionEvent e) {
		int index = list.getSelectedIndex();
		toggleSelection(index);
	}

	public JList<E> getList() {
		return list;
	}

	public ListModel<E> getListModel() {
		ListModel<E> model = list.getModel();
		return model;
	}

	public ListSelectionModel getSelectionModel() {
		return selectionModel;
	}

	public void mouseClicked(MouseEvent e) {
		int index = list.locationToIndex(e.getPoint());
		if (index < 0) {
			return;
		}
		Rectangle bounds = list.getCellBounds(index, index);
		if (e.getX() > bounds.x + hotspot) {
			return;
		}
		toggleSelection(index);
	}

	private void toggleSelection(int index) {
		if (index < 0) {
			return;
		}

		if (selectionModel.isSelectedIndex(index)) {
			selectionModel.removeSelectionInterval(index, index);
		} else {
			selectionModel.addSelectionInterval(index, index);
		}
	}

	public void valueChanged(ListSelectionEvent e) {
		Rectangle bounds = list.getCellBounds(e.getFirstIndex(), e.getLastIndex());
		list.repaint(bounds);
	}
}
