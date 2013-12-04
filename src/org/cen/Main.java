package org.cen;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
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
import org.cen.trajectories.ITrajectoryFile;
import org.cen.trajectories.TrajectoryFile;
import org.cen.trajectories.TrajectoryFilesFactory;
import org.cen.ui.CheckListController;
import org.cen.ui.gameboard.GameBoardMouseMoveEvent;
import org.cen.ui.gameboard.GameBoardView;
import org.cen.ui.gameboard.IGameBoardElement;
import org.cen.ui.gameboard.IGameBoardEvent;
import org.cen.ui.gameboard.IGameBoardEventListener;
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

	private JPanel statusBar;

	private JLabel statusBarLabel;

	private DefaultListModel<ITrajectoryFile> trajectoriesModel;

	private ListSelectionModel trajectoriesSelection;

	public Main() {
		super();
		initGUI();
	}

	private void addTrajectoriesList(Container c) {
		TrajectoryFilesFactory trajectoryFilesFactory = new TrajectoryFilesFactory();
		String dir = System.getProperty("user.dir");
		File file = new File(dir, "trajectories");
		URI uri = file.toURI();
		watchDirectory(file);
		trajectoryFilesFactory.addSource(uri);
		try {
			URL url = this.getClass().getResource("/org/cen/test");
			uri = url.toURI();
			if (uri.getScheme().equals("jar")) {
				FileSystems.newFileSystem(uri, new HashMap<String, String>());
			}
			trajectoryFilesFactory.addSource(uri);
		} catch (Exception e) {
			e.printStackTrace();
		}

		trajectoriesModel = trajectoryFilesFactory.getListModel();
		final JList<ITrajectoryFile> list = new JList<ITrajectoryFile>(trajectoriesModel);
		CheckListController<ITrajectoryFile> controller = new CheckListController<ITrajectoryFile>(list);
		trajectoriesSelection = controller.getSelectionModel();
		trajectoriesSelection.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int start = e.getFirstIndex();
				int end = e.getLastIndex();
				for (int i = start; i <= end; i++) {
					ITrajectoryFile file = trajectoriesModel.get(i);
					boolean b = trajectoriesSelection.isSelectedIndex(i);
					updateElement(file, b);
				}
			}
		});

		list.setPreferredSize(new Dimension(160, 320));
		c.add(list, BorderLayout.LINE_START);
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
		addTrajectoriesList(c);
		frame.pack();
		frame.setVisible(true);
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

	private boolean isDisplayed(ITrajectoryFile file) {
		int i = trajectoriesModel.indexOf(file);
		boolean b = trajectoriesSelection.isSelectedIndex(i);
		return b;
	}

	@Override
	public void onGameBoardEvent(IGameBoardEvent event) {
		if (event instanceof GameBoardMouseMoveEvent) {
			GameBoardMouseMoveEvent e = (GameBoardMouseMoveEvent) event;
			handleMouseMove(e);
		}
	}

	private void updateElement(ITrajectoryFile file, boolean display) {
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

	protected void watchDirectory(File directory) {
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
						TrajectoryFile file = new TrajectoryFile(p);
						if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
							addFile(file);
						} else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
							removeFile(file);
						} else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
							boolean b = isDisplayed(file);
							updateElement(file, b);
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

	protected void removeFile(TrajectoryFile file) {
		updateElement(file, false);
		trajectoriesModel.removeElement(file);
	}

	protected void addFile(TrajectoryFile file) {
		trajectoriesModel.addElement(file);
	}
}
