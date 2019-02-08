package com.arkrud.pokerconsole.TreeInterface;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

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

import org.dtools.ini.IniItem;

import com.arkrud.Shareware.SpringUtilities;
import com.arkrud.pokerconsole.Poker.PokerAction;
import com.arkrud.pokerconsole.Poker.PokerGroup;
import com.arkrud.pokerconsole.Poker.PokerHandSizing;
import com.arkrud.pokerconsole.Poker.PokerOpponentPosition;
import com.arkrud.pokerconsole.Poker.PokerPosition;
import com.arkrud.pokerconsole.Poker.PokerStrategy;
import com.arkrud.pokerconsole.UI.Dashboard.Dashboard;
import com.arkrud.pokerconsole.Util.INIFilesFactory;
import com.arkrud.pokerconsole.Util.UtilMethodsFactory;

/**
 * Class to build AWS Objects Tree (OT) and AWS Objects Configuration Tree (OCT).<br>
 * OT will organize AWS object accessible by user by Region, AWS Account, AWS Service, and AWS Object type. <br>
 * OT nodes provide drop down menus relevant to the actions allowed for object the node represents including action to refresh node children. <br>
 * Double click on container nodes will add associated object table control with data for filtered list of objects in multitable view area. <br>
 * OT objects are rendered with object and container specific icons and object and container names <br>
 * OCT provides the view of the same tree hierarchy as OT tree provides. <br>
 * But each object is presented as check box to activate or deactivate retrieval of AWS objects data based on accounts, services, and object types user desired. <br>
 * The OCT also provides filter objects which can be used to filter retrieval of the AWS objects based on text pattern. <br>
 * After all selections in OCT tree are done the root "Select To Finish Configuration" node needs to be selected to apply the changes. <br>
 * After objects and containers are checked in OCT OT tree needs to be refreshed to reflect the configuration change and retrieve desired objects from AWS cloud. <br>
 * Tree interface provides indeterministic progress bar while the AWS objects are retrieving which can take significant time for of large amount of them requested. <br>
 */
public class CustomTree extends JPanel implements TreeWillExpandListener, TreeSelectionListener, PropertyChangeListener {
	private static final long serialVersionUID = 1L;
	private DefaultMutableTreeNode top;
	private DefaultTreeModel treeModel;
	private JTree cloudTree;
	private Dashboard dash;
	private String treeType;
	private DefaultMutableTreeNode pokerActionTreeNode = null;
	private DefaultMutableTreeNode pokerHandSizingTreeNode = null;
	private DefaultMutableTreeNode pokerPositionTreeNode = null;
	private PokerAction pokerAction = null;
	private PokerHandSizing pokerHandSizing = null;
	private PokerPosition pokerPosition = null;
	private DefaultMutableTreeNode pokerOpponentPositionTreeNode = null;

	/**
	 *
	 * Sole constructor of CustomTree object. <br>
	 * Defines the behavior of the tree interface and adds it to the JPanel<br>
	 *
	 *
	 * <ul>
	 * <li>Set tree top element to AwsCommon object
	 * <li>Initiate default JTree model
	 * <li>Add CustomTreeModelListener
	 * </ul>
	 * <p>
	 *
	 * @param dash reference to the Dashboard object
	 * @param treeType tree usage identifier (OT or OCT)
	 */
	public CustomTree(Dashboard dash, String treeType) {
		this.dash = dash;
		this.treeType = treeType;
		if (treeType.equals("config")) {
			top = new DefaultMutableTreeNode(new PokerStrategy("Configuration"));
		} else {
			top = new DefaultMutableTreeNode(new PokerStrategy(treeType));
		}
		treeModel = new DefaultTreeModel(top);
		treeModel.addTreeModelListener(new CustomTreeModelListener());
		cloudTree = new JTree(treeModel);
		cloudTree.addTreeWillExpandListener(this);
		cloudTree.setRowHeight(0);
		cloudTree.setToggleClickCount(0);
		try {
			createNodes(top);
		} catch (Exception e) {
			e.printStackTrace();
		}
		cloudTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		cloudTree.addTreeSelectionListener(this);
		cloudTree.setShowsRootHandles(true);
		cloudTree.setBackground(Color.WHITE);
		ToolTipManager.sharedInstance().registerComponent(cloudTree);
		if (treeType.equals("config")) {
			cloudTree.setCellRenderer(new StateRenderer());
			cloudTree.setCellEditor(new StateEditor());
			cloudTree.setEditable(true);
		} else {
			cloudTree.setCellRenderer(new CustomTreeCellRenderer());
		}
		try {
			getTreePopUpMenu(cloudTree);
		} catch (Exception e) {
			e.printStackTrace();
		}
		setLayout(new SpringLayout());
		setBackground(Color.WHITE);
		add(cloudTree);
		SpringUtilities.makeCompactGrid(this, 1, 1, 1, 1, 1, 1);
	}

	private void buildSavedGroupNodes(DefaultMutableTreeNode top) {
		Collection<IniItem> items = INIFilesFactory.getAllItemsFromSection(UtilMethodsFactory.getConsoleConfig(), "Groups");
		Set<String> groupNames = new LinkedHashSet<String>();
		Iterator<IniItem> iterator = items.iterator();
		while (iterator.hasNext()) {
			IniItem iniItem = iterator.next();
			groupNames.add(iniItem.getName().substring(0, UtilMethodsFactory.getIndexOfFirstIntInString(iniItem.getName())));
			// groupNames.add(iniItem.getName().substring(0, iniItem.getName().indexOf("-")));
		}
		Iterator<String> it = groupNames.iterator();
		while (it.hasNext()) {
			String groupName = it.next();
			PokerGroup pokerGroup = new PokerGroup(groupName);
			DefaultMutableTreeNode pokerGroupTreeNode = new DefaultMutableTreeNode(pokerGroup);
			top.add(pokerGroupTreeNode);
			Iterator<IniItem> iterator1 = items.iterator();
			while (iterator1.hasNext()) {
				IniItem iniItem = iterator1.next();
				if (iniItem.getName().startsWith(groupName)) {
					String extOut = iniItem.getValue().substring(0, iniItem.getValue().indexOf("."));
					DefaultMutableTreeNode pokerOpponentPositionTreeNode = null;
					if (extOut.split("/").length > 3) {
						PokerOpponentPosition pokerOpponentPosition = new PokerOpponentPosition(extOut.split("/")[4]);
						pokerOpponentPosition.setPokerAction(extOut.split("/")[1]);
						pokerOpponentPosition.setPokerHandSizing(extOut.split("/")[2]);
						pokerOpponentPosition.setPokerPosition(extOut.split("/")[3]);
						pokerOpponentPosition.setChartPaneTitle(extOut.split("/")[1] + "-" + extOut.split("/")[2] + "-" + extOut.split("/")[3] + "-" + extOut.split("/")[4].substring(1));
						pokerOpponentPosition.setChartImagePath(iniItem.getValue());
						pokerOpponentPosition.setNodeText(extOut.split("/")[1] + "-" + extOut.split("/")[2] + "-" + extOut.split("/")[3] + "-" + extOut.split("/")[4].substring(1));
						pokerOpponentPositionTreeNode = new DefaultMutableTreeNode(pokerOpponentPosition);
					} else {
						PokerOpponentPosition pokerOpponentPosition = new PokerOpponentPosition(extOut.split("/")[2]);
						pokerOpponentPosition.setPokerAction(extOut.split("/")[1]);
						pokerOpponentPositionTreeNode = new DefaultMutableTreeNode(extOut.split("/")[1]);
						pokerOpponentPosition.setChartPaneTitle(extOut.split("/")[1] + "-" + extOut.split("/")[2].substring(1));
						pokerOpponentPosition.setChartImagePath(iniItem.getValue());
						pokerOpponentPositionTreeNode = new DefaultMutableTreeNode(pokerOpponentPosition);
					}
					pokerGroupTreeNode.add(pokerOpponentPositionTreeNode);
				}
			}
		}
	}

	private void buildTreeNodes(File node, DefaultMutableTreeNode treeNode) {
		int level = node.getAbsoluteFile().getPath().split("\\\\").length - UtilMethodsFactory.getConfigPath().split("/").length;
		if (level == 0) {
		} else if (level == 1) {
			pokerAction = new PokerAction(node.getName());
			pokerActionTreeNode = new DefaultMutableTreeNode(pokerAction);
			treeNode.add(pokerActionTreeNode);
		} else if (level == 2) {
			if (node.getAbsoluteFile().getPath().split("\\\\")[level + UtilMethodsFactory.getConfigPath().split("/").length / 2].equals("RFI")) {
				
				if (!node.getName().contains("ini")) {
					PokerOpponentPosition pokerOpponentPosition = new PokerOpponentPosition(node.getName().split("\\.")[0]);
					pokerOpponentPosition.setPokerAction(pokerAction.getNodeText());
					pokerOpponentPosition.setChartPaneTitle(pokerAction.getNodeText() + "-" + pokerOpponentPosition.getNodeText().substring(1));
					pokerOpponentPosition.setChartImagePath("Images/" + pokerAction.getNodeText() + "/" + pokerOpponentPosition.getNodeText() + ".jpg");
					DefaultMutableTreeNode pokerOpponentPositionTreeNode = new DefaultMutableTreeNode(pokerOpponentPosition);
					pokerActionTreeNode.add(pokerOpponentPositionTreeNode);
				} 
			} else {
				pokerHandSizing = new PokerHandSizing(node.getName(), pokerAction);
				pokerHandSizingTreeNode = new DefaultMutableTreeNode(pokerHandSizing);
				pokerActionTreeNode.add(pokerHandSizingTreeNode);
				
			}
		} else if (level == 3) {
			pokerPosition = new PokerPosition(node.getName().split("\\.")[0]);
			pokerPositionTreeNode = new DefaultMutableTreeNode(pokerPosition);
			pokerPosition.setChartPaneTitle(pokerAction.getNodeText() + "-" + pokerHandSizing.getNodeText() + "-" + pokerPosition.getNodeText());
			pokerHandSizingTreeNode.add(pokerPositionTreeNode);
		} else if (level == 4) {
			PokerOpponentPosition pokerOpponentPosition = new PokerOpponentPosition(node.getName().split("\\.")[0]);
			if (!node.getName().contains("ini")) {
				pokerOpponentPosition.setPokerAction(pokerAction.getNodeText());
				pokerOpponentPosition.setPokerHandSizing(pokerHandSizing.getNodeText());
				pokerOpponentPosition.setPokerPosition(pokerPosition.getNodeText());
				pokerOpponentPosition.setChartPaneTitle(pokerAction.getNodeText() + "-" + pokerHandSizing.getNodeText() + "-" + pokerPosition.getNodeText() + "-" + pokerOpponentPosition.getNodeText().substring(1));
				pokerOpponentPosition.setChartImagePath("Images/" + pokerAction.getNodeText() + "/" + pokerHandSizing.getNodeText() + "/" + pokerPosition.getNodeText() + "/" + pokerOpponentPosition.getNodeText() + ".jpg");
				pokerOpponentPositionTreeNode = new DefaultMutableTreeNode(pokerOpponentPosition);
				pokerPositionTreeNode.add(pokerOpponentPositionTreeNode);
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

	public Integer checkIfAnythingIsSelected(DefaultMutableTreeNode node) {
		Set<Boolean> selections = new LinkedHashSet<Boolean>();
		checkIfAnythingSelected(node, selections);
		return selections.size();
	}

	private int checkIfAnythingSelected(DefaultMutableTreeNode node, Set<Boolean> selections) {
		int childCount = node.getChildCount();
		for (int i = 0; i < childCount; i++) {
			DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) node.getChildAt(i);
			if (childNode.getChildCount() > 0) {
				checkIfAnythingSelected(childNode, selections);
			} else {
				if (childNode.getUserObject() instanceof PokerOpponentPosition) {
					boolean seleceed = ((PokerOpponentPosition) childNode.getUserObject()).isSelected();
					if (seleceed) {
					}
					selections.add(seleceed);
				}
			}
		}
		return selections.size();
	}

	public void clearAllTreeSelections(DefaultMutableTreeNode node) {
		int childCount = node.getChildCount();
		for (int i = 0; i < childCount; i++) {
			DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) node.getChildAt(i);
			if (childNode.getChildCount() > 0) {
				clearAllTreeSelections(childNode);
			} else {
				if (childNode.getUserObject() instanceof PokerOpponentPosition) {
					((PokerOpponentPosition) childNode.getUserObject()).setSelected(false);
				}
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
		buildTreeNodes(new File(UtilMethodsFactory.getConfigPath() + "Images"), top);
		buildSavedGroupNodes(top);
		expandTwoDeep();
	}

	/**
	 * Expand nodes below selected node after tree refresh. <br>
	 */
	private void expandNodesBelow(DefaultMutableTreeNode node) {
		cloudTree.expandPath(new TreePath(((DefaultTreeModel) cloudTree.getModel()).getPathToRoot(node)));
	}

	public void expandNodesBelow(DefaultMutableTreeNode node, JTree cloudTree) {
		cloudTree.expandPath(new TreePath(((DefaultTreeModel) cloudTree.getModel()).getPathToRoot(node)));
	}

	/**
	 * Expand tree 2 nodes deep. <br>
	 */
	public void expandTwoDeep() {
		for (int i = 0; i < 1; i++) {
			cloudTree.expandRow(i);
		}
	}

	/**
	 * Add menu items to PopUp menu with various actions defined. <br>
	 */
	private JMenuItem getMenuItem(String s, ActionListener al) {
		JMenuItem menuItem = new JMenuItem(s);
		menuItem.setActionCommand(s.toUpperCase());
		menuItem.addActionListener(al);
		return menuItem;
	}

	public DefaultTreeModel getTreeModel() {
		return treeModel;
	}

	/**
	 * Add PopUp menu to cluster icons in the tree to initiate actions on them. <br>
	 */
	private void getTreePopUpMenu(JTree servicesTree) throws Exception {
		JPopupMenu popup = null;
		if (popup == null) {
			popup = new JPopupMenu();
			popup.setInvoker(servicesTree);
			// Instantiate Pop-up Menu handler class instance
			CustomTreePopupHandler handler = new CustomTreePopupHandler(servicesTree, popup, dash, this);
			for (String dropDownMenuName : UtilMethodsFactory.dropDownsNames) {
				popup.add(getMenuItem(dropDownMenuName, handler));
			}
		}
	}

	public String getTreeType() {
		return treeType;
	}

	/**
	 * This method gets called when a bound property is changed. <br>
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
	}

	public void refreshTreeNode(DefaultMutableTreeNode node) {
		node.removeAllChildren();
		treeModel.nodeStructureChanged(node);
		try {
			createNodes(node);
		} catch (Exception e) {
			e.printStackTrace();
		}
		expandNodesBelow(node);
	}

	public void renameNode(DefaultMutableTreeNode node, String nodeText) {
		if (node.getUserObject() instanceof PokerStrategy) {
			PokerStrategy pokerStrategy = (PokerStrategy) (node.getUserObject());
			pokerStrategy.setNodeText(nodeText);
		} else if (node.getUserObject() instanceof PokerGroup) {
			PokerGroup pokerGroup = (PokerGroup) (node.getUserObject());
			pokerGroup.setNodeText(nodeText);
		}
		treeModel.nodeStructureChanged(node);
	}

	public TreePath selectTreeNode(DefaultMutableTreeNode node, String pathString, CustomTree tree) {
		String[] pathNodes = pathString.split("-");
		int childCount = node.getChildCount();
		TreePath path = null;
		if (pathNodes.length == 1) {
			for (int i = 0; i < childCount; i++) {
				DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) node.getChildAt(i);
				if (childNode.getUserObject() instanceof PokerAction) {
					if (((PokerAction) (childNode.getUserObject())).getNodeText().equals(pathNodes[0])) {
						path = new TreePath(childNode.getPath());
						cloudTree.setSelectionPath(path);
						cloudTree.scrollPathToVisible(path);
						cloudTree.expandPath(path);
					}
				} else if (childNode.getUserObject() instanceof PokerGroup) {
					if (((PokerGroup) (childNode.getUserObject())).getNodeText().equals(pathNodes[0])) {
						path = new TreePath(childNode.getPath());
						cloudTree.setSelectionPath(path);
						cloudTree.scrollPathToVisible(path);
						cloudTree.expandPath(path);
					}
				} else {
				}
			}
		} else {
			for (int i = 0; i < childCount; i++) {
				DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) node.getChildAt(i);
				if (childNode.getUserObject() instanceof PokerAction) {
					if (((PokerAction) (childNode.getUserObject())).getNodeText().equals(pathNodes[0])) {
						int childCount1 = childNode.getChildCount();
						for (int x = 0; x < childCount1; x++) {
							DefaultMutableTreeNode childNode1 = (DefaultMutableTreeNode) childNode.getChildAt(x);
							int childCount2 = childNode1.getChildCount();
							if (((PokerHandSizing) (childNode1.getUserObject())).getNodeText().equals(pathNodes[1])) {
								for (int y = 0; y < childCount2; y++) {
									DefaultMutableTreeNode childNode2 = (DefaultMutableTreeNode) childNode1.getChildAt(y);
									if (((PokerPosition) (childNode2.getUserObject())).getNodeText().equals(pathNodes[2])) {
										path = new TreePath(childNode2.getPath());
										cloudTree.setSelectionPath(path);
										cloudTree.scrollPathToVisible(path);
										return path;
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

	public void setTreeModel(DefaultTreeModel treeModel) {
		this.treeModel = treeModel;
	}

	public void setTreeType(String treeType) {
		this.treeType = treeType;
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
	 */
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		JTree sourceTree = (JTree) e.getSource();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) sourceTree.getLastSelectedPathComponent();
		if (node == null)
			return;
	}
}
