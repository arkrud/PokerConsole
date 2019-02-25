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
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.arkrud.pokerconsole.Poker.PokerAction;
import com.arkrud.pokerconsole.Poker.PokerGroup;
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

public class CustomTreeMouseListener implements MouseListener, PropertyChangeListener { // NO_UCD (use default)
	private JPopupMenu popup;
	private Dashboard dash;
	private JTree tree;
	private CustomTree theTree;
	private Point loc;
	private HashMap<String, Boolean> dropDownMenus = new HashMap<String, Boolean>();
	private boolean editable;

	public CustomTreeMouseListener(JPopupMenu popup, JTree tree, Dashboard dash, CustomTree theTree, boolean editable) { // NO_UCD (use default)
		this.editable = editable;
		this.popup = popup;
		this.tree = tree;
		this.dash = dash;
		this.theTree = theTree;
	}

	private void checkForPopup(MouseEvent e) {
		// Set all tree nodes drop-down menus not visible for all nodes
		for (int i = 0; i < UtilMethodsFactory.dropDownsNames.length; i++) {
			dropDownMenus.put(UtilMethodsFactory.dropDownsNames[i], false);
		}
		// Set drop-down menu visible on specific tree nodes right click
		if (e.isPopupTrigger()) {
			loc = e.getPoint();
			TreePath path = ((JTree) e.getSource()).getPathForLocation(loc.x, loc.y);
			Object treeObject = ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();
			tree.setSelectionPath(path);
			if (path != null) {
				if (treeObject instanceof PokerStrategy) {
					if (theTree.getTreeType().equals("config")) {
						dropDownMenus.put("Add Group", true);
					} else {
						dropDownMenus.put("Refresh", true);
						dropDownMenus.put("Remove", true);
						dropDownMenus.put("Rename", true);
					}
				} else if (treeObject instanceof PokerGroup) {
					if (theTree.getTreeType().equals("config")) {
						dropDownMenus.put("Delete", true);
						dropDownMenus.put("Rename", true);
					} else {
					}
				} else if (treeObject instanceof PokerOpponentPosition) {
					if (!theTree.getTreeType().equals("config")) {
						dropDownMenus.put("Apply Template", true);
					} else {
					}
				} else if (treeObject instanceof PokerAction) {
					if (!theTree.getTreeType().equals("config")) {
						dropDownMenus.put("Add Sizing", true);
					}
				} else {
				}
				// Set menu attributes
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
				// Show pop-up
				popup.show(tree, loc.x, loc.y);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		JScrollableDesktopPane pane = dash.getJScrollableDesktopPane();
		TreePath path = tree.getPathForLocation(e.getX(), e.getY());
		if (path != null) {
			Object obj = ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();
			if (path != null && !theTree.getTreeType().equals("config")) {
				if (((DefaultMutableTreeNode) path.getLastPathComponent()).isLeaf()) {
					ChartPanel chartPanel = new ChartPanel(((PokerOpponentPosition) obj).getChartImagePath(), true);
					dash.getJScrollableDesktopPane().getDesktopMediator().closeAllFrames();
					BaseInternalFrame theFrame = new CustomTableViewInternalFrame(((PokerOpponentPosition) obj).getChartPaneTitle(), chartPanel);
					UtilMethodsFactory.addInternalFrameToScrolableDesctopPane(((PokerOpponentPosition) obj).getChartPaneTitle(), pane, theFrame);
				} else {
					if (obj instanceof PokerAction && ((PokerAction) obj).getNodeText().equals("RFI")) {
						showDiagrams(path, pane);
					} else if (obj instanceof PokerPosition || obj instanceof PokerGroup) {
						showDiagrams(path, pane);
					} else {
					}
				}
			}
		} else {
			//System.out.println("single");
		}
	}

	private void showDiagrams(TreePath path, JScrollableDesktopPane pane) {
		dash.getJScrollableDesktopPane().getDesktopMediator().closeAllFrames();
		ChartPanel chartPanel;
		ImageChartPanel imageChartPanel;
		Enumeration<?> en = ((DefaultMutableTreeNode) path.getLastPathComponent()).children();
		@SuppressWarnings("unchecked")
		List<DefaultMutableTreeNode> list = (List<DefaultMutableTreeNode>) Collections.list(en);
		for (DefaultMutableTreeNode s : reversed(list)) {
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
		if (obj instanceof PokerAction) {
			iniItemName = ((PokerAction) (((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject())).getNodeText();
		} else if (obj instanceof PokerPosition) {
			iniItemName = ((PokerPosition) (((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject())).getChartPaneTitle();
		} else if (obj instanceof PokerGroup) {
			iniItemName = ((PokerGroup) (((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject())).getNodeText();
		}
		INIFilesFactory.addINIFileItemToSection(UtilMethodsFactory.getConsoleConfig(), "Applications", theTree.getTreeType() + "opened", iniItemName);
	}

	public <T> Reversed<T> reversed(List<T> original) {
		return new Reversed<T>(original);
	}

	public Point getLoc() {
		return loc;
	}

	public void setLoc(Point loc) {
		this.loc = loc;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// checkForPopup(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// checkForPopup(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// checkForPopup(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		checkForPopup(e);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
	}
}
