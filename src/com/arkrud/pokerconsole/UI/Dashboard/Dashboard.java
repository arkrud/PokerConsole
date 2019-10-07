package com.arkrud.pokerconsole.UI.Dashboard;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.arkrud.pokerconsole.Poker.PokerOpponentPosition;
import com.arkrud.pokerconsole.TreeInterface.CustomTree;
import com.arkrud.pokerconsole.UI.ChartPanel;
import com.arkrud.pokerconsole.UI.CustomMouseAdapter;
import com.arkrud.pokerconsole.UI.ImageChartPanel;
import com.arkrud.pokerconsole.UI.scrollabledesktop.BaseInternalFrame;
import com.arkrud.pokerconsole.UI.scrollabledesktop.JScrollableDesktopPane;
import com.arkrud.pokerconsole.Util.INIFilesFactory;
import com.arkrud.pokerconsole.Util.Reversed;
import com.arkrud.pokerconsole.Util.UtilMethodsFactory;

public class Dashboard extends JFrame implements InternalFrameListener, WindowListener, ChangeListener   {
	private static final long serialVersionUID = 1L;
	public static Hashtable<String, BaseInternalFrame> INTERNAL_FRAMES = new Hashtable<String, BaseInternalFrame>();
	public static String CURRENT_TREE_TITLE = "";
	private JMenuBar jJMenuBar = null;
	private JScrollableDesktopPane jScrollableDesktopPane = null;
	private CustomTree customTree;
	private CustomTree configTree;
	private JTabbedPane treeTabbedPane;
	private JScrollPane jScrollPane;
	private boolean editable;

	// Constructor
	public Dashboard(boolean editable) throws Exception  {
		this.editable = editable;
		super.addWindowListener(this);
		initialize(editable);
	}

	public void addTreeTabPaneTab(String appName) {
		String treeName = "";
		if (appName.contains("-")){
			treeName = appName.split("-")[0];
		} else {
			treeName = appName;
		}
		CustomTree tree = getCustomTree(treeName, true);
		// tree.expandTwoDeep();
		JScrollPane jScrollPane = new JScrollPane();
		jScrollPane.setViewportView(tree);
		if (!hasTab(appName)) {
			treeTabbedPane.insertTab(appName, null, jScrollPane, null, 0);
		}
	}

	// Get reference to CustomTree navigation object to manipulate tree nodes from other interface objects
	private CustomTree getCustomTree(String type, boolean editable) {
		if (type.equals("config")) {
			if (customTree == null) {
				customTree = new CustomTree(this, type, editable);
			}
			return customTree;
		} else {
			configTree = new CustomTree(this, type, editable);
			return configTree;
		}
	}

	// Get reference to JScrollableDesktopPane object
	public JScrollableDesktopPane getJScrollableDesktopPane() {
		if (jScrollableDesktopPane == null) {
			jScrollableDesktopPane = new JScrollableDesktopPane(jJMenuBar);
		}
		return jScrollableDesktopPane;
	}

	public JTabbedPane getTreeTabbedPane() {
		return treeTabbedPane;
	}

	private boolean hasTab(String appName) {
		int count = treeTabbedPane.getTabCount();
		for (int i = 0; i < count; i++) {
			String label = treeTabbedPane.getTitleAt(i);
			if (label.equals(appName)) {
				return true;
			}
		}
		return false;
	}

	public void hideTreeTabPaneTab(String appName) {
		int count = treeTabbedPane.getTabCount();
		for (int i = 0; i < count; i++) {
			String label = treeTabbedPane.getTitleAt(i);
			if (label.equals(appName)) {
				treeTabbedPane.setEnabledAt(i, false);
			}
		}
	}

	// Initialization of the visual interface
	private void initialize(boolean editable) throws Exception {
		// Create dashboard menu items and add menu to dashboard
		jJMenuBar = new JMenuBar();
		jJMenuBar.add(new DashboardMenu(this, editable));
		this.setJMenuBar(jJMenuBar);
		// Create dashboard interface
		JPanel jContentPane = new JPanel();
		jContentPane.setLayout(new BorderLayout());
		JSplitPane jSplitPane = new JSplitPane();
		jSplitPane.setDividerLocation(175);
		jSplitPane.setRightComponent(getJScrollableDesktopPane());
		jScrollPane = new JScrollPane();
		treeTabbedPane = new JTabbedPane();
		treeTabbedPane.addChangeListener(this);
		treeTabbedPane.setUI(new BasicTabbedPaneUI() {
			@Override
			protected MouseListener createMouseListener() {
				return new CustomMouseAdapter(treeTabbedPane, Dashboard.this);
			}
		});
		/*if (editable) {
			configTree = getCustomTree("config", editable);
			// configTree.expandTwoDeep();
			jScrollPane.setViewportView(configTree);
			treeTabbedPane.addTab("Configuration", null, jScrollPane, null);
		}*/
		ArrayList<String> trees = new ArrayList<String>();
		Iterator<ArrayList<Object>> it = INIFilesFactory.getAppTreesConfigInfo(UtilMethodsFactory.getConsoleConfig()).iterator();

		while (it.hasNext()) {
			ArrayList<Object> appData = it.next();
			if (((Boolean) appData.get(1))) {
				trees.add((String) appData.get(0));
			}
		}
		int x = 0;
		while (x < trees.size()) {
			JScrollPane treeScroll = new JScrollPane();

			String treeName = "";
			if (trees.get(x).contains("-")){
				treeName = trees.get(x).split("-")[0];
			} else {
				treeName = trees.get(x);
			}

			customTree = getCustomTree(treeName, editable);
			treeScroll.setViewportView(customTree);
			// customTree.expandTwoDeep();
			treeTabbedPane.insertTab(trees.get(x), null, treeScroll, null, 0);
			treeTabbedPane.setSelectedIndex(0);
			x++;
		}
		jSplitPane.setLeftComponent(treeTabbedPane);
		jContentPane.add(jSplitPane, BorderLayout.CENTER);
		this.setContentPane(jContentPane);
		this.setTitle("Dashboard");
		this.setBounds(new Rectangle(0, 0, 1500, 850));
	}

	// Overwrites
	@Override
	public void internalFrameActivated(InternalFrameEvent e) {
	}

	// Remove internal frame info from static data structure when window is closed
	@Override
	public void internalFrameClosed(InternalFrameEvent e) {
		BaseInternalFrame thisFrame = (BaseInternalFrame) e.getSource();
		Dashboard.INTERNAL_FRAMES.remove(thisFrame.getTitle());
	}

	@Override
	public void internalFrameClosing(InternalFrameEvent e) {
	}

	@Override
	public void internalFrameDeactivated(InternalFrameEvent e) {
	}

	@Override
	public void internalFrameDeiconified(InternalFrameEvent e) {
	}

	@Override
	public void internalFrameIconified(InternalFrameEvent e) {
	}

	@Override
	public void internalFrameOpened(InternalFrameEvent e) {
	}

	public boolean isEditable() {
		return editable;
	}

	public void removeTreeTabPaneTab(String appName) {
		int count = treeTabbedPane.getTabCount();
		for (int i = 0; i < count; i++) {
			try {
				String label = treeTabbedPane.getTitleAt(i);
				if (label.equals(appName)) {
					treeTabbedPane.remove(i);
				}
			} catch (Exception e) {
			}
		}
	}

	private <T> Reversed<T> reversed(List<T> original) {
		return new Reversed<T>(original);
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public void setTreeTabbedPane(JTabbedPane treeTabbedPane) {
		this.treeTabbedPane = treeTabbedPane;
	}

	@Override
	public void stateChanged(ChangeEvent changeEvent) {
		JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
		int index = sourceTabbedPane.getSelectedIndex();
		CURRENT_TREE_TITLE = sourceTabbedPane.getTitleAt(index);
		getJScrollableDesktopPane().getDesktopMediator().closeAllFrames();
		if (INIFilesFactory.hasItemInSection(UtilMethodsFactory.getConsoleConfig(), "Applications", sourceTabbedPane.getTitleAt(index) + "opened")) {
			String pathString = INIFilesFactory.getItemValueFromINI(UtilMethodsFactory.getConsoleConfig(), "Applications", sourceTabbedPane.getTitleAt(index) + "opened");
			JScrollPane scroll = (JScrollPane) (sourceTabbedPane.getSelectedComponent());
			CustomTree tree = (CustomTree) scroll.getViewport().getView();
			TreePath path = tree.selectTreeNode((DefaultMutableTreeNode) tree.getTreeModel().getRoot(), pathString, tree);
			ChartPanel chartPanel = null;
			ImageChartPanel imageChartPanel;
			if (path != null) {
			if(((DefaultMutableTreeNode) path.getLastPathComponent()).isLeaf()) {
				PokerOpponentPosition pokerOpponentPosition = (PokerOpponentPosition)((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();
				chartPanel = new ChartPanel(pokerOpponentPosition.getChartImagePath(), editable);
				BaseInternalFrame theFrame = new CustomTableViewInternalFrame(pokerOpponentPosition.getChartPaneTitle(), chartPanel);
				UtilMethodsFactory.addInternalFrameToScrolableDesctopPane(pokerOpponentPosition.getChartPaneTitle(), getJScrollableDesktopPane(), theFrame);
			} else {
				Enumeration<?> en = ((DefaultMutableTreeNode) path.getLastPathComponent()).children();
				@SuppressWarnings("unchecked")
				List<DefaultMutableTreeNode> list = (List<DefaultMutableTreeNode>) Collections.list(en);
				for (DefaultMutableTreeNode s : reversed(list)) {
					PokerOpponentPosition pokerOpponentPosition = (PokerOpponentPosition) s.getUserObject();
					if (editable) {
						chartPanel = new ChartPanel(pokerOpponentPosition.getChartImagePath(), editable);
						BaseInternalFrame theFrame = new CustomTableViewInternalFrame(pokerOpponentPosition.getChartPaneTitle(), chartPanel);
						UtilMethodsFactory.addInternalFrameToScrolableDesctopPane(pokerOpponentPosition.getChartPaneTitle(), getJScrollableDesktopPane(), theFrame);
					} else {
						imageChartPanel = new ImageChartPanel(pokerOpponentPosition.getChartImagePath());
						BaseInternalFrame theFrame = new CustomTableViewInternalFrame(pokerOpponentPosition.getChartPaneTitle(), imageChartPanel);
						UtilMethodsFactory.addInternalFrameToScrolableDesctopPane(pokerOpponentPosition.getChartPaneTitle(), getJScrollableDesktopPane(), theFrame);
					}
				}
			}

		}
		}


	}

	@Override
	public void windowActivated(WindowEvent arg0) {
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
	}

	// Exit application
	@Override
	public void windowClosing(WindowEvent arg0) {
		UtilMethodsFactory.exitApp();
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
	}
}