package com.arkrud.pokerconsole.UI;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
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

		String tabTitle = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
		String solutionName = tabTitle.split("-")[0];
		String fileSystemPath = UtilMethodsFactory.getConfigPath().substring(1, UtilMethodsFactory.getConfigPath().length()) + "Images/" + solutionName + "/";
		JInternalFrame[] frames = dash.getAllFrames();
		List<?>[] framesAndLocations = getInternalFramesPositions(frames);
		@SuppressWarnings("unchecked")
		List<String> frameTitles = (List<String>)framesAndLocations[1];
		printList(frameTitles);
		@SuppressWarnings("unchecked")

		List<String> framePositions = (List<String>)framesAndLocations[0];
		printList(framePositions);
		StringJoiner joiner = new StringJoiner("/");
		for (String title : frameTitles) {
			int x = 0;
			String[] fileSystemPathTockens = title.split("-");
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
		Collections.reverse(filesList);
		// renaming files
		for (String title : frameTitles) {
			String[] fileSystemPathTockens = title.split("-");
			newFilePath = fileSystemPath + "\\" + String.valueOf(framePositions.get(y)) + fileSystemPathTockens[fileSystemPathTockens.length - 1] + ".ini";
			if (filesList.get(y).contains("png")) {
				UtilMethodsFactory.renameFile(filesList.get(y),
						fileSystemPath + "\\" + String.valueOf(framePositions.get(y)) + fileSystemPathTockens[fileSystemPathTockens.length - 1] + ".ini");
				if (hasPNGFile(filesList)) {
					UtilMethodsFactory.renameFile(filesList.get(y).replace("ini", "png"), fileSystemPath + "\\" + String.valueOf(framePositions.get(y)) + fileSystemPathTockens[fileSystemPathTockens.length - 1] + ".png");
				}
			} else {
				for (String theTitle : frameTitles) {
					if (theTitle.split("-")[theTitle.split("-").length - 1].contains(fileSystemPathTockens[fileSystemPathTockens.length - 1])) {
						UtilMethodsFactory.renameFile(filesList.get(y), fileSystemPath + "\\" + String.valueOf(framePositions.get(y)) + fileSystemPathTockens[fileSystemPathTockens.length - 1] + ".ini");
						//System.out.println("From:" + findTockenInTheList(filesList,fileSystemPathTockens[fileSystemPathTockens.length - 1]));
						//System.out.println("To: " + fileSystemPath + "\\" + String.valueOf(framePositions.get(y)) + fileSystemPathTockens[fileSystemPathTockens.length - 1] + ".ini");
						updatePOPFilePathParameter (dash,  newFilePath);
					}
				}

			}

			y++;
		}
		dash.getJScrollableDesktopPane().getDesktopMediator().tileInternalFrames();

	}

	public List<?>[] getInternalFramesPositions(JInternalFrame[] rawFrames) {

		List<?>[] result = new List<?>[2];
		List<String> frameTitles = new ArrayList<String>();
		List<Integer> framePositions = new ArrayList<Integer>();
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
							frameTitles.add(rawFrames[i].getTitle());
							framePositions.add(numCols * curRow + curCol + 1);
						}
					}
				}
				i++;
			}
		}

		result[0] = framePositions;
		result[1] = frameTitles;
		return result;

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
				//System.out.println("newPOPName: " +  newPOPName);
				System.out.println("pokerOpponentPositionname: " +  pokerOpponentPositionname);
				if(newPOPName.contains(pokerOpponentPositionname)) {
					//.out.println("old: " + pokerOpponentPosition.getChartImagePath());
					//.out.println("new: " + newFilePath.substring(newFilePath.indexOf("Images")).replace("\\", "/"));
					break;
				}

			}
		}

	}

	private String findTockenInTheList (List<String> filesList, String tocken)  {
		int y = 0;
		String path = "";
		while (y < filesList.size()) {
			String filePath =  filesList.get(y);
			if(filePath.split("/")[filePath.split("/").length - 1].contains(tocken)) {
				path = filePath;
			}

			y++;
		}
		return path;
	}

	private void printList (List<?> list) {
		int y = 0;
		while (y < list.size()) {
			System.out.println(list.get(y));
			y++;
		}
	}

}
