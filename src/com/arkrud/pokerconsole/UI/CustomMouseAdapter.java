package com.arkrud.pokerconsole.UI;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

import com.arkrud.pokerconsole.Poker.PokerAction;
import com.arkrud.pokerconsole.Poker.PokerHandSizing;
import com.arkrud.pokerconsole.Poker.PokerOpponentPosition;
import com.arkrud.pokerconsole.Poker.PokerPosition;
import com.arkrud.pokerconsole.Poker.PokerStrategy;
import com.arkrud.pokerconsole.TreeInterface.CustomTree;
import com.arkrud.pokerconsole.Util.INIFilesFactory;
import com.arkrud.pokerconsole.Util.UtilMethodsFactory;

public class CustomMouseAdapter extends MouseAdapter {
	private JTabbedPane tabbedPane;

	public CustomMouseAdapter(JTabbedPane tabbedPane) {
		super();
		this.tabbedPane = tabbedPane;
	}

	public void mousePressed(MouseEvent e) {
		final int index = tabbedPane.getUI().tabForCoordinate(tabbedPane, e.getX(), e.getY());
		if (index != -1) {
			if (SwingUtilities.isLeftMouseButton(e)) {
				if (tabbedPane.getSelectedIndex() != index) {
					tabbedPane.setSelectedIndex(index);
				} else if (tabbedPane.isRequestFocusEnabled()) {
					tabbedPane.requestFocusInWindow();
				}
			} else if (SwingUtilities.isMiddleMouseButton(e)) {
				tabbedPane.removeTabAt(index);
			} else if (SwingUtilities.isRightMouseButton(e)) {
				final JPopupMenu popupMenu = new JPopupMenu();
				final JMenuItem addNew = new JMenuItem("Set Name");
				addNew.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						renameStrategyTab(tabbedPane);
					}
				});
				popupMenu.add(addNew);
				final Rectangle tabBounds = tabbedPane.getBoundsAt(index);
				popupMenu.show(tabbedPane, tabBounds.x, tabBounds.y + tabBounds.height);
			}
		}
	}

	private void renameStrategyTab(JTabbedPane tabbedPane) {
		String newName = "";
		CustomTree customTree = (CustomTree) ((JScrollPane) tabbedPane.getComponentAt(tabbedPane.getSelectedIndex())).getViewport().getView();
		Object elements[] = customTree.getCloudTree().getSelectionPath().getPath();
		for (int i = 0, n = elements.length; i < n; i++) {
			Object obj = ((DefaultMutableTreeNode) elements[i]).getUserObject();
			if (obj instanceof PokerStrategy) {
				newName = newName + ((PokerStrategy) obj).getNodeText();
			} else if (obj instanceof PokerAction) {
				newName = newName + "-" + ((PokerAction) obj).getNodeText();
			} else if (obj instanceof PokerHandSizing) {
				newName = newName + "-" + ((PokerHandSizing) obj).getNodeText();
			} else if (obj instanceof PokerPosition) {
				newName = newName + "-" + ((PokerPosition) obj).getNodeText();
			} else if (obj instanceof PokerOpponentPosition) {
				newName = newName + "-" + ((PokerOpponentPosition) obj).getNodeText();
			}
		}
		String oldTreeName = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
		String oldItemValue = INIFilesFactory.getItemValueFromINI(UtilMethodsFactory.getConsoleConfig(), "Applications", oldTreeName + "opened");
		INIFilesFactory.updateINIFileItemName(UtilMethodsFactory.getConsoleConfig(), "Applications", newName + "opened", oldTreeName + "opened");
		INIFilesFactory.updateINIFileItemName(UtilMethodsFactory.getConsoleConfig(), "Applications", newName, oldTreeName);
		INIFilesFactory.updateINIFileItems(UtilMethodsFactory.getConsoleConfig(), "Applications", oldItemValue, newName + "opened");
		tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(), newName);
		return;
	}
}