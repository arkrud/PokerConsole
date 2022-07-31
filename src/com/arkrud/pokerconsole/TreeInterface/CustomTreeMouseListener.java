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
import javax.swing.JOptionPane;
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

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving customTreeMouse events.
 * The class that is interested in processing a customTreeMouse
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addCustomTreeMouseListener<code> method. When
 * the customTreeMouse event occurs, that object's appropriate
 * method is invoked.
 *
 * @see CustomTreeMouseEvent
 */
public class CustomTreeMouseListener implements MouseListener, PropertyChangeListener {
	
	/** The popup. */
	private JPopupMenu popup;
	
	/** The dash. */
	private Dashboard dash;
	
	/** The loc. */
	private Point loc;
	
	/** The drop down menus. */
	private HashMap<String, Boolean> dropDownMenus = new HashMap<String, Boolean>();
	
	/** The editable. */
	private boolean editable;

	/**
	 * Instantiates a new custom tree mouse listener.
	 *
	 * @param popup the popup
	 * @param dash the dash
	 * @param editable the editable
	 */
	public CustomTreeMouseListener(JPopupMenu popup, Dashboard dash, boolean editable) {
		this.editable = editable;
		this.popup = popup;
		this.dash = dash;
	}

	/**
	 * Gets the loc.
	 *
	 * @return the loc
	 */
	public Point getLoc() {
		return loc;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
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

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent e) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		checkForPopup(e);
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
	}

	/**
	 * Sets the loc.
	 *
	 * @param loc the new loc
	 */
	public void setLoc(Point loc) {
		this.loc = loc;
	}

	/**
	 * Check for popup.
	 *
	 * @param e the e
	 */
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

	/**
	 * Hide all menu items.
	 */
	private void hideAllMenuItems() {
		for (int i = 0; i < UtilMethodsFactory.dropDownsNames.length; i++) {
			dropDownMenus.put(UtilMethodsFactory.dropDownsNames[i], false);
		}
	}

	/**
	 * Sets the menu attributes.
	 */
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

	/**
	 * Construct new tab name.
	 *
	 * @param jTabbedPane the j tabbed pane
	 * @return the string
	 */
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

	/**
	 * Show diagrams.
	 *
	 * @param path the path
	 * @param pane the pane
	 * @param treeTabName the tree tab name
	 */
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
