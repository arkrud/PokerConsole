package com.arkrud.pokerconsole.UI.Dashboard;

import java.awt.Dimension;
import java.awt.Toolkit;
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

public class DashboardMenu extends JMenu implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JMenuItem exit, addDashboardUser, clearUser, addTree, loadSolution, manageTrees, openReadOnlyDash, populateChartDB, dataSourceSelection, manualSolutionNaming;
	private Dashboard dash;
	private boolean editable;
	final JFileChooser fc = new JFileChooser();

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
		if (INIFilesFactory.getItemValueFromINI(UtilMethodsFactory.getConsoleConfig(), "data", "ini").equals("true")) {
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

	@Override
	public void actionPerformed(ActionEvent e) {
		String menuText = ((JMenuItem) e.getSource()).getText();
		if (menuText.contains("Exit")) {
			UtilMethodsFactory.exitApp();
		} else if (menuText.contains("Add User")) {
			showConsoleLoginAccountFrame(addDashboardUser);
		} else if (menuText.contains("Add Tree")) {
			UtilMethodsFactory.showDialogToDesctop("AddTreesFrame", 250, 140, dash, null, null, null, null, null);
		} else if (menuText.contains("Load Solution")) {
			/*fc.setCurrentDirectory(new File(UtilMethodsFactory.getConfigPath() + "Images/"));
			fc.setDialogTitle("Load Solution Package");
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fc.setAcceptAllFileFilterUsed(false);
			int returnVal = fc.showOpenDialog(null);
			String solutionPackagePath = "";
			File dir = null;
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				dir = fc.getSelectedFile();
				solutionPackagePath = dir.getAbsolutePath();
				System.out.println(solutionPackagePath);
				//templatePath = templatePath.substring(templatePath.indexOf("Images"), templatePath.length());
				//templatePath = templatePath.replace("\\", "/");
			} else {
			}	*/
			String zipFilePath = "C:/QA/Exported.zip";
	        String destDirectory = "C:/QA";
	        UtilMethodsFactory.unZipUpdate(zipFilePath, destDirectory);
			
		} else if (menuText.contains("Enable Manual Solution Copy Naming")) {
			INIFilesFactory.updateINIFileItems(UtilMethodsFactory.getConsoleConfig(), "data", "true", "manualtreenaming");
			manualSolutionNaming.setText("Disable Manual Solution Copy Naming");
		} else if (menuText.contains("Disable Manual Solution Copy Naming")) {
			INIFilesFactory.updateINIFileItems(UtilMethodsFactory.getConsoleConfig(), "data", "false", "manualtreenaming");
			manualSolutionNaming.setText("Enable Manual Solution Copy Naming");
		} else if (menuText.contains("Hide/Show Trees")) {
			UtilMethodsFactory.showDialogToDesctop("ManageTreesDialog", 250, 150 + 25 * INIFilesFactory.getTreesData().size(), dash, null, null, null, null, null);
		} else if (menuText.contains("Update User")) {
			showConsoleLoginAccountFrame(addDashboardUser);
		} else if (menuText.contains("Open Read Only Dashboard")) {
			JScrollPane jScrollPane = (JScrollPane) dash.getTreeTabbedPane().getSelectedComponent();
			CustomTree customTree = (CustomTree) jScrollPane.getViewport().getView();
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) customTree.getTreeModel().getRoot();
			PokerStrategy pokerStrategy = (PokerStrategy) node.getUserObject();
			generateChartImages(new File(UtilMethodsFactory.getConfigPath() + "Images/" + pokerStrategy.getNodeText()), editable);
			INIFilesFactory.updateINIFileItems(UtilMethodsFactory.getConsoleConfig(), "data", "false", "editable");
			showDashboard();
		} else if (menuText.contains("Load Charts in MongoDB")) {
			MongoDBFactory.crateMongoConnection();
			addDocuments(new File(UtilMethodsFactory.getConfigPath() + "Images"));
			MongoDBFactory.closeMongoConnection();
		} else if (menuText.contains("Use MongoDB")) {
			INIFilesFactory.updateINIFileItems(UtilMethodsFactory.getConsoleConfig(), "data", "true", "mongo");
			INIFilesFactory.updateINIFileItems(UtilMethodsFactory.getConsoleConfig(), "data", "false", "ini");
			reOpenDashboard();
		} else if (menuText.contains("Use INI Files")) {
			INIFilesFactory.updateINIFileItems(UtilMethodsFactory.getConsoleConfig(), "data", "false", "mongo");
			INIFilesFactory.updateINIFileItems(UtilMethodsFactory.getConsoleConfig(), "data", "true", "ini");
			reOpenDashboard();
		} else if (menuText.contains("Clear Security")) {
			int response = JOptionPane.showConfirmDialog(null, "Do you want to disable security?", "Disable security", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (response == JOptionPane.NO_OPTION) {
			} else if (response == JOptionPane.YES_OPTION) {
				INIFilesFactory.removeINIFileSection(UtilMethodsFactory.getConsoleConfig(), "Security");
			} else if (response == JOptionPane.CLOSED_OPTION) {
			}
		}
	}

	private void generateChartImages(File node, boolean editable) {
		int level = node.getAbsoluteFile().getPath().split("\\\\").length - UtilMethodsFactory.getConfigPath().split("/").length;
		if (level == 0) {
		} else if (level == 2) {
		} else if (level == 3) {
			if (node.isFile()) {
				if (!node.getName().contains("png")) {
					generateCharts(node, editable);
				}
			} else {
			}
		} else if (level == 4) {
		} else if (level == 5) {
			if (!node.getName().contains("png")) {
				generateCharts(node, editable);
			}
		} else {
		}
		if (node.isDirectory()) {
			String[] subNote = node.list();
			for (String filename : subNote) {
				generateChartImages(new File(node, filename), editable);
			}
		}
	}

	public static void addDocuments(File node) {
		int level = node.getAbsoluteFile().getPath().split("\\\\").length - UtilMethodsFactory.getConfigPath().split("/").length;
		if (level == 0) {
		} else if (level == 1) {
		} else if (level == 2) {
			if (node.isFile()) {
				if (node.getName().contains("ini")) {
					String absolutePath = node.getAbsoluteFile().getPath();
					String imagePath = absolutePath.substring(absolutePath.indexOf("Images"), absolutePath.length()).split("\\.")[0].replaceAll("\\\\", "/");
					MongoDBFactory.addDocument(INIFilesFactory.getItemValuesFromINI(node), imagePath);
					File pngfile = new File(UtilMethodsFactory.getConfigPath() + imagePath + ".png");
					MongoDBFactory.updateDocuments(imagePath, pngfile);
				}
			} else {
			}
		} else if (level == 3) {
		} else if (level == 4) {
			if (node.getName().contains("ini")) {
				String absolutePath = node.getAbsoluteFile().getPath();
				String imagePath = absolutePath.substring(absolutePath.indexOf("Images"), absolutePath.length()).split("\\.")[0].replaceAll("\\\\", "/");
				MongoDBFactory.addDocument(INIFilesFactory.getItemValuesFromINI(node), imagePath);
				File pngfile = new File(UtilMethodsFactory.getConfigPath() + imagePath + ".png");
				MongoDBFactory.updateDocuments(imagePath, pngfile);
			}
		} else {
		}
		if (node.isDirectory()) {
			String[] subNote = node.list();
			for (String filename : subNote) {
				addDocuments(new File(node, filename));
			}
		}
	}

	private void generateCharts(File node, boolean editable) {
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

	private void showConsoleLoginAccountFrame(JMenuItem addUser) {
		ConsoleLoginAccountFrame consoleLoginAccountFrame = new ConsoleLoginAccountFrame(addUser);
		consoleLoginAccountFrame.setSize(350, 140);
		// Get the size of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		// Determine the new location of the window
		int w = consoleLoginAccountFrame.getSize().width;
		int h = consoleLoginAccountFrame.getSize().height;
		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;
		// Move the window
		consoleLoginAccountFrame.setLocation(x, y);
		consoleLoginAccountFrame.setVisible(true);
	}
}
