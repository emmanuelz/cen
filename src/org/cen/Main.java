package org.cen;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.cen.cup.cup2014.gameboard.GameBoard2014;
import org.cen.trajectories.IInputFile;
import org.cen.trajectories.InputFile;
import org.cen.trajectories.InputFileType;
import org.cen.trajectories.InputFilesFactory;
import org.cen.ui.CheckListController;
import org.cen.ui.ListController;
import org.cen.ui.gameboard.GameBoardMouseMoveEvent;
import org.cen.ui.gameboard.GameBoardView;
import org.cen.ui.gameboard.IGameBoardElement;
import org.cen.ui.gameboard.IGameBoardEvent;
import org.cen.ui.gameboard.IGameBoardEventListener;
import org.cen.ui.gameboard.elements.trajectory.Gauge;
import org.cen.ui.gameboard.elements.trajectory.GaugeFactory;
import org.cen.ui.gameboard.elements.trajectory.IGauge;
import org.cen.ui.gameboard.elements.trajectory.ITrajectoryPath;
import org.cen.ui.gameboard.elements.trajectory.TextTrajectoryFactory;
import org.cen.ui.gameboard.elements.trajectory.XYParser;
import org.cen.ui.trajectories.DisplayedTrajectory;

public class Main implements IGameBoardEventListener {
	private static final int HEIGHT_LISTS = 160;

	private static final int WIDTH_LISTS = 200;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		new Main();
	}

	private IGameBoardElement currentTrajectory = null;

	private CheckListController<DisplayedTrajectory> elementsController;

	private GameBoard2014 gameBoard;

	private GameBoardView gameBoardView;

	private ListController<IInputFile> gaugesController;

	private boolean repaintPending;

	private JPanel statusBar;

	private JLabel statusBarLabel;

	private ListController<IInputFile> trajectoriesController;

	public Main() {
		super();
		initGUI();
	}

	private void addButton(Container c, Action action) {
		JButton button = new JButton(action);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		c.add(button, gbc);
	}

	protected void addCurrentTrajectory() {
		ListSelectionModel selection = trajectoriesController.getSelectionModel();
		int index = selection.getMinSelectionIndex();
		IInputFile path = trajectoriesController.getListModel().getElementAt(index);

		selection = gaugesController.getSelectionModel();
		index = selection.getMinSelectionIndex();
		IInputFile gauge = gaugesController.getListModel().getElementAt(index);

		DefaultListModel<DisplayedTrajectory> model = (DefaultListModel<DisplayedTrajectory>) elementsController.getListModel();
		int start = model.getSize();

		DisplayedTrajectory trajectory = new DisplayedTrajectory(path.getName(), gauge.getName(), currentTrajectory);
		model.addElement(trajectory);

		gameBoard.removeElements(currentTrajectory.getName());

		currentTrajectory = null;
		selection = trajectoriesController.getSelectionModel();
		selection.clearSelection();
		selection = gaugesController.getSelectionModel();
		selection.clearSelection();

		selection = elementsController.getSelectionModel();
		selection.addSelectionInterval(start, start);

		updateGameBoard();
	}

	private void addElementsList(Container c) {
		final DefaultListModel<DisplayedTrajectory> model = new DefaultListModel<DisplayedTrajectory>();
		JList<DisplayedTrajectory> list = new JList<DisplayedTrajectory>(model);
		elementsController = new CheckListController<DisplayedTrajectory>(list);
		final ListSelectionModel selection = elementsController.getSelectionModel();
		selection.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int start = e.getFirstIndex();
				int end = e.getLastIndex();
				for (int i = start; i <= end; i++) {
					DisplayedTrajectory trajectory = model.get(i);
					boolean b = selection.isSelectedIndex(i);
					updateTrajectory(trajectory, b);
				}
			}
		});

		list.setPreferredSize(new Dimension(WIDTH_LISTS, HEIGHT_LISTS));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.VERTICAL;
		c.add(list, gbc);
	}

	protected void addFile(InputFile file) {
		DefaultListModel<IInputFile> model = getModel(file);
		model.addElement(file);
	}

	private void addGaugesList(Container c) {
		InputFilesFactory gaugesFilesFactory = new InputFilesFactory();
		gaugesController = createList("gauges", "/org/cen/test/gauges", gaugesFilesFactory, InputFileType.GAUGE);
		JList<IInputFile> list = gaugesController.getList();
		list.setPreferredSize(new Dimension(WIDTH_LISTS, HEIGHT_LISTS));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.VERTICAL;
		c.add(list, gbc);
	}

	private void addLabel(Container c, String text) {
		JLabel label = new JLabel(text);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		c.add(label, gbc);
	}

	private void addStatusBar(Container c) {
		statusBar = new JPanel();
		statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusBar.setPreferredSize(new Dimension(0, 16));
		statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.X_AXIS));
		c.add(statusBar, BorderLayout.SOUTH);

		statusBarLabel = new JLabel();
		statusBarLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusBar.add(statusBarLabel);
	}

	private void addTrajectoriesList(Container c) {
		InputFilesFactory trajectoryFilesFactory = new InputFilesFactory();
		trajectoriesController = createList("trajectories", "/org/cen/test/trajectories", trajectoryFilesFactory, InputFileType.TRAJECTORY);
		JList<IInputFile> list = trajectoriesController.getList();
		list.setPreferredSize(new Dimension(WIDTH_LISTS, HEIGHT_LISTS));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.VERTICAL;
		c.add(list, gbc);
	}

	private void addTrajectoriesPanel(Container c) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		c.add(panel, BorderLayout.LINE_START);

		addLabel(panel, "Trajectories");
		addTrajectoriesList(panel);
		addLabel(panel, "Gauges");
		addGaugesList(panel);
		addButton(panel, new AbstractAction("add") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				addCurrentTrajectory();
			}
		});
		addLabel(panel, "Displayed");
		addElementsList(panel);
	}

	private ListController<IInputFile> createList(String directory, String packagePath, InputFilesFactory factory, InputFileType type) {
		try {
			URL url = this.getClass().getResource(packagePath);
			if (url != null) {
				URI uri = url.toURI();
				if (uri.getScheme().equals("jar")) {
					FileSystems.newFileSystem(uri, new HashMap<String, String>());
				}
				factory.addSource(uri, type);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		String dir = System.getProperty("user.dir");
		File file = new File(dir, directory);
		URI uri = file.toURI();
		factory.addSource(uri, type);

		final DefaultListModel<IInputFile> listModel = factory.getListModel();
		final JList<IInputFile> list = new JList<IInputFile>(listModel);
		ListController<IInputFile> controller = new ListController<IInputFile>(list);
		final ListSelectionModel selectionModel = controller.getSelectionModel();
		selectionModel.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int start = e.getFirstIndex();
				int end = e.getLastIndex();
				for (int i = start; i <= end; i++) {
					IInputFile file = listModel.get(i);
					boolean selected = selectionModel.isSelectedIndex(i);
					if (selected) {
						displayElement(file);
					}
				}
			}
		});

		watchDirectory(file, type);

		return controller;
	}

	private void displayElement(IInputFile file) {
		switch (file.getType()) {
		case GAUGE:
			displayGaugeElement(file);
			break;
		case TRAJECTORY:
			displayTrajectoryElement(file);
			break;
		}
	}

	private void displayGaugeElement(IInputFile file) {
		if (!(currentTrajectory instanceof ITrajectoryPath)) {
			return;
		}

		ITrajectoryPath path = (ITrajectoryPath) currentTrajectory;

		GaugeFactory factory = new GaugeFactory();
		try (InputStream is = file.getInputStream()) {
			Gauge gauge = factory.getGauge(is);
			path.setGauge(gauge);
		} catch (Exception e) {
			e.printStackTrace();
		}
		updateGameBoard();
	}

	private void displayTrajectoryElement(IInputFile file) {
		String name = file.getName();
		if (currentTrajectory != null && currentTrajectory.getName().equals(name)) {
			return;
		}

		List<IGameBoardElement> elements = gameBoard.getElements();
		elements.remove(currentTrajectory);

		ITrajectoryPath trajectory = loadTrajectory(file);
		if (trajectory == null) {
			return;
		}

		IGameBoardElement element = (IGameBoardElement) trajectory;
		elements.add(element);
		currentTrajectory = element;

		JList<IInputFile> list = gaugesController.getList();
		list.clearSelection();

		updateGameBoard();
	}

	private DefaultListModel<IInputFile> getModel(IInputFile file) {
		DefaultListModel<IInputFile> model = null;
		switch (file.getType()) {
		case GAUGE:
			model = (DefaultListModel<IInputFile>) gaugesController.getListModel();
			break;
		case TRAJECTORY:
			model = (DefaultListModel<IInputFile>) trajectoriesController.getListModel();
			break;
		}
		return model;
	}

	private ListSelectionModel getSelectionModel(IInputFile file) {
		ListSelectionModel model = null;
		switch (file.getType()) {
		case GAUGE:
			model = gaugesController.getSelectionModel();
			break;
		case TRAJECTORY:
			model = trajectoriesController.getSelectionModel();
			break;
		}
		return model;
	}

	protected void handleFileModified(final InputFile file, final InputFileType type) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				updateDisplayedElements(file, type);
			}
		});
	}

	private void handleMouseMove(GameBoardMouseMoveEvent e) {
		Point2D p = e.getPosition();
		double x = p.getX();
		double y = p.getY();
		String s = String.format("x=%.0f mm ; y=%.0f mm", x, y);
		statusBarLabel.setText(s);
	}

	private void initGUI() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = frame.getContentPane();
		c.setLayout(new BorderLayout(10, 10));

		gameBoard = new GameBoard2014();
		gameBoardView = new GameBoardView(gameBoard);
		gameBoardView.setPreferredSize(new Dimension(640, 480));
		gameBoardView.addGameBoardEventListener(this);
		c.add(gameBoardView, BorderLayout.CENTER);

		addStatusBar(c);
		addTrajectoriesPanel(c);
		frame.pack();
		frame.setVisible(true);
	}

	private boolean isSelected(IInputFile file, InputFileType type) {
		DefaultListModel<IInputFile> model = getModel(file);
		ListSelectionModel selection = getSelectionModel(file);
		int index = selection.getMinSelectionIndex();
		boolean b = (index >= 0) && (model.getElementAt(index).equals(file));
		return b;
	}

	private ITrajectoryPath loadTrajectory(IInputFile file) {
		ITrajectoryPath trajectory = null;
		String name = file.getName();
		XYParser parser = new XYParser(";");
		TextTrajectoryFactory factory = new TextTrajectoryFactory(parser);
		try (InputStream is = file.getInputStream()) {
			trajectory = factory.getTrajectoryPath(name, is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return trajectory;
	}

	@Override
	public void onGameBoardEvent(IGameBoardEvent event) {
		if (event instanceof GameBoardMouseMoveEvent) {
			GameBoardMouseMoveEvent e = (GameBoardMouseMoveEvent) event;
			handleMouseMove(e);
		}
	}

	protected void removeFile(InputFile file) {
		DefaultListModel<IInputFile> model = getModel(file);
		model.removeElement(file);
	}

	private void updateDisplayedElements(IInputFile file, InputFileType type) {
		if (isSelected(file, type)) {
			switch (type) {
			case TRAJECTORY:
				// save selected gauge
				ListSelectionModel selection = gaugesController.getSelectionModel();
				int index = selection.getMinSelectionIndex();
				// update trajectory
				if (currentTrajectory != null) {
					gameBoard.removeElements(currentTrajectory.getName());
					currentTrajectory = null;
				}
				displayTrajectoryElement(file);
				// restore selected gauge
				if (index >= 0) {
					selection.addSelectionInterval(index, index);
				}
				break;
			case GAUGE:
				displayGaugeElement(file);
				break;
			}
		}

		String name = file.getName();
		ListModel<DisplayedTrajectory> model = elementsController.getListModel();
		for (int i = 0; i < model.getSize(); i++) {
			DisplayedTrajectory t = model.getElementAt(i);
			ITrajectoryPath trajectory = t.getTrajectoryPath();
			String gaugeLabel = t.getGaugeLabel();
			String pathLabel = t.getPathLabel();
			if (type == InputFileType.GAUGE && gaugeLabel.equals(name)) {
				// update gauge
				GaugeFactory f = new GaugeFactory();
				try (InputStream is = file.getInputStream()) {
					Gauge gauge = f.getGauge(is);
					trajectory.setGauge(gauge);
				} catch (Exception e) {
					e.printStackTrace();
				}

				// update display
				boolean display = t.isVisible();
				updateTrajectory(t, display);
			} else if (type == InputFileType.TRAJECTORY && pathLabel.equals(name)) {
				// reload trajectory
				ITrajectoryPath e = loadTrajectory(file);

				// copy old gauge
				IGauge gauge = t.getGauge();
				e.setGauge(gauge);

				// update display
				updateTrajectory(t, false);
				t.setElement((IGameBoardElement) e);
				boolean display = t.isVisible();
				if (display) {
					updateTrajectory(t, true);
				}
			}
		}
	}

	private void updateGameBoard() {
		if (repaintPending) {
			return;
		}
		repaintPending = true;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				gameBoardView.repaint();
				repaintPending = false;
			}
		});
	}

	protected void updateTrajectory(DisplayedTrajectory trajectory, boolean display) {
		List<IGameBoardElement> elements = gameBoard.getElements();
		IGameBoardElement element = trajectory.getElement();
		if (display && !elements.contains(element)) {
			elements.add(element);
		} else if (!display) {
			elements.remove(element);
		}
		updateGameBoard();
	}

	protected void watchDirectory(File directory, final InputFileType type) {
		try {
			final WatchService watcher = FileSystems.getDefault().newWatchService();
			final Path path = directory.toPath();
			path.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE);

			Timer timer = new Timer();
			TimerTask task = new TimerTask() {
				@Override
				public void run() {
					WatchKey key = watcher.poll();
					if (key == null) {
						return;
					}
					List<WatchEvent<?>> events = key.pollEvents();
					for (WatchEvent<?> event : events) {
						Kind<?> kind = event.kind();
						if (kind == StandardWatchEventKinds.OVERFLOW) {
							continue;
						}

						WatchEvent<Path> ev = (WatchEvent<Path>) event;
						Path filename = ev.context();
						Path p = path.resolve(filename);
						InputFile file = new InputFile(p, type);
						if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
							addFile(file);
						} else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
							removeFile(file);
						} else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
							handleFileModified(file, type);
						}
					}
					key.reset();
				}
			};
			timer.scheduleAtFixedRate(task, 1000, 1000);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
