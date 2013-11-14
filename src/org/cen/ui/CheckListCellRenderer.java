package org.cen.ui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

public class CheckListCellRenderer<E> extends JPanel implements ListCellRenderer<E> {
	private static final long serialVersionUID = 1L;
	private JCheckBox checkBox = new JCheckBox();
	private ListCellRenderer<? super E> delegate;
	private ListSelectionModel selectionModel;

	public CheckListCellRenderer(ListCellRenderer<? super E> renderer, ListSelectionModel selectionModel) {
		super();
		setLayout(new BorderLayout());
		setOpaque(false);
		checkBox.setOpaque(false);
		this.delegate = renderer;
		this.selectionModel = selectionModel;
	}

	public int getHotspotWidth() {
		return checkBox.getMinimumSize().width;
	}

	public Component getListCellRendererComponent(JList<? extends E> list, E value, int index, boolean isSelected, boolean cellHasFocus) {
		Component renderer = delegate.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		checkBox.setSelected(selectionModel.isSelectedIndex(index));
		removeAll();
		add(checkBox, BorderLayout.WEST);
		add(renderer, BorderLayout.CENTER);
		return this;
	}
}
