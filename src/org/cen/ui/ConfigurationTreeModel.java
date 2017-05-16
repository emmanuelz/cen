package org.cen.ui;

import java.util.List;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import org.cen.models.IModel;
import org.cen.models.MatchModel;
import org.cen.models.ModelType;
import org.cen.models.RobotModel;
import org.cen.models.RobotTrajectoryModel;
import org.cen.models.actuators.ActuatorModel;
import org.cen.models.actuators.ActuatorStateModel;
import org.cen.models.trajectories.TrajectoryElementModel;
import org.cen.models.trajectories.TrajectoryModel;

public class ConfigurationTreeModel extends DefaultTreeModel {
	private class ModelPlaceholder {
		private DefaultMutableTreeNode parent;
		private ModelType type;

		public ModelPlaceholder(DefaultMutableTreeNode parent, ModelType type) {
			super();
			this.parent = parent;
			this.type = type;
		}

		public DefaultMutableTreeNode getParent() {
			return parent;
		}

		public ModelType getType() {
			return type;
		}

		@Override
		public String toString() {
			String s;
			switch (type) {
			case ACTUATOR_STATE:
				s = "state";
				break;
			case MATCH:
				s = "match";
				break;
			case ROBOT:
				s = "robot";
				break;
			case TRAJECTORY:
				s = "trajectory";
				break;
			case TRAJECTORY_ELEMENT:
				s = "trajectory element";
				break;
			default:
				s = "";
				break;
			}
			s = String.format("<add new %s>", s);
			return s;
		}
	}

	private class ModelTreeNode extends DefaultMutableTreeNode {
		public ModelTreeNode(IModel model) {
			super(model);
		}

		@Override
		public String toString() {
			Object object = getUserObject();
			if (object instanceof IModel) {
				IModel model = (IModel) object;
				return model.getName();
			}
			return object.toString();
		}
	}

	private static final long serialVersionUID = 1L;

	private DefaultMutableTreeNode top;

	public ConfigurationTreeModel() {
		super(null);
		top = new DefaultMutableTreeNode();
		addPlaceholder(top, ModelType.MATCH);
		setRoot(top);
	}

	private void addActuator(DefaultMutableTreeNode parent, ActuatorModel actuator) {
		ModelTreeNode node = new ModelTreeNode(actuator);
		addActuatorStates(node, actuator.getStates());
		addNode(parent, node);
	}

	private void addNode(MutableTreeNode parent, MutableTreeNode node) {
		int n = parent.getChildCount();
		insertNodeInto(node, parent, (n > 0) ? n - 1 : 0);
	}

	private void addActuators(DefaultMutableTreeNode parent, Map<String, ActuatorModel> actuators) {
		for (ActuatorModel actuator : actuators.values()) {
			addActuator(parent, actuator);
		}
	}

	private void addActuatorState(DefaultMutableTreeNode parent, ActuatorStateModel state) {
		ModelTreeNode node = new ModelTreeNode(state);
		addNode(parent, node);
	}

	private void addActuatorStates(DefaultMutableTreeNode parent, Map<String, ActuatorStateModel> states) {
		for (ActuatorStateModel state : states.values()) {
			addActuatorState(parent, state);
		}
	}

	public void addMatch(MatchModel model) {
		ModelTreeNode node = new ModelTreeNode(model);
		addRobots(node, model.getRobots());
		addPlaceholder(node, ModelType.ROBOT);
		addNode(top, node);
	}

	private void addPlaceholder(DefaultMutableTreeNode parent, ModelType type) {
		ModelPlaceholder placeholder = new ModelPlaceholder(parent, type);
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(placeholder);
		parent.add(node);
	}

	private void addRobot(DefaultMutableTreeNode parent, MatchModel match, RobotModel model) {
		List<RobotTrajectoryModel> list = match.getRobots();
		RobotTrajectoryModel robot = new RobotTrajectoryModel(model);
		list.add(robot);
		addRobot(parent, robot);
	}

	private void addRobot(DefaultMutableTreeNode parent, RobotTrajectoryModel model) {
		ModelTreeNode node = new ModelTreeNode(model);
		addRobotModel(node, model.getRobot());
		addNode(parent, node);
		parent = node;

		DefaultMutableTreeNode trajectoriesNode = new DefaultMutableTreeNode("Trajectories");
		addTrajectories(trajectoriesNode, model.getTrajectories());
		addPlaceholder(trajectoriesNode, ModelType.TRAJECTORY);
		parent.add(trajectoriesNode);
	}

	private void addRobotModel(DefaultMutableTreeNode parent, RobotModel robot) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("Actuators");
		addActuators(node, robot.getActuators());
		parent.add(node);
	}

	private void addRobots(DefaultMutableTreeNode parent, List<RobotTrajectoryModel> robots) {
		for (RobotTrajectoryModel model : robots) {
			addRobot(parent, model);
		}
	}

	public void addToNode(Object node, Object model) {
		if (!(node instanceof DefaultMutableTreeNode)) {
			return;
		}
		DefaultMutableTreeNode n = (DefaultMutableTreeNode) node;
		Object placeholder = n.getUserObject();
		if (!(placeholder instanceof ModelPlaceholder)) {
			return;
		}
		ModelPlaceholder p = (ModelPlaceholder) placeholder;
		DefaultMutableTreeNode parent = p.getParent();
		if (model instanceof MatchModel) {
			addMatch((MatchModel) model);
		} else if (model instanceof RobotModel) {
			MatchModel match = (MatchModel) parent.getUserObject();
			addRobot(parent, match, (RobotModel) model);
		} else if (model instanceof TrajectoryModel) {
			DefaultMutableTreeNode robotNode = (DefaultMutableTreeNode) parent.getParent();
			RobotTrajectoryModel robot = (RobotTrajectoryModel) robotNode.getUserObject();
			addTrajectory(parent, robot, (TrajectoryModel) model);
		} else if (model instanceof TrajectoryElementModel) {
			TrajectoryModel trajectory = (TrajectoryModel) parent.getUserObject();
			addTrajectoryElement(parent, trajectory, (TrajectoryElementModel) model);
		}
	}

	private void addTrajectoryElement(DefaultMutableTreeNode parent, TrajectoryModel trajectory, TrajectoryElementModel model) {
		List<TrajectoryElementModel> elements = trajectory.getElements();
		elements.add(model);
		addTrajectoryElement(parent, model);
	}

	private void addTrajectories(DefaultMutableTreeNode parent, Map<String, TrajectoryModel> trajectories) {
		for (TrajectoryModel trajectory : trajectories.values()) {
			addTrajectory(parent, trajectory);
		}
	}

	private void addTrajectory(DefaultMutableTreeNode parent, RobotTrajectoryModel robot, TrajectoryModel model) {
		Map<String, TrajectoryModel> trajectories = robot.getTrajectories();
		String key = model.getName();
		trajectories.put(key, model);
		addTrajectory(parent, model);
	}

	private void addTrajectory(DefaultMutableTreeNode parent, TrajectoryModel trajectory) {
		ModelTreeNode node = new ModelTreeNode(trajectory);
		addTrajectoryElements(node, trajectory.getElements());
		addPlaceholder(node, ModelType.TRAJECTORY_ELEMENT);
		addNode(parent, node);
	}

	private void addTrajectoryElement(DefaultMutableTreeNode parent, TrajectoryElementModel element) {
		ModelTreeNode node = new ModelTreeNode(element);
		addNode(parent, node);
	}

	private void addTrajectoryElements(DefaultMutableTreeNode parent, List<TrajectoryElementModel> elements) {
		for (TrajectoryElementModel element : elements) {
			addTrajectoryElement(parent, element);
		}
	}

	public MatchModel getMatch(TreePath path) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getPathComponent(1);
		Object model = node.getUserObject();
		if (model instanceof MatchModel) {
			return (MatchModel) model;
		}
		return null;
	}

	public ModelType getNodeType(TreePath path) {
		if (path == null) {
			return null;
		}
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
		Object object = node.getUserObject();
		if (object instanceof ModelPlaceholder) {
			ModelPlaceholder placeholder = (ModelPlaceholder) object;
			ModelType type = placeholder.getType();
			return type;
		}
		return null;
	}
}
