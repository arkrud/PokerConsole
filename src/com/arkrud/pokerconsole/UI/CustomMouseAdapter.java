package com.arkrud.pokerconsole.UI;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;

import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.arkrud.pokerconsole.Poker.PokerOpponentPosition;
import com.arkrud.pokerconsole.Poker.PokerPosition;
import com.arkrud.pokerconsole.TreeInterface.CustomTree;
import com.arkrud.pokerconsole.UI.Dashboard.Dashboard;
import com.arkrud.pokerconsole.Util.INIFilesFactory;
import com.arkrud.pokerconsole.Util.UtilMethodsFactory;

public class CustomMouseAdapter extends MouseAdapter {
	private JTabbedPane tabbedPane;
	private Dashboard dash;

	public CustomMouseAdapter(JTabbedPane tabbedPane, Dashboard dash) {
		super();
		this.tabbedPane = tabbedPane;
		this.dash = dash;
	}

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
				if (INIFilesFactory.getItemValueFromINI(UtilMethodsFactory.getConsoleConfig(), "Autonaming", selectedTabName).equals("false")) {
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
				final JMenuItem saveChartsLayout = new JMenuItem("Save Charts Layout");
				saveChartsLayout.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						saveChartsLayout(tabbedPane, dash);
					}
				});
				popupMenu.add(saveChartsLayout);
				final Rectangle tabBounds = tabbedPane.getBoundsAt(index);
				popupMenu.show(tabbedPane, tabBounds.x, tabBounds.y + tabBounds.height);
			}
		}
	}

	private void renameStrategyTab(JTabbedPane tabbedPane) {
		UtilMethodsFactory.showDialogToDesctop("RenameSolutioTab", 250, 140, dash, null, null, null, null, tabbedPane, null);
		INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Autonaming", "true", tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
	}

	private void removeTreeCopy(JTabbedPane tabbedPane, Dashboard dash) {
		int response = JOptionPane.showConfirmDialog(null, "Do you want to remove remove this Tree Copy", "Solution Tree Copy removal", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.NO_OPTION) {
		} else if (response == JOptionPane.YES_OPTION) {
			String treeTabTitle = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
			dash.removeTreeTabPaneTab(treeTabTitle);
			INIFilesFactory.removeINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Applications", treeTabTitle);
			INIFilesFactory.removeINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Selections", treeTabTitle);
		} else if (response == JOptionPane.CLOSED_OPTION) {
		}
	}

	private void saveChartsLayout(JTabbedPane tabbedPane, Dashboard dash) {
		//dash.getJScrollableDesktopPane().getDesktopMediator().tileInternalFrames();
		String tabTitle = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
		String solutionName = tabTitle.split("-")[0];
		String fileSystemPath = UtilMethodsFactory.getConfigPath().substring(1, UtilMethodsFactory.getConfigPath().length()) + "Images/" + solutionName + "/";
		JInternalFrame[] frames = dash.getAllFrames();
		StringJoiner joiner = new StringJoiner("/");
		for (JInternalFrame jInternalFrame : frames) {
			int x = 0;
			String[] fileSystemPathTockens = jInternalFrame.getTitle().split("-");
			while (x < fileSystemPathTockens.length - 1) {
				joiner.add(fileSystemPathTockens[x]);
				x++;
			}
			break;
		}
		fileSystemPath = (fileSystemPath + joiner.toString()).replace("/", "\\");
		List<String> filesList = UtilMethodsFactory.listFiles(fileSystemPath);
		String newFilePath = "";
		int y = 0;
		int prefix = 1;
		Collections.reverse(Arrays.asList(frames));
		// renaming files
		for (JInternalFrame jInternalFrame : frames) {
			String[] fileSystemPathTockens = jInternalFrame.getTitle().split("-");
			newFilePath = fileSystemPath + "\\" + String.valueOf(prefix) + fileSystemPathTockens[fileSystemPathTockens.length - 1] + ".ini";
			if (filesList.get(y).contains("png")) {
				UtilMethodsFactory.renameFile(filesList.get(y),
						fileSystemPath + "\\" + String.valueOf(prefix) + fileSystemPathTockens[fileSystemPathTockens.length - 1] + ".ini");
				if (hasPNGFile(filesList)) {
					UtilMethodsFactory.renameFile(filesList.get(y).replace("ini", "png"), fileSystemPath + "\\" + String.valueOf(prefix) + fileSystemPathTockens[fileSystemPathTockens.length - 1] + ".png");
				}
			} else {
				UtilMethodsFactory.renameFile(filesList.get(y), fileSystemPath + "\\" + String.valueOf(prefix) + fileSystemPathTockens[fileSystemPathTockens.length - 1] + ".ini");
			}
			prefix++;
			y++;
		}
		updatePOPFilePathParameter (dash,  newFilePath);
		//dash.getJScrollableDesktopPane().getDesktopMediator().tileInternalFrames();
	}

	private boolean hasPNGFile(List<String> filesList) {
		Iterator<String> it = filesList.iterator();
		boolean hasPNG = false;
		while (it.hasNext()) {
			String fileName = it.next();
			if (fileName.contains("png")) {
				hasPNG = true;
				break;
			}
		}
		return hasPNG;
	}

	private static void updatePOPFilePathParameter (Dashboard dash, String newFilePath) {
		JScrollPane scroll = (JScrollPane) (dash.getTreeTabbedPane().getSelectedComponent());
		CustomTree tree = (CustomTree) scroll.getViewport().getView();
		TreePath selectedPath = tree.getTheTree().getSelectionPaths()[0];
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) (selectedPath.getLastPathComponent());
		if (node.getUserObject() instanceof PokerPosition) {
			Enumeration<?> en = node.children();
			@SuppressWarnings("unchecked")
			List<DefaultMutableTreeNode> children = (List<DefaultMutableTreeNode>) Collections.list(en);
			for (DefaultMutableTreeNode s : UtilMethodsFactory.reversed(children)) {
				PokerOpponentPosition pokerOpponentPosition = (PokerOpponentPosition) s.getUserObject();
				String newPOPName = newFilePath.split("\\\\")[newFilePath.split("\\\\").length - 1];
				int theIndesOfFirstLiteral = UtilMethodsFactory.getIndexOfFirstLiteralInString(pokerOpponentPosition.getNodeText());
				String pokerOpponentPositionname = pokerOpponentPosition.getNodeText().substring(theIndesOfFirstLiteral, pokerOpponentPosition.getNodeText().length() - theIndesOfFirstLiteral + 1);
				System.out.println("newPOPName: " +  newPOPName);
				System.out.println("pokerOpponentPositionname: " +  pokerOpponentPositionname);
				if(newPOPName.contains(pokerOpponentPositionname)) {
					System.out.println("old: " + pokerOpponentPosition.getChartImagePath());
					System.out.println("new: " + newFilePath.substring(newFilePath.indexOf("Images")).replace("\\", "/"));
					break;
				}

			}
		}

	}
}
