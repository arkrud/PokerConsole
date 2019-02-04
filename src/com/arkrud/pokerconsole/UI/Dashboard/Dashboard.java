package com.arkrud.pokerconsole.UI.Dashboard;

import java.awt.BorderLayout;
import java.awt.Rectangle;
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
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.arkrud.pokerconsole.Poker.PokerOpponentPosition;
import com.arkrud.pokerconsole.TreeInterface.CustomTree;
import com.arkrud.pokerconsole.UI.ChartPanel;
import com.arkrud.pokerconsole.UI.scrollabledesktop.BaseInternalFrame;
import com.arkrud.pokerconsole.UI.scrollabledesktop.JScrollableDesktopPane;
import com.arkrud.pokerconsole.Util.INIFilesFactory;
import com.arkrud.pokerconsole.Util.Reversed;
import com.arkrud.pokerconsole.Util.UtilMethodsFactory;

public class Dashboard extends JFrame implements InternalFrameListener, WindowListener, ChangeListener {
	private static final long serialVersionUID = 1L;
	public static Hashtable<String, BaseInternalFrame> INTERNAL_FRAMES = new Hashtable<String, BaseInternalFrame>();
	private JMenuBar jJMenuBar = null;
	private JScrollableDesktopPane jScrollableDesktopPane = null;
	private CustomTree customTree;
	private CustomTree configTree;
	private JTabbedPane treeTabbedPane;
	private JScrollPane jScrollPane;

	//// Constructor
	public Dashboard() throws Exception {
		super();
		super.addWindowListener(this);
		initialize();
	}

	// Initialization of the visual interface
	private void initialize() throws Exception {
		// Create dashboard menu items and add menu to dashboard
		jJMenuBar = new JMenuBar();
		jJMenuBar.add(new DashboardMenu(this));
		this.setJMenuBar(jJMenuBar);
		// Create dashboard interface
		JPanel jContentPane = new JPanel();
		jContentPane.setLayout(new BorderLayout());
		JSplitPane jSplitPane = new JSplitPane();
		jSplitPane.setDividerLocation(300);
		jSplitPane.setRightComponent(getJScrollableDesktopPane());
		jScrollPane = new JScrollPane();
		treeTabbedPane = new JTabbedPane();
		treeTabbedPane.addChangeListener(this);
		configTree = getCustomTree("config");
		configTree.expandTwoDeep();
		jScrollPane.setViewportView(configTree);
		treeTabbedPane.addTab("Configuration", null, jScrollPane, null);
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
			customTree = getCustomTree(trees.get(x));
			treeScroll.setViewportView(customTree);
			customTree.expandTwoDeep();
			//treeTabbedPane.addTab(trees.get(x), null, treeScroll, null);
			treeTabbedPane.insertTab(trees.get(x), null, treeScroll, null,0);
			treeTabbedPane.setSelectedIndex(0);
			x++;
		}
		
		jSplitPane.setLeftComponent(treeTabbedPane);
		jContentPane.add(jSplitPane, BorderLayout.CENTER);
		this.setContentPane(jContentPane);
		this.setTitle("Dashboard");
		this.setBounds(new Rectangle(0, 0, 1500, 850));
	}

	public void addTreeTabPaneTab(String appName) {
		CustomTree tree = getCustomTree(appName);
		tree.expandTwoDeep();
		JScrollPane jScrollPane = new JScrollPane();
		jScrollPane.setViewportView(tree);
		if (!hasTab(appName)) {
			treeTabbedPane.addTab(appName, null, jScrollPane, null);
			}
	}
	
	private boolean hasTab (String appName) {
		int count = treeTabbedPane.getTabCount();
		for (int i = 0; i < count; i++) {
			String label = treeTabbedPane.getTitleAt(i);
			if (label.equals(appName)) {
				return true;
			}
		}
		return false;
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
	
	
	
	public void hideTreeTabPaneTab(String appName) {
		int count = treeTabbedPane.getTabCount();
		for (int i = 0; i < count; i++) {
			String label = treeTabbedPane.getTitleAt(i);
			if (label.equals(appName)) {
				treeTabbedPane.setEnabledAt(i, false);
			}
		}
	}

	// Get reference to JScrollableDesktopPane object
	public JScrollableDesktopPane getJScrollableDesktopPane() {
		if (jScrollableDesktopPane == null) {
			jScrollableDesktopPane = new JScrollableDesktopPane(jJMenuBar);
		}
		return jScrollableDesktopPane;
	}

	// Get reference to CustomTree navigation object to manipulate tree nodes from other interface objects
	private CustomTree getCustomTree(String type) {
		if (type.equals("config")) {
			if (customTree == null) {
				customTree = new CustomTree(this, type);
			}
			return customTree;
		} else {
			configTree = new CustomTree(this, type);
			return configTree;
		}
	}

	public JTabbedPane getTreeTabbedPane() {
		return treeTabbedPane;
	}

	public void setTreeTabbedPane(JTabbedPane treeTabbedPane) {
		this.treeTabbedPane = treeTabbedPane;
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

	@Override
	public void stateChanged(ChangeEvent changeEvent) {
		JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
		int index = sourceTabbedPane.getSelectedIndex();
		getJScrollableDesktopPane().getDesktopMediator().closeAllFrames();
		if (INIFilesFactory.hasItemInSection(UtilMethodsFactory.getConsoleConfig(), "Applications", sourceTabbedPane.getTitleAt(index) + "opened")) {
			String pathString = INIFilesFactory.getItemValueFromINI(UtilMethodsFactory.getConsoleConfig(), "Applications", sourceTabbedPane.getTitleAt(index) + "opened");
			JScrollPane scroll = (JScrollPane) (sourceTabbedPane.getSelectedComponent());
			CustomTree tree = (CustomTree) scroll.getViewport().getView();
			TreePath path = tree.selectTreeNode((DefaultMutableTreeNode) tree.getTreeModel().getRoot(), pathString, tree);
			ChartPanel chartPanel = null;
			Enumeration<?> en = ((DefaultMutableTreeNode) path.getLastPathComponent()).children();
			@SuppressWarnings("unchecked")
			List<DefaultMutableTreeNode> list = (List<DefaultMutableTreeNode>) Collections.list(en);
			for (DefaultMutableTreeNode s : reversed(list)) {
				PokerOpponentPosition pokerOpponentPosition = (PokerOpponentPosition) s.getUserObject();
				chartPanel = new ChartPanel(pokerOpponentPosition.getChartImagePath());
				BaseInternalFrame theFrame = new CustomTableViewInternalFrame(pokerOpponentPosition.getChartPaneTitle(), chartPanel);
				UtilMethodsFactory.addInternalFrameToScrolableDesctopPane(pokerOpponentPosition.getChartPaneTitle(), getJScrollableDesktopPane(), theFrame);
			}
		}
	}

	private <T> Reversed<T> reversed(List<T> original) {
		return new Reversed<T>(original);
	}
}