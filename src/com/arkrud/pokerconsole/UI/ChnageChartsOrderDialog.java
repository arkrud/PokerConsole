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
import java.util.StringJoiner;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
import com.arkrud.pokerconsole.Util.UtilMethodsFactory;

/**
 * @author arkrud
 *
 */
public class ChnageChartsOrderDialog extends JDialog implements ActionListener {
	private JTree tree;
	private Dashboard dash;
	private Object obj;
	private DefaultMutableTreeNode node;
	private CustomTree theTree;
	private JButton applyButton, cancelButton;
	private JPanel chartsPanel, popNamesPanel, popChartOrderPrefixesPanel;
	private List<String> popNames = new ArrayList<String>();
	private List<String> popChartOrderPrefixes = new ArrayList<String>();
	private TreeMap<String, JComboBox<String>> positionMap = new TreeMap<String, JComboBox<String>>();
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	public ChnageChartsOrderDialog(JTree tree, DefaultMutableTreeNode node, Dashboard dash, Object obj, CustomTree theTree) {
		this.tree = tree;
		this.obj = obj;
		this.dash = dash;
		this.node = node;
		this.theTree = theTree;
		setModal(true);
		Enumeration<?> en = node.children();
		@SuppressWarnings("unchecked")
		List<DefaultMutableTreeNode> list = (List<DefaultMutableTreeNode>) Collections.list(en);
		for (DefaultMutableTreeNode s : UtilMethodsFactory.reversed(list)) {
			if (s.getUserObject() instanceof PokerOpponentPosition) {
				PokerOpponentPosition pokerOpponentPosition = (PokerOpponentPosition) s.getUserObject();
				int indexOfFirstLiteral = UtilMethodsFactory.getIndexOfFirstLiteralInString(pokerOpponentPosition.getNodeText());
				String popName = pokerOpponentPosition.getNodeText().substring(indexOfFirstLiteral,
						pokerOpponentPosition.getNodeText().length() - indexOfFirstLiteral + 1);
				popNames.add(popName);
				String chartImagePath = pokerOpponentPosition.getChartImagePath();
				String fileName = chartImagePath.split("/")[chartImagePath.split("/").length - 1];
				String prefix = fileName.substring(0, UtilMethodsFactory.getIndexOfFirstLiteralInString(fileName));
				popChartOrderPrefixes.add(prefix);
			}
		}
		popNamesPanel = new JPanel(new SpringLayout());
		int x = 0;
		while (x < popNames.size()) {
			popNamesPanel.add(new JLabel("   " + popNames.get(x) + "    "));
			x++;
		}
		applyButton = new JButton(" Apply");
		applyButton.addActionListener(this);
		popNamesPanel.add(applyButton);
		SpringUtilities.makeCompactGrid(popNamesPanel, 1, popNames.size() + 1, 10, 10, 10, 1);
		popChartOrderPrefixesPanel = new JPanel(new SpringLayout());
		int y = 0;
		while (y < popNames.size()) {
			JComboBox<String> jComboBox = new JComboBox<String>(popChartOrderPrefixes.toArray(new String[0]));
			jComboBox.setSelectedIndex(y);
			popChartOrderPrefixesPanel.add(jComboBox);
			positionMap.put(popNames.get(y), jComboBox);
			y++;
		}
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		popChartOrderPrefixesPanel.add(cancelButton);
		SpringUtilities.makeCompactGrid(popChartOrderPrefixesPanel, 1, popChartOrderPrefixes.size() + 1, 10, 10, 10, 10);
		chartsPanel = new JPanel(new SpringLayout());
		chartsPanel.add(popNamesPanel);
		chartsPanel.add(popChartOrderPrefixesPanel);
		SpringUtilities.makeCompactGrid(chartsPanel, 2, 1, 10, 10, 10, 10);
		add(chartsPanel, BorderLayout.CENTER);
		setTitle("Chnage Charts Order");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		JButton theButton = (JButton) ae.getSource();
		if (theButton.getText().equals(" Apply")) {
			DefaultMutableTreeNode top = (DefaultMutableTreeNode) tree.getModel().getRoot();
			String solutionName = ((PokerStrategy) top.getUserObject()).getNodeText();
			String fileSystemPath = UtilMethodsFactory.getConfigPath().substring(1, UtilMethodsFactory.getConfigPath().length()) + "Images/" + solutionName
					+ "/";
			StringJoiner joiner = new StringJoiner("/");
			JInternalFrame[] frames = dash.getAllFrames();
			List<String> frameTitles = new ArrayList<String>();
			int z = 0;
			while (z < frames.length) {
				frameTitles.add(frames[z].getTitle());
				z++;
			}
			int x = 0;
			String[] fileSystemPathTockens = frameTitles.get(0).split("-");
			while (x < fileSystemPathTockens.length - 1) {
				joiner.add(fileSystemPathTockens[x]);
				x++;
			}
			fileSystemPath = (fileSystemPath + joiner.toString()).replace("/", "\\");
			List<String> filesList = UtilMethodsFactory.listFiles(fileSystemPath);
			UtilMethodsFactory.printList(filesList);
			Collections.reverse(filesList);
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
			// renaming files
			for (String title : frameTitles) {
				fileSystemPathTockens = title.split("-");
				String lastTocken = fileSystemPathTockens[fileSystemPathTockens.length - 1];
				String newSequencePrefix = (String) positionMap.get(lastTocken).getSelectedItem();
				System.out.println(newSequencePrefix);
				if (map.get(lastTocken).contains("png")) {
					for (String theTitle : frameTitles) {
						if (theTitle.split("-")[theTitle.split("-").length - 1].contains(lastTocken)) {
							UtilMethodsFactory.renameFile(map.get(lastTocken), fileSystemPath + "\\" + String.valueOf(newSequencePrefix) + lastTocken + ".ini");
						}
					}
					if (hasPNGFile(filesList)) {
						for (String theTitle : frameTitles) {
							if (theTitle.split("-")[theTitle.split("-").length - 1].contains(lastTocken)) {
								UtilMethodsFactory.renameFile(map.get(lastTocken).replace("ini", "png"),
										fileSystemPath + "\\" + String.valueOf(newSequencePrefix) + lastTocken + ".png");
							}
						}
					}
				} else {
					for (String theTitle : frameTitles) {
						if (theTitle.split("-")[theTitle.split("-").length - 1].contains(lastTocken)) {
							UtilMethodsFactory.renameFile(map.get(lastTocken), fileSystemPath + "\\" + String.valueOf(newSequencePrefix) + lastTocken + ".ini");
						}
					}
				}
			}
			System.out.println(node);
			System.out.println(theTree);
			System.out.println(obj);
			theTree.refreshTreeNode(node, solutionName);
			this.dispose();
		} else {
			this.dispose();
		}
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
				String pokerOpponentPositionname = pokerOpponentPosition.getNodeText().substring(theIndesOfFirstLiteral,
						pokerOpponentPosition.getNodeText().length() - theIndesOfFirstLiteral + 1);
				// System.out.println("newPOPName: " + newPOPName);
				String newPath = newFilePath.substring(newFilePath.indexOf("Images")).replace("\\", "/");
				System.out.println("pokerOpponentPositionname: " + pokerOpponentPositionname);
				if (newPOPName.contains(pokerOpponentPositionname)) {
					System.out.println("old: " + pokerOpponentPosition.getChartImagePath());
					System.out.println("new: " + newFilePath.substring(newFilePath.indexOf("Images")).replace("\\", "/"));
					pokerOpponentPosition.setChartImagePath(newPath);
					break;
				}
			}
		}
	}
}
