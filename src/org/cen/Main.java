package org.cen;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
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

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
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
import org.cen.ui.gameboard.GameBoardMouseMoveEvent;
import org.cen.ui.gameboard.GameBoardView;
import org.cen.ui.gameboard.IGameBoardElement;
import org.cen.ui.gameboard.IGameBoardEvent;
import org.cen.ui.gameboard.IGameBoardEventListener;
import org.cen.ui.gameboard.elements.trajectory.Gauge;
import org.cen.ui.gameboard.elements.trajectory.GaugeFactory;
import org.cen.ui.gameboard.elements.trajectory.ITrajectoryPath;
import org.cen.ui.gameboard.elements.trajectory.TextTrajectoryFactory;
import org.cen.ui.gameboard.elements.trajectory.XYParser;

public class Main implements IGameBoardEventListener {
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

	private GameBoard2014 gameBoard;

	private GameBoardView gameBoardView;

	private CheckListController<IInputFile> gaugesController;

	private JPanel statusBar;

	private JLabel statusBarLabel;

	private CheckListController<IInputFile> trajectoriesController;

	public Main() {
		super();
		initGUI();
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

	protected void addFile(InputFile file) {
		DefaultListModel<IInputFile> model = getModel(file);
		model.addElement(file);
	}

	private void addGaugesList(Container c) {
		InputFilesFactory gaugesFilesFactory = new InputFilesFactory();
		gaugesController = createList("gauges", "/org/cen/test/gauges", gaugesFilesFactory, InputFileType.GAUGE);
		JList<IInputFile> list = gaugesController.getList();
		list.setPreferredSize(new Dimension(160, 320));
		c.add(list);
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
		list.setPreferredSize(new Dimension(160, 320));
		c.add(list);
	}

	private void addTrajectoriesPanel(Container c) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 1));
		c.add(panel, BorderLayout.LINE_START);

		addTrajectoriesList(panel);
		addGaugesList(panel);
	}

	private CheckListController<IInputFile> createList(String directory, String packagePath, InputFilesFactory factory, InputFileType type) {
		try {
			URL url = this.getClass().getResource(packagePath);
			URI uri = url.toURI();
			if (uri.getScheme().equals("jar")) {
				FileSystems.newFileSystem(uri, new HashMap<String, String>());
			}
			factory.addSource(uri, type);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String dir = System.getProperty("user.dir");
		File file = new File(dir, directory);
		URI uri = file.toURI();
		factory.addSource(uri, type);

		final DefaultListModel<IInputFile> listModel = factory.getListModel();
		final JList<IInputFile> list = new JList<IInputFile>(listModel);
		CheckListController<IInputFile> controller = new CheckListController<IInputFile>(list);
		final ListSelectionModel selectionModel = controller.getSelectionModel();
		selectionModel.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int start = e.getFirstIndex();
				int end = e.getLastIndex();
				for (int i = start; i <= end; i++) {
					IInputFile file = listModel.get(i);
					boolean b = selectionModel.isSelectedIndex(i);
					updateElement(file, b);
				}
			}
		});

		watchDirectory(file, listModel, selectionModel, type);

		return controller;
	}

	protected void handleFileModified(InputFile file, DefaultListModel<IInputFile> listModel, ListSelectionModel selectionModel) {
		boolean b = isDisplayed(file, listModel, selectionModel);
		updateElement(file, b);
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

	private boolean isDisplayed(InputFile file, DefaultListModel<IInputFile> listModel, ListSelectionModel selectionModel) {
		int i = listModel.indexOf(file);
		boolean b = selectionModel.isSelectedIndex(i);
		return b;
	}

	@Override
	public void onGameBoardEvent(IGameBoardEvent event) {
		if (event instanceof GameBoardMouseMoveEvent) {
			GameBoardMouseMoveEvent e = (GameBoardMouseMoveEvent) event;
			handleMouseMove(e);
		}
	}

	protected void removeFile(InputFile file) {
		updateElement(file, false);
		DefaultListModel<IInputFile> model = getModel(file);
		model.removeElement(file);
	}

	private void updateElement(IInputFile file, boolean display) {
		switch (file.getType()) {
		case GAUGE:
			updateGaugeElement(file, display);
			break;
		case TRAJECTORY:
			updateTrajectoryElement(file, display);
			break;
		}
	}

	private void updateGaugeElement(IInputFile file, boolean display) {
		ListSelectionModel selection = trajectoriesController.getSelectionModel();
		int start = selection.getMinSelectionIndex();
		int end = selection.getMaxSelectionIndex();
		for (int i = start; i <= end; i++) {
			if (selection.isSelectedIndex(i)) {
				DefaultListModel<IInputFile> model = (DefaultListModel<IInputFile>) trajectoriesController.getListModel();
				IInputFile item = model.elementAt(i);
				String name = item.getName();
				List<IGameBoardElement> elements = gameBoard.findElements(name);
				if (elements.size() == 0) {
					continue;
				}
				IGameBoardElement element = elements.get(0);
				if (!(element instanceof ITrajectoryPath)) {
					continue;
				}

				ITrajectoryPath path = (ITrajectoryPath) element;
				if (!display) {
					path.setGauge(null);
					continue;
				}

				GaugeFactory factory = new GaugeFactory();
				name = file.getPath().toString();
				try {
					Gauge gauge = factory.getGauge(name);
					path.setGauge(gauge);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		gameBoardView.repaint();
	}

	private void updateTrajectoryElement(IInputFile file, boolean display) {
		String name = file.getName();
		gameBoard.removeElements(name);

		if (!display) {
			gameBoardView.repaint();
			return;
		}

		XYParser parser = new XYParser(";");
		TextTrajectoryFactory factory = new TextTrajectoryFactory(parser);

		ITrajectoryPath trajectory = null;
		Path path = file.getPath();
		try {
			URI uri = path.toUri();
			URL url = uri.toURL();
			InputStream is = url.openStream();
			try {
				trajectory = factory.getTrajectoryPath(name, is);
			} finally {
				is.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (trajectory == null) {
			return;
		}
		IGameBoardElement element = (IGameBoardElement) trajectory;
		List<IGameBoardElement> elements = gameBoard.getElements();
		elements.add(element);
		gameBoardView.repaint();
	}

	protected void watchDirectory(File directory, final DefaultListModel<IInputFile> listModel, final ListSelectionModel selectionModel, final InputFileType type) {
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
							handleFileModified(file, listModel, selectionModel);
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
