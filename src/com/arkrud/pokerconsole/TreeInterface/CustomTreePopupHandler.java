package com.arkrud.pokerconsole.TreeInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.dtools.ini.IniItem;

import com.arkrud.pokerconsole.Poker.PokerAction;
import com.arkrud.pokerconsole.Poker.PokerGroup;
import com.arkrud.pokerconsole.Poker.PokerHandSizing;
import com.arkrud.pokerconsole.Poker.PokerOpponentPosition;
import com.arkrud.pokerconsole.Poker.PokerPosition;
import com.arkrud.pokerconsole.Poker.PokerStrategy;
import com.arkrud.pokerconsole.UI.ChartPanel;
import com.arkrud.pokerconsole.UI.ImageChartPanel;
import com.arkrud.pokerconsole.UI.Dashboard.CustomTableViewInternalFrame;
import com.arkrud.pokerconsole.UI.Dashboard.Dashboard;
import com.arkrud.pokerconsole.UI.scrollabledesktop.BaseInternalFrame;
import com.arkrud.pokerconsole.UI.scrollabledesktop.JScrollableDesktopPane;
import com.arkrud.pokerconsole.Util.INIFilesFactory;
import com.arkrud.pokerconsole.Util.Reversed;
import com.arkrud.pokerconsole.Util.UtilMethodsFactory;

public class CustomTreePopupHandler implements ActionListener, PropertyChangeListener {
	private JTree tree;
	private TreePath path;
	private Dashboard dash;
	private CustomTreeMouseListener cml;
	private CustomTree theTree;
	private int groupItemUniqueCaracter = 0;
	private String[] defaultPositions = { "BB", "BU", "CO", "HJ", "LJ", "SB", "UTG1", "UTG2" };
	private String[] defaultOpponetPositions = { "0SB", "1BU", "2CO", "3HJ", "4LJ", "5UTG2", "6UTG1", "7UTG" };
	private String[] defaultSizings = { "2BB", "3BB" };
	final JFileChooser fc = new JFileChooser();
	private boolean editable;

	public CustomTreePopupHandler(JTree tree, JPopupMenu popup, Dashboard dash, CustomTree theTree, boolean editable) {
		// Pass variables values into the class
		this.tree = tree;
		this.dash = dash;
		this.theTree = theTree;
		this.editable = editable;
		// Add Mouse listener to control which menu items will show up in drop-down menu
		cml = new CustomTreeMouseListener(popup, tree, dash, theTree, editable);
		tree.addMouseListener(cml);
	}

	// Perform actions on drop-down menu selections
	@Override
	public void actionPerformed(ActionEvent e) {
		String ac = e.getActionCommand();
		DefaultMutableTreeNode node = null;
		path = tree.getPathForLocation(cml.getLoc().x, cml.getLoc().y);
		node = (DefaultMutableTreeNode) path.getLastPathComponent();
		Object obj = ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();
		// For leaf objects
		if (obj instanceof PokerStrategy) {
			if (ac.equals("ADD GROUP")) {
				if (theTree.checkIfAnythingIsSelected(node) == 1) {
					int reply = JOptionPane.showConfirmDialog(null, "Please select charts for the group!", "No Charts Selected", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
					if (reply == JOptionPane.YES_OPTION) {
					}
				} else {
					String s = (String) JOptionPane.showInputDialog(dash, "Group Name", "Save New Group", JOptionPane.PLAIN_MESSAGE, null, null, null);
					if (checkForNewObjectName(node, s)) {
						JOptionPane.showConfirmDialog(null, "The selected Group name is Duplicated!", "Duplicated Group Name Warning", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
					} else {
						if (!checkIfGroupNameHasLettersOnly(s)) {
							JOptionPane.showConfirmDialog(null, "The selected Group name must have only Letters!", "Formatting Warning", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
						} else {
							if ((s != null) && (s.length() > 0)) {
								PokerGroup group = new PokerGroup(s);
								DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode(group);
								DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
								DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
								((DefaultTreeModel) tree.getModel()).insertNodeInto(groupNode, root, root.getChildCount());
								dash.getJScrollableDesktopPane().getDesktopMediator().closeAllFrames();
								addGroup(node, s, groupNode);
							}
						}
					}
				}
			} else if (ac.equals("REFRESH")) {
				theTree.refreshTreeNode(node);
			} else if (ac.equals("ADD ACTION")) {
				String s = (String) JOptionPane.showInputDialog(dash, "New Action", "Add Action", JOptionPane.PLAIN_MESSAGE, null, null, null);
				System.out.println(s);
				if (s != null) {
					if (checkForNewObjectName(node, s)) {
						JOptionPane.showConfirmDialog(null, "The Action is Already there", "Duplicated Action Warning", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
					} else {
						if ((s != null) && (s.length() > 0)) {
							PokerAction pokerAction = new PokerAction(s);
							File actionDir = new File(UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerStrategy) obj).getNodeText() + "/" + s);
							UtilMethodsFactory.createGRoupFolder(actionDir);
							DefaultMutableTreeNode actionNode = new DefaultMutableTreeNode(pokerAction);
							DefaultMutableTreeNode root = (DefaultMutableTreeNode) theTree.getTreeModel().getRoot();
							((DefaultTreeModel) tree.getModel()).insertNodeInto(actionNode, root, root.getChildCount());
							theTree.expandNodesBelow(root, tree);
							// dash.getJScrollableDesktopPane().getDesktopMediator().closeAllFrames();
							// int x = 0;
							// while (x < defaultSizings.length) {
							// PokerHandSizing sizing = new PokerHandSizing(defaultSizings[x], pokerAction);
							// DefaultMutableTreeNode sizingNode = new DefaultMutableTreeNode(sizing);
							// ((DefaultTreeModel) tree.getModel()).insertNodeInto(sizingNode, actionNode, sizingNode.getChildCount());
							// File sizingDir = new File(UtilMethodsFactory.getConfigPath() + "Images/" + s + "/" + defaultSizings[x]);
							// UtilMethodsFactory.createGRoupFolder(sizingDir); addSizing(defaultSizings[x], sizingNode);
							// theTree.expandNodesBelow(sizingNode, tree);
							// showDiagrams( new TreePath(sizingNode.getFirstChild()), dash.getJScrollableDesktopPane());
							// x++;
							// }
						}
					}
				} else {
				}
			} else if (ac.equals("RENAME")) {
				String s = (String) JOptionPane.showInputDialog(dash, "Rename Tree To", "Rename Tree", JOptionPane.PLAIN_MESSAGE, null, null, "");
				if ((s != null) && (s.length() > 0)) {
					String oldTreeName = ((PokerStrategy) node.getUserObject()).getNodeText();
					String oldItemValue = INIFilesFactory.getItemValueFromINI(UtilMethodsFactory.getConsoleConfig(), "Applications", oldTreeName + "opened");
					INIFilesFactory.updateINIFileItemName(UtilMethodsFactory.getConsoleConfig(), "Applications", s + "opened", oldTreeName + "opened");
					INIFilesFactory.updateINIFileItemName(UtilMethodsFactory.getConsoleConfig(), "Applications", s, ((PokerStrategy) node.getUserObject()).getNodeText());
					INIFilesFactory.updateINIFileItems(UtilMethodsFactory.getConsoleConfig(), "Applications", oldItemValue, s + "opened");
					theTree.renameNode(node, s);
					JTabbedPane pane = dash.getTreeTabbedPane();
					for (int i = 0; i < pane.getTabCount(); i++) {
						if (SwingUtilities.isDescendingFrom(theTree, pane.getComponentAt(i))) {
							pane.setTitleAt(i, s);
							break;
						}
					}
					return;
				}
			} else if (ac.equals("REMOVE")) {
				int response = JOptionPane.showConfirmDialog(null, "Do you want to remove remove this Tree", "Application Tree removal", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (response == JOptionPane.NO_OPTION) {
				} else if (response == JOptionPane.YES_OPTION) {
					String treeName = ((PokerStrategy) node.getUserObject()).getNodeText();
					dash.removeTreeTabPaneTab(treeName);
					String[] trees = { treeName };
					INIFilesFactory.removeINIFileItems(UtilMethodsFactory.getConsoleConfig(), "Applications", trees);
				} else if (response == JOptionPane.CLOSED_OPTION) {
				}
			}
		} else if (obj instanceof PokerGroup) {
			if (ac.equals("DELETE")) {
				int response = JOptionPane.showConfirmDialog(null, "Do you want to delete teh Group", "Group Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (response == JOptionPane.NO_OPTION) {
				} else if (response == JOptionPane.YES_OPTION) {
					INIFilesFactory.removeINIFileItemsByPrefix(UtilMethodsFactory.getConsoleConfig(), "Groups", ((PokerGroup) obj).getNodeText());
					INIFilesFactory.removeINIFileItemsByPatternInItemValues(UtilMethodsFactory.getConsoleConfig(), "Applications", ((PokerGroup) obj).getNodeText());
					((DefaultTreeModel) tree.getModel()).removeNodeFromParent(node);
					dash.getJScrollableDesktopPane().getDesktopMediator().closeAllFrames();
				} else if (response == JOptionPane.CLOSED_OPTION) {
				}
			} else if (ac.equals("RENAME")) {
				String s = (String) JOptionPane.showInputDialog(dash, "Rename Group To:", "Rename Group", JOptionPane.PLAIN_MESSAGE, null, null, "");
				String oldGroupName = ((PokerGroup) node.getUserObject()).getNodeText();
				if ((s != null) && (s.length() > 0)) {
					Iterator<IniItem> it = INIFilesFactory.getAllItemsFromSection(UtilMethodsFactory.getConsoleConfig(), "Groups").iterator();
					while (it.hasNext()) {
						IniItem iniItem = it.next();
						if (iniItem.getName().startsWith(oldGroupName)) {
							String oldItemValue = iniItem.getValue();
							String newItemName = s + iniItem.getName().substring(oldGroupName.length(), iniItem.getName().length());
							INIFilesFactory.updateINIFileItemName(UtilMethodsFactory.getConsoleConfig(), "Groups", newItemName, iniItem.getName());
							INIFilesFactory.updateINIFileItems(UtilMethodsFactory.getConsoleConfig(), "Groups", oldItemValue, newItemName);
						}
					}
					theTree.renameNode(node, s);
					return;
				}
			}
		} else if (obj instanceof PokerOpponentPosition) {
			String templatePath = "";
			File file = null;
			if (ac.equals("APPLY TEMPLATE")) {
				fc.setCurrentDirectory(new File(UtilMethodsFactory.getConfigPath() + "Images/"));
				int returnVal = fc.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					file = fc.getSelectedFile();
					templatePath = file.getAbsolutePath();
					templatePath = templatePath.substring(templatePath.indexOf("Images"), templatePath.length());
					templatePath = templatePath.replace("\\", "/");
				} else {
				}
				ChartPanel chartPanel = new ChartPanel(templatePath, true);
				dash.getJScrollableDesktopPane().getDesktopMediator().closeAllFrames();
				BaseInternalFrame theFrame = new CustomTableViewInternalFrame(((PokerOpponentPosition) obj).getChartPaneTitle(), chartPanel);
				UtilMethodsFactory.addInternalFrameToScrolableDesctopPane(((PokerOpponentPosition) obj).getChartPaneTitle(), dash.getJScrollableDesktopPane(), theFrame);
				Path from = file.toPath(); // convert from File to Path
				String toPath = UtilMethodsFactory.getConfigPath() + ((PokerOpponentPosition) obj).getChartImagePath();
				toPath = toPath.replace("/", "\\\\");
				toPath = toPath.substring(2, toPath.length());
				toPath = toPath.replace("jpg", "ini");
				Path to = Paths.get(toPath); // convert from String to Path
				try {
					Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} else if (obj instanceof PokerAction) {
			if (ac.equals("ADD SIZING")) {
				String s = (String) JOptionPane.showInputDialog(dash, "New Sizing", "Add Sizing", JOptionPane.PLAIN_MESSAGE, null, null, null);
				System.out.println(s);
				if (s != null) {
					if (checkForNewObjectName(node, s)) {
						JOptionPane.showConfirmDialog(null, "The sizing is Already there", "Duplicated Sizing Warning", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
					} else {
						if ((s != null) && (s.length() > 0)) {
							PokerHandSizing sizing = new PokerHandSizing(s, ((PokerAction) obj));
							DefaultMutableTreeNode top = (DefaultMutableTreeNode) tree.getModel().getRoot();
							File sizingDir = new File(UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/" + ((PokerAction) obj).getNodeText() + "/" + s);
							UtilMethodsFactory.createGRoupFolder(sizingDir);
							DefaultMutableTreeNode sizingNode = new DefaultMutableTreeNode(sizing);
							((DefaultTreeModel) tree.getModel()).insertNodeInto(sizingNode, node, sizingNode.getChildCount());
							// dash.getJScrollableDesktopPane().getDesktopMediator().closeAllFrames();
							// addSizing(s, sizingNode);
							theTree.expandNodesBelow(node, tree);
						}
					}
				} else {
				}
			} else if (ac.equals("REMOVE")) {
				int response = JOptionPane.showConfirmDialog(null, "Do you want to remove the Poker Action", "Poker Action Removal", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (response == JOptionPane.NO_OPTION) {
				} else if (response == JOptionPane.YES_OPTION) {
					PokerAction pokerAction = (PokerAction) obj;
					DefaultMutableTreeNode top = (DefaultMutableTreeNode) tree.getModel().getRoot();
					String fileSystemPath = UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/" + pokerAction.getNodeText();
					UtilMethodsFactory.deleteDirectory(new File(fileSystemPath));
					((DefaultTreeModel) tree.getModel()).removeNodeFromParent(node);
				} else if (response == JOptionPane.CLOSED_OPTION) {
				}
			} else {
			}
		} else if (obj instanceof PokerHandSizing) {
			if (ac.equals("DELETE SIZING")) {
				int response = JOptionPane.showConfirmDialog(null, "Do you want to delete the Sizing", "Sizing Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (response == JOptionPane.NO_OPTION) {
				} else if (response == JOptionPane.YES_OPTION) {
					PokerHandSizing sizing = (PokerHandSizing) obj;
					PokerAction action = sizing.getPokerAction();
					// PokerPosition position = (PokerPosition) (((DefaultMutableTreeNode) node.getChildAt(0)).getUserObject());
					DefaultMutableTreeNode top = (DefaultMutableTreeNode) tree.getModel().getRoot();
					String fileSystemPath = UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/" + action.getNodeText() + '/' + sizing.getNodeText();
					// String treeINIDesignator = ((PokerStrategy) top.getUserObject()).getNodeText() + "opened";
					// PokerHandSizing firstNodeSizing = (PokerHandSizing) ((DefaultMutableTreeNode) node.getParent().getChildAt(0)).getUserObject();
					// PokerHandSizing secondNodeSizing = (PokerHandSizing) ((DefaultMutableTreeNode) node.getParent().getChildAt(1)).getUserObject();
					// ((DefaultMutableTreeNode) node.getChildAt(0)).getUserObject();
					// String newSaveSelectionString = "";
					// if (((PokerHandSizing) node.getUserObject()).equals(firstNodeSizing)) {
					// newSaveSelectionString = action.getNodeText() + "-" + secondNodeSizing.getNodeText() + "-" + position.getNodeText();
					// } else {
					// newSaveSelectionString = action.getNodeText() + "-" + firstNodeSizing.getNodeText() + "-" + position.getNodeText();
					// }
					UtilMethodsFactory.deleteDirectory(new File(fileSystemPath));
					((DefaultTreeModel) tree.getModel()).removeNodeFromParent(node);
					// path = path.pathByAddingChild(new DefaultMutableTreeNode(secondNodeSizing));
					// showDiagrams(path, dash.getJScrollableDesktopPane());
					// INIFilesFactory.updateINIFileItems(UtilMethodsFactory.getConsoleConfig(), "Applications", newSaveSelectionString, treeINIDesignator);
				} else if (response == JOptionPane.CLOSED_OPTION) {
				}
			}
		} else {
		}
	}

	private boolean checkForNewObjectName(DefaultMutableTreeNode node, String name) {
		boolean hit = false;
		Enumeration<?> en = ((DefaultMutableTreeNode) node).children();
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
			}
		}
		return hit;
	}

	private boolean checkIfGroupNameHasLettersOnly(String name) {
		return name.matches("[a-zA-Z]+");
	}

	private void addGroup(DefaultMutableTreeNode node, String groupName, DefaultMutableTreeNode groupNode) {
		int childCount = node.getChildCount();
		for (int i = 0; i < childCount; i++) {
			DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) node.getChildAt(i);
			if (childNode.getChildCount() > 0) {
				addGroup(childNode, groupName, groupNode);
			} else {
				if (childNode.getUserObject() instanceof PokerOpponentPosition) {
					if (((PokerOpponentPosition) childNode.getUserObject()).isSelected()) {
						INIFilesFactory.addINIFileItemToSection(UtilMethodsFactory.getConsoleConfig(), "Groups", groupName + groupItemUniqueCaracter, ((PokerOpponentPosition) childNode.getUserObject()).getChartImagePath());
						groupItemUniqueCaracter++;
						PokerOpponentPosition pokerOpponentPosition = (PokerOpponentPosition) childNode.getUserObject();
						PokerOpponentPosition newPokerOpponentPosition = new PokerOpponentPosition(pokerOpponentPosition.getNodeText());
						newPokerOpponentPosition.setSelected(false);
						newPokerOpponentPosition.setNodeText(((PokerOpponentPosition) childNode.getUserObject()).getChartPaneTitle());
						newPokerOpponentPosition.setChartImagePath(pokerOpponentPosition.getChartImagePath());
						newPokerOpponentPosition.setChartPaneTitle(pokerOpponentPosition.getChartPaneTitle());
						DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(newPokerOpponentPosition);
						((DefaultTreeModel) tree.getModel()).insertNodeInto(newNode, groupNode, groupNode.getChildCount());
						ChartPanel chartPanel = new ChartPanel(newPokerOpponentPosition.getChartImagePath(), true);
						BaseInternalFrame theFrame = new CustomTableViewInternalFrame(newPokerOpponentPosition.getChartPaneTitle(), chartPanel);
						UtilMethodsFactory.addInternalFrameToScrolableDesctopPane(newPokerOpponentPosition.getChartPaneTitle(), dash.getJScrollableDesktopPane(), theFrame);
					}
				}
			}
		}
		theTree.clearAllTreeSelections(node);
		// theTree.expandTwoDeep();
	}

	private void addSizing(String sizing, DefaultMutableTreeNode sizingNode) {
		int x = 0;
		while (x < defaultPositions.length) {
			File pokerPositionDir = new File(
					UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerHandSizing) sizingNode.getUserObject()).getPokerAction().getNodeText() + "/" + ((PokerHandSizing) sizingNode.getUserObject()).getNodeText() + "/" + defaultPositions[x]);
			UtilMethodsFactory.createGRoupFolder(pokerPositionDir);
			PokerPosition pokerPosition = new PokerPosition(defaultPositions[x]);
			DefaultMutableTreeNode pokerPositionNode = new DefaultMutableTreeNode(pokerPosition);
			sizingNode.add(pokerPositionNode);
			int y = 0;
			while (y < defaultOpponetPositions.length) {
				File pokerOpponentPositionINIFile = new File(UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerHandSizing) sizingNode.getUserObject()).getPokerAction().getNodeText() + "/"
						+ ((PokerHandSizing) sizingNode.getUserObject()).getNodeText() + "/" + defaultPositions[x] + "/" + Integer.toString(y + 1) + defaultOpponetPositions[y] + ".ini");
				File template = new File(UtilMethodsFactory.getConfigPath() + "/chart_template.ini");
				try {
					copyFileUsingJava7Files(template, pokerOpponentPositionINIFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
				File pokerOpponentPositionFakeJpgFile = new File(UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerHandSizing) sizingNode.getUserObject()).getPokerAction().getNodeText() + "/"
						+ ((PokerHandSizing) sizingNode.getUserObject()).getNodeText() + "/" + defaultPositions[x] + "/" + Integer.toString(y + 1) + defaultOpponetPositions[y] + ".jpg");
				try {
					copyFileUsingJava7Files(template, pokerOpponentPositionFakeJpgFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
				PokerOpponentPosition pokerOpponentPosition = new PokerOpponentPosition(defaultOpponetPositions[y]);
				pokerOpponentPosition.setSelected(false);
				pokerOpponentPosition.setChartPaneTitle(((PokerHandSizing) sizingNode.getUserObject()).getPokerAction().getNodeText() + "-" + sizing + "-" + defaultPositions[0] + "-" + defaultOpponetPositions[y]);
				pokerOpponentPosition
						.setChartImagePath("Images/" + ((PokerHandSizing) sizingNode.getUserObject()).getPokerAction().getNodeText() + "/" + sizing + "/" + defaultPositions[0] + "/" + Integer.toString(y + 1) + defaultOpponetPositions[y] + ".jpg");
				DefaultMutableTreeNode pokerOpponentPositionNode = new DefaultMutableTreeNode(pokerOpponentPosition);
				pokerPositionNode.add(pokerOpponentPositionNode);
				y++;
			}
			x++;
		}
	}

	private void showDiagrams(TreePath path, JScrollableDesktopPane pane) {
		dash.getJScrollableDesktopPane().getDesktopMediator().closeAllFrames();
		ChartPanel chartPanel;
		ImageChartPanel imageChartPanel;
		Enumeration<?> en = ((DefaultMutableTreeNode) path.getLastPathComponent()).children();
		@SuppressWarnings("unchecked")
		List<DefaultMutableTreeNode> list = (List<DefaultMutableTreeNode>) Collections.list(en);
		for (DefaultMutableTreeNode s : UtilMethodsFactory.reversed(list)) {
			PokerOpponentPosition pokerOpponentPosition = (PokerOpponentPosition) s.getUserObject();
			if (editable) {
				chartPanel = new ChartPanel(pokerOpponentPosition.getChartImagePath(), true);
				BaseInternalFrame theFrame = new CustomTableViewInternalFrame(pokerOpponentPosition.getChartPaneTitle(), chartPanel);
				UtilMethodsFactory.addInternalFrameToScrolableDesctopPane(pokerOpponentPosition.getChartPaneTitle(), pane, theFrame);
			} else {
				imageChartPanel = new ImageChartPanel(pokerOpponentPosition.getChartImagePath());
				BaseInternalFrame theFrame = new CustomTableViewInternalFrame(pokerOpponentPosition.getChartPaneTitle(), imageChartPanel);
				UtilMethodsFactory.addInternalFrameToScrolableDesctopPane(pokerOpponentPosition.getChartPaneTitle(), pane, theFrame);
			}
		}
		Object obj = ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();
		String iniItemName = "";
		System.out.println(obj.getClass().getSimpleName());
		if (obj instanceof PokerAction) {
			iniItemName = ((PokerAction) (((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject())).getNodeText();
		} else if (obj instanceof PokerPosition) {
			TreeNode firstOpponentPositionNode = ((DefaultMutableTreeNode) path.getLastPathComponent()).getFirstChild();
			PokerOpponentPosition pokerOpponentPosition = (PokerOpponentPosition) ((DefaultMutableTreeNode) firstOpponentPositionNode).getUserObject();
			iniItemName = pokerOpponentPosition.getChartPaneTitle();
			// iniItemName = ((PokerPosition) (((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject())).getChartPaneTitle();
		} else if (obj instanceof PokerGroup) {
			iniItemName = ((PokerGroup) (((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject())).getNodeText();
		}
		INIFilesFactory.addINIFileItemToSection(UtilMethodsFactory.getConsoleConfig(), "Applications", theTree.getTreeType() + "opened", iniItemName);
	}

	private static void copyFileUsingJava7Files(File source, File dest) throws IOException {
		Files.copy(source.toPath(), dest.toPath());
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
	}
}