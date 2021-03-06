package com.arkrud.pokerconsole.TreeInterface;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.arkrud.pokerconsole.Poker.Fork;
import com.arkrud.pokerconsole.Poker.PokerAction;
import com.arkrud.pokerconsole.Poker.PokerHandSizing;
import com.arkrud.pokerconsole.Poker.PokerOpponentPosition;
import com.arkrud.pokerconsole.Poker.PokerPosition;
import com.arkrud.pokerconsole.Poker.PokerStrategy;
import com.arkrud.pokerconsole.UI.ImageChartPanel;
import com.arkrud.pokerconsole.UI.Dashboard.CustomTableViewInternalFrame;
import com.arkrud.pokerconsole.UI.Dashboard.Dashboard;
import com.arkrud.pokerconsole.UI.scrollabledesktop.BaseInternalFrame;
import com.arkrud.pokerconsole.UI.scrollabledesktop.JScrollableDesktopPane;
import com.arkrud.pokerconsole.Util.INIFilesFactory;
import com.arkrud.pokerconsole.Util.UtilMethodsFactory;

public class CustomTreeMouseListener implements MouseListener, PropertyChangeListener {
	private JPopupMenu popup;
	private Dashboard dash;
	private Point loc;
	private HashMap<String, Boolean> dropDownMenus = new HashMap<String, Boolean>();
	private boolean editable;

	public CustomTreeMouseListener(JPopupMenu popup, Dashboard dash, boolean editable) {
		this.editable = editable;
		this.popup = popup;
		this.dash = dash;
	}

	public Point getLoc() {
		return loc;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			((JTree) e.getSource()).setToggleClickCount(0);
		} else {
			JScrollableDesktopPane pane = dash.getJScrollableDesktopPane();
			TreePath path = ((JTree) e.getSource()).getPathForLocation(e.getX(), e.getY());
			if (path != null) {
				Object obj = ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();
				if (path != null) {
					JTabbedPane jTabbedPane = dash.getTreeTabbedPane();
					String dynamicTreeName = "";
					String oldTreeName = jTabbedPane.getTitleAt(jTabbedPane.getSelectedIndex());
					if (INIFilesFactory.getItemValueFromINI(UtilMethodsFactory.getConsoleConfig(), "Autonaming", oldTreeName).equals("true")) {
						dynamicTreeName = constructNewTabName(jTabbedPane);
						INIFilesFactory.removeINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Autonaming", oldTreeName);
						INIFilesFactory.addINIFileItemToSection(UtilMethodsFactory.getConsoleConfig(), "Autonaming", dynamicTreeName, "true");
					} else {
						dynamicTreeName = oldTreeName;
						INIFilesFactory.removeINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Autonaming", oldTreeName);
						INIFilesFactory.addINIFileItemToSection(UtilMethodsFactory.getConsoleConfig(), "Autonaming", dynamicTreeName, "false");
					}
					if (((DefaultMutableTreeNode) path.getLastPathComponent()).isLeaf()) {
						if (obj instanceof PokerOpponentPosition) {
							PokerOpponentPosition pokerOpponentPosition = (PokerOpponentPosition) obj;
							dash.closeAllFrames();
							UtilMethodsFactory.addChartFrameToScrolableDesctop(pokerOpponentPosition.getChartImagePath(), pokerOpponentPosition.getChartPaneTitle(), editable, dash);
							INIFilesFactory.addINIFileItemToSection(UtilMethodsFactory.getConsoleConfig(), "Selections", dash.getTreeTabbedPane().getTitleAt(dash.getTreeTabbedPane().getSelectedIndex()),
									((PokerOpponentPosition) (((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject())).getChartPaneTitle());
						}
					} else {
						showDiagrams(path, pane, dash.getTreeTabbedPane().getTitleAt(dash.getTreeTabbedPane().getSelectedIndex()));
					}
					if (INIFilesFactory.getItemValueFromINI(UtilMethodsFactory.getConsoleConfig(), "Autonaming", dynamicTreeName).equals("true")) {
						String oldItemValue = INIFilesFactory.getItemValueFromINI(UtilMethodsFactory.getConsoleConfig(), "Selections", oldTreeName);
						INIFilesFactory.updateINIFileItemName(UtilMethodsFactory.getConsoleConfig(), "Selections", dynamicTreeName, oldTreeName);
						INIFilesFactory.updateINIFileItemName(UtilMethodsFactory.getConsoleConfig(), "Solutions", dynamicTreeName, oldTreeName);
						INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Selections", oldItemValue, dynamicTreeName);
						jTabbedPane.setTitleAt(jTabbedPane.getSelectedIndex(), dynamicTreeName);
					} else {
						String selectedChartName = constructNewTabName(jTabbedPane);
						String newPosition = selectedChartName.substring(selectedChartName.indexOf("-") + 1, selectedChartName.length());
						INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Selections", newPosition, oldTreeName);
					}
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		checkForPopup(e);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
	}

	public void setLoc(Point loc) {
		this.loc = loc;
	}

	private void checkForPopup(MouseEvent e) {
		// Set all tree nodes drop-down menus not visible for all nodes
		hideAllMenuItems();
		// Set drop-down menu visible on specific tree nodes right click
		if (e.isPopupTrigger()) {
			loc = e.getPoint();
			TreePath path = ((JTree) e.getSource()).getPathForLocation(loc.x, loc.y);
			Object treeObject = ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();
			((JTree) e.getSource()).setSelectionPath(path);
			if (path != null) {
				if (treeObject instanceof PokerStrategy) {
					dropDownMenus.put("Refresh", true);
					dropDownMenus.put("Remove", true);
					dropDownMenus.put("Add Action", true);
					dropDownMenus.put("Duplicate", true);
				} else if (treeObject instanceof PokerOpponentPosition) {
					dropDownMenus.put("Apply Template", true);
					dropDownMenus.put("Remove", true);
				} else if (treeObject instanceof PokerPosition) {
					dropDownMenus.put("Remove", true);
					dropDownMenus.put("Add Opponents Position / Hero Range", true);
					dropDownMenus.put("Add Fork", true);
					dropDownMenus.put("Change Charts Order", true);
				} else if (treeObject instanceof PokerAction) {
					dropDownMenus.put("Add Sizing", true);
					dropDownMenus.put("Add Hero Position", true);
					dropDownMenus.put("Add Opponents Position / Hero Range", true);
					dropDownMenus.put("Remove", true);
					dropDownMenus.put("Change Charts Order", true);
				} else if (treeObject instanceof PokerHandSizing) {
					dropDownMenus.put("Delete Sizing", true);
					dropDownMenus.put("Add Hero Position", true);
					dropDownMenus.put("Add Opponents Position / Hero Range", true);
					dropDownMenus.put("Change Charts Order", true);
				} else if (treeObject instanceof Fork) {
					dropDownMenus.put("Add Opponents Position / Hero Range", true);
					dropDownMenus.put("Remove", true);
				} else {
				}
				setMenuAttributes();
				// Show pop-up
				popup.show((JTree) e.getSource(), loc.x, loc.y);
			}
		}
	}

	private void hideAllMenuItems() {
		for (int i = 0; i < UtilMethodsFactory.dropDownsNames.length; i++) {
			dropDownMenus.put(UtilMethodsFactory.dropDownsNames[i], false);
		}
	}

	private void setMenuAttributes() {
		int i = 0;
		while (i < popup.getComponentCount()) {
			JMenuItem item = (JMenuItem) popup.getComponents()[i];
			for (Map.Entry<String, Boolean> entry : dropDownMenus.entrySet()) {
				if (item.getText().equals(entry.getKey())) {
					item.setEnabled(entry.getValue());
					item.setVisible(entry.getValue());
				}
			}
			i++;
		}
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
			} else if (userObject instanceof Fork) {
				newName = newName + "-" + ((Fork) userObject).getNodeText();
			} else if (userObject instanceof PokerOpponentPosition) {
				String nameWithPrefix = ((PokerOpponentPosition) userObject).getNodeText();
				String firstLetter = UtilMethodsFactory.firstLetterOccurence(nameWithPrefix);
				int firstLetterPosition = (nameWithPrefix.indexOf(firstLetter));
				newName = newName + "-" + ((PokerOpponentPosition) userObject).getNodeText().substring(firstLetterPosition, nameWithPrefix.length());
			}
		}
		return newName;
	}

	private void showDiagrams(TreePath path, JScrollableDesktopPane pane, String treeTabName) {
		dash.closeAllFrames();
		ImageChartPanel imageChartPanel;
		Enumeration<?> en = ((DefaultMutableTreeNode) path.getLastPathComponent()).children();
		@SuppressWarnings("unchecked")
		List<DefaultMutableTreeNode> list = (List<DefaultMutableTreeNode>) Collections.list(en);
		for (DefaultMutableTreeNode s : list) {
			if (s.getUserObject() instanceof PokerOpponentPosition) {
				PokerOpponentPosition pokerOpponentPosition = (PokerOpponentPosition) s.getUserObject();
				if (editable) {
					UtilMethodsFactory.addChartFrameToScrolableDesctop(pokerOpponentPosition.getChartImagePath(), pokerOpponentPosition.getChartPaneTitle(), true, dash);
				} else {
					imageChartPanel = new ImageChartPanel(pokerOpponentPosition.getChartImagePath());
					BaseInternalFrame theFrame = new CustomTableViewInternalFrame(pokerOpponentPosition.getChartPaneTitle(), imageChartPanel);
					theFrame.setName(pokerOpponentPosition.getChartImagePath());
					UtilMethodsFactory.addInternalFrameToScrolableDesctopPane(pokerOpponentPosition.getChartPaneTitle(), pane, theFrame);
				}
			}
		}
		Object obj = ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();
		String iniItemName = "";
		if (obj instanceof PokerAction) {
			iniItemName = ((PokerAction) (((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject())).getNodeText();
		} else if (obj instanceof PokerPosition) {
			iniItemName = ((PokerPosition) (((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject())).getChartPaneTitle();
		} else if (obj instanceof PokerHandSizing) {
			iniItemName = ((PokerHandSizing) (((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject())).getPokerAction().getNodeText() + "-"
					+ ((PokerHandSizing) (((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject())).getNodeText();
		}
		INIFilesFactory.addINIFileItemToSection(UtilMethodsFactory.getConsoleConfig(), "Selections", treeTabName, iniItemName);
	}
}
