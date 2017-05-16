package org.cen.ui;

import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

public class ListController<E> {
	private JList<E> list;

	public ListController(JList<E> list) {
		this.list = list;
	}

	public JList<E> getList() {
		return list;
	}

	public ListModel<E> getListModel() {
		ListModel<E> model = list.getModel();
		return model;
	}

	public ListSelectionModel getSelectionModel() {
		return list.getSelectionModel();
	}
}
