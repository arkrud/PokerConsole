/**
 *
 */
package com.arkrud.pokerconsole.UI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.SpringLayout;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.arkrud.Shareware.SpringUtilities;
import com.arkrud.pokerconsole.Poker.PokerOpponentPosition;
import com.arkrud.pokerconsole.Poker.PokerPosition;
import com.arkrud.pokerconsole.Poker.PokerStrategy;
import com.arkrud.pokerconsole.TreeInterface.CustomTree;
import com.arkrud.pokerconsole.UI.Dashboard.Dashboard;
import com.arkrud.pokerconsole.UI.scrollabledesktop.JScrollableDesktopPane;
import com.arkrud.pokerconsole.Util.INIFilesFactory;
import com.arkrud.pokerconsole.Util.UtilMethodsFactory;

/**
 * Class to build chart ordering dialog.<br>
 * 
 * @author arkrud
 */
public class ChnageChartsOrderDialog extends JDialog implements ActionListener {
	private JTree tree;
	private Dashboard dash;
	private CustomTree theTree;
	private JButton applyButton, cancelButton;
	private JPanel chartsPanel, chartDataPanel, buttonsPanel;
	private List<String> popNames = new ArrayList<String>();
	private List<String> popChartOrderPrefixes = new ArrayList<String>();
	private TreeMap<String, JComboBox<String>> positionMap = new TreeMap<String, JComboBox<String>>();
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	public ChnageChartsOrderDialog(JTree tree, DefaultMutableTreeNode node, Dashboard dash, CustomTree theTree) {
		this.tree = tree;
		this.dash = dash;
		this.theTree = theTree;
		setModal(true);
		Enumeration<?> en = node.children();
		@SuppressWarnings("unchecked")
		List<DefaultMutableTreeNode> list = (List<DefaultMutableTreeNode>) Collections.list(en);
		for (DefaultMutableTreeNode s : UtilMethodsFactory.reversed(list)) {
			if (s.getUserObject() instanceof PokerOpponentPosition) {
				PokerOpponentPosition pokerOpponentPosition = (PokerOpponentPosition) s.getUserObject();
				String popNodeName = pokerOpponentPosition.getNodeText();
				int indexOfFirstLiteral = UtilMethodsFactory.getIndexOfFirstLiteralInString(popNodeName);
				String popName = popNodeName.substring(indexOfFirstLiteral, popNodeName.length() - indexOfFirstLiteral + 1);
				popNames.add(popName);
				String chartImagePath = pokerOpponentPosition.getChartImagePath();
				String fileName = chartImagePath.split("/")[chartImagePath.split("/").length - 1];
				String prefix = fileName.substring(0, UtilMethodsFactory.getIndexOfFirstLiteralInString(fileName));
				popChartOrderPrefixes.add(prefix);
			}
		}
		chartsPanel = new JPanel(new SpringLayout());
		int x = 0;
		while (x < popNames.size()) {
			chartDataPanel = new JPanel(new SpringLayout());
			chartDataPanel.add(new JLabel(" " + popNames.get(x) + " "));
			JComboBox<String> jComboBox = new JComboBox<String>(popChartOrderPrefixes.toArray(new String[0]));
			jComboBox.setSelectedIndex(x);
			chartDataPanel.add(jComboBox);
			positionMap.put(popNames.get(x), jComboBox);
			SpringUtilities.makeCompactGrid(chartDataPanel, 2, 1, 10, 10, 10, 5);
			chartsPanel.add(chartDataPanel);
			x++;
		}
		buttonsPanel = new JPanel(new SpringLayout());
		applyButton = new JButton(" Apply");
		applyButton.addActionListener(this);
		buttonsPanel.add(applyButton);
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		buttonsPanel.add(cancelButton);
		SpringUtilities.makeCompactGrid(buttonsPanel, 2, 1, 10, 10, 10, 5);
		chartsPanel.add(buttonsPanel);
		SpringUtilities.makeCompactGrid(chartsPanel, 1, popNames.size() + 1, 10, 10, 1, 10);
		add(chartsPanel, BorderLayout.CENTER);
		setTitle("Chnage Charts Order");
	}

	/*
	 * Listens to click on Apply button
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		JButton theButton = (JButton) ae.getSource();
		if (theButton.getText().equals(" Apply")) {
			// Get tree root node (poker solution)
			DefaultMutableTreeNode top = (DefaultMutableTreeNode) tree.getModel().getRoot();
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
			//Populate joiner with folder names in he path
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
			if (checkForDupSequenceIndex(positionMap)) {
				JOptionPane.showMessageDialog(this, "Duplicated locations. Please correct", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				// Rename files 
				// Loop over frame titles 
				for (String title : frameTitles) {
					//Get the last token of dash-delimited frame title 
					fileSystemPathTockens = title.split("-");
					String lastTocken = fileSystemPathTockens[fileSystemPathTockens.length - 1];
					// Get POP name from the map associating used inputed sequence of the frame location with frame POP name.   
					String newSequencePrefix = (String) positionMap.get(lastTocken).getSelectedItem();
					// Verify if read-only PNG images of the charts are already created
					if (hasPNGFile(filesList, lastTocken)) {
						// If PNG files are already created loop over frame titles list and change sequence prefix in the file names of both INI and PNG files 
						for (String theTitle : frameTitles) {
							// Rename only if frame title contains the name of POP object rename the INI file with sequence number requested by user
							if (theTitle.split("-")[theTitle.split("-").length - 1].contains(lastTocken)) {
								UtilMethodsFactory.renameFile(map.get(lastTocken), fileSystemPath + "\\" + String.valueOf(newSequencePrefix) + lastTocken + ".ini");
								UtilMethodsFactory.renameFile(map.get(lastTocken).replace("ini", "png"), fileSystemPath + "\\" + String.valueOf(newSequencePrefix) + lastTocken + ".png");
							}
						}
						
					} else {
						// If PNG files are not there yet loop over frame titles list and change sequence prefix in the file names of INI files  
						for (String theTitle : frameTitles) {
							// Rename only if frame title contains the name of POP object rename the INI file with sequence number requested by user
							if (theTitle.split("-")[theTitle.split("-").length - 1].contains(lastTocken)) {
								UtilMethodsFactory.renameFile(map.get(lastTocken), fileSystemPath + "\\" + String.valueOf(newSequencePrefix) + lastTocken + ".ini");
							}
						}
					}
				}
				repositionCharts(top, solutionName);
				this.dispose();
			}
		} else {
			this.dispose();
		}
	}

	private void repositionCharts(DefaultMutableTreeNode top, String solutionName) {
		theTree.refreshTreeNode(top, solutionName);
		JTabbedPane sourceTabbedPane = dash.getTreeTabbedPane();
		int index = sourceTabbedPane.getSelectedIndex();
		dash.closeAllFrames();
		if (INIFilesFactory.hasItemInSection(UtilMethodsFactory.getConsoleConfig(), "Selections", sourceTabbedPane.getTitleAt(index))) {
			String pathString = INIFilesFactory.getItemValueFromINI(UtilMethodsFactory.getConsoleConfig(), "Selections", sourceTabbedPane.getTitleAt(index));
			JScrollPane scroll = (JScrollPane) (sourceTabbedPane.getSelectedComponent());
			JScrollableDesktopPane desctopPane = dash.getJScrollableDesktopPane();
			CustomTree tree = (CustomTree) scroll.getViewport().getView();
			TreePath path = tree.selectTreeNode((DefaultMutableTreeNode) tree.getTreeModel().getRoot(), pathString, tree);
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

	private boolean checkForDupSequenceIndex(TreeMap<String, JComboBox<String>> comboBoxes) {
		Set<Entry<String, JComboBox<String>>> set = comboBoxes.entrySet();
		Iterator<Entry<String, JComboBox<String>>> it = set.iterator();
		List<String> allItems = new ArrayList<String>();
		while (it.hasNext()) {
			Map.Entry<String, JComboBox<String>> me = (Map.Entry<String, JComboBox<String>>) it.next();
			allItems.add((String) me.getValue().getSelectedItem());
		}
		Collections.sort(allItems);
		for (int i = 0; i < allItems.size() - 1; i++) {
			if (allItems.get(i).equals(allItems.get(i + 1))) {
				return true;
			}
		}
		return false;
	}
	
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
