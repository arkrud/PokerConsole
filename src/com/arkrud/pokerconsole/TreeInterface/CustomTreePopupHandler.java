package com.arkrud.pokerconsole.TreeInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.arkrud.pokerconsole.Poker.PokerAction;
import com.arkrud.pokerconsole.Poker.PokerHandSizing;
import com.arkrud.pokerconsole.Poker.PokerOpponentPosition;
import com.arkrud.pokerconsole.Poker.PokerPosition;
import com.arkrud.pokerconsole.Poker.PokerStrategy;
import com.arkrud.pokerconsole.UI.TableChartPanel;
import com.arkrud.pokerconsole.UI.Dashboard.Dashboard;
import com.arkrud.pokerconsole.Util.INIFilesFactory;
import com.arkrud.pokerconsole.Util.UtilMethodsFactory;

public class CustomTreePopupHandler implements ActionListener {
	private JTree tree;
	private TreePath path;
	private Dashboard dash;
	private boolean editable;
	private CustomTreeMouseListener cml;
	private CustomTree theTree;
	final JFileChooser fc = new JFileChooser();

	public CustomTreePopupHandler(JTree tree, JPopupMenu popup, Dashboard dash, CustomTree theTree, boolean editable) {
		// Pass variables values into the class
		this.tree = tree;
		this.dash = dash;
		this.editable = editable;
		this.theTree = theTree;
		// Add Mouse listener to control which menu items will show up in drop-down menu
		cml = new CustomTreeMouseListener(popup, dash, editable);
		tree.addMouseListener(cml);
	}

	// Perform actions on drop-down menu selections
	@Override
	public void actionPerformed(ActionEvent e) {
		String ac = e.getActionCommand();
		DefaultMutableTreeNode node = null;
		path = tree.getPathForLocation(cml.getLoc().x, cml.getLoc().y);
		node = (DefaultMutableTreeNode) path.getLastPathComponent();
		Object obj = ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();
		if (obj instanceof PokerStrategy) {
			if (ac.equals("REFRESH")) {
				theTree.refreshTreeNode(node, ((PokerStrategy) obj).getNodeText());
			} else if (ac.equals("ADD ACTION")) {
				addPokerAction(node, obj);
			} else if (ac.equals("REMOVE")) {
				removePokerStrategy(node);
			} else if (ac.equals("DUPLICATE")) {
				duplicateSolution(node);
			}
		} else if (obj instanceof PokerAction) {
			if (ac.equals("ADD SIZING")) {
				addPokerSizing(node, obj);
			} else if (ac.equals("ADD HANDS")) {
				UtilMethodsFactory.showDialogToDesctop("AddHandsDialog", 210, 360, dash, tree, theTree, obj, node, null, null);
			} else if (ac.equals("ADD OPPONENTS POSITION")) {
				addOpponentPosition(node);
			} else if (ac.equals("REMOVE")) {
				removePokerAction(node, obj);
			} else {
			}
		} else if (obj instanceof PokerHandSizing) {
			if (ac.equals("DELETE SIZING")) {
				removePokerSizing(node, obj);
			} else if (ac.equals("ADD HANDS")) {
				UtilMethodsFactory.showDialogToDesctop("AddHandsDialog", 210, 360, dash, tree, theTree, obj, node, null, null);
			} else if (ac.equals("ADD OPPONENTS POSITION")) {
				addOpponentPosition(node);
			} else {
			}
		} else if (obj instanceof PokerPosition) {
			if (ac.equals("REMOVE")) {
				removePokerPositon(node, obj);
			} else if (ac.equals("ADD OPPONENTS POSITION")) {
				addOpponentPosition(node);
			}
		} else if (obj instanceof PokerOpponentPosition) {
			if (ac.equals("APPLY TEMPLATE")) {
				applyTemplate(obj);
			} else if (ac.equals("REMOVE")) {
				removePokerOpponentPositon(node, obj);
			} else {
			}
		} else {
		}
	}

	private void addOpponentPosition(DefaultMutableTreeNode node) {
		String s = ((String) JOptionPane.showInputDialog(dash, "New Opponets Position", "Add Opponets Position", JOptionPane.PLAIN_MESSAGE, null, null, null)).toUpperCase();
		if (s != null) {
			if (checkForNewObjectName(node, s)) {
				JOptionPane.showConfirmDialog(null, "This Opponents Position is Already there", "Duplicated Opponents Position Warning", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
			} else {
				if ((s != null) && (s.length() > 0)) {
					DefaultMutableTreeNode top = (DefaultMutableTreeNode) tree.getModel().getRoot();
					int opponentsHandsNodesCount = countRelevantNodes(node);
					PokerOpponentPosition pokerOpponentPosition = new PokerOpponentPosition(Integer.toString(opponentsHandsNodesCount + 1) + s);
					pokerOpponentPosition.setSelected(false);
					if (node.getUserObject() instanceof PokerAction) {
						PokerAction pokerAction = (PokerAction) (node.getUserObject());
						copyChartINIFile(UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/" + pokerAction.getNodeText() + "/" + Integer.toString(opponentsHandsNodesCount + 1) + s + ".ini");
						pokerOpponentPosition.setChartPaneTitle(pokerAction.getNodeText() + "-" + s);
						pokerOpponentPosition.setChartImagePath("Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/" + pokerAction.getNodeText() + "/" + Integer.toString(opponentsHandsNodesCount + 1) + s + ".ini");
					} else if (node.getUserObject() instanceof PokerHandSizing) {
						PokerHandSizing pokerHandSizing = (PokerHandSizing) (node.getUserObject());
						copyChartINIFile(UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/" + pokerHandSizing.getPokerAction().getNodeText() + "/" + pokerHandSizing.getNodeText() + "/"
								+ Integer.toString(opponentsHandsNodesCount + 1) + s + ".ini");
						pokerOpponentPosition.setChartPaneTitle(pokerHandSizing.getPokerAction().getNodeText() + "-" + pokerHandSizing.getNodeText() + "-" + s);
						pokerOpponentPosition.setChartImagePath("Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/" + pokerHandSizing.getPokerAction().getNodeText() + "/" + pokerHandSizing.getNodeText() + "/"
								+ Integer.toString(opponentsHandsNodesCount + 1) + s + ".ini");
					} else if (node.getUserObject() instanceof PokerPosition) {
						PokerPosition pokerPosition = (PokerPosition) (node.getUserObject());
						Object treeObject = ((DefaultMutableTreeNode) node.getParent()).getUserObject();
						if (treeObject instanceof PokerHandSizing) {
							PokerHandSizing pokerHandSizing = (PokerHandSizing) treeObject;
							copyChartINIFile(UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/" + pokerHandSizing.getPokerAction().getNodeText() + "/" + pokerHandSizing.getNodeText() + "/"
									+ pokerPosition.getNodeText() + "/" + Integer.toString(node.getChildCount() + 1) + s + ".ini");
							pokerOpponentPosition.setChartPaneTitle(pokerHandSizing.getPokerAction().getNodeText() + "-" + pokerHandSizing.getNodeText() + "-" + pokerPosition.getNodeText() + "-" + s);
							pokerOpponentPosition.setChartImagePath("Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/" + pokerHandSizing.getPokerAction().getNodeText() + "/" + pokerHandSizing.getNodeText() + "/"
									+ pokerPosition.getNodeText() + "/" + Integer.toString(node.getChildCount() + 1) + s + ".ini");
						} else if (treeObject instanceof PokerAction) {
							PokerAction pokerAction = (PokerAction) treeObject;
							copyChartINIFile(UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/" + pokerAction.getNodeText() + "/" + pokerPosition.getNodeText() + "/"
									+ Integer.toString(node.getChildCount() + 1) + s + ".ini");
							pokerOpponentPosition.setChartPaneTitle(pokerAction.getNodeText() + "-" + pokerPosition.getNodeText() + "-" + s);
							pokerOpponentPosition.setChartImagePath(
									"Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/" + pokerAction.getNodeText() + "/" + pokerPosition.getNodeText() + "/" + Integer.toString(node.getChildCount() + 1) + s + ".ini");
						}
					}
					displayChartFarame(pokerOpponentPosition, node);
				}
			}
		}
	}

	private void addPokerAction(DefaultMutableTreeNode node, Object obj) {
		String s = (String) JOptionPane.showInputDialog(dash, "New Action", "Add Action", JOptionPane.PLAIN_MESSAGE, null, null, null);
		if (s != null) {
			if (checkForNewObjectName(node, s)) {
				JOptionPane.showConfirmDialog(null, "The Action is Already there", "Duplicated Action Warning", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
			} else {
				if ((s != null) && (s.length() > 0)) {
					PokerAction pokerAction = new PokerAction(s);
					File actionDir = new File(UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerStrategy) obj).getNodeText() + "/" + s);
					UtilMethodsFactory.createFolder(actionDir);
					DefaultMutableTreeNode actionNode = new DefaultMutableTreeNode(pokerAction);
					DefaultMutableTreeNode root = (DefaultMutableTreeNode) theTree.getTreeModel().getRoot();
					((DefaultTreeModel) tree.getModel()).insertNodeInto(actionNode, root, root.getChildCount());
					theTree.expandNodesBelow(root, tree);
				}
			}
		} else {
		}
	}

	private void addPokerSizing(DefaultMutableTreeNode node, Object obj) {
		String s = (String) JOptionPane.showInputDialog(dash, "New Sizing", "Add Sizing", JOptionPane.PLAIN_MESSAGE, null, null, null);
		if (s != null) {
			if (checkForNewObjectName(node, s)) {
				JOptionPane.showConfirmDialog(null, "The sizing is Already there", "Duplicated Sizing Warning", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
			} else {
				if ((s != null) && (s.length() > 0)) {
					PokerHandSizing sizing = new PokerHandSizing(s, ((PokerAction) obj));
					DefaultMutableTreeNode top = (DefaultMutableTreeNode) tree.getModel().getRoot();
					File sizingDir = new File(UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/" + ((PokerAction) obj).getNodeText() + "/" + s);
					UtilMethodsFactory.createFolder(sizingDir);
					DefaultMutableTreeNode sizingNode = new DefaultMutableTreeNode(sizing);
					((DefaultTreeModel) tree.getModel()).insertNodeInto(sizingNode, node, sizingNode.getChildCount());
					theTree.expandNodesBelow(node, tree);
				}
			}
		} else {
		}
	}

	private void applyTemplate(Object obj) {
		String templatePath = "";
		File file = null;
		String chartTitle = ((PokerOpponentPosition) obj).getChartPaneTitle();
		String toPath = UtilMethodsFactory.getConfigPath() + ((PokerOpponentPosition) obj).getChartImagePath();
		toPath = toPath.replace("/", "\\\\");
		toPath = toPath.substring(2, toPath.length());
		toPath = toPath.replace("jpg", "ini");
		Path to = Paths.get(toPath); // convert from String to Path
		fc.setCurrentDirectory(new File(UtilMethodsFactory.getConfigPath() + "Images/"));
		int returnVal = fc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();
			templatePath = file.getAbsolutePath();
			templatePath = templatePath.substring(templatePath.indexOf("Images"), templatePath.length());
			templatePath = templatePath.replace("\\", "/");
		} else {
		}
		Path from = file.toPath(); // convert from File to Path
		try {
			Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		dash.closeAllFrames();
		UtilMethodsFactory.addChartFrameToScrolableDesctop(templatePath, chartTitle, true, dash.getJScrollableDesktopPane());
		String thePath = UtilMethodsFactory.getConfigPath() + ((PokerOpponentPosition) obj).getChartImagePath();
		thePath = thePath.replace("/", "\\");
		thePath = thePath.substring(1, thePath.length());
		thePath = thePath.replace("jpg", "ini");
		String a = thePath.split("\\.")[0];
		TableChartPanel chartPanel = new TableChartPanel(templatePath, editable);
		UtilMethodsFactory.tableToImage(chartPanel.getTable(), a);
	}

	private boolean checkForNewObjectName(DefaultMutableTreeNode node, String name) {
		boolean hit = false;
		Enumeration<?> en = node.children();
		@SuppressWarnings("unchecked")
		List<DefaultMutableTreeNode> list = (List<DefaultMutableTreeNode>) Collections.list(en);
		for (DefaultMutableTreeNode s : list) {
			if (s.getUserObject() instanceof PokerHandSizing) {
				PokerHandSizing pokerHandSizing = (PokerHandSizing) s.getUserObject();
				if (pokerHandSizing.getNodeText().equals(name)) {
					hit = true;
					break;
				} else {
					hit = false;
				}
			} else if (s.getUserObject() instanceof PokerAction) {
				PokerAction pokerAction = (PokerAction) s.getUserObject();
				if (pokerAction.getNodeText().equals(name)) {
					hit = true;
					break;
				} else {
					hit = false;
				}
			} else if (s.getUserObject() instanceof PokerOpponentPosition) {
				PokerOpponentPosition pokerOpponentPosition = (PokerOpponentPosition) s.getUserObject();
				String nodeText = pokerOpponentPosition.getNodeText();
				String firstLetter = UtilMethodsFactory.firstLetterOccurence(nodeText);
				int firstLetterPosition = nodeText.indexOf(firstLetter);
				String noPrefixName = nodeText.substring(firstLetterPosition, nodeText.length());
				if (noPrefixName.equals(name)) {
					hit = true;
					break;
				} else {
					hit = false;
				}
			}
		}
		return hit;
	}

	private void copyChartINIFile(String filePath) {
		File chartINIFileName = new File(filePath);
		File template = new File(UtilMethodsFactory.getConfigPath() + "/chart_template.ini");
		try {
			UtilMethodsFactory.copyFileUsingJava7Files(template, chartINIFileName);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private int countRelevantNodes(DefaultMutableTreeNode node) {
		int n = 0;
		int opponentsHandsNodesCount = 0;
		while (n < node.getChildCount()) {
			if (node.getChildAt(n) instanceof PokerOpponentPosition) {
				opponentsHandsNodesCount++;
			}
			n++;
		}
		return opponentsHandsNodesCount;
	}

	private void displayChartFarame(PokerOpponentPosition pokerOpponentPosition, DefaultMutableTreeNode node) {
		DefaultMutableTreeNode pokerOpponentPositionNode = new DefaultMutableTreeNode(pokerOpponentPosition);
		((DefaultTreeModel) tree.getModel()).insertNodeInto(pokerOpponentPositionNode, node, findSameNodeTypePosition(node));
		tree.setSelectionPath(new TreePath(pokerOpponentPositionNode.getPath()));
		theTree.expandNodesBelow(node, tree);
		dash.closeAllFrames();
		UtilMethodsFactory.addChartFrameToScrolableDesctop(pokerOpponentPosition.getChartImagePath(), pokerOpponentPosition.getChartPaneTitle(), editable, dash.getJScrollableDesktopPane());
	}

	private void duplicateSolution(DefaultMutableTreeNode node) {
		String treeName = ((PokerStrategy) node.getUserObject()).getNodeText();
		INIFilesFactory.addINIFileItemToSection(UtilMethodsFactory.getConsoleConfig(), "Applications", treeName + "-copy", true);
		dash.addTreeTabPaneTab(treeName + "-copy");
	}

	private int findSameNodeTypePosition(DefaultMutableTreeNode node) {
		int n = 0;
		int sameNodeTypePosition = 0;
		while (n < node.getChildCount()) {
			if (node.getChildAt(n) instanceof PokerOpponentPosition) {
				sameNodeTypePosition = n;
			}
			n++;
		}
		return sameNodeTypePosition;
	}

	private void removePokerAction(DefaultMutableTreeNode node, Object obj) {
		int response = JOptionPane.showConfirmDialog(null, "Do you want to remove the Poker Action", "Poker Action Removal", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.NO_OPTION) {
		} else if (response == JOptionPane.YES_OPTION) {
			PokerAction pokerAction = (PokerAction) obj;
			DefaultMutableTreeNode top = (DefaultMutableTreeNode) tree.getModel().getRoot();
			String fileSystemPath = UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/" + pokerAction.getNodeText();
			UtilMethodsFactory.deleteDirectory(new File(fileSystemPath));
			((DefaultTreeModel) tree.getModel()).removeNodeFromParent(node);
		} else if (response == JOptionPane.CLOSED_OPTION) {
		}
	}

	private void removePokerOpponentPositon(DefaultMutableTreeNode node, Object obj) {
		int response = JOptionPane.showConfirmDialog(null, "Do you want to remove the Opponents Position", "Opponents Position Removal", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.NO_OPTION) {
		} else if (response == JOptionPane.YES_OPTION) {
			PokerOpponentPosition pokerOpponentPosition = (PokerOpponentPosition) obj;
			DefaultMutableTreeNode top = (DefaultMutableTreeNode) tree.getModel().getRoot();
			String fileSystemPath = "";
			if (((DefaultMutableTreeNode) node.getParent()).getUserObject() instanceof PokerPosition) {
				PokerPosition pokerPosition = (PokerPosition) ((DefaultMutableTreeNode) node.getParent()).getUserObject();
				DefaultMutableTreeNode sizingNode = (DefaultMutableTreeNode) ((DefaultMutableTreeNode) node.getParent()).getParent();
				if (sizingNode.getUserObject() instanceof PokerHandSizing) {
					DefaultMutableTreeNode actionNode = (DefaultMutableTreeNode) (sizingNode.getParent());
					PokerHandSizing pokerHandSizing = (PokerHandSizing) sizingNode.getUserObject();
					PokerAction pokerAction = (PokerAction) actionNode.getUserObject();
					fileSystemPath = UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/" + pokerAction.getNodeText() + "/" + pokerHandSizing.getNodeText() + "/" + pokerPosition.getNodeText()
							+ "/" + pokerOpponentPosition.getNodeText() + ".ini";
				} else if (sizingNode.getUserObject() instanceof PokerAction) {
					PokerAction pokerAction = (PokerAction) sizingNode.getUserObject();
					fileSystemPath = UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/" + pokerAction.getNodeText() + "/" + pokerPosition.getNodeText() + "/"
							+ pokerOpponentPosition.getNodeText() + ".ini";
				}
			} else if (((DefaultMutableTreeNode) node.getParent()).getUserObject() instanceof PokerAction) {
				PokerAction pokerAction = (PokerAction) ((DefaultMutableTreeNode) node.getParent()).getUserObject();
				fileSystemPath = UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/" + pokerAction.getNodeText() + "/" + pokerOpponentPosition.getNodeText() + ".ini";
			} else if (((DefaultMutableTreeNode) node.getParent()).getUserObject() instanceof PokerHandSizing) {
				PokerHandSizing pokerHandSizing = (PokerHandSizing) ((DefaultMutableTreeNode) node.getParent()).getUserObject();
				fileSystemPath = UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/" + pokerHandSizing.getPokerAction().getNodeText() + "/" + pokerHandSizing.getNodeText() + "/"
						+ pokerOpponentPosition.getNodeText() + ".ini";
			}
			dash.closeAllFrames();
			UtilMethodsFactory.deleteDirectory(new File(fileSystemPath));
			((DefaultTreeModel) tree.getModel()).removeNodeFromParent(node);
		} else if (response == JOptionPane.CLOSED_OPTION) {
		}
	}

	private void removePokerPositon(DefaultMutableTreeNode node, Object obj) {
		int response = JOptionPane.showConfirmDialog(null, "Do you want to remove the Poker Position", "Poker Position Removal", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.NO_OPTION) {
		} else if (response == JOptionPane.YES_OPTION) {
			PokerPosition pokerPosition = (PokerPosition) obj;
			DefaultMutableTreeNode top = (DefaultMutableTreeNode) tree.getModel().getRoot();
			if (((DefaultMutableTreeNode) node.getParent()).getUserObject() instanceof PokerHandSizing) {
				PokerHandSizing pokerHandSizing = (PokerHandSizing) ((DefaultMutableTreeNode) node.getParent()).getUserObject();
				String fileSystemPath = UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/" + pokerHandSizing.getPokerAction().getNodeText() + "/" + pokerHandSizing.getNodeText() + "/"
						+ pokerPosition.getNodeText();
				UtilMethodsFactory.deleteDirectory(new File(fileSystemPath));
				((DefaultTreeModel) tree.getModel()).removeNodeFromParent(node);
			} else if (((DefaultMutableTreeNode) node.getParent()).getUserObject() instanceof PokerAction) {
				PokerAction pokerAction = (PokerAction) ((DefaultMutableTreeNode) node.getParent()).getUserObject();
				String fileSystemPath = UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/" + pokerAction.getNodeText() + "/" + pokerPosition.getNodeText();
				UtilMethodsFactory.deleteDirectory(new File(fileSystemPath));
				((DefaultTreeModel) tree.getModel()).removeNodeFromParent(node);
			}
		} else if (response == JOptionPane.CLOSED_OPTION) {
		}
	}

	private void removePokerSizing(DefaultMutableTreeNode node, Object obj) {
		int response = JOptionPane.showConfirmDialog(null, "Do you want to delete the Sizing", "Sizing Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.NO_OPTION) {
		} else if (response == JOptionPane.YES_OPTION) {
			PokerHandSizing sizing = (PokerHandSizing) obj;
			PokerAction action = sizing.getPokerAction();
			DefaultMutableTreeNode top = (DefaultMutableTreeNode) tree.getModel().getRoot();
			String fileSystemPath = UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/" + action.getNodeText() + '/' + sizing.getNodeText();
			UtilMethodsFactory.deleteDirectory(new File(fileSystemPath));
			((DefaultTreeModel) tree.getModel()).removeNodeFromParent(node);
		} else if (response == JOptionPane.CLOSED_OPTION) {
		}
	}

	private void removePokerStrategy(DefaultMutableTreeNode node) {
		String treeName = ((PokerStrategy) node.getUserObject()).getNodeText();
		String treeTabTitle = dash.getTreeTabbedPane().getTitleAt(dash.getTreeTabbedPane().getSelectedIndex());
		int response = JOptionPane.showConfirmDialog(null, "Do you want to remove this Tree With All Copies", "Solution Tree Complete removal", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.NO_OPTION) {
		} else if (response == JOptionPane.YES_OPTION) {
			int x = 0;
			ArrayList<String> treesArrayLIst = new ArrayList<String>();
			while (x < dash.getTreeTabbedPane().getTabCount()) {
				if (dash.getTreeTabbedPane().getTitleAt(x).contains(treeTabTitle)) {
					String title = dash.getTreeTabbedPane().getTitleAt(x);
					dash.removeTreeTabPaneTab(title);
					treesArrayLIst.add(title);
				}
				;
				x++;
			}
			String[] trees = treesArrayLIst.toArray(new String[treesArrayLIst.size()]);
			INIFilesFactory.removeINIFileItems(UtilMethodsFactory.getConsoleConfig(), "Applications", trees);
			INIFilesFactory.removeINIFileItems(UtilMethodsFactory.getConsoleConfig(), "Selections", trees);
			String fileSystemPath = UtilMethodsFactory.getConfigPath() + "Images/" + treeName;
			UtilMethodsFactory.deleteDirectory(new File(fileSystemPath));
		} else if (response == JOptionPane.CLOSED_OPTION) {
		}
	}
	
}