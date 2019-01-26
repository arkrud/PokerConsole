package com.arkrud.pokerconsole.TreeInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;

import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.dtools.ini.IniItem;

import com.arkrud.pokerconsole.Poker.PokerGroup;
import com.arkrud.pokerconsole.Poker.PokerOpponentPosition;
import com.arkrud.pokerconsole.Poker.PokerStrategy;
import com.arkrud.pokerconsole.UI.ChartPanel;
import com.arkrud.pokerconsole.UI.Dashboard.CustomTableViewInternalFrame;
import com.arkrud.pokerconsole.UI.Dashboard.Dashboard;
import com.arkrud.pokerconsole.UI.scrollabledesktop.BaseInternalFrame;
import com.arkrud.pokerconsole.Util.INIFilesFactory;
import com.arkrud.pokerconsole.Util.UtilMethodsFactory;

public class CustomTreePopupHandler implements ActionListener, PropertyChangeListener {
	private JTree tree;
	private TreePath path;
	private Dashboard dash;
	private CustomTreeMouseListener cml;
	private CustomTree theTree;
	private int groupItemUniqueCaracter = 0;

	public CustomTreePopupHandler(JTree tree, JPopupMenu popup, Dashboard dash, CustomTree theTree) {
		// Pass variables values into the class
		this.tree = tree;
		this.dash = dash;
		this.theTree = theTree;
		// Add Mouse listener to control which menu items will show up in drop-down menu
		cml = new CustomTreeMouseListener(popup, tree, dash, theTree);
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
					int reply = JOptionPane.showConfirmDialog(null, "Please select charts for the group!", "No Charts Selected", JOptionPane.CLOSED_OPTION,
							JOptionPane.INFORMATION_MESSAGE);
					if (reply == JOptionPane.YES_OPTION) {
					}
				} else {
					String s = (String) JOptionPane.showInputDialog(dash, "Group Name", "Save New Group", JOptionPane.PLAIN_MESSAGE, null, null, null);
					if (checkForGroupName(s)) {
						JOptionPane.showConfirmDialog(null, "The selected Group name is Duplicated!", "Duplicated Group Name Warning",
								JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
					} else {
						if (!checkIfGroupNameHasLettersOnly(s)) {
							JOptionPane.showConfirmDialog(null, "The selected Group name must have only Letters!", "Formatting Warning",
									JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
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
			} else if (ac.equals("RENAME")) {
				String s = (String) JOptionPane.showInputDialog(dash, "Rename Tree To", "Rename Tree", JOptionPane.PLAIN_MESSAGE, null, null, "");
				if ((s != null) && (s.length() > 0)) {
					String oldTreeName = ((PokerStrategy) node.getUserObject()).getNodeText();
					String oldItemValue = INIFilesFactory.getItemValueFromINI(UtilMethodsFactory.getConsoleConfig(), "Applications", oldTreeName + "opened");
					INIFilesFactory.updateINIFileItemName(UtilMethodsFactory.getConsoleConfig(), "Applications", s + "opened",  oldTreeName + "opened");
					INIFilesFactory.updateINIFileItemName(UtilMethodsFactory.getConsoleConfig(), "Applications", s,
							((PokerStrategy) node.getUserObject()).getNodeText());
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
				int response = JOptionPane.showConfirmDialog(null, "Do you want to remove remove this Tree", "Application Tree removal",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
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
				int response = JOptionPane.showConfirmDialog(null, "Do you want to delete teh Group", "Group Deletion", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE);
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
		} else {
		}
	}

	private boolean checkForGroupName(String newGrouName) {
		Iterator<IniItem> it = INIFilesFactory.getAllItemsFromSection(UtilMethodsFactory.getConsoleConfig(), "Groups").iterator();
		boolean hit = false;
		A: while (it.hasNext()) {
			IniItem iniItem = it.next();
			if (iniItem.getName().contains(newGrouName)) {
				hit = true;
				break A;
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
						INIFilesFactory.addINIFileItemToSection(UtilMethodsFactory.getConsoleConfig(), "Groups", groupName + groupItemUniqueCaracter,
								((PokerOpponentPosition) childNode.getUserObject()).getChartImagePath());
						groupItemUniqueCaracter++;
						PokerOpponentPosition pokerOpponentPosition = (PokerOpponentPosition) childNode.getUserObject();
						PokerOpponentPosition newPokerOpponentPosition = new PokerOpponentPosition(pokerOpponentPosition.getNodeText());
						newPokerOpponentPosition.setSelected(false);
						newPokerOpponentPosition.setNodeText(((PokerOpponentPosition) childNode.getUserObject()).getChartPaneTitle());
						newPokerOpponentPosition.setChartImagePath(pokerOpponentPosition.getChartImagePath());
						newPokerOpponentPosition.setChartPaneTitle(pokerOpponentPosition.getChartPaneTitle());
						DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(newPokerOpponentPosition);
						((DefaultTreeModel) tree.getModel()).insertNodeInto(newNode, groupNode, groupNode.getChildCount());
						ChartPanel chartPanel = new ChartPanel(newPokerOpponentPosition.getChartImagePath());
						BaseInternalFrame theFrame = new CustomTableViewInternalFrame(newPokerOpponentPosition.getChartPaneTitle(), chartPanel);
						UtilMethodsFactory.addInternalFrameToScrolableDesctopPane(newPokerOpponentPosition.getChartPaneTitle(),
								dash.getJScrollableDesktopPane(), theFrame);
					}
				}
			}
		}
		theTree.clearAllTreeSelections(node);
		theTree.expandTwoDeep();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
	}
}