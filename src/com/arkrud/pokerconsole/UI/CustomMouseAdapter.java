package com.arkrud.pokerconsole.UI;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import com.arkrud.pokerconsole.UI.Dashboard.Dashboard;
import com.arkrud.pokerconsole.Util.INIFilesFactory;
import com.arkrud.pokerconsole.Util.UtilMethodsFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class CustomMouseAdapter.
 */
public class CustomMouseAdapter extends MouseAdapter {
	
	/** The tabbed pane. */
	private JTabbedPane tabbedPane;
	
	/** The dash. */
	private Dashboard dash;

	/**
	 * Instantiates a new custom mouse adapter.
	 *
	 * @param tabbedPane the tabbed pane
	 * @param dash the dash
	 */
	public CustomMouseAdapter(JTabbedPane tabbedPane, Dashboard dash) {
		super();
		this.tabbedPane = tabbedPane;
		this.dash = dash;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
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
				final JMenuItem setName = new JMenuItem("Set Name");
				setName.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						renameStrategyTab(tabbedPane);
					}
				});
				String selectedTabName = dash.getTreeTabbedPane().getTitleAt(dash.getTreeTabbedPane().getSelectedIndex());
				if (INIFilesFactory.getItemValueFromINI(UtilMethodsFactory.getConsoleConfig(), "Autonaming", selectedTabName).equals("true")) {
					setName.setEnabled(false);
				}
				popupMenu.add(setName);
				final JMenuItem removeTreeCopy = new JMenuItem("Remove Tree Copy");
				removeTreeCopy.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						removeTreeCopy(tabbedPane, dash);
					}
				});
				popupMenu.add(removeTreeCopy);
				final Rectangle tabBounds = tabbedPane.getBoundsAt(index);
				popupMenu.show(tabbedPane, tabBounds.x, tabBounds.y + tabBounds.height);
			}
		}
	}

	/**
	 * Rename strategy tab.
	 *
	 * @param tabbedPane the tabbed pane
	 */
	private void renameStrategyTab(JTabbedPane tabbedPane) {
		UtilMethodsFactory.showDialogToDesctop("RenameSolutioTab", 250, 140, dash, null, null, null, null, tabbedPane, null);
		INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Autonaming", "false", tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
	}

	/**
	 * Removes the tree copy.
	 *
	 * @param tabbedPane the tabbed pane
	 * @param dash the dash
	 */
	private void removeTreeCopy(JTabbedPane tabbedPane, Dashboard dash) {
		int response = JOptionPane.showConfirmDialog(null, "Do you want to remove remove this Tree Copy", "Solution Tree Copy removal",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.NO_OPTION) {
		} else if (response == JOptionPane.YES_OPTION) {
			String treeTabTitle = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
			dash.removeTreeTabPaneTab(treeTabTitle);
			INIFilesFactory.removeINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Solutions", treeTabTitle);
			INIFilesFactory.removeINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Selections", treeTabTitle);
		} else if (response == JOptionPane.CLOSED_OPTION) {
		}
	}
}
