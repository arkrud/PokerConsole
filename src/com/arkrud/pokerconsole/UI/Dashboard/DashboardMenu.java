package com.arkrud.pokerconsole.UI.Dashboard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;

import com.arkrud.pokerconsole.Poker.PokerStrategy;
import com.arkrud.pokerconsole.TreeInterface.CustomTree;
import com.arkrud.pokerconsole.UI.ChartPanel;
import com.arkrud.pokerconsole.UI.scrollabledesktop.BaseInternalFrame;
import com.arkrud.pokerconsole.UI.scrollabledesktop.JScrollableDesktopPane;
import com.arkrud.pokerconsole.Util.INIFilesFactory;
import com.arkrud.pokerconsole.Util.MongoDBFactory;
import com.arkrud.pokerconsole.Util.UtilMethodsFactory;

/**
 * Class to build dashboard drop-down menu.<br>
 */
public class DashboardMenu extends JMenu implements ActionListener {
	private static final long serialVersionUID = 1L;
	/**
	 * Menu items.
	 */
	private JMenuItem exit, addDashboardUser, clearUser, addTree, loadSolution, manageTrees, openReadOnlyDash, populateChartDB, dataSourceSelection, manualSolutionNaming;
	/**
	 * REference to dashboard object
	 */
	private Dashboard dash;
	/**
	 * Flag to define if the charts presented in scrollable frames are editable.<br>
	 * And define limited interface controls set in non-editable state
	 */
	private boolean editable;
	/**
	 * File chooser object to load solution package for import.<br>
	 */
	final JFileChooser fc = new JFileChooser();

	/**
	 * Sole constructor of Dashboard object. <br>
	 * Adding Window Listener and initializing graphics controls
	 *
	 * @param dash dashboard object reference
	 * @param editable flag to define the editable state of the Poker hand charts
	 */
	public DashboardMenu(Dashboard dash, boolean editable) {
		super();
		this.dash = dash;
		this.editable = editable;
		setText("Edit");
		exit = new JMenuItem("Exit");
		addTree = new JMenuItem("Add Tree");
		loadSolution = new JMenuItem("Load Solution");
		manualSolutionNaming = new JMenuItem("Enable Manual Solution Copy Naming");
		manageTrees = new JMenuItem("Hide/Show Trees");
		openReadOnlyDash = new JMenuItem("Open Read Only Dashboard");
		populateChartDB = new JMenuItem("Load Charts in MongoDB");
		dataSourceSelection = new JMenuItem();
		addDashboardUser = new JMenuItem();
		clearUser = new JMenuItem("Clear Security");
		if (INIFilesFactory.readINI(UtilMethodsFactory.getConsoleConfig()).hasSection("Security")) {
			addDashboardUser.setText("Update User");
		} else {
			addDashboardUser.setText("Add User");
		}
		if (INIFilesFactory.readINI(UtilMethodsFactory.getConsoleConfig()).hasSection("Security")) {
			clearUser.setEnabled(true);
		} else {
			clearUser.setEnabled(false);
		}
		if (INIFilesFactory.getItemValueFromINI(UtilMethodsFactory.getConsoleConfig(), "Config", "ini").equals("true")) {
			dataSourceSelection.setText("Use MongoDB");
		} else {
			dataSourceSelection.setText("Use INI Files");
		}
		exit.addActionListener(this);
		addTree.addActionListener(this);
		loadSolution.addActionListener(this);
		addDashboardUser.addActionListener(this);
		clearUser.addActionListener(this);
		manageTrees.addActionListener(this);
		openReadOnlyDash.addActionListener(this);
		populateChartDB.addActionListener(this);
		dataSourceSelection.addActionListener(this);
		manualSolutionNaming.addActionListener(this);
		if (editable) {
			add(addDashboardUser);
			add(clearUser);
			add(addTree);
			add(loadSolution);
			add(manualSolutionNaming);
			add(manageTrees);
			add(openReadOnlyDash);
			// add(dataSourceSelection);
			// add(populateChartDB);
		}
		add(exit);
	}

	/**
	 * Initiates methods to perform menu actions on menu items selection. <br>
	 *
	 * @Override
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String menuText = ((JMenuItem) e.getSource()).getText();
		if (menuText.contains("Exit")) {
			UtilMethodsFactory.exitApp();
		} else if (menuText.contains("Add User")) {
			UtilMethodsFactory.showDialogToDesctop("AddUser", 350, 140, null, null, null, null, null, null, addDashboardUser);
		} else if (menuText.contains("Add Tree")) {
			UtilMethodsFactory.showDialogToDesctop("AddTreesFrame", 250, 140, dash, null, null, null, null, null, null);
		} else if (menuText.contains("Load Solution")) {
			loadSolution();
		} else if (menuText.contains("Enable Manual Solution Copy Naming")) {
			enableManualNaming();
		} else if (menuText.contains("Disable Manual Solution Copy Naming")) {
			disableManualNaming();
		} else if (menuText.contains("Hide/Show Trees")) {
			UtilMethodsFactory.showDialogToDesctop("ManageTreesDialog", 250, 150 + 25 * INIFilesFactory.getTreesData().size(), dash, null, null, null, null, null, null);
		} else if (menuText.contains("Update User")) {
			UtilMethodsFactory.showDialogToDesctop("AddUser", 350, 140, null, null, null, null, null, null, addDashboardUser);
		} else if (menuText.contains("Open Read Only Dashboard")) {
			openReadOnlyDashboard();
		} else if (menuText.contains("Load Charts in MongoDB")) {
			loadChartsToMongo();
		} else if (menuText.contains("Use MongoDB")) {
			useMongo();
		} else if (menuText.contains("Use INI Files")) {
			useINI();
		} else if (menuText.contains("Clear Security")) {
			clearSecurity();
		}
	}

	/**
	 * Allows to have previously selected tree node to be selected and Poker hand charts placed into scrollable desktop on clicking on the header of tab pane tabs. <br>
	 * <ul>
	 * <li>Calculates the level on the node in the tree independent from application deployment location in file system.
	 * <li>Generates charts at specific levels if filesystem object is file.
	 * <ul>
	 *
	 * @param node INI file object to be loaded into Poker hands chart
	 * @param editable flag to define the editable state of the Poker hand charts
	 */
	private void generateChartImages(File node, boolean editable) {
		int level = node.getAbsoluteFile().getPath().split("\\\\").length - UtilMethodsFactory.getConfigPath().split("/").length;
		if (node.isDirectory()) {
			String[] subNote = node.list();
			for (String filename : subNote) {
				generateChartImages(new File(node, filename), editable);
			}
		} else {
			if (level == 0) {
			} else if (level == 2) {
			} else if (level == 3) {
				if (node.isFile()) {
					if (!node.getName().contains("png")) {
						generateChart(node, editable);
					}
				} else {
				}
			} else if (level == 4) {
				if (!node.getName().contains("png")) {
					generateChart(node, editable);
				}
			} else if (level == 5) {
				if (!node.getName().contains("png")) {
					generateChart(node, editable);
				}
			} else {
			}
		}
	}

	
	
	/**
	 * Generates chart from INI file to produce PNG image and removes it. <br>
	 * <ul>
	 * <li>Get absolute file path.
	 * <li>Strip the path before application Images directory to produce relative path string.
	 * <li>Generate ChartPanel object using relative path and according editable flag value.
	 * <li>Instantiate BaseInternalFrame to hold the ChartPanel and using relative path as a title.
	 * <li>Get reference to JScrollableDesktopPane from Dashboard.
	 * <li>Add frame to the scrollable desktop using relative path as a frame title.
	 * <li>Generate chart PNG image from chart table in the ChartPanel in the same location as initial INI file.
	 * <li>Remove the frame from the JScrollableDesktopPane.
	 * <ul>
	 *
	 * @param node INI file object to be loaded into Poker hands chart
	 * @param editable flag to define the editable state of the Poker hand charts
	 */
	private void generateChart(File node, boolean editable) {
		String absolutePath = node.getAbsoluteFile().getPath();
		String imagePath = absolutePath.substring(absolutePath.indexOf("Images"), absolutePath.length());
		ChartPanel chartPanel = new ChartPanel(imagePath, editable);
		BaseInternalFrame theFrame = new CustomTableViewInternalFrame(imagePath, chartPanel);
		JScrollableDesktopPane pane = dash.getJScrollableDesktopPane();
		UtilMethodsFactory.addInternalFrameToScrolableDesctopPane(imagePath, pane, theFrame);
		UtilMethodsFactory.tableToImage(chartPanel.getTable(), imagePath.split("\\.")[0]);
		pane.remove(theFrame);
	}

	private void showDashboard() {
		Dashboard readOnlyDash = null;
		try {
			readOnlyDash = new Dashboard(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		readOnlyDash.setVisible(true);
		dash.dispose();
	}

	private void reOpenDashboard() {
		Dashboard refreshedDash = null;
		try {
			refreshedDash = new Dashboard(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		refreshedDash.setVisible(true);
		dash.dispose();
	}

	private void loadSolution() {
		String solutionPackagePath = "";
		File file = null;
		fc.setCurrentDirectory(new File(UtilMethodsFactory.getConfigPath() + "Images/"));
		fc.setDialogTitle("Load Solution Package");
		int returnVal = fc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();
			solutionPackagePath = file.getAbsolutePath();
			solutionPackagePath = solutionPackagePath.replace("\\", "/");
			String strategyName = file.getName().split("\\.")[0];
			INIFilesFactory.addINIFileItemToSection(UtilMethodsFactory.getConsoleConfig(), "Applications", strategyName, "true");
			File sizingDir = new File(UtilMethodsFactory.getConfigPath() + "Images/" + strategyName);
			UtilMethodsFactory.createGRoupFolder(sizingDir);
			CustomTree tree = dash.addTreeTabPaneTab(strategyName);
			String destDirectory = (UtilMethodsFactory.getConfigPath() + "Images/").substring(1);
			UtilMethodsFactory.unZipUpdate(solutionPackagePath, destDirectory);
			tree.refreshTreeNode((DefaultMutableTreeNode) tree.getTreeModel().getRoot(), strategyName);
			dash.getTreeTabbedPane().setSelectedIndex(dash.getTreeTabbedPane().indexOfTab(strategyName));
		} else {
		}
	}

	private void openReadOnlyDashboard() {
		JScrollPane jScrollPane = (JScrollPane) dash.getTreeTabbedPane().getSelectedComponent();
		CustomTree customTree = (CustomTree) jScrollPane.getViewport().getView();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) customTree.getTreeModel().getRoot();
		PokerStrategy pokerStrategy = (PokerStrategy) node.getUserObject();
		generateChartImages(new File(UtilMethodsFactory.getConfigPath() + "Images/" + pokerStrategy.getNodeText()), editable);
		INIFilesFactory.updateINIFileItems(UtilMethodsFactory.getConsoleConfig(), "Config", "false", "editable");
		showDashboard();
	}

	private void enableManualNaming() {
		INIFilesFactory.updateINIFileItems(UtilMethodsFactory.getConsoleConfig(), "Config", "true", "manualtreenaming");
		manualSolutionNaming.setText("Disable Manual Solution Copy Naming");
	}

	private void disableManualNaming() {
		INIFilesFactory.updateINIFileItems(UtilMethodsFactory.getConsoleConfig(), "Config", "false", "manualtreenaming");
		manualSolutionNaming.setText("Enable Manual Solution Copy Naming");
	}

	private void loadChartsToMongo() {
		MongoDBFactory.crateMongoConnection();
		addDocuments(new File(UtilMethodsFactory.getConfigPath() + "Images"));
		MongoDBFactory.closeMongoConnection();
	}

	private void useMongo() {
		INIFilesFactory.updateINIFileItems(UtilMethodsFactory.getConsoleConfig(), "Config", "true", "mongo");
		INIFilesFactory.updateINIFileItems(UtilMethodsFactory.getConsoleConfig(), "Config", "false", "ini");
		reOpenDashboard();
	}

	private void useINI() {
		INIFilesFactory.updateINIFileItems(UtilMethodsFactory.getConsoleConfig(), "Config", "false", "mongo");
		INIFilesFactory.updateINIFileItems(UtilMethodsFactory.getConsoleConfig(), "Config", "true", "ini");
		reOpenDashboard();
	}

	private void clearSecurity() {
		int response = JOptionPane.showConfirmDialog(null, "Do you want to disable security?", "Disable security", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.NO_OPTION) {
		} else if (response == JOptionPane.YES_OPTION) {
			INIFilesFactory.removeINIFileSection(UtilMethodsFactory.getConsoleConfig(), "Security");
		} else if (response == JOptionPane.CLOSED_OPTION) {
		}
	}
	
	private void addDocuments(File node) {
		int level = node.getAbsoluteFile().getPath().split("\\\\").length - UtilMethodsFactory.getConfigPath().split("/").length;
		if (node.isDirectory()) {
			String[] subNote = node.list();
			for (String filename : subNote) {
				addDocuments(new File(node, filename));
			}
		} else {
			if (level == 0) {
			} else if (level == 1) {
			} else if (level == 2) {
				if (node.isFile()) {
					if (node.getName().contains("ini")) {
						updateMongoDocument(node);
					}
				} else {
				}
			} else if (level == 3) {
				if (node.isFile()) {
					if (node.getName().contains("ini")) {
						updateMongoDocument(node);
					}
				} else {
				}
			} else if (level == 4) {
				if (node.getName().contains("ini")) {
					updateMongoDocument(node);
				}
			} else {
			}
		}
	}
	
	private void updateMongoDocument(File node) {
		String absolutePath = node.getAbsoluteFile().getPath();
		String imagePath = absolutePath.substring(absolutePath.indexOf("Images"), absolutePath.length()).split("\\.")[0].replaceAll("\\\\", "/");
		MongoDBFactory.addDocument(INIFilesFactory.getItemValuesFromINI(node), imagePath);
		File pngfile = new File(UtilMethodsFactory.getConfigPath() + imagePath + ".png");
		MongoDBFactory.updateDocuments(imagePath, pngfile);
	}
}
