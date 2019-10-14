package com.arkrud.pokerconsole.TreeInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
import com.arkrud.pokerconsole.UI.ChartPanel;
import com.arkrud.pokerconsole.UI.Dashboard.CustomTableViewInternalFrame;
import com.arkrud.pokerconsole.UI.Dashboard.Dashboard;
import com.arkrud.pokerconsole.UI.scrollabledesktop.BaseInternalFrame;
import com.arkrud.pokerconsole.Util.INIFilesFactory;
import com.arkrud.pokerconsole.Util.UtilMethodsFactory;

public class CustomTreePopupHandler implements ActionListener, PropertyChangeListener {
	private JTree tree;
	private TreePath path;
	private Dashboard dash;
	private CustomTreeMouseListener cml;
	private CustomTree theTree;
	final JFileChooser fc = new JFileChooser();

	public CustomTreePopupHandler(JTree tree, JPopupMenu popup, Dashboard dash, CustomTree theTree, boolean editable) {
		// Pass variables values into the class
		this.tree = tree;
		this.dash = dash;
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
		// For leaf objects
		if (obj instanceof PokerStrategy) {
			if (ac.equals("REFRESH")) {
				theTree.refreshTreeNode(node, ((PokerStrategy) obj).getNodeText());
			} else if (ac.equals("ADD ACTION")) {
				String s = (String) JOptionPane.showInputDialog(dash, "New Action", "Add Action", JOptionPane.PLAIN_MESSAGE, null, null, null);
				if (s != null) {
					if (checkForNewObjectName(node, s)) {
						JOptionPane.showConfirmDialog(null, "The Action is Already there", "Duplicated Action Warning", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
					} else {
						if ((s != null) && (s.length() > 0)) {
							PokerAction pokerAction = new PokerAction(s);
							File actionDir = new File(UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerStrategy) obj).getNodeText() + "/" + s);
							UtilMethodsFactory.createGRoupFolder(actionDir);
							DefaultMutableTreeNode actionNode = new DefaultMutableTreeNode(pokerAction);
							DefaultMutableTreeNode root = (DefaultMutableTreeNode) theTree.getTreeModel().getRoot();
							((DefaultTreeModel) tree.getModel()).insertNodeInto(actionNode, root, root.getChildCount());
							theTree.expandNodesBelow(root, tree);
						}
					}
				} else {
				}
			} else if (ac.equals("REMOVE")) {
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
			} else if (ac.equals("DUPLICATE")) {
				String treeName = ((PokerStrategy) node.getUserObject()).getNodeText();
				INIFilesFactory.addINIFileItemToSection(UtilMethodsFactory.getConsoleConfig(), "Applications", treeName + "-copy", true);
				dash.addTreeTabPaneTab(treeName + "-copy");
			}
		} else if (obj instanceof PokerAction) {
			if (ac.equals("ADD SIZING")) {
				String s = (String) JOptionPane.showInputDialog(dash, "New Sizing", "Add Sizing", JOptionPane.PLAIN_MESSAGE, null, null, null);
				if (s != null) {
					if (checkForNewObjectName(node, s)) {
						JOptionPane.showConfirmDialog(null, "The sizing is Already there", "Duplicated Sizing Warning", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
					} else {
						if ((s != null) && (s.length() > 0)) {
							PokerHandSizing sizing = new PokerHandSizing(s, ((PokerAction) obj));
							DefaultMutableTreeNode top = (DefaultMutableTreeNode) tree.getModel().getRoot();
							File sizingDir = new File(UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/" + ((PokerAction) obj).getNodeText() + "/" + s);
							UtilMethodsFactory.createGRoupFolder(sizingDir);
							DefaultMutableTreeNode sizingNode = new DefaultMutableTreeNode(sizing);
							((DefaultTreeModel) tree.getModel()).insertNodeInto(sizingNode, node, sizingNode.getChildCount());
							theTree.expandNodesBelow(node, tree);
						}
					}
				} else {
				}
			} else if (ac.equals("ADD HANDS")) {
				UtilMethodsFactory.showDialogToDesctop("AddHandsDialog", 210, 360, dash, tree, theTree, obj, node, null,null);
			} else if (ac.equals("ADD OPPONENTS POSITION")) {
				String s = (String) JOptionPane.showInputDialog(dash, "New Opponets Position", "Add Opponets Position", JOptionPane.PLAIN_MESSAGE, null, null, null);
				if (s != null) {
					if (checkForNewObjectName(node, s)) {
						JOptionPane.showConfirmDialog(null, "This Opponents Position is Already there", "Duplicated Opponents Position Warning", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
					} else {
						if ((s != null) && (s.length() > 0)) {
							PokerAction pokerAction = (PokerAction) (node.getUserObject());
							DefaultMutableTreeNode top = (DefaultMutableTreeNode) tree.getModel().getRoot();
							File chartINIFileName = new File(
									UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/" + pokerAction.getNodeText() + "/" + Integer.toString(node.getChildCount() + 1) + s + ".ini");
							File template = new File(UtilMethodsFactory.getConfigPath() + "/chart_template.ini");
							try {
								UtilMethodsFactory.copyFileUsingJava7Files(template, chartINIFileName);
							} catch (IOException ex) {
								ex.printStackTrace();
							}
							PokerOpponentPosition pokerOpponentPosition = new PokerOpponentPosition(Integer.toString(node.getChildCount() + 1) + s);
							pokerOpponentPosition.setSelected(false);
							pokerOpponentPosition.setChartPaneTitle(pokerAction.getNodeText() + "-" + s);
							pokerOpponentPosition.setChartImagePath("Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/" + pokerAction.getNodeText() + "/" + Integer.toString(node.getChildCount() + 1) + s + ".ini");
							DefaultMutableTreeNode pokerOpponentPositionNode = new DefaultMutableTreeNode(pokerOpponentPosition);
							((DefaultTreeModel) tree.getModel()).insertNodeInto(pokerOpponentPositionNode, node, node.getChildCount());
							theTree.expandNodesBelow(node, tree);
						}
					}
				}
			} else if (ac.equals("REMOVE")) {
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
			} else {
			}
		} else if (obj instanceof PokerHandSizing) {
			if (ac.equals("DELETE SIZING")) {
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
			} else if (ac.equals("ADD HANDS")) {
				UtilMethodsFactory.showDialogToDesctop("AddHandsDialog", 210, 360, dash, tree, theTree, obj, node, null, null);
			} else if (ac.equals("ADD OPPONENTS POSITION")) {
				String s = (String) JOptionPane.showInputDialog(dash, "New Opponets Position", "Add Opponets Position", JOptionPane.PLAIN_MESSAGE, null, null, null);
				if (s != null) {
					if (checkForNewObjectName(node, s)) {
						JOptionPane.showConfirmDialog(null, "This Opponents Position is Already there", "Duplicated Opponents Position Warning", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
					} else {
						if ((s != null) && (s.length() > 0)) {
							PokerHandSizing pokerHandSizing = (PokerHandSizing) (node.getUserObject());
							DefaultMutableTreeNode top = (DefaultMutableTreeNode) tree.getModel().getRoot();
							int n = 0;
							int opponentsHangsNodesCount = 0;
							while (n < node.getChildCount()) {
								if (node.getChildAt(n) instanceof PokerOpponentPosition) {
									opponentsHangsNodesCount++;
								}
								n++;
							}
							File chartINIFileName = new File(UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/" + pokerHandSizing.getPokerAction().getNodeText() + "/"
									+ pokerHandSizing.getNodeText() + "/" + Integer.toString(opponentsHangsNodesCount + 1) + s + ".ini");
							File template = new File(UtilMethodsFactory.getConfigPath() + "/chart_template.ini");
							try {
								UtilMethodsFactory.copyFileUsingJava7Files(template, chartINIFileName);
							} catch (IOException ex) {
								ex.printStackTrace();
							}
							PokerOpponentPosition pokerOpponentPosition = new PokerOpponentPosition(Integer.toString(opponentsHangsNodesCount + 1) + s);
							pokerOpponentPosition.setSelected(false);
							pokerOpponentPosition.setChartPaneTitle(pokerHandSizing.getPokerAction().getNodeText() + "-" + pokerHandSizing.getNodeText() + "-" + s);
							pokerOpponentPosition.setChartImagePath("Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/" + pokerHandSizing.getPokerAction().getNodeText() + "/" + pokerHandSizing.getNodeText() + "/"
									+ Integer.toString(opponentsHangsNodesCount + 1) + s + ".ini");
							DefaultMutableTreeNode pokerOpponentPositionNode = new DefaultMutableTreeNode(pokerOpponentPosition);
							((DefaultTreeModel) tree.getModel()).insertNodeInto(pokerOpponentPositionNode, node, opponentsHangsNodesCount);
							theTree.expandNodesBelow(node, tree);
						}
					}
				}
			} else {
			}
		} else if (obj instanceof PokerPosition) {
			if (ac.equals("REMOVE")) {
				int response = JOptionPane.showConfirmDialog(null, "Do you want to remove the Poker Position", "Poker Position Removal", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (response == JOptionPane.NO_OPTION) {
				} else if (response == JOptionPane.YES_OPTION) {
					PokerPosition pokerPosition = (PokerPosition) obj;
					DefaultMutableTreeNode top = (DefaultMutableTreeNode) tree.getModel().getRoot();
					PokerHandSizing pokerHandSizing = (PokerHandSizing) ((DefaultMutableTreeNode) node.getParent()).getUserObject();
					String fileSystemPath = UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/" + pokerHandSizing.getPokerAction().getNodeText() + "/" + pokerHandSizing.getNodeText() + "/"
							+ pokerPosition.getNodeText();
					UtilMethodsFactory.deleteDirectory(new File(fileSystemPath));
					((DefaultTreeModel) tree.getModel()).removeNodeFromParent(node);
				} else if (response == JOptionPane.CLOSED_OPTION) {
				}
			} else if (ac.equals("ADD OPPONENTS POSITION")) {
				String s = (String) JOptionPane.showInputDialog(dash, "New Opponets Position", "Add Opponets Position", JOptionPane.PLAIN_MESSAGE, null, null, null);
				if (s != null) {
					if (checkForNewObjectName(node, s)) {
						JOptionPane.showConfirmDialog(null, "This Opponents Position is Already there", "Duplicated Opponents Position Warning", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
					} else {
						if ((s != null) && (s.length() > 0)) {
							PokerPosition pokerPosition = (PokerPosition) (node.getUserObject());
							DefaultMutableTreeNode top = (DefaultMutableTreeNode) tree.getModel().getRoot();
							PokerHandSizing pokerHandSizing = (PokerHandSizing) ((DefaultMutableTreeNode) node.getParent()).getUserObject();
							File chartINIFileName = new File(UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/" + pokerHandSizing.getPokerAction().getNodeText() + "/"
									+ pokerHandSizing.getNodeText() + "/" + pokerPosition.getNodeText() + "/" + Integer.toString(node.getChildCount() + 1) + s + ".ini");
							File template = new File(UtilMethodsFactory.getConfigPath() + "/chart_template.ini");
							try {
								UtilMethodsFactory.copyFileUsingJava7Files(template, chartINIFileName);
							} catch (IOException ex) {
								ex.printStackTrace();
							}
							PokerOpponentPosition pokerOpponentPosition = new PokerOpponentPosition(Integer.toString(node.getChildCount() + 1) + s);
							pokerOpponentPosition.setSelected(false);
							pokerOpponentPosition.setChartPaneTitle(pokerHandSizing.getPokerAction().getNodeText() + "-" + pokerHandSizing.getNodeText() + "-" + pokerPosition.getNodeText() + "-" + s);
							pokerOpponentPosition.setChartImagePath("Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/" + pokerHandSizing.getPokerAction().getNodeText() + "/" + pokerHandSizing.getNodeText() + "/"
									+ pokerPosition.getNodeText() + "/" + Integer.toString(node.getChildCount() + 1) + s + ".ini");
							DefaultMutableTreeNode pokerOpponentPositionNode = new DefaultMutableTreeNode(pokerOpponentPosition);
							((DefaultTreeModel) tree.getModel()).insertNodeInto(pokerOpponentPositionNode, node, node.getChildCount());
							theTree.expandNodesBelow(node, tree);
						}
					}
				} else {
				}
			}
		} else if (obj instanceof PokerOpponentPosition) {
			String templatePath = "";
			File file = null;
			if (ac.equals("APPLY TEMPLATE")) {
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
				ChartPanel chartPanel = new ChartPanel(templatePath, true);
				dash.getJScrollableDesktopPane().getDesktopMediator().closeAllFrames();
				BaseInternalFrame theFrame = new CustomTableViewInternalFrame(chartTitle, chartPanel);
				UtilMethodsFactory.addInternalFrameToScrolableDesctopPane(chartTitle, dash.getJScrollableDesktopPane(), theFrame);
				String thePath = UtilMethodsFactory.getConfigPath() + ((PokerOpponentPosition) obj).getChartImagePath();
				thePath = thePath.replace("/", "\\");
				thePath = thePath.substring(1, thePath.length());
				thePath = thePath.replace("jpg", "ini");
				String a = thePath.split("\\.")[0];
				UtilMethodsFactory.tableToImage(chartPanel.getTable(), a);
			} else if (ac.equals("REMOVE")) {
				int response = JOptionPane.showConfirmDialog(null, "Do you want to remove the Opponents Position", "Opponents Position Removal", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (response == JOptionPane.NO_OPTION) {
				} else if (response == JOptionPane.YES_OPTION) {
					PokerOpponentPosition pokerOpponentPosition = (PokerOpponentPosition) obj;
					DefaultMutableTreeNode top = (DefaultMutableTreeNode) tree.getModel().getRoot();
					String fileSystemPath = "";
					if (((DefaultMutableTreeNode) node.getParent()).getUserObject() instanceof PokerPosition) {
						PokerPosition pokerPosition = (PokerPosition) ((DefaultMutableTreeNode) node.getParent()).getUserObject();
						DefaultMutableTreeNode sizingNode = (DefaultMutableTreeNode) ((DefaultMutableTreeNode) node.getParent()).getParent();
						DefaultMutableTreeNode actionNode = (DefaultMutableTreeNode) (sizingNode.getParent());
						PokerHandSizing pokerHandSizing = (PokerHandSizing) sizingNode.getUserObject();
						PokerAction pokerAction = (PokerAction) actionNode.getUserObject();
						fileSystemPath = UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/" + pokerAction.getNodeText() + "/" + pokerHandSizing.getNodeText() + "/" + pokerPosition.getNodeText()
								+ "/" + pokerOpponentPosition.getNodeText() + ".ini";
					} else if (((DefaultMutableTreeNode) node.getParent()).getUserObject() instanceof PokerAction) {
						PokerAction pokerAction = (PokerAction) ((DefaultMutableTreeNode) node.getParent()).getUserObject();
						fileSystemPath = UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/" + pokerAction.getNodeText() + "/" + pokerOpponentPosition.getNodeText() + ".ini";
					} else if (((DefaultMutableTreeNode) node.getParent()).getUserObject() instanceof PokerHandSizing) {
						PokerHandSizing pokerHandSizing = (PokerHandSizing) ((DefaultMutableTreeNode) node.getParent()).getUserObject();
						fileSystemPath = UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/" + pokerHandSizing.getPokerAction().getNodeText() + "/" + pokerHandSizing.getNodeText() + "/"
								+ pokerOpponentPosition.getNodeText() + ".ini";
					}
					dash.getJScrollableDesktopPane().getDesktopMediator().closeAllFrames();
					UtilMethodsFactory.deleteDirectory(new File(fileSystemPath));
					((DefaultTreeModel) tree.getModel()).removeNodeFromParent(node);
				} else if (response == JOptionPane.CLOSED_OPTION) {
				}
			} else {
			}
		} else {
		}
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
			}
		}
		return hit;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
	}
}