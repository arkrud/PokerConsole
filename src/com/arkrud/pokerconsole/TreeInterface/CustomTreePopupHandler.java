package com.arkrud.pokerconsole.TreeInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.arkrud.pokerconsole.Poker.PokerAction;
import com.arkrud.pokerconsole.Poker.PokerHandSizing;
import com.arkrud.pokerconsole.Poker.PokerOpponentPosition;
import com.arkrud.pokerconsole.Poker.PokerPosition;
import com.arkrud.pokerconsole.Poker.PokerStrategy;
import com.arkrud.pokerconsole.UI.Dashboard.Dashboard;
import com.arkrud.pokerconsole.Util.INIFilesFactory;
import com.arkrud.pokerconsole.Util.UtilMethodsFactory;

/**
 * The Class CustomTreePopupHandler.<br>
 * Creates drop-down menu on right click on tree nodes.<br>
 * Specific menu items appear depending on tree nodes user object type. <br>
 * Defines the methods for menu actions.
 */
public class CustomTreePopupHandler implements ActionListener {
	/**
	 * Private JTree object variable to pass the constructor parameter to class methods.
	 */
	private JTree tree;
	/**
	 * Private Dashboard object variable to pass the constructor parameter to class methods.
	 */
	private Dashboard dash;
	/**
	 * Private boolean variable to pass the constructor parameter to class methods. Flag to define if the charts presented in scrollable frames are
	 * editable.<br>
	 *
	 */
	private boolean editable;
	/**
	 * Reference to CustomTreeListener object used in private method.
	 */
	private CustomTreeMouseListener customTreeMouseListener;
	/**
	 * Private CustomTree object variable to pass the constructor parameter to class methods.
	 */
	private CustomTree theTree;

	/**
	 * Instantiates a new custom tree popup handler class.<br>
	 * Pass variables values into the class.<br>
	 * Add Mouse listener to control which menu items will show up in drop-down menu.
	 *
	 * @param tree
	 *            JTree object passed from CustomTree class
	 * @param popup
	 *            JPopupMenu object passed from CustomTree class
	 * @param dash
	 *            Dashboard object passed from CustomTree class
	 * @param theTree
	 *            CustomTree object passed from CustomTree class
	 * @param editable
	 *            Flag to define if the charts presented in scrollable frames are editable
	 */
	public CustomTreePopupHandler(JTree tree, JPopupMenu popup, Dashboard dash, CustomTree theTree, boolean editable) {
		this.tree = tree;
		this.dash = dash;
		this.editable = editable;
		this.theTree = theTree;
		customTreeMouseListener = new CustomTreeMouseListener(popup, dash, editable);
		tree.addMouseListener(customTreeMouseListener);
	}

	/*
	 * Performed actions on the drop-down menu items click.<br> <ul> <li>Get action command string. <li>Get current TreePath from the JTree object. <li>Get
	 * current node from the TreePath. <li>Get user object from node. <li>Perform actions based on user object type and menu string. <ul>
	 */
	// Perform actions on drop-down menu selections
	@Override
	public void actionPerformed(ActionEvent e) {
		String ac = e.getActionCommand();
		DefaultMutableTreeNode node = null;
		TreePath path = tree.getPathForLocation(customTreeMouseListener.getLoc().x, customTreeMouseListener.getLoc().y);
		node = (DefaultMutableTreeNode) path.getLastPathComponent();
		Object obj = ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();
		if (obj instanceof PokerStrategy) {
			if (ac.equals("REFRESH")) {
				theTree.refreshTreeNode(node, ((PokerStrategy) obj).getNodeText());
			} else if (ac.equals("ADD ACTION")) {
				addPokerAction(node, obj);
			} else if (ac.equals("REMOVE")) {
				removePokerStrategy(node);
			} else if (ac.equals("DUPLICATE")) {
				duplicateSolution(node);
			}
		} else if (obj instanceof PokerAction) {
			if (ac.equals("ADD SIZING")) {
				addPokerSizing(node, obj);
			} else if (ac.equals("ADD HANDS")) {
				UtilMethodsFactory.showDialogToDesctop("AddHandsDialog", 210, 360, dash, tree, theTree, obj, node, null, null);
			} else if (ac.equals("ADD OPPONENTS POSITION")) {
				addOpponentPosition(node);
			} else if (ac.equals("REMOVE")) {
				removePokerAction(node, obj);
			} else {
			}
		} else if (obj instanceof PokerHandSizing) {
			if (ac.equals("DELETE SIZING")) {
				removePokerSizing(node, obj);
			} else if (ac.equals("ADD HANDS")) {
				UtilMethodsFactory.showDialogToDesctop("AddHandsDialog", 210, 360, dash, tree, theTree, obj, node, null, null);
			} else if (ac.equals("ADD OPPONENTS POSITION")) {
				addOpponentPosition(node);
			} else {
			}
		} else if (obj instanceof PokerPosition) {
			if (ac.equals("REMOVE")) {
				removePokerPositon(node, obj);
			} else if (ac.equals("ADD OPPONENTS POSITION")) {
				addOpponentPosition(node);
			}
		} else if (obj instanceof PokerOpponentPosition) {
			if (ac.equals("APPLY TEMPLATE")) {
				applyTemplate(obj);
			} else if (ac.equals("REMOVE")) {
				removePokerOpponentPositon(node, obj);
			} else {
			}
		} else {
		}
	}

	/**
	 * Adds the PokerOpponentPosition (POP) node to the solution tree PokerAction(PA), PokerSizing(PZ), or PokerPosition(PP) branch.
	 *
	 * <ul>
	 * <li>Opens input dialog for user to provide the name of the POP and convert user input to all upper case letters.
	 * <li>Get top of the tree representing poker solution user is working with.
	 * <li>Count how many POP nodes are already added if any.
	 * <li>Initialize POP object with the name desired by user with consecutive number prefix to lined up POP nodes in the tree in the order they are added.
	 * <li>Set the POP object selection property to false.
	 * <li>Depending on which branch object type POP is added to proceed with initializing respective PA, PZ, or PP object.
	 * <li>Construct file system path based on POP leaf tree path and INI file extension with the Images directory of the application file structure as root
	 * folder.
	 * <li>Copy chart INI file blank template to the file system location to constructed relative path.
	 * <li>Set chart window title property of POP object to dash-delimited name constructed based on object names in POP leaf tree path.
	 * <li>Set the title of tree JTabbedPane tab
	 * <li>Set chart image path property to dash-delimited name constructed from the root of the file system and same relative path.
	 * <li>If root is PP check if the parent of PP node is HS or PA node to provide respective path.
	 * <li>Display new POP chart frame in the desktop area.
	 * </ul>
	 *
	 * @param node
	 *            The parent node.
	 */
	private void addOpponentPosition(DefaultMutableTreeNode node) {
		String s = ((String) JOptionPane.showInputDialog(dash, "New Opponets Position", "Add Opponets Position", JOptionPane.PLAIN_MESSAGE, null, null, null))
				.toUpperCase();
		if (s != null) {
			if (checkForNewObjectName(node, s)) {
				JOptionPane.showConfirmDialog(null, "This Opponents Position is Already there", "Duplicated Opponents Position Warning",
						JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
			} else {
				if ((s != null) && (s.length() > 0)) {
					DefaultMutableTreeNode top = (DefaultMutableTreeNode) tree.getModel().getRoot();
					int opponentsHandsNodesCount = countRelevantNodes(node);
					System.out.println("opponentsHandsNodesCount: " + opponentsHandsNodesCount);
					String oldTreeName = dash.getTreeTabbedPane().getTitleAt(dash.getTreeTabbedPane().getSelectedIndex());
					String oldAppStatus = INIFilesFactory.getItemValueFromINI(UtilMethodsFactory.getConsoleConfig(), "Applications", oldTreeName);
					String oldAutoNamingStatus = INIFilesFactory.getItemValueFromINI(UtilMethodsFactory.getConsoleConfig(), "Autonaming", oldTreeName);
					PokerOpponentPosition pokerOpponentPosition = new PokerOpponentPosition(Integer.toString(opponentsHandsNodesCount + 1) + s);
					pokerOpponentPosition.setSelected(false);
					if (node.getUserObject() instanceof PokerAction) {
						PokerAction pokerAction = (PokerAction) (node.getUserObject());
						String relativePath = "Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/" + pokerAction.getNodeText() + "/"
								+ Integer.toString(opponentsHandsNodesCount + 1) + s + ".ini";
						copyBlankChartINIFile(UtilMethodsFactory.getConfigPath() + relativePath);
						pokerOpponentPosition.setChartPaneTitle(pokerAction.getNodeText() + "-" + s);
						pokerOpponentPosition.setChartImagePath(relativePath);
						String newTabTitle = ((PokerStrategy) top.getUserObject()).getNodeText() + "-" + pokerAction.getNodeText() + "-" + s;
						if (!editable) {
							dash.getTreeTabbedPane().setTitleAt(dash.getTreeTabbedPane().getSelectedIndex(), newTabTitle);
							INIFilesFactory.updateINIFileItemName(UtilMethodsFactory.getConsoleConfig(), "Selections", newTabTitle, oldTreeName);
							INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Selections", pokerAction.getNodeText() + "-" + s,
									newTabTitle);
							INIFilesFactory.updateINIFileItemName(UtilMethodsFactory.getConsoleConfig(), "Applications", newTabTitle, oldTreeName);
							INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Applications", oldAppStatus, newTabTitle);
							INIFilesFactory.updateINIFileItemName(UtilMethodsFactory.getConsoleConfig(), "Autonaming", newTabTitle, oldTreeName);
							INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Autonaming", oldAutoNamingStatus, newTabTitle);
						} else {
							INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Selections", pokerAction.getNodeText() + "-" + s,
									oldTreeName);
						}
					} else if (node.getUserObject() instanceof PokerHandSizing) {
						PokerHandSizing pokerHandSizing = (PokerHandSizing) (node.getUserObject());
						String relativePath = "Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/"
								+ pokerHandSizing.getPokerAction().getNodeText() + "/" + pokerHandSizing.getNodeText() + "/"
								+ Integer.toString(opponentsHandsNodesCount + 1) + s + ".ini";
						copyBlankChartINIFile(UtilMethodsFactory.getConfigPath() + relativePath);
						pokerOpponentPosition.setChartPaneTitle(pokerHandSizing.getPokerAction().getNodeText() + "-" + pokerHandSizing.getNodeText() + "-" + s);
						String newTabTitle = ((PokerStrategy) top.getUserObject()).getNodeText() + "-" + pokerHandSizing.getPokerAction().getNodeText() + "-"
								+ pokerHandSizing.getNodeText() + "-" + s;
						if (!editable) {
							dash.getTreeTabbedPane().setTitleAt(dash.getTreeTabbedPane().getSelectedIndex(), newTabTitle);
							INIFilesFactory.updateINIFileItemName(UtilMethodsFactory.getConsoleConfig(), "Selections", newTabTitle, oldTreeName);
							INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Selections",
									pokerHandSizing.getPokerAction().getNodeText() + "-" + pokerHandSizing.getNodeText() + "-" + s, newTabTitle);
							INIFilesFactory.updateINIFileItemName(UtilMethodsFactory.getConsoleConfig(), "Applications", newTabTitle, oldTreeName);
							INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Applications", oldAppStatus, newTabTitle);
							INIFilesFactory.updateINIFileItemName(UtilMethodsFactory.getConsoleConfig(), "Autonaming", newTabTitle, oldTreeName);
							INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Autonaming", oldAutoNamingStatus, newTabTitle);
						} else {
							INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Selections",
									pokerHandSizing.getPokerAction().getNodeText() + "-" + pokerHandSizing.getNodeText() + "-" + s, oldTreeName);
						}
						pokerOpponentPosition.setChartImagePath(relativePath);
					} else if (node.getUserObject() instanceof PokerPosition) {
						PokerPosition pokerPosition = (PokerPosition) (node.getUserObject());
						Object treeObject = ((DefaultMutableTreeNode) node.getParent()).getUserObject();
						if (treeObject instanceof PokerHandSizing) {
							PokerHandSizing pokerHandSizing = (PokerHandSizing) treeObject;
							String relativePath = "Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/"
									+ pokerHandSizing.getPokerAction().getNodeText() + "/" + pokerHandSizing.getNodeText() + "/" + pokerPosition.getNodeText()
									+ "/" + Integer.toString(node.getChildCount() + 1) + s + ".ini";
							copyBlankChartINIFile(UtilMethodsFactory.getConfigPath() + relativePath);
							pokerOpponentPosition.setChartPaneTitle(pokerHandSizing.getPokerAction().getNodeText() + "-" + pokerHandSizing.getNodeText() + "-"
									+ pokerPosition.getNodeText() + "-" + s);
							String newTabTitle = ((PokerStrategy) top.getUserObject()).getNodeText() + "-" + pokerHandSizing.getPokerAction().getNodeText()
									+ "-" + pokerHandSizing.getNodeText() + "-" + pokerPosition.getNodeText() + "-" + s;
							if (!editable) {
								dash.getTreeTabbedPane().setTitleAt(dash.getTreeTabbedPane().getSelectedIndex(), newTabTitle);
								INIFilesFactory.updateINIFileItemName(UtilMethodsFactory.getConsoleConfig(), "Selections", newTabTitle, oldTreeName);
								INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Selections",
										pokerHandSizing.getPokerAction().getNodeText() + "-" + pokerHandSizing.getNodeText() + "-" + pokerPosition.getNodeText()
												+ "-" + s,
										newTabTitle);
								INIFilesFactory.updateINIFileItemName(UtilMethodsFactory.getConsoleConfig(), "Applications", newTabTitle, oldTreeName);
								INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Applications", oldAppStatus, newTabTitle);
								INIFilesFactory.updateINIFileItemName(UtilMethodsFactory.getConsoleConfig(), "Autonaming", newTabTitle, oldTreeName);
								INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Autonaming", oldAutoNamingStatus, newTabTitle);
							} else {
								INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Selections",
										pokerHandSizing.getPokerAction().getNodeText() + "-" + pokerHandSizing.getNodeText() + "-" + pokerPosition.getNodeText()
												+ "-" + s,
										oldTreeName);
							}
							pokerOpponentPosition.setChartImagePath(relativePath);
						} else if (treeObject instanceof PokerAction) {
							PokerAction pokerAction = (PokerAction) treeObject;
							String relativePath = "Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/" + pokerAction.getNodeText() + "/"
									+ pokerPosition.getNodeText() + "/" + Integer.toString(node.getChildCount() + 1) + s + ".ini";
							copyBlankChartINIFile(UtilMethodsFactory.getConfigPath() + relativePath);
							pokerOpponentPosition.setChartPaneTitle(pokerAction.getNodeText() + "-" + pokerPosition.getNodeText() + "-" + s);
							String newTabTitle = ((PokerStrategy) top.getUserObject()).getNodeText() + "-" + pokerAction.getNodeText() + "-"
									+ pokerPosition.getNodeText() + "-" + s;
							if (!editable) {
								dash.getTreeTabbedPane().setTitleAt(dash.getTreeTabbedPane().getSelectedIndex(), newTabTitle);
								INIFilesFactory.updateINIFileItemName(UtilMethodsFactory.getConsoleConfig(), "Selections", newTabTitle, oldTreeName);
								INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Selections",
										pokerAction.getNodeText() + "-" + pokerPosition.getNodeText() + "-" + s, newTabTitle);
								INIFilesFactory.updateINIFileItemName(UtilMethodsFactory.getConsoleConfig(), "Applications", newTabTitle, oldTreeName);
								INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Applications", oldAppStatus, newTabTitle);
								INIFilesFactory.updateINIFileItemName(UtilMethodsFactory.getConsoleConfig(), "Autonaming", newTabTitle, oldTreeName);
								INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Autonaming", oldAutoNamingStatus, newTabTitle);
							} else {
								INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Selections",
										pokerAction.getNodeText() + "-" + pokerPosition.getNodeText() + "-" + s, oldTreeName);
							}
							pokerOpponentPosition.setChartImagePath(relativePath);
						}
					}
					displayChartFarame(pokerOpponentPosition, node);
				}
			}
		}
	}

	/**
	 * Adds the PokerAction (PA) node to the solution tree node.
	 * <ul>
	 * <li>Opens input dialog for user to provide the name of the PA.
	 * <li>Initialize PA object with the name provided by user.
	 * <li>Create File object using absolute path to directory representing PA object.
	 * <li>Create this folder on file system.
	 * <li>Create DefaultMutableTreeNode with user object of PA object.
	 * <li>Create DefaultMutableTreeNode of the top of the solution.
	 * <li>Add PA node as a last child of root node.
	 * <li>Expand all nodes in the branch to show all PA nodes including new one.
	 * </ul>
	 *
	 * @param node
	 *            The parent Solution node
	 * @param obj
	 *            The parent node user object
	 */
	private void addPokerAction(DefaultMutableTreeNode node, Object obj) {
		String s = (String) JOptionPane.showInputDialog(dash, "New Action", "Add Action", JOptionPane.PLAIN_MESSAGE, null, null, null);
		if (s != null) {
			if (checkForNewObjectName(node, s)) {
				JOptionPane.showConfirmDialog(null, "The Action is Already there", "Duplicated Action Warning", JOptionPane.CLOSED_OPTION,
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				if ((s != null) && (s.length() > 0)) {
					String oldTreeName = dash.getTreeTabbedPane().getTitleAt(dash.getTreeTabbedPane().getSelectedIndex());
					String oldAppStatus = INIFilesFactory.getItemValueFromINI(UtilMethodsFactory.getConsoleConfig(), "Applications", oldTreeName);
					String oldAutoNamingStatus = INIFilesFactory.getItemValueFromINI(UtilMethodsFactory.getConsoleConfig(), "Autonaming", oldTreeName);
					PokerAction pokerAction = new PokerAction(s);
					File actionDir = new File(UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerStrategy) obj).getNodeText() + "/" + s);
					UtilMethodsFactory.createFolder(actionDir);
					DefaultMutableTreeNode actionNode = new DefaultMutableTreeNode(pokerAction);
					DefaultMutableTreeNode top = (DefaultMutableTreeNode) tree.getModel().getRoot();
					((DefaultTreeModel) tree.getModel()).insertNodeInto(actionNode, top, top.getChildCount());
					theTree.setSelection(actionNode, theTree.getTheTree());
					JTabbedPane pane = dash.getTreeTabbedPane();
					pane.setTitleAt(pane.getSelectedIndex(), constructNewTabName(pane));
					String newTabName = constructNewTabName(pane);
					INIFilesFactory.updateINIFileItemName(UtilMethodsFactory.getConsoleConfig(), "Selections", newTabName, oldTreeName);
					INIFilesFactory.updateINIFileItemName(UtilMethodsFactory.getConsoleConfig(), "Applications", newTabName, oldTreeName);
					INIFilesFactory.updateINIFileItemName(UtilMethodsFactory.getConsoleConfig(), "Autonaming", newTabName, oldTreeName);
					INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Selections", s, newTabName);
					INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Applications", oldAppStatus, newTabName);
					INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Autonaming", oldAutoNamingStatus, newTabName);
				}
			}
		} else {
		}
	}

	/**
	 * Adds the PokerSizing (PS) node to the solution tree node.
	 * <ul>
	 * <li>Opens input dialog for user to provide the name of the PS.
	 * <li>Initialize PS object with the name provided by user.
	 * <li>Create File object using absolute path to directory representing PS object.
	 * <li>Create this folder on file system.
	 * <li>Create DefaultMutableTreeNode with user object of PS object.
	 * <li>Create DefaultMutableTreeNode of the top of the solution.
	 * <li>Add PS node as a last child of root node.
	 * <li>Expand all nodes in the branch to show all PA nodes including new one.
	 * </ul>
	 *
	 * @param node
	 *            The parent PA node
	 * @param obj
	 *            The parent node user object
	 */
	private void addPokerSizing(DefaultMutableTreeNode node, Object obj) {
		String s = (String) JOptionPane.showInputDialog(dash, "New Sizing", "Add Sizing", JOptionPane.PLAIN_MESSAGE, null, null, null);
		if (s != null) {
			if (checkForNewObjectName(node, s)) {
				JOptionPane.showConfirmDialog(null, "The sizing is Already there", "Duplicated Sizing Warning", JOptionPane.CLOSED_OPTION,
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				if ((s != null) && (s.length() > 0)) {
					String oldTreeName = dash.getTreeTabbedPane().getTitleAt(dash.getTreeTabbedPane().getSelectedIndex());
					String oldAppStatus = INIFilesFactory.getItemValueFromINI(UtilMethodsFactory.getConsoleConfig(), "Applications", oldTreeName);
					String oldAutoNamingStatus = INIFilesFactory.getItemValueFromINI(UtilMethodsFactory.getConsoleConfig(), "Autonaming", oldTreeName);
					DefaultMutableTreeNode top = (DefaultMutableTreeNode) tree.getModel().getRoot();
					PokerHandSizing sizing = new PokerHandSizing(s, ((PokerAction) obj));
					File sizingDir = new File(UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/"
							+ ((PokerAction) obj).getNodeText() + "/" + s);
					UtilMethodsFactory.createFolder(sizingDir);
					DefaultMutableTreeNode sizingNode = new DefaultMutableTreeNode(sizing);
					((DefaultTreeModel) tree.getModel()).insertNodeInto(sizingNode, node, sizingNode.getChildCount());
					theTree.setSelection(sizingNode, theTree.getTheTree());
					dash.getTreeTabbedPane().setTitleAt(dash.getTreeTabbedPane().getSelectedIndex(), constructNewTabName(dash.getTreeTabbedPane()));
					INIFilesFactory.updateINIFileItemName(UtilMethodsFactory.getConsoleConfig(), "Selections", constructNewTabName(dash.getTreeTabbedPane()),
							oldTreeName);
					INIFilesFactory.updateINIFileItemName(UtilMethodsFactory.getConsoleConfig(), "Applications", constructNewTabName(dash.getTreeTabbedPane()),
							oldTreeName);
					INIFilesFactory.updateINIFileItemName(UtilMethodsFactory.getConsoleConfig(), "Autonaming", constructNewTabName(dash.getTreeTabbedPane()),
							oldTreeName);
					INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Selections", s, constructNewTabName(dash.getTreeTabbedPane()));
					INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Applications", oldAppStatus,
							constructNewTabName(dash.getTreeTabbedPane()));
					INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Autonaming", oldAutoNamingStatus,
							constructNewTabName(dash.getTreeTabbedPane()));
				}
			}
		} else {
		}
	}

	/**
	 * Apply existing template to blank chart selecting INI file from file system.
	 * <ul>
	 * <li>Get future chart title from POP object property .
	 * <li>Get target chart file object.
	 * <li>Set default directory for File Chooser to the Images folder in application installation location.
	 * <li>Apply filter to File Chooser to see only INI files.
	 * <li>Select template for the chart using File Chooser.
	 * <li>Get template file system path string.
	 * <li>Copy template file to target template directory.
	 * <li>Close all frames in the dashboard desktop.
	 * <li>Add new chart to dashboard desktop.
	 * </ul>
	 *
	 * @param obj
	 *            The PokerOpponentPosition (POP) node user object
	 */
	private void applyTemplate(Object obj) {
		String templatePath = "";
		File file = null;
		String chartTitle = ((PokerOpponentPosition) obj).getChartPaneTitle();
		File targetChartFile = getTargetChartFile(obj);
		final JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File(UtilMethodsFactory.getConfigPath() + "Images/"));
		fc.addChoosableFileFilter(new FileNameExtensionFilter("INI Documents", "ini"));
		fc.setAcceptAllFileFilterUsed(true);
		int returnVal = fc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();
			templatePath = getChartTeplatePath(file);
			try {
				UtilMethodsFactory.copyFile(file, targetChartFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			dash.closeAllFrames();
			UtilMethodsFactory.addChartFrameToScrolableDesctop(templatePath, chartTitle, true, dash.getJScrollableDesktopPane());
		} else {
			System.out.println("Cancel was selected");
		}
	}

	/**
	 * Check if object with the same name does not exist yet.
	 * <ul>
	 * <li>Retrieve the collection of all child nodes on the parent.
	 * <li>Go over the list of objects and return true if object already have this name.
	 * <li>Brake the loop if match found.
	 * <li>For POP object name make sure to exclude sorting number prefix.
	 * </ul>
	 *
	 * @param node
	 *            The object node parent.
	 * @param name
	 *            The object name.
	 * @return true, if node with this name already exists
	 */
	private boolean checkForNewObjectName(DefaultMutableTreeNode node, String name) {
		boolean hit = false;
		Enumeration<?> en = node.children();
		@SuppressWarnings("unchecked")
		List<DefaultMutableTreeNode> list = (List<DefaultMutableTreeNode>) Collections.list(en);
		for (DefaultMutableTreeNode s : list) {
			if (s.getUserObject() instanceof PokerHandSizing) {
				PokerHandSizing pokerHandSizing = (PokerHandSizing) s.getUserObject();
				if (pokerHandSizing.getNodeText().equals(name)) {
					hit = true;
					break;
				} else {
					hit = false;
				}
			} else if (s.getUserObject() instanceof PokerAction) {
				PokerAction pokerAction = (PokerAction) s.getUserObject();
				if (pokerAction.getNodeText().equals(name)) {
					hit = true;
					break;
				} else {
					hit = false;
				}
			} else if (s.getUserObject() instanceof PokerOpponentPosition) {
				PokerOpponentPosition pokerOpponentPosition = (PokerOpponentPosition) s.getUserObject();
				String nodeText = pokerOpponentPosition.getNodeText();
				String firstLetter = UtilMethodsFactory.firstLetterOccurence(nodeText);
				int firstLetterPosition = nodeText.indexOf(firstLetter);
				String noPrefixName = nodeText.substring(firstLetterPosition, nodeText.length());
				if (noPrefixName.equals(name)) {
					hit = true;
					break;
				} else {
					hit = false;
				}
			}
		}
		return hit;
	}

	private String constructNewTabName(JTabbedPane jTabbedPane) {
		String newName = "";
		CustomTree customTree = (CustomTree) ((JScrollPane) jTabbedPane.getComponentAt(jTabbedPane.getSelectedIndex())).getViewport().getView();
		Object elements[] = customTree.getTheTree().getSelectionPath().getPath();
		for (int i = 0, n = elements.length; i < n; i++) {
			Object userObject = ((DefaultMutableTreeNode) elements[i]).getUserObject();
			if (userObject instanceof PokerStrategy) {
				newName = newName + ((PokerStrategy) userObject).getNodeText();
			} else if (userObject instanceof PokerAction) {
				newName = newName + "-" + ((PokerAction) userObject).getNodeText();
			} else if (userObject instanceof PokerHandSizing) {
				newName = newName + "-" + ((PokerHandSizing) userObject).getNodeText();
			} else if (userObject instanceof PokerPosition) {
				newName = newName + "-" + ((PokerPosition) userObject).getNodeText();
			} else if (userObject instanceof PokerOpponentPosition) {
				String nameWithPrefix = ((PokerOpponentPosition) userObject).getNodeText();
				String firstLetter = UtilMethodsFactory.firstLetterOccurence(nameWithPrefix);
				int firstLetterPosition = (nameWithPrefix.indexOf(firstLetter));
				newName = newName + "-" + ((PokerOpponentPosition) userObject).getNodeText().substring(firstLetterPosition, nameWithPrefix.length());
			}
		}
		return newName;
	}

	/**
	 * Copy blank chart INI file.
	 *
	 * @param filePath
	 *            the file path
	 */
	private void copyBlankChartINIFile(String filePath) {
		File chartINIFileName = new File(filePath);
		File template = new File(UtilMethodsFactory.getConfigPath() + "/chart_template.ini");
		try {
			UtilMethodsFactory.copyFile(template, chartINIFileName);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Count how many POP nodes are already added if any to assist with sequence prefix prepending.
	 *
	 * @param node
	 *            The parent branch node.
	 * @return The nodes with POP objects count in the parent branch node.
	 */
	private int countRelevantNodes(DefaultMutableTreeNode node) {
		int n = 0;
		int opponentsHandsNodesCount = 0;
		while (n < node.getChildCount()) {
			if (((DefaultMutableTreeNode) node.getChildAt(n)).getUserObject() instanceof PokerOpponentPosition) {
				opponentsHandsNodesCount++;
			}
			n++;
		}
		return opponentsHandsNodesCount;
	}

	/**
	 * Display chart farame.
	 *
	 * @param pokerOpponentPosition
	 *            the poker opponent position
	 * @param node
	 *            the node
	 */
	private void displayChartFarame(PokerOpponentPosition pokerOpponentPosition, DefaultMutableTreeNode node) {
		DefaultMutableTreeNode pokerOpponentPositionNode = new DefaultMutableTreeNode(pokerOpponentPosition);
		((DefaultTreeModel) tree.getModel()).insertNodeInto(pokerOpponentPositionNode, node, findSameNodeTypePosition(node));
		tree.setSelectionPath(new TreePath(pokerOpponentPositionNode.getPath()));
		theTree.expandNodesBelow(node, tree);
		dash.closeAllFrames();
		UtilMethodsFactory.addChartFrameToScrolableDesctop(pokerOpponentPosition.getChartImagePath(), pokerOpponentPosition.getChartPaneTitle(), editable,
				dash.getJScrollableDesktopPane());
	}

	/**
	 * Duplicate solution.
	 *
	 * @param node
	 *            the node
	 */
	private void duplicateSolution(DefaultMutableTreeNode node) {
		String treeName = ((PokerStrategy) node.getUserObject()).getNodeText();
		INIFilesFactory.addINIFileItemToSection(UtilMethodsFactory.getConsoleConfig(), "Applications", treeName + "-copy", true);
		dash.addTreeTabPaneTab(treeName + "-copy");
	}

	/**
	 * Find same node type position.
	 *
	 * @param node
	 *            the node
	 * @return the int
	 */
	private int findSameNodeTypePosition(DefaultMutableTreeNode node) {
		int n = 0;
		int sameNodeTypePosition = 0;
		while (n < node.getChildCount()) {
			if (node.getChildAt(n) instanceof PokerOpponentPosition) {
				sameNodeTypePosition = n;
			}
			n++;
		}
		return sameNodeTypePosition;
	}

	/**
	 * Gets the chart teplate path.
	 *
	 * @param file
	 *            the file
	 * @return the chart teplate path
	 */
	private String getChartTeplatePath(File file) {
		String templatePath = file.getAbsolutePath();
		templatePath = templatePath.substring(templatePath.indexOf("Images"), templatePath.length());
		templatePath = templatePath.replace("\\", "/");
		return templatePath;
	}

	/**
	 * Gets the target chart file.
	 *
	 * @param obj
	 *            the obj
	 * @return the target chart file
	 */
	private File getTargetChartFile(Object obj) {
		String targetChartPathString = UtilMethodsFactory.getConfigPath() + ((PokerOpponentPosition) obj).getChartImagePath();
		targetChartPathString = targetChartPathString.replace("/", "\\\\");
		targetChartPathString = targetChartPathString.substring(2, targetChartPathString.length());
		targetChartPathString = targetChartPathString.replace("jpg", "ini");
		return new File(targetChartPathString);
	}

	private boolean hasTabWithTitle(JTabbedPane pane, String title) {
		int tabCount = pane.getTabCount();
		boolean hasTabWithTitle = false;
		int x = 0;
		while (x < tabCount) {
			if (pane.getTitleAt(x).contains(title)) {
				hasTabWithTitle = true;
				break;
			}
			x++;
		}
		return hasTabWithTitle;
	}

	/**
	 * Removes the poker action.
	 *
	 * @param node
	 *            the node
	 * @param obj
	 *            the obj
	 */
	private void removePokerAction(DefaultMutableTreeNode node, Object obj) {
		int response = JOptionPane.showConfirmDialog(null, "Do you want to remove the Poker Action", "Poker Action Removal", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.NO_OPTION) {
		} else if (response == JOptionPane.YES_OPTION) {
			PokerAction pokerAction = (PokerAction) obj;
			DefaultMutableTreeNode top = (DefaultMutableTreeNode) tree.getModel().getRoot();
			String fileSystemPath = UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/"
					+ pokerAction.getNodeText();
			UtilMethodsFactory.deleteDirectory(new File(fileSystemPath));
			((DefaultTreeModel) tree.getModel()).removeNodeFromParent(node);
		} else if (response == JOptionPane.CLOSED_OPTION) {
		}
	}

	/**
	 * Removes the poker opponent positon.
	 *
	 * @param node
	 *            the node
	 * @param obj
	 *            the obj
	 */
	private void removePokerOpponentPositon(DefaultMutableTreeNode node, Object obj) {
		int response = JOptionPane.showConfirmDialog(null, "Do you want to remove the Opponents Position", "Opponents Position Removal",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.NO_OPTION) {
		} else if (response == JOptionPane.YES_OPTION) {
			PokerOpponentPosition opToDelete = (PokerOpponentPosition) obj;
			DefaultMutableTreeNode parentNode = prepareDelete(node, opToDelete);
			DefaultMutableTreeNode top = (DefaultMutableTreeNode) tree.getModel().getRoot();
			String fileSystemPath = "";
			if (((DefaultMutableTreeNode) node.getParent()).getUserObject() instanceof PokerPosition) {
				PokerPosition pokerPosition = (PokerPosition) parentNode.getUserObject();
				DefaultMutableTreeNode sizingNode = (DefaultMutableTreeNode) ((DefaultMutableTreeNode) node.getParent()).getParent();
				if (sizingNode.getUserObject() instanceof PokerHandSizing) {
					DefaultMutableTreeNode actionNode = (DefaultMutableTreeNode) (sizingNode.getParent());
					PokerHandSizing pokerHandSizing = (PokerHandSizing) sizingNode.getUserObject();
					PokerAction pokerAction = (PokerAction) actionNode.getUserObject();
					fileSystemPath = UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/"
							+ pokerAction.getNodeText() + "/" + pokerHandSizing.getNodeText() + "/" + pokerPosition.getNodeText() + "/"
							+ opToDelete.getNodeText();
				} else if (sizingNode.getUserObject() instanceof PokerAction) {
					PokerAction pokerAction = (PokerAction) sizingNode.getUserObject();
					fileSystemPath = UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/"
							+ pokerAction.getNodeText() + "/" + pokerPosition.getNodeText() + "/" + opToDelete.getNodeText();
				}
			} else if (((DefaultMutableTreeNode) node.getParent()).getUserObject() instanceof PokerAction) {
				PokerAction pokerAction = (PokerAction) parentNode.getUserObject();
				fileSystemPath = UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/"
						+ pokerAction.getNodeText() + "/" + opToDelete.getNodeText();
			} else if (((DefaultMutableTreeNode) node.getParent()).getUserObject() instanceof PokerHandSizing) {
				PokerHandSizing pokerHandSizing = (PokerHandSizing) parentNode.getUserObject();
				fileSystemPath = UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/"
						+ pokerHandSizing.getPokerAction().getNodeText() + "/" + pokerHandSizing.getNodeText() + "/" + opToDelete.getNodeText();
			}
			dash.closeAllFrames();
			UtilMethodsFactory.deleteFile(fileSystemPath + ".ini");
			UtilMethodsFactory.deleteFile(fileSystemPath + ".png");
			((DefaultTreeModel) tree.getModel()).removeNodeFromParent(node);
		} else if (response == JOptionPane.CLOSED_OPTION) {
		}
	}


	private DefaultMutableTreeNode prepareDelete (DefaultMutableTreeNode node, PokerOpponentPosition opToDelete) {
		DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent();
		Enumeration<?> nodes = parentNode.children();
		while (nodes.hasMoreElements()) {
			DefaultMutableTreeNode child = (DefaultMutableTreeNode) nodes.nextElement();
			Object cjhildrenUO = child.getUserObject();
			if (cjhildrenUO instanceof PokerOpponentPosition) {
				PokerOpponentPosition opObject = (PokerOpponentPosition) cjhildrenUO;
				String chartImagePath = opObject.getChartImagePath();
				String opImageFileName = chartImagePath.split("/")[chartImagePath.split("/").length - 1];
				String opImagePath = chartImagePath.substring(0, chartImagePath.length() - opImageFileName.length());
				int deletedItemSequenceNumber = Integer
						.valueOf(opToDelete.getNodeText().substring(0, UtilMethodsFactory.getIndexOfFirstLiteralInString(opToDelete.getNodeText())));
				if (!opImageFileName.split("\\.")[0].equals(opToDelete.getNodeText())) {
					int theIndesOfFirstLiteral = UtilMethodsFactory.getIndexOfFirstLiteralInString(opImageFileName);
					int sequenceNumber = Integer.valueOf(opImageFileName.substring(0, theIndesOfFirstLiteral));
					String theRest = opImageFileName.substring(theIndesOfFirstLiteral, opImageFileName.length() - theIndesOfFirstLiteral + 1);
					if (sequenceNumber >= deletedItemSequenceNumber) {
						String temp = UtilMethodsFactory.getConfigPath().substring(1, UtilMethodsFactory.getConfigPath().length());
						String a = (temp + opObject.getChartImagePath()).replace("/", "\\");
						String b = (temp + opImagePath + String.valueOf(sequenceNumber - 1) + theRest).replace("/", "\\");
						opObject.setChartImagePath(opImagePath + String.valueOf(sequenceNumber - 1) + theRest);
						String oldText = opObject.getNodeText();
						int indexOfFirstLiteral = UtilMethodsFactory.getIndexOfFirstLiteralInString(oldText);
						String prefix = oldText.substring(0, oldText.length() - indexOfFirstLiteral - 1);
						String postfix = oldText.substring(indexOfFirstLiteral, oldText.length() - indexOfFirstLiteral + 1);
						int newPrefix = Integer.valueOf(prefix) - 1;
						String newText = String.valueOf(newPrefix) + postfix;
						opObject.setNodeText(newText);
						UtilMethodsFactory.renameFile(a, b);
					}
				}
			}
		}
		return parentNode;

	}

	/**
	 * Removes the poker positon.
	 *
	 * @param node
	 *            the node
	 * @param obj
	 *            the obj
	 */
	private void removePokerPositon(DefaultMutableTreeNode node, Object obj) {
		int response = JOptionPane.showConfirmDialog(null, "Do you want to remove the Poker Position", "Poker Position Removal", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.NO_OPTION) {
		} else if (response == JOptionPane.YES_OPTION) {
			PokerPosition pokerPosition = (PokerPosition) obj;
			DefaultMutableTreeNode top = (DefaultMutableTreeNode) tree.getModel().getRoot();
			if (((DefaultMutableTreeNode) node.getParent()).getUserObject() instanceof PokerHandSizing) {
				PokerHandSizing pokerHandSizing = (PokerHandSizing) ((DefaultMutableTreeNode) node.getParent()).getUserObject();
				String fileSystemPath = UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/"
						+ pokerHandSizing.getPokerAction().getNodeText() + "/" + pokerHandSizing.getNodeText() + "/" + pokerPosition.getNodeText();
				UtilMethodsFactory.deleteDirectory(new File(fileSystemPath));
				((DefaultTreeModel) tree.getModel()).removeNodeFromParent(node);
			} else if (((DefaultMutableTreeNode) node.getParent()).getUserObject() instanceof PokerAction) {
				PokerAction pokerAction = (PokerAction) ((DefaultMutableTreeNode) node.getParent()).getUserObject();
				String fileSystemPath = UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/"
						+ pokerAction.getNodeText() + "/" + pokerPosition.getNodeText();
				UtilMethodsFactory.deleteDirectory(new File(fileSystemPath));
				((DefaultTreeModel) tree.getModel()).removeNodeFromParent(node);
			}
		} else if (response == JOptionPane.CLOSED_OPTION) {
		}
		dash.closeAllFrames();
	}

	/**
	 * Removes the poker sizing.
	 *
	 * @param node
	 *            the node
	 * @param obj
	 *            the obj
	 */
	private void removePokerSizing(DefaultMutableTreeNode node, Object obj) {
		int response = JOptionPane.showConfirmDialog(null, "Do you want to delete the Sizing", "Sizing Deletion", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.NO_OPTION) {
		} else if (response == JOptionPane.YES_OPTION) {
			PokerHandSizing sizing = (PokerHandSizing) obj;
			PokerAction action = sizing.getPokerAction();
			DefaultMutableTreeNode top = (DefaultMutableTreeNode) tree.getModel().getRoot();
			String fileSystemPath = UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/"
					+ action.getNodeText() + '/' + sizing.getNodeText();
			UtilMethodsFactory.deleteDirectory(new File(fileSystemPath));
			((DefaultTreeModel) tree.getModel()).removeNodeFromParent(node);
		} else if (response == JOptionPane.CLOSED_OPTION) {
		}
	}

	/**
	 * Removes the Poker Strategy (PS) tree and all its copy tree tabs from tabbed pane.
	 * <ul>
	 * <li>Get the name of the PS from selected node .
	 * <li>Bring up deletion confirmation dialog.
	 * <li>Get all PS copies names from tabbed pane tabs.
	 * <li>Remove all info about solution and solution copies from Application, Selection, and Autonaming sections of configuration INI file.
	 * <li>Get PS folder file system path.
	 * <li>Remove PS folder and subfolders from file system.
	 * <li>Remove all tabs related to this PS from the tabbed pane.
	 * </ul>
	 *
	 * @param node
	 *            the node
	 */
	private void removePokerStrategy(DefaultMutableTreeNode node) {
		String treeName = ((PokerStrategy) node.getUserObject()).getNodeText();
		int response = JOptionPane.showConfirmDialog(null, "Do you want to remove this Tree With All Copies", "Solution Tree Complete removal",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.NO_OPTION) {
		} else if (response == JOptionPane.YES_OPTION) {
			INIFilesFactory.removeINIFileItemsWithPattern(UtilMethodsFactory.getConsoleConfig(), "Applications", treeName);
			INIFilesFactory.removeINIFileItemsWithPattern(UtilMethodsFactory.getConsoleConfig(), "Selections", treeName);
			INIFilesFactory.removeINIFileItemsWithPattern(UtilMethodsFactory.getConsoleConfig(), "Autonaming", treeName);
			String fileSystemPath = UtilMethodsFactory.getConfigPath() + "Images/" + treeName;
			UtilMethodsFactory.deleteDirectory(new File(fileSystemPath));
			JTabbedPane pane = dash.getTreeTabbedPane();
			while (hasTabWithTitle(pane, treeName)) {
				removeTabWithTitle(pane, treeName);
			}
		} else if (response == JOptionPane.CLOSED_OPTION) {
		}
	}

	private void removeTabWithTitle(JTabbedPane pane, String title) {
		int x = 0;
		while (x < pane.getTabCount()) {
			if (pane.getTitleAt(x).contains(title)) {
				pane.remove(x);
				break;
			}
			x++;
		}
	}
}