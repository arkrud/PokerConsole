package com.arkrud.pokerconsole.UI;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;
import java.util.TreeMap;

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
import com.arkrud.pokerconsole.Poker.PokerStrategy;
import com.arkrud.pokerconsole.TreeInterface.CustomTree;
import com.arkrud.pokerconsole.UI.Dashboard.Dashboard;
import com.arkrud.pokerconsole.UI.scrollabledesktop.JScrollableDesktopPane;
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
		// Get tree root node (poker solution)
		JScrollPane scroll = (JScrollPane) (tabbedPane.getSelectedComponent());
		CustomTree tree = (CustomTree) scroll.getViewport().getView();
		DefaultMutableTreeNode top = (DefaultMutableTreeNode) tree.getTreeModel().getRoot();
		// Get solution name
		String solutionName = ((PokerStrategy) top.getUserObject()).getNodeText();
		// Construct file system path to solution folder
		String fileSystemPath = UtilMethodsFactory.getConfigPath().substring(1, UtilMethodsFactory.getConfigPath().length()) + "Images/" + solutionName + "/";
		// Get all chart frames from desktop
		JInternalFrame[] frames = dash.getAllFrames();
		// Create a list of the titles of all frames
		List<String> frameTitles = new ArrayList<String>();
		int z = 0;
		while (z < frames.length) {
			frameTitles.add(frames[z].getTitle());
			z++;
		}
		// Create joiner class to construct forward-slash delimited file path
		StringJoiner joiner = new StringJoiner("/");
		// Get folder path for node holding currently selected chart group
		String[] fileSystemPathTockens = frameTitles.get(0).split("-");
		// Populate joiner with folder names in he path
		int x = 0;
		while (x < fileSystemPathTockens.length - 1) {
			joiner.add(fileSystemPathTockens[x]);
			x++;
		}
		// Replace forward-slashes with backslashes
		fileSystemPath = (fileSystemPath + joiner.toString()).replace("/", "\\");
		// Generate list of file paths for all files in folder with charts INI files
		List<String> filesList = UtilMethodsFactory.listFiles(fileSystemPath);
		// Reverse the file path list order
		Collections.reverse(filesList);
		// Create map object to associate chart file name without sequence prefix and extension with the file path
		int n = 0;
		TreeMap<String, String> map = new TreeMap<String, String>();
		while (n < filesList.size()) {
			String noExtensionPath = filesList.get(n).split("\\.")[0];
			String lastTocken = noExtensionPath.split("\\\\")[noExtensionPath.split("\\\\").length - 1];
			int theIndesOfFirstLiteral = UtilMethodsFactory.getIndexOfFirstLiteralInString(lastTocken);
			String postfix = lastTocken.substring(theIndesOfFirstLiteral, lastTocken.length() - theIndesOfFirstLiteral + 1);
			map.put(postfix, filesList.get(n));
			n++;
		}
		// Check for duplicates in selected combo boxes, warn user about the error, and bring him back to correct it
		// Rename files
		// Loop over frame titles
		for (String title : frameTitles) {
			// Get the last token of dash-delimited frame title
			fileSystemPathTockens = title.split("-");
			String lastTocken = fileSystemPathTockens[fileSystemPathTockens.length - 1];
			// Get POP name from the map associating used inputed sequence of the frame location with frame POP name.
			String newSequencePrefix = String.valueOf(getInternalFramesPositions(frames).get(lastTocken));
			//System.out.println(newSequencePrefix);
			// Verify if read-only PNG images of the charts are already created
			if (hasPNGFile(filesList, lastTocken)) {
				// If PNG files are already created loop over frame titles list and change sequence prefix in the file names of both INI and PNG files
				for (String theTitle : frameTitles) {
					// Rename only if frame title contains the name of POP object rename the INI file with sequence number requested by user
					if (theTitle.split("-")[theTitle.split("-").length - 1].contains(lastTocken)) {
						UtilMethodsFactory.renameFile(map.get(lastTocken), fileSystemPath + "\\" + newSequencePrefix + lastTocken + ".ini");
						UtilMethodsFactory.renameFile(map.get(lastTocken).replace("ini", "png"), fileSystemPath + "\\" + newSequencePrefix + lastTocken + ".png");
					}
				}
			} else {
				// If PNG files are not there yet loop over frame titles list and change sequence prefix in the file names of INI files
				for (String theTitle : frameTitles) {
					// Rename only if frame title contains the name of POP object rename the INI file with sequence number requested by user
					if (theTitle.split("-")[theTitle.split("-").length - 1].contains(lastTocken)) {
						UtilMethodsFactory.renameFile(map.get(lastTocken), fileSystemPath + "\\" + newSequencePrefix + lastTocken + ".ini");
					}
				}
			}
		}
		repositionCharts(top, solutionName, tree);
	}
	private void repositionCharts(DefaultMutableTreeNode top, String solutionName, CustomTree tree) {
		tree.refreshTreeNode(top, solutionName);
		JTabbedPane sourceTabbedPane = dash.getTreeTabbedPane();
		int index = sourceTabbedPane.getSelectedIndex();
		dash.closeAllFrames();
		if (INIFilesFactory.hasItemInSection(UtilMethodsFactory.getConsoleConfig(), "Selections", sourceTabbedPane.getTitleAt(index))) {
			String pathString = INIFilesFactory.getItemValueFromINI(UtilMethodsFactory.getConsoleConfig(), "Selections", sourceTabbedPane.getTitleAt(index));
			JScrollPane scroll = (JScrollPane) (sourceTabbedPane.getSelectedComponent());
			JScrollableDesktopPane desctopPane = dash.getJScrollableDesktopPane();
			CustomTree theTree = (CustomTree) scroll.getViewport().getView();
			TreePath path = theTree.selectTreeNode((DefaultMutableTreeNode) theTree.getTreeModel().getRoot(), pathString, theTree);
			if (path != null) {
				if (((DefaultMutableTreeNode) path.getLastPathComponent()).isLeaf() && ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject() instanceof PokerOpponentPosition) {
					PokerOpponentPosition pokerOpponentPosition = (PokerOpponentPosition) ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();
					UtilMethodsFactory.addChartFrameToScrolableDesctop(pokerOpponentPosition.getChartImagePath(), pokerOpponentPosition.getChartPaneTitle(), true, desctopPane);
				} else {
					Enumeration<?> en = ((DefaultMutableTreeNode) path.getLastPathComponent()).children();
					@SuppressWarnings("unchecked")
					List<DefaultMutableTreeNode> list = (List<DefaultMutableTreeNode>) Collections.list(en);
					for (DefaultMutableTreeNode s : UtilMethodsFactory.reversed(list)) {
						if (s.getUserObject() instanceof PokerOpponentPosition) {
							PokerOpponentPosition pokerOpponentPosition = (PokerOpponentPosition) s.getUserObject();
							UtilMethodsFactory.addChartFrameToScrolableDesctop(pokerOpponentPosition.getChartImagePath(), pokerOpponentPosition.getChartPaneTitle(), true, desctopPane);
						} else if (s.getUserObject() instanceof PokerPosition) {
						}
					}
				}
			}
		}
	}

	public TreeMap<String, Integer> getInternalFramesPositions(JInternalFrame[] rawFrames) {
		TreeMap<String, Integer> positionMap = new TreeMap<String, Integer>();
		int totalNonIconFrames = 0;
		int z = 0;
		while (z < rawFrames.length) {
			if (!rawFrames[z].isIcon()) { // don't include iconified frames...
				totalNonIconFrames++;
			}
			z++;
		}
		int i = 0;
		double xposition = 0;
		double yposition = 0;
		if (totalNonIconFrames > 0) {
			while (i < totalNonIconFrames) {
				// compute number of columns and rows then tile the frames
				int curCol = 0;
				int curRow = 0;
				int numRows = (int) Math.sqrt(totalNonIconFrames);
				if (numRows > 2) {
					numRows = 2;
				}
				for (curRow = 0; curRow < numRows; curRow++) {
					int numCols = totalNonIconFrames / numRows;
					int remainder = totalNonIconFrames % numRows;
					if ((numRows - curRow) <= remainder) {
						numCols++; // add an extra row for this guy
					}
					for (curCol = 0; curCol < numCols; curCol++) {
						xposition = rawFrames[i].getBounds().getX();
						yposition = rawFrames[i].getBounds().getY();
						if (curCol * 430 <= xposition && xposition < (curCol + 1) * 430 - 215 && curRow * 440 <= yposition && yposition < (curRow + 1) * 440 - 220) {
							System.out.println(rawFrames[i].getTitle().split("-")[rawFrames[i].getTitle().split("-").length -1]);
							int position = 0;
							if (curRow == 0) {
								position = curCol + 1;
							} else if (curRow == 1) {
								position = curCol + 4;
							}
							System.out.println(position);
							positionMap.put(rawFrames[i].getTitle().split("-")[rawFrames[i].getTitle().split("-").length -1], position );
						}
					}
				}
				i++;
			}
		}
		return positionMap;
	}

	/*private boolean hasPNGFile(List<String> filesList) {
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

	private static void updatePOPFilePathParameter(Dashboard dash, String newFilePath) {
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
				if (newPOPName.contains(pokerOpponentPositionname)) {
					// .out.println("old: " + pokerOpponentPosition.getChartImagePath());
					// .out.println("new: " + newFilePath.substring(newFilePath.indexOf("Images")).replace("\\", "/"));
					break;
				}
			}
		}
	}*/

	/*private String findTockenInTheList(List<String> filesList, String tocken) {
		int y = 0;
		String path = "";
		while (y < filesList.size()) {
			String filePath = filesList.get(y);
			if (filePath.split("/")[filePath.split("/").length - 1].contains(tocken)) {
				path = filePath;
			}
			y++;
		}
		return path;
	}*/

	private boolean hasPNGFile(List<String> filesList, String commonElement) {
		int x = 0;
		List<String> pngFiles = new ArrayList<String>();
		List<String> iniFiles = new ArrayList<String>();
		while (x < filesList.size()) {
			if (filesList.get(x).contains("png")) {
				pngFiles.add(filesList.get(x).split("\\.")[0]);
			} else if (filesList.get(x).contains("ini")) {
				iniFiles.add(filesList.get(x).split("\\.")[0]);
			}
			x++;
		}
		iniFiles.retainAll(pngFiles);
		int y = 0;
		while (y < iniFiles.size()) {
			if (iniFiles.get(y).contains(commonElement)) {
				return true;
			}
			y++;
		}
		return false;
	}
}
