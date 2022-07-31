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
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
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
import com.arkrud.pokerconsole.UI.CustomMouseAdapter;
import com.arkrud.pokerconsole.UI.ImageChartPanel;
import com.arkrud.pokerconsole.UI.scrollabledesktop.BaseInternalFrame;
import com.arkrud.pokerconsole.UI.scrollabledesktop.JScrollableDesktopPane;
import com.arkrud.pokerconsole.Util.INIFilesFactory;
import com.arkrud.pokerconsole.Util.UtilMethodsFactory;

// TODO: Auto-generated Javadoc
/**
 * Class to build advanced desktop interface with tree controls and scrollable are to show multiple frames.<br>
 *
 */
public class Dashboard extends JFrame implements InternalFrameListener, WindowListener, ChangeListener {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	/**
	 * Collection of Frames added to scrollable desktop.
	 */
	public static Hashtable<String, BaseInternalFrame> INTERNAL_FRAMES = new Hashtable<String, BaseInternalFrame>();
	
	/** The j J menu bar. */
	private JMenuBar jJMenuBar = null;
	/**
	 * Scrollable Frames to be added to scrollable desktop.
	 */
	private JScrollableDesktopPane jScrollableDesktopPane = null;
	
	/**
	 * Gets the dashboard menu.
	 *
	 * @return the dashboard menu
	 */
	public DashboardMenu getDashboardMenu() {
		return dashboardMenu;
	}

	/**
	 * Sets the dashboard menu.
	 *
	 * @param dashboardMenu the new dashboard menu
	 */
	public void setDashboardMenu(DashboardMenu dashboardMenu) {
		this.dashboardMenu = dashboardMenu;
	}

	/**
	 * Tabbed Pane to hold multiple custom trees.
	 */
	private JTabbedPane treeTabbedPane;
	/**
	 * Flag to define if the charts presented in scrollable frames are editable.<br>
	 * And define limited interface controls set in non-editable state
	 */
	private boolean editable;
	
	/** The dashboard menu. */
	private DashboardMenu dashboardMenu;

	/**
	 * Sole constructor of Dashboard object. <br>
	 * Adding Window Listener and initializing graphics controls
	 *
	 * @param editable flag to define the editable state of the Poker hand charts
	 * @throws Exception the exception
	 */
	public Dashboard(boolean editable) throws Exception {
		this.editable = editable;
		super.addWindowListener(this);
		initialize(editable);
	}

	/**
	 * Public method to add new navigation tree tab. <br>
	 *
	 * @param tabName String to define the Solution name
	 * @return the custom tree
	 */
	public CustomTree addTreeTabPaneTab(String tabName) {
		String treeName = "";
		if (tabName.contains("-")) {
			treeName = tabName.split("-")[0];
		} else {
			treeName = tabName;
		}
		CustomTree tree = new CustomTree(this, treeName, true);
		JScrollPane jScrollPane = new JScrollPane();
		jScrollPane.setViewportView(tree);
		if (!hasTab(tabName)) {
			treeTabbedPane.insertTab(tabName, null, jScrollPane, tabName, 0);
		}
		return tree;
	}

	/**
	 * Public method to get reference to JScrollableDesktopPane object.<br>
	 *
	 * @return the j scrollable desktop pane
	 */
	public JScrollableDesktopPane getJScrollableDesktopPane() {
		if (jScrollableDesktopPane == null) {
			jScrollableDesktopPane = new JScrollableDesktopPane(jJMenuBar);
		}
		return jScrollableDesktopPane;
	}

	/**
	 * Public method to get reference to JTabbedPane object.<br>
	 *
	 * @return the tree tabbed pane
	 */
	public JTabbedPane getTreeTabbedPane() {
		return treeTabbedPane;
	}

	/**
	 * Close all frames.
	 */
	public void closeAllFrames() {
		jScrollableDesktopPane.getDesktopMediator().closeAllFrames();
	}

	/**
	 * Gets the all frames.
	 *
	 * @return the all frames
	 */
	public JInternalFrame[] getAllFrames() {
		JInternalFrame[] frames = jScrollableDesktopPane.getDesktopMediator().getAllFrames();
		return frames;
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.InternalFrameListener#internalFrameActivated(javax.swing.event.InternalFrameEvent)
	 */
	@Override
	public void internalFrameActivated(InternalFrameEvent e) {
	}

	/**
	 * Public method to remove internal frame info from static data structure when window is closed. <br>
	 *
	 * @param e the e
	 * @Override 
	 */
	@Override
	public void internalFrameClosed(InternalFrameEvent e) {
		BaseInternalFrame thisFrame = (BaseInternalFrame) e.getSource();
		Dashboard.INTERNAL_FRAMES.remove(thisFrame.getTitle());
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.InternalFrameListener#internalFrameClosing(javax.swing.event.InternalFrameEvent)
	 */
	@Override
	public void internalFrameClosing(InternalFrameEvent e) {
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.InternalFrameListener#internalFrameDeactivated(javax.swing.event.InternalFrameEvent)
	 */
	@Override
	public void internalFrameDeactivated(InternalFrameEvent e) {
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.InternalFrameListener#internalFrameDeiconified(javax.swing.event.InternalFrameEvent)
	 */
	@Override
	public void internalFrameDeiconified(InternalFrameEvent e) {
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.InternalFrameListener#internalFrameIconified(javax.swing.event.InternalFrameEvent)
	 */
	@Override
	public void internalFrameIconified(InternalFrameEvent e) {
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.InternalFrameListener#internalFrameOpened(javax.swing.event.InternalFrameEvent)
	 */
	@Override
	public void internalFrameOpened(InternalFrameEvent e) {
	}

	/**
	 * Public method to remove tab from tabbed pane. <br>
	 *
	 * @param tabText string representing tab header text
	 */
	public void removeTreeTabPaneTab(String tabText) {
		int count = treeTabbedPane.getTabCount();
		for (int i = 0; i < count; i++) {
			try {
				String label = treeTabbedPane.getTitleAt(i);
				if (label.equals(tabText)) {
					treeTabbedPane.remove(i);
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Allows to have Navigation tree to have previously selected node to be selected and Poker hand charts placed into scrollable desktop on clicking on the header of tab pane tabs. <br>
	 * <ul>
	 * <li>Get selected tab index.
	 * <li>If last solution is deleted and index is -1 do nothing.
	 * <li>Check if this solution tree has something previously selected.
	 * <li>Retrieve the selection path string from the INI file <FRI-BB>.
	 * <li>Check if selected tree node is leaf object or branch.
	 * <li>If this is leaf object.
	 * <ul>
	 * <li>Get PokerOpponentPosition object from the tree node.
	 * <li>Generate the Poker hand ChartPanel object based on PokerOpponentPosition object ChartImagePath property.
	 * <li>Poker hand chart can be editable or static based on the value of editable flag.
	 * <li>Generate chart window.
	 * <li>Add hand chart window to the Scrollable Desktop.
	 * </ul>
	 * <li>If this is branch object, get PokerOpponentPosition object from the tree node
	 * <ul>
	 * <li>Loop over all children under branch node.
	 * <li>If the flag is set to editable.
	 * <ul>
	 * <li>Generate the Poker hand chart based on PokerOpponentPosition object ChartImagePath property
	 * <li>Poker hand chart can be editable or static based on the value of editable flag.
	 * <li>Generate chart window.
	 * <li>Add hand chart window to the Scrollable Desktop.
	 * </ul>
	 * <li>If the flag is set to not editable.
	 * <ul>
	 * <li>Generate the Poker hand chart ImageChartPanel object based on PokerOpponentPosition object ChartImagePath property
	 * <li>Generate chart window.
	 * <li>Add hand chart window to the Scrollable Desktop. </ul
	 * </ul>
	 * </ul>
	 * <p>
	 *
	 * @param changeEvent the change event
	 * @Override 
	 */
	@Override
	public void stateChanged(ChangeEvent changeEvent) {
		JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
		int index = sourceTabbedPane.getSelectedIndex();
		if (index < 0) {
		} else {
			if (INIFilesFactory.getItemValueFromINI(UtilMethodsFactory.getConsoleConfig(), "Autonaming", sourceTabbedPane.getTitleAt(sourceTabbedPane.getSelectedIndex())).equals("false")) {
				dashboardMenu.setManualEditingMenu(true);
			} else {
				dashboardMenu.setManualEditingMenu(false);
			}
			closeAllFrames();
			if (INIFilesFactory.hasItemInSection(UtilMethodsFactory.getConsoleConfig(), "Selections", sourceTabbedPane.getTitleAt(index))) {
				String pathString = INIFilesFactory.getItemValueFromINI(UtilMethodsFactory.getConsoleConfig(), "Selections", sourceTabbedPane.getTitleAt(index));
				JScrollPane scroll = (JScrollPane) (sourceTabbedPane.getSelectedComponent());
				CustomTree tree = (CustomTree) scroll.getViewport().getView();
				TreePath path = tree.selectTreeNode((DefaultMutableTreeNode) tree.getTreeModel().getRoot(), pathString, tree);
				ImageChartPanel imageChartPanel;
				if (path != null) {
					if (((DefaultMutableTreeNode) path.getLastPathComponent()).isLeaf() && ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject() instanceof PokerOpponentPosition) {
						PokerOpponentPosition pokerOpponentPosition = (PokerOpponentPosition) ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();
						UtilMethodsFactory.addChartFrameToScrolableDesctop(pokerOpponentPosition.getChartImagePath(), pokerOpponentPosition.getChartPaneTitle(), editable, this);
					} else {
						Enumeration<?> en = ((DefaultMutableTreeNode) path.getLastPathComponent()).children();
						@SuppressWarnings("unchecked")
						List<DefaultMutableTreeNode> list = (List<DefaultMutableTreeNode>) Collections.list(en);
						for (DefaultMutableTreeNode s : list) {
							if (s.getUserObject() instanceof PokerOpponentPosition) {
								PokerOpponentPosition pokerOpponentPosition = (PokerOpponentPosition) s.getUserObject();
								if (editable) {
									UtilMethodsFactory.addChartFrameToScrolableDesctop(pokerOpponentPosition.getChartImagePath(), pokerOpponentPosition.getChartPaneTitle(), editable, this);
								} else {
									imageChartPanel = new ImageChartPanel(pokerOpponentPosition.getChartImagePath());
									BaseInternalFrame theFrame = new CustomTableViewInternalFrame(pokerOpponentPosition.getChartPaneTitle(), imageChartPanel);
									theFrame.setName(pokerOpponentPosition.getChartImagePath());
									UtilMethodsFactory.addInternalFrameToScrolableDesctopPane(pokerOpponentPosition.getChartPaneTitle(), getJScrollableDesktopPane(), theFrame);
								}
							}
						}
					}
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowActivated(WindowEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosed(WindowEvent arg0) {
	}

	/**
	 * Includes method to exit application. <br>
	 *
	 * @param arg0 the arg 0
	 * @Override 
	 */
	@Override
	public void windowClosing(WindowEvent arg0) {
		UtilMethodsFactory.exitApp();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowIconified(WindowEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowOpened(WindowEvent arg0) {
	}

	/**
	 * Private method to check if tab with given header name already exist.<br>
	 *
	 * @param tabName String to define the tab name
	 * @return true, if successful
	 */
	private boolean hasTab(String tabName) {
		int count = treeTabbedPane.getTabCount();
		for (int i = 0; i < count; i++) {
			String label = treeTabbedPane.getTitleAt(i);
			if (label.equals(tabName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Private method to initialize all controls. <br>
	 *
	 * @param editable flag to define the editable state of the Poker hand charts
	 * @throws Exception the exception
	 */
	private void initialize(boolean editable) throws Exception {
		addMenu();
		JPanel frameContentPanel = initializeFrameContentPanel();
		JSplitPane jSplitPane = initializeSplitPane();
		treeTabbedPane = initializeTabbedPane();
		ArrayList<String> trees = INIFilesFactory.getAppTreesConfigInfo(UtilMethodsFactory.getConsoleConfig());
		addTreesToTabs(treeTabbedPane, trees);
		jSplitPane.setLeftComponent(treeTabbedPane);
		frameContentPanel.add(jSplitPane, BorderLayout.CENTER);
		this.setContentPane(frameContentPanel);
		this.setTitle("Poker Strategies Dashboard");
		this.setBounds(new Rectangle(0, 0, 1500, 850));
	}

	/**
	 * Private method to create dashboard menu items and add menu to dashboard. <br>
	 *
	 */
	private void addMenu() {
		jJMenuBar = new JMenuBar();
		dashboardMenu = new DashboardMenu(this, editable);
		jJMenuBar.add(dashboardMenu);
		this.setJMenuBar(jJMenuBar);
	}

	/**
	 * Private method to initialize Desktop frame content panel. <br>
	 *
	 * @return the j panel
	 */
	private JPanel initializeFrameContentPanel() {
		JPanel frameContentPanel = new JPanel();
		frameContentPanel.setLayout(new BorderLayout());
		return frameContentPanel;
	}

	/**
	 * Private method to initialize Split pane to hole trees tabbed pane and scrollable desktop. <br>
	 *
	 * @return the j split pane
	 */
	private JSplitPane initializeSplitPane() {
		JSplitPane jSplitPane = new JSplitPane();
		jSplitPane.setDividerLocation(175);
		jSplitPane.setRightComponent(getJScrollableDesktopPane());
		return jSplitPane;
	}

	/**
	 * Private method to initialize trees tabbed pane. <br>
	 * Tabbed pane has CustomMouseAdapter set to provide dropm down nemu functionality on tab headers.
	 *
	 * @return the j tabbed pane
	 */
	private JTabbedPane initializeTabbedPane() {
		treeTabbedPane = new JTabbedPane();
		treeTabbedPane.addChangeListener(this);
		treeTabbedPane.setUI(new BasicTabbedPaneUI() {
			@Override
			protected MouseListener createMouseListener() {
				return new CustomMouseAdapter(treeTabbedPane, Dashboard.this);
			}
		});
		return treeTabbedPane;
	}

	/**
	 * Private method to add trees to tabbed pane. <br>
	 *
	 * @param treeTabbedPane JTabbedPane object to hold navigation trees.
	 * @param trees Array of Poker Strategy names.
	 */
	private void addTreesToTabs(JTabbedPane treeTabbedPane, ArrayList<String> trees) {
		int x = 0;
		while (x < trees.size()) {
			JScrollPane treeScroll = new JScrollPane();
			String treeName = "";
			if (trees.get(x).contains("-")) {
				treeName = trees.get(x).split("-")[0];
			} else {
				treeName = trees.get(x);
			}
			CustomTree customTree = new CustomTree(this, treeName, editable);
			treeScroll.setViewportView(customTree);
			treeTabbedPane.insertTab(trees.get(x), null, treeScroll, trees.get(x), 0);
			treeTabbedPane.setSelectedIndex(0);
			x++;
		}
	}
}