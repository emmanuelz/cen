package org.cen;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.cen.cup.cup2015.gameboard.GameBoard2015;
import org.cen.services.InputData;
import org.cen.services.InputParser;
import org.cen.services.SerialInputService;
import org.cen.trajectories.IInputFile;
import org.cen.trajectories.InputFile;
import org.cen.trajectories.InputFileType;
import org.cen.trajectories.InputFilesFactory;
import org.cen.ui.CheckListController;
import org.cen.ui.ListController;
import org.cen.ui.gameboard.AbstractGameBoard;
import org.cen.ui.gameboard.GameBoardMouseMoveEvent;
import org.cen.ui.gameboard.GameBoardView;
import org.cen.ui.gameboard.IGameBoardElement;
import org.cen.ui.gameboard.IGameBoardEvent;
import org.cen.ui.gameboard.IGameBoardEventListener;
import org.cen.ui.gameboard.elements.object.IMovable;
import org.cen.ui.gameboard.elements.trajectory.Gauge;
import org.cen.ui.gameboard.elements.trajectory.GaugeFactory;
import org.cen.ui.gameboard.elements.trajectory.IGauge;
import org.cen.ui.gameboard.elements.trajectory.ITrajectoryPath;
import org.cen.ui.gameboard.elements.trajectory.TextTrajectoryFactory;
import org.cen.ui.gameboard.elements.trajectory.XYParser;
import org.cen.ui.trajectories.DisplayedTrajectory;

public class Main implements IGameBoardEventListener {
	private static final String FIELD_DELIMITOR = ";";

	private static final int HEIGHT_LISTS = 160;

	private static final int MATCH_DURATION = 900;

	private static final String TXT_PLAY = "play";

	private static final String TXT_STOP = "stop";

	private static final int WIDTH_LISTS = 200;

	private static final String WORKSPACE_TRAJECTORIES = "workspace\\trajectories.txt";

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

	private AbstractGameBoard gameBoard;

	private GameBoardView gameBoardView;

	private ListController<IInputFile> gaugesController;

	private boolean repaintPending;

	private JPanel statusBar;

	private JLabel statusBarLabel;

	private ListController<IInputFile> trajectoriesController;

	public Main() {
		super();
		initServices();
		initGUI();
		load();
	}

	private void initServices() {
		InputData inputData = new InputData() {
			@Override
			public void setPosition(Point2D position, double angle) {
				setRobotPosition(position, angle);
			}
		};
		InputParser parser = new InputParser(inputData);
		new SerialInputService("COM10", parser);
	}

	protected void setRobotPosition(Point2D position, double angle) {
		System.out.println(position);
		List<IGameBoardElement> elements = gameBoard.findElements("robot");
		for (IGameBoardElement element : elements) {
			if (element instanceof IMovable) {
				IMovable movable = (IMovable) element;
				movable.setPosition(position);
				movable.setOrientation(angle);
				break;
			}
		}
		updateGameBoard();
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

		DisplayedTrajectory trajectory = new DisplayedTrajectory(path, gauge, currentTrajectory);
		addDisplayedTrajectory(trajectory);

		gameBoard.removeElements(currentTrajectory.getName());

		currentTrajectory = null;
		selection = trajectoriesController.getSelectionModel();
		selection.clearSelection();
		selection = gaugesController.getSelectionModel();
		selection.clearSelection();

		updateGameBoard();
	}

	private void addDisplayedTrajectory(DisplayedTrajectory trajectory) {
		DefaultListModel<DisplayedTrajectory> model = (DefaultListModel<DisplayedTrajectory>) elementsController.getListModel();
		int start = model.getSize();
		model.addElement(trajectory);

		ListSelectionModel selection = elementsController.getSelectionModel();
		selection.addSelectionInterval(start, start);
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

	private void addGameBoard(Container c) {
		gameBoard = new GameBoard2015();
		gameBoardView = new GameBoardView(gameBoard);
		gameBoardView.setPreferredSize(new Dimension(640, 480));
		gameBoardView.addGameBoardEventListener(this);
		c.add(gameBoardView, BorderLayout.CENTER);
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

	private void addMenu(JFrame frame) {
		JMenuBar menu = new JMenuBar();

		JMenu file = new JMenu("File");
		menu.add(file);

		Action action = getActionExit();
		JMenuItem menuItem = new JMenuItem(action);
		file.add(menuItem);

		JMenu display = new JMenu("Display");
		menu.add(display);

		action = getActionShowElementsLabels();
		menuItem = new JCheckBoxMenuItem(action);
		display.add(menuItem);

		frame.setJMenuBar(menu);
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

	private void addTimePanel(Container c) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));

		final JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, MATCH_DURATION, 0);
		final JLabel timeLabel = new JLabel("0 s");
		final JButton playButton = new JButton(TXT_PLAY);

		panel.add(slider);
		panel.add(Box.createRigidArea(new Dimension(10, 0)));
		panel.add(timeLabel);
		panel.add(Box.createRigidArea(new Dimension(10, 0)));
		panel.add(playButton);

		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int value = slider.getValue();
				double timestamp = 0.1d * value;
				setTimeStamp(timestamp);
				timeLabel.setText(String.format("%.1f s", timestamp));
			}
		});

		playButton.addActionListener(new ActionListener() {
			final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
			private volatile ScheduledFuture<?> task = null;

			@Override
			public void actionPerformed(ActionEvent e) {
				class Player implements Runnable {
					private int position = slider.getValue();
					private long start = System.currentTimeMillis();

					@Override
					public void run() {
						long now = System.currentTimeMillis();
						long timestamp = (now - start) / 100 + position;
						if (timestamp > MATCH_DURATION) {
							cancel();
						}
						slider.setValue((int) timestamp);
					}

					public void scheduleExecution() {
						task = executor.scheduleAtFixedRate(this, 0, 100, TimeUnit.MILLISECONDS);
					}
				}

				if (task != null) {
					playButton.setText(TXT_PLAY);
					cancel();
				} else {
					playButton.setText(TXT_STOP);
					new Player().scheduleExecution();
				}
			}

			private void cancel() {
				if (task == null) {
					return;
				}
				task.cancel(false);
				task = null;
			}
		});

		c.add(panel, BorderLayout.SOUTH);
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
		addButton(panel, new AbstractAction("remove") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				removeSelectedTrajectory();
			}
		});
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
		IGauge gauge = loadGauge(file);
		path.setGauge(gauge);

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

	private IInputFile findGauge(String pathName) {
		ListModel<IInputFile> model = gaugesController.getListModel();
		return findInModel(model, pathName);
	}

	private IInputFile findInModel(ListModel<IInputFile> model, String pathName) {
		for (int i = 0; i < model.getSize(); i++) {
			IInputFile file = model.getElementAt(i);
			if (file.getName().equals(pathName)) {
				return file;
			}
		}
		return null;
	}

	private IInputFile findPath(String pathName) {
		ListModel<IInputFile> model = trajectoriesController.getListModel();
		return findInModel(model, pathName);
	}

	private Action getActionExit() {
		Action action = new AbstractAction("Exit") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		};
		return action;
	}

	private Action getActionShowElementsLabels() {
		Action action = new AbstractAction("Name of the elements") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean b = gameBoardView.getDisplayLabels();
				gameBoardView.setDisplayLabels(!b);
				updateGameBoard();
			}
		};
		return action;
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
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				save();
				super.windowClosing(e);
			}
		});
		Container c = frame.getContentPane();
		c.setLayout(new BorderLayout(10, 10));

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.PAGE_AXIS));

		addGameBoard(centerPanel);
		addTimePanel(centerPanel);

		addStatusBar(c);
		addTrajectoriesPanel(c);

		c.add(centerPanel, BorderLayout.CENTER);

		addMenu(frame);

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

	private void load() {
		String dir = System.getProperty("user.dir");
		File file = new File(dir, WORKSPACE_TRAJECTORIES);
		try (FileReader fr = new FileReader(file); Scanner scanner = new Scanner(fr)) {
			scanner.useDelimiter("[;\r\n]+");
			while (scanner.hasNext()) {
				String pathName = scanner.next();
				String gaugeName = scanner.next();
				IInputFile pathFile = findPath(pathName);
				IInputFile gaugeFile = findGauge(gaugeName);

				if (pathFile == null || gaugeFile == null) {
					continue;
				}

				ITrajectoryPath element = loadTrajectory(pathFile);
				IGauge gauge = loadGauge(gaugeFile);
				element.setGauge(gauge);

				DisplayedTrajectory trajectory = new DisplayedTrajectory(pathFile, gaugeFile, (IGameBoardElement) element);
				addDisplayedTrajectory(trajectory);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private IGauge loadGauge(IInputFile file) {
		GaugeFactory factory = new GaugeFactory();
		try (InputStream is = file.getInputStream()) {
			IGauge gauge = factory.getGauge(is);
			return gauge;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private ITrajectoryPath loadTrajectory(IInputFile file) {
		ITrajectoryPath trajectory = null;
		String name = file.getName();
		XYParser parser = new XYParser(FIELD_DELIMITOR);
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

	protected void removeSelectedTrajectory() {
		JList<DisplayedTrajectory> list = elementsController.getList();
		DisplayedTrajectory trajectory = list.getSelectedValue();
		if (trajectory == null) {
			return;
		}

		DefaultListModel<DisplayedTrajectory> model = (DefaultListModel<DisplayedTrajectory>) elementsController.getListModel();
		model.removeElement(trajectory);

		IGameBoardElement element = trajectory.getElement();
		List<IGameBoardElement> elements = gameBoard.getElements();
		elements.remove(element);

		updateGameBoard();
	}

	private void replaceElement(final IGameBoardElement oldElement, final IGameBoardElement newElement) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				List<IGameBoardElement> elements = gameBoard.getElements();
				elements.remove(oldElement);
				elements.add(newElement);
				updateGameBoard();
			}
		});
	}

	private void save() {
		ListModel<DisplayedTrajectory> model = (DefaultListModel<DisplayedTrajectory>) elementsController.getListModel();
		String dir = System.getProperty("user.dir");
		File file = new File(dir, WORKSPACE_TRAJECTORIES);
		try (FileWriter fw = new FileWriter(file)) {
			for (int i = 0; i < model.getSize(); i++) {
				DisplayedTrajectory trajectory = model.getElementAt(i);
				IInputFile path = trajectory.getPathFile();
				IInputFile gauge = trajectory.getGaugeFile();
				String s = path.getName() + FIELD_DELIMITOR + gauge.getName() + "\r\n";
				fw.write(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void setTimeStamp(double timestamp) {
		gameBoardView.setTimestamp(timestamp);
		updateGameBoard();
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
				IGameBoardElement oldElement = t.getElement();
				IGameBoardElement newElement = (IGameBoardElement) e;
				t.setElement(newElement);
				replaceElement(oldElement, newElement);
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

	protected void updateTrajectory(final DisplayedTrajectory trajectory, final boolean display) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				List<IGameBoardElement> elements = gameBoard.getElements();
				IGameBoardElement element = trajectory.getElement();
				if (display && !elements.contains(element)) {
					elements.add(element);
				} else if (!display) {
					elements.remove(element);
				}
				updateGameBoard();
			}
		});
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
