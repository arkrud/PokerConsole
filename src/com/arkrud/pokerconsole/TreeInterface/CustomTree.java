package com.arkrud.pokerconsole.TreeInterface;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DropMode;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SpringLayout;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.arkrud.Shareware.SpringUtilities;
import com.arkrud.pokerconsole.Poker.Fork;
import com.arkrud.pokerconsole.Poker.PokerAction;
import com.arkrud.pokerconsole.Poker.PokerHandSizing;
import com.arkrud.pokerconsole.Poker.PokerOpponentPosition;
import com.arkrud.pokerconsole.Poker.PokerPosition;
import com.arkrud.pokerconsole.Poker.PokerStrategy;
import com.arkrud.pokerconsole.UI.Dashboard.Dashboard;
import com.arkrud.pokerconsole.Util.UtilMethodsFactory;

/**
 * CustomTree class to build Poker charts management Tree.<br>
 * It will organize chart by Poker Strategy, Poker Action, Sizing, Poker table position, and Poker table opponent position.<br>
 * Tree nodes provide drop down menus relevant to the actions allowed for object the node represents including action to refresh node children.<br>
 * Double click on container nodes will show the charts or chart group in scrollable desktop for fast view of defined hand ranges. <br>
 * Tree objects are rendered with object and container specific icons and object and container names <br>
 *
 */
public class CustomTree extends JPanel implements TreeWillExpandListener, TreeSelectionListener {
	private static final long serialVersionUID = 1L;
	private DefaultMutableTreeNode top;
	private DefaultTreeModel treeModel;
	private JTree jTree;
	private Dashboard dash;
	private DefaultMutableTreeNode pokerActionTreeNode = null;
	private DefaultMutableTreeNode pokerHandSizingTreeNode = null;
	private DefaultMutableTreeNode pokerPositionTreeNode = null;
	private DefaultMutableTreeNode pokerOpponentPositionTreeNode = null;
	private DefaultMutableTreeNode forkTreeNode = null;
	private PokerAction pokerAction = null;
	private PokerHandSizing pokerHandSizing = null;
	private PokerPosition pokerPosition = null;
	private Fork fork = null;

	/**
	 *
	 * Sole constructor of CustomTree object. <br>
	 * Defines the behavior of the tree interface and adds it to the JPanel<br>
	 * <ul>
	 * <li>Set tree top element to PokerStrategy object.
	 * <li>Initiate default JTree model.
	 * <li>Add CustomTreeModelListener to model to implement dynamic tree behaver.
	 * <li>Instantiate JTee object.
	 * <li>Add TreeWillExpandListener to listen to Tree will expand events.
	 * <li>Set row height to 0 to have tree object sizes to be defined by rendered images .
	 * <li>Create tree structure from INI or PNG image files in file system or from image objects in Mongo DB tables.
	 * <li>Set tree selection model to single selection.
	 * <li>Add TreeSelectionListener to listen to tree selection events.
	 * <li>Set root handles to be shown.
	 * <li>Set white background for tree area.
	 * <li>Set cell renderer to show images and custom text for the nodes.
	 * <li>Add pop-up menu to the nodes.
	 * <li>Set SpringLayout for panel holding the tree.
	 * <li>Set white background for panel under the tree.
	 * <li>Add three to panel.
	 * <li>Set settings for SpringLayout.
	 * </ul>
	 * <p>
	 *
	 * @param dash reference to the Dashboard object
	 * @param treeName tree usage identifier (OT or OCT)
	 */
	public CustomTree(Dashboard dash, String treeName, boolean editable) {
		this.dash = dash;
		top = new DefaultMutableTreeNode(new PokerStrategy(treeName));
		treeModel = new DefaultTreeModel(top);
		treeModel.addTreeModelListener(new CustomTreeModelListener());
		jTree = new JTree(treeModel);
		jTree.addTreeWillExpandListener(this);
		jTree.setRowHeight(0);
		try {
			createNodes(top);
		} catch (Exception e) {
			e.printStackTrace();
		}
		jTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		jTree.addTreeSelectionListener(this);
		jTree.setShowsRootHandles(true);
		jTree.setBackground(Color.WHITE);
		ToolTipManager.sharedInstance().registerComponent(jTree);
		jTree.setCellRenderer(new CustomTreeCellRenderer());
		/*
		 * jTree.setDragEnabled(true); jTree.setDropMode(DropMode.ON_OR_INSERT); jTree.setTransferHandler(new TreeTransferHandler());
		 */
		try {
			getTreePopUpMenu(jTree, editable);
		} catch (Exception e) {
			e.printStackTrace();
		}
		setLayout(new SpringLayout());
		setBackground(Color.WHITE);
		add(jTree);
		SpringUtilities.makeCompactGrid(this, 1, 1, 1, 1, 1, 1);
	}

	/**
	 * Expand nodes below selected tree node.
	 *
	 * @param node the node
	 * @param cloudTree the cloud tree
	 */
	public void expandNodesBelow(DefaultMutableTreeNode node, JTree cloudTree) {
		cloudTree.expandPath(new TreePath(((DefaultTreeModel) cloudTree.getModel()).getPathToRoot(node)));
	}

	/**
	 * Gets the tree reference.
	 *
	 * @return the tree
	 */
	public JTree getTheTree() {
		return jTree;
	}

	/**
	 * Gets the tree model reference.
	 *
	 * @return the tree model
	 */
	public DefaultTreeModel getTreeModel() {
		return treeModel;
	}

	/**
	 * Refresh tree node.
	 * <ul>
	 * <li>Remove all nodes.
	 * <li>Call node structure changed to reflect removal.
	 * <li>Recreate nodes from underlying data.
	 * </ul>
	 * 
	 * @param node the node
	 * @param strategyName the strategy name
	 */
	public void refreshTreeNode(DefaultMutableTreeNode node, String strategyName) {
		node.removeAllChildren();
		treeModel.nodeStructureChanged(node);
		try {
			createNodes(node);
		} catch (Exception e) {
			e.printStackTrace();
		}
		expandNodesBelow(node);
	}

	/**
	 * Defines logic by which specific tree node is selected when tabs of various strategies and strategy copies are clicked.
	 *
	 * <ul>
	 * <li>Represent dash-delimited INI file string as array of tokens.
	 * <li>Calculate the child count of node.
	 * <li>For every length of path extract the object properties match with the data from INI and initiate selection.
	 * </ul>
	 *
	 * @param node The root node of the tree
	 * @param pathString The charts or chart groups to be opened represented by string defined in INI file
	 * @param tree The tree object to work on
	 * @return The TreePath object of selected node
	 */
	public TreePath selectTreeNode(DefaultMutableTreeNode node, String pathString, CustomTree tree) {
		String[] pathNodes = pathString.split("-");
		int childCount = node.getChildCount();
		TreePath path = null;
		if (pathNodes.length == 1) {
			for (int i = 0; i < childCount; i++) {
				DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) node.getChildAt(i);
				if (childNode.getUserObject() instanceof PokerAction) {
					if (((PokerAction) (childNode.getUserObject())).getNodeText().equals(pathNodes[0])) {
						path = setSelection(childNode, jTree, false);
					}
				}
			}
		} else if (pathNodes.length == 2) {
			for (int i = 0; i < childCount; i++) {
				DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) node.getChildAt(i);
				if (childNode.getUserObject() instanceof PokerAction) {
					if (((PokerAction) (childNode.getUserObject())).getNodeText().equals(pathNodes[0])) {
						int childCount1 = childNode.getChildCount();
						for (int x = 0; x < childCount1; x++) {
							DefaultMutableTreeNode childNode1 = (DefaultMutableTreeNode) childNode.getChildAt(x);
							if (childNode1.getUserObject() instanceof PokerOpponentPosition) {
								if (((PokerOpponentPosition) (childNode1.getUserObject())).getNodeText().equals(Integer.toString(x + 1) + pathNodes[1])) {
									path = setSelection(childNode1, jTree, false);
								}
							} else if (childNode1.getUserObject() instanceof PokerHandSizing) {
								if (((PokerHandSizing) (childNode1.getUserObject())).getNodeText().equals(pathNodes[1])) {
									path = setSelection(childNode1, jTree, false);
								}
							}
						}
					}
				}
			}
		} else if (pathNodes.length == 4) {
			for (int i = 0; i < childCount; i++) {
				DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) node.getChildAt(i);
				if (childNode.getUserObject() instanceof PokerAction) {
					if (((PokerAction) (childNode.getUserObject())).getNodeText().equals(pathNodes[0])) {
						int childCount1 = childNode.getChildCount();
						for (int x = 0; x < childCount1; x++) {
							DefaultMutableTreeNode childNode1 = (DefaultMutableTreeNode) childNode.getChildAt(x);
							int childCount2 = childNode1.getChildCount();
							if (childNode1.getUserObject() instanceof PokerHandSizing) {
								if (((PokerHandSizing) (childNode1.getUserObject())).getNodeText().equals(pathNodes[1])) {
									for (int y = 0; y < childCount2; y++) {
										DefaultMutableTreeNode childNode2 = (DefaultMutableTreeNode) childNode1.getChildAt(y);
										int childCount3 = childNode2.getChildCount();
										if (childNode2.getUserObject() instanceof PokerPosition) {
											if (((PokerPosition) (childNode2.getUserObject())).getNodeText().equals(pathNodes[2])) {
												for (int z = 0; z < childCount3; z++) {
													DefaultMutableTreeNode childNode3 = (DefaultMutableTreeNode) childNode2.getChildAt(z);
													if (childNode3.getUserObject() instanceof PokerOpponentPosition) {
														if (childCount3 < 10) {
															if (((PokerOpponentPosition) (childNode3.getUserObject())).getNodeText().equals(Integer.toString(z + 1) + pathNodes[3])) {
																path = setSelection(childNode3, jTree, false);
															}
														} else {
															if (Character.isDigit(((PokerOpponentPosition) (childNode3.getUserObject())).getNodeText().charAt(1))) {
																if (((PokerOpponentPosition) (childNode3.getUserObject())).getNodeText().equals(Integer.toString(z + 10) + pathNodes[3])) {
																	path = setSelection(childNode3, jTree, false);
																}
															} else {
																if (((PokerOpponentPosition) (childNode3.getUserObject())).getNodeText().equals(Integer.toString(z) + pathNodes[3])) {
																	path = setSelection(childNode3, jTree, false);
																}
															}
														}
													} else if (childNode3.getUserObject() instanceof Fork) {
														path = setSelection(childNode3, jTree, false);
													}
												}
											}
										}
									}
								}
							} else if (childNode1.getUserObject() instanceof PokerOpponentPosition) {
								if (childCount1 < 10) {
									if (((PokerOpponentPosition) (childNode1.getUserObject())).getNodeText().equals(Integer.toString(x + 1) + pathNodes[2])) {
										path = setSelection(childNode1, jTree, false);
									} else {
										if (Character.isDigit(((PokerOpponentPosition) (childNode1.getUserObject())).getNodeText().charAt(1))) {
											if (((PokerOpponentPosition) (childNode1.getUserObject())).getNodeText().equals(Integer.toString(x + 10) + pathNodes[3])) {
												path = setSelection(childNode1, jTree, false);
											}
										} else {
											if (((PokerOpponentPosition) (childNode1.getUserObject())).getNodeText().equals(Integer.toString(x) + pathNodes[3])) {
												path = setSelection(childNode1, jTree, false);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} else if (pathNodes.length == 3) {
			for (int i = 0; i < childCount; i++) {
				DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) node.getChildAt(i);
				if (childNode.getUserObject() instanceof PokerAction) {
					if (((PokerAction) (childNode.getUserObject())).getNodeText().equals(pathNodes[0])) {
						int childCount1 = childNode.getChildCount();
						for (int x = 0; x < childCount1; x++) {
							DefaultMutableTreeNode childNode1 = (DefaultMutableTreeNode) childNode.getChildAt(x);
							int childCount2 = childNode1.getChildCount();
							if (childNode1.getUserObject() instanceof PokerHandSizing) {
								if (((PokerHandSizing) (childNode1.getUserObject())).getNodeText().equals(pathNodes[1])) {
									for (int y = 0; y < childCount2; y++) {
										DefaultMutableTreeNode childNode2 = (DefaultMutableTreeNode) childNode1.getChildAt(y);
										if (childNode2.getUserObject() instanceof PokerPosition) {
											if (((PokerPosition) (childNode2.getUserObject())).getNodeText().equals(pathNodes[2])) {
												path = setSelection(childNode2, jTree, false);
											}
										} else if (childNode2.getUserObject() instanceof PokerOpponentPosition) {
											if (childCount2 < 10) {
												if (((PokerOpponentPosition) (childNode2.getUserObject())).getNodeText().equals(Integer.toString(y+1) + pathNodes[2])) {
													path = setSelection(childNode2, jTree, false);
												}
											} else {
												if (Character.isDigit(((PokerOpponentPosition) (childNode2.getUserObject())).getNodeText().charAt(1))) {
													if (((PokerOpponentPosition) (childNode2.getUserObject())).getNodeText().equals(Integer.toString(y+10) + pathNodes[2])) {
														path = setSelection(childNode2, jTree, false);
													}
												} else {
													if (((PokerOpponentPosition) (childNode2.getUserObject())).getNodeText().equals(Integer.toString(y) + pathNodes[2])) {
														path = setSelection(childNode2, jTree, false);
													}
												}
											}
										}
									}
								}
							} else if (childNode1.getUserObject() instanceof PokerPosition) {
								if (((PokerPosition) (childNode1.getUserObject())).getNodeText().equals(pathNodes[1])) {
									for (int y = 0; y < childCount2; y++) {
										DefaultMutableTreeNode childNode2 = (DefaultMutableTreeNode) childNode1.getChildAt(y);
										if (((PokerOpponentPosition) (childNode2.getUserObject())).getNodeText().equals(Integer.toString(y + 1) + pathNodes[2])) {
											path = setSelection(childNode2, jTree, false);
										}
									}
								}
							} else if (childNode1.getUserObject() instanceof PokerOpponentPosition) {
								if (((PokerOpponentPosition) (childNode1.getUserObject())).getNodeText().equals(pathNodes[1])) {
									for (int y = 0; y < childCount2; y++) {
										DefaultMutableTreeNode childNode2 = (DefaultMutableTreeNode) childNode1.getChildAt(y);
										if (((PokerPosition) (childNode2.getUserObject())).getNodeText().equals(pathNodes[2])) {
											path = setSelection(childNode2, jTree, false);
										}
									}
								}
							}
						}
					}
				} else {
				}
			}
		} else if (pathNodes.length == 5) {
			for (int i = 0; i < childCount; i++) {
				DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) node.getChildAt(i);
				if (childNode.getUserObject() instanceof PokerAction) {
					if (((PokerAction) (childNode.getUserObject())).getNodeText().equals(pathNodes[0])) {
						int childCount1 = childNode.getChildCount();
						for (int x = 0; x < childCount1; x++) {
							DefaultMutableTreeNode childNode1 = (DefaultMutableTreeNode) childNode.getChildAt(x);
							int childCount2 = childNode1.getChildCount();
							if (childNode1.getUserObject() instanceof PokerHandSizing) {
								if (((PokerHandSizing) (childNode1.getUserObject())).getNodeText().equals(pathNodes[1])) {
									for (int y = 0; y < childCount2; y++) {
										DefaultMutableTreeNode childNode2 = (DefaultMutableTreeNode) childNode1.getChildAt(y);
										int childCount3 = childNode2.getChildCount();
										if (childNode2.getUserObject() instanceof PokerPosition) {
											if (((PokerPosition) (childNode2.getUserObject())).getNodeText().equals(pathNodes[2])) {
												for (int z = 0; z < childCount3; z++) {
													DefaultMutableTreeNode childNode3 = (DefaultMutableTreeNode) childNode2.getChildAt(z);
													int childCount4 = childNode3.getChildCount();
													if (childNode3.getUserObject() instanceof Fork) {
														if (((Fork) (childNode3.getUserObject())).getNodeText().equals(pathNodes[3])) {
															for (int n = 0; n < childCount4; n++) {
																DefaultMutableTreeNode childNode4 = (DefaultMutableTreeNode) childNode3.getChildAt(n);
																if (((PokerOpponentPosition) (childNode4.getUserObject())).getNodeText().equals(Integer.toString(n + 1) + pathNodes[4])) {
																	path = setSelection(childNode4, jTree, false);
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return path;
	}

	/**
	 * Override interface method to specify what to do when tree will collapse. <br>
	 */
	@Override
	public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
	}

	@Override
	public void treeWillExpand(TreeExpansionEvent e) throws ExpandVetoException {
	}

	/**
	 * Register a tree selection listener to detect when the user selects a node in a tree. <br>
	 *
	 * @param e the e
	 */
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		JTree sourceTree = (JTree) e.getSource();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) sourceTree.getLastSelectedPathComponent();
		if (node == null)
			return;
	}

	/**
	 * Builds the tree nodes from structure of files on the file system.
	 *
	 * @param node the node
	 * @param treeNode the tree node
	 */
	private void buildTreeNodes(File node, DefaultMutableTreeNode treeNode) {
		int level = node.getAbsoluteFile().getPath().split("\\\\").length - UtilMethodsFactory.getConfigPath().split("/").length;
		if (level == 0) {
		} else if (level == 2) {
			pokerAction = new PokerAction(node.getName());
			pokerActionTreeNode = new DefaultMutableTreeNode(pokerAction);
			treeNode.add(pokerActionTreeNode);
		} else if (level == 3) {
			if (node.isFile()) {
				if (!node.getName().contains("png")) {
					PokerOpponentPosition pokerOpponentPosition = new PokerOpponentPosition(node.getName().split("\\.")[0]);
					pokerOpponentPosition.setPokerAction(pokerAction.getNodeText());
					pokerOpponentPosition.setChartPaneTitle(pokerAction.getNodeText() + "-" + pokerOpponentPosition.getNodeText().substring(1));
					pokerOpponentPosition.setChartImagePath("Images/" + ((PokerStrategy) treeNode.getUserObject()).getNodeText() + "/" + pokerAction.getNodeText() + "/" + pokerOpponentPosition.getNodeText() + ".ini");
					DefaultMutableTreeNode pokerOpponentPositionTreeNode = new DefaultMutableTreeNode(pokerOpponentPosition);
					pokerActionTreeNode.add(pokerOpponentPositionTreeNode);
				}
			} else if (node.isDirectory()) {
				if (isPokerPosition(node.getName())) {
					pokerPosition = new PokerPosition(node.getName());
					pokerPosition.setChartPaneTitle(pokerAction.getNodeText() + "-" + pokerHandSizing.getNodeText() + "-" + pokerPosition.getNodeText());
					pokerPositionTreeNode = new DefaultMutableTreeNode(pokerPosition);
					pokerActionTreeNode.add(pokerPositionTreeNode);
				} else {
					pokerHandSizing = new PokerHandSizing(node.getName(), pokerAction);
					pokerHandSizingTreeNode = new DefaultMutableTreeNode(pokerHandSizing);
					pokerActionTreeNode.add(pokerHandSizingTreeNode);
				}
			}
		} else if (level == 4) {
			if (node.isFile()) {
				if (!node.getName().contains("png")) {
					PokerOpponentPosition pokerOpponentPosition = new PokerOpponentPosition(node.getName().split("\\.")[0]);
					if (isParentAPokerPosition(node.getParent())) {
						pokerOpponentPosition.setChartPaneTitle(pokerAction.getNodeText() + "/" + pokerPosition.getNodeText() + "/" + pokerOpponentPosition.getNodeText().substring(1));
						pokerOpponentPosition
								.setChartImagePath("Images/" + ((PokerStrategy) treeNode.getUserObject()).getNodeText() + "/" + pokerAction.getNodeText() + "/" + pokerPosition.getNodeText() + "/" + pokerOpponentPosition.getNodeText() + ".ini");
						DefaultMutableTreeNode pokerOpponentPositionTreeNode = new DefaultMutableTreeNode(pokerOpponentPosition);
						pokerPositionTreeNode.add(pokerOpponentPositionTreeNode);
					} else {
						pokerOpponentPosition.setChartPaneTitle(pokerAction.getNodeText() + "-" + pokerHandSizing.getNodeText() + "-" + pokerOpponentPosition.getNodeText().substring(1));
						pokerOpponentPosition
								.setChartImagePath("Images/" + ((PokerStrategy) treeNode.getUserObject()).getNodeText() + "/" + pokerAction.getNodeText() + "/" + pokerHandSizing.getNodeText() + "/" + pokerOpponentPosition.getNodeText() + ".ini");
						DefaultMutableTreeNode pokerOpponentPositionTreeNode = new DefaultMutableTreeNode(pokerOpponentPosition);
						pokerHandSizingTreeNode.add(pokerOpponentPositionTreeNode);
					}
				}
			} else if (node.isDirectory()) {
				pokerPosition = new PokerPosition(node.getName());
				pokerPositionTreeNode = new DefaultMutableTreeNode(pokerPosition);
				pokerPosition.setChartPaneTitle(pokerAction.getNodeText() + "-" + pokerHandSizing.getNodeText() + "-" + pokerPosition.getNodeText());
				pokerHandSizingTreeNode.add(pokerPositionTreeNode);
			}
		} else if (level == 5) {
			if (node.isFile()) {
				PokerOpponentPosition pokerOpponentPosition = new PokerOpponentPosition(node.getName().split("\\.")[0]);
				if (!node.getName().contains("png")) {
					if (Character.isDigit(pokerOpponentPosition.getNodeText().charAt(0)) & Character.isDigit(pokerOpponentPosition.getNodeText().charAt(1))) {
						pokerOpponentPosition.setChartPaneTitle(pokerAction.getNodeText() + "-" + pokerHandSizing.getNodeText() + "-" + pokerPosition.getNodeText() + "-" + pokerOpponentPosition.getNodeText().substring(2));
					} else {
						pokerOpponentPosition.setChartPaneTitle(pokerAction.getNodeText() + "-" + pokerHandSizing.getNodeText() + "-" + pokerPosition.getNodeText() + "-" + pokerOpponentPosition.getNodeText().substring(1));
					}
					pokerOpponentPosition.setPokerAction(pokerAction.getNodeText());
					pokerOpponentPosition.setPokerHandSizing(pokerHandSizing.getNodeText());
					pokerOpponentPosition.setPokerPosition(pokerPosition.getNodeText());
					pokerOpponentPosition.setChartImagePath("Images/" + ((PokerStrategy) treeNode.getUserObject()).getNodeText() + "/" + pokerAction.getNodeText() + "/" + pokerHandSizing.getNodeText() + "/" + pokerPosition.getNodeText() + "/"
							+ pokerOpponentPosition.getNodeText() + ".ini");
					pokerOpponentPosition.getNodeText();
					pokerOpponentPositionTreeNode = new DefaultMutableTreeNode(pokerOpponentPosition);
					pokerPositionTreeNode.add(pokerOpponentPositionTreeNode);
				}
			} else if (node.isDirectory()) {
				fork = new Fork(node.getName());
				forkTreeNode = new DefaultMutableTreeNode(fork);
				fork.setChartPaneTitle(pokerAction.getNodeText() + "-" + pokerHandSizing.getNodeText() + "-" + fork.getNodeText());
				pokerPositionTreeNode.add(forkTreeNode);
			}
		} else if (level == 6) {
			PokerOpponentPosition pokerOpponentPosition = new PokerOpponentPosition(node.getName().split("\\.")[0]);
			if (!node.getName().contains("png")) {
				pokerOpponentPosition.setPokerAction(pokerAction.getNodeText());
				pokerOpponentPosition.setPokerHandSizing(pokerHandSizing.getNodeText());
				pokerOpponentPosition.setPokerPosition(pokerPosition.getNodeText());
				pokerOpponentPosition.setChartPaneTitle(pokerAction.getNodeText() + "-" + pokerHandSizing.getNodeText() + "-" + pokerPosition.getNodeText() + "-" + fork.getNodeText() + "-" + pokerOpponentPosition.getNodeText().substring(1));
				pokerOpponentPosition.setChartImagePath("Images/" + ((PokerStrategy) treeNode.getUserObject()).getNodeText() + "/" + pokerAction.getNodeText() + "/" + pokerHandSizing.getNodeText() + "/" + pokerPosition.getNodeText() + "/"
						+ fork.getNodeText() + "/" + pokerOpponentPosition.getNodeText() + ".ini");
				pokerOpponentPosition.getNodeText();
				pokerOpponentPositionTreeNode = new DefaultMutableTreeNode(pokerOpponentPosition);
				forkTreeNode.add(pokerOpponentPositionTreeNode);
			}
		} else {
		}
		if (node.isDirectory()) {
			String[] subNote = node.list();
			for (String filename : subNote) {
				buildTreeNodes(new File(node, filename), treeNode);
			}
		}
	}

	/**
	 * Populate tree with data. <br>
	 * Defines the behavior of the tree interface and adds it to the JPanel<br>
	 *
	 * @param top root node
	 *
	 */
	private void createNodes(DefaultMutableTreeNode top) throws Exception {
		buildTreeNodes(new File(UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerStrategy) top.getUserObject()).getNodeText()), top);
	}

	/**
	 * Expand nodes below selected node.<br>
	 *
	 * @param node the node
	 */
	private void expandNodesBelow(DefaultMutableTreeNode node) {
		jTree.expandPath(new TreePath(((DefaultTreeModel) jTree.getModel()).getPathToRoot(node)));
	}

	/**
	 * Add menu items to PopUp menu with various actions defined. <br>
	 *
	 * @param s the menu item text
	 * @param al the Action listener
	 * @return the menu item
	 */
	private JMenuItem getMenuItem(String s, ActionListener al) {
		JMenuItem menuItem = new JMenuItem(s);
		menuItem.setActionCommand(s.toUpperCase());
		menuItem.addActionListener(al);
		return menuItem;
	}

	/**
	 * Add PopUp menu to cluster icons in the tree to initiate actions on them. <br>
	 *
	 * @param servicesTree the services tree
	 * @param editable the editable
	 * @return the tree pop up menu
	 * @throws Exception the exception
	 */
	private void getTreePopUpMenu(JTree servicesTree, boolean editable) throws Exception {
		JPopupMenu popup = null;
		if (popup == null) {
			popup = new JPopupMenu();
			popup.setInvoker(servicesTree);
			// Instantiate Pop-up Menu handler class instance
			CustomTreePopupHandler handler = new CustomTreePopupHandler(servicesTree, popup, dash, this, editable);
			if (editable) {
				for (String dropDownMenuName : UtilMethodsFactory.dropDownsNames) {
					popup.add(getMenuItem(dropDownMenuName, handler));
				}
			}
		}
	}

	/**
	 * Checks if branch node is one of the Poker Position nodes not sizing or strategy node.
	 *
	 * @param nodeText the node text
	 * @return true, if is poker position
	 */
	private boolean isPokerPosition(String nodeText) {
		String[] handScreenNames = { "BB", "SB", "BU", "CO", "HJ", "LJ", "UTG", "UTG1", "UTG2" };
		boolean isPokerPosition = false;
		int x = 0;
		while (x < handScreenNames.length) {
			if (handScreenNames[x].equals(nodeText)) {
				isPokerPosition = true;
				break;
			}
			x++;
		}
		return isPokerPosition;
	}

	private boolean isParentAPokerPosition(String nodeText) {
		String[] handScreenNames = { "BB", "SB", "BU", "CO", "HJ", "LJ", "UTG", "UTG1", "UTG2" };
		boolean isPokerPosition = false;
		int x = 0;
		while (x < handScreenNames.length) {
			if (handScreenNames[x].equals(nodeText.split("\\\\")[nodeText.split("\\\\").length - 1])) {
				isPokerPosition = true;
				break;
			}
			x++;
		}
		return isPokerPosition;
	}

	/**
	 * Checks if branch node is one of the Fork nodes not sizing or strategy node.
	 *
	 * @param nodeText the node text
	 * @return true, if is poker position
	 */
	/*
	 * private boolean isFork(String nodeText) { String[] handScreenNames = { "3BetOrFold", "WithFlats" }; boolean isFork = false; int x = 0; while (x < handScreenNames.length) { if
	 * (handScreenNames[x].equals(nodeText)) { isFork = true; break; } x++; } return isFork; }
	 */
	/**
	 * Sets selected node, scrolls to it, and expand children.
	 *
	 * @param node the node
	 * @param tree the tree
	 * @return the tree path
	 */
	public TreePath setSelection(DefaultMutableTreeNode node, JTree tree, Boolean expandNodesBelow) {
		TreePath treePath = new TreePath(node.getPath());
		tree.setSelectionPath(treePath);
		tree.scrollPathToVisible(treePath);
		if (expandNodesBelow) {
			expandNodesBelow(node, tree);
		}
		return treePath;
	}
}
