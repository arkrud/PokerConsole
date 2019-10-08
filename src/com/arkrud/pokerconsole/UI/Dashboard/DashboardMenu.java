package com.arkrud.pokerconsole.UI.Dashboard;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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
	private JMenuItem exit, addDashboardUser, clearUser, addTree, manageTrees, openReadOnlyDash, populateChartDB, dataSourceSelection;
	private Dashboard dash;

	public DashboardMenu(Dashboard dash, boolean editable) {
		super();
		this.dash = dash;
		setText("Edit");
		exit = new JMenuItem("Exit");
		addTree = new JMenuItem("Add Tree");
		manageTrees = new JMenuItem("Manage Trees");
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
		addDashboardUser.addActionListener(this);
		clearUser.addActionListener(this);
		manageTrees.addActionListener(this);
		openReadOnlyDash.addActionListener(this);
		populateChartDB.addActionListener(this);
		dataSourceSelection.addActionListener(this);
		if (editable) {
			add(addDashboardUser);
			add(clearUser);
			add(addTree);
			add(manageTrees);
			add(openReadOnlyDash);
			add(dataSourceSelection);
			add(populateChartDB);
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
			UtilMethodsFactory.showDialogToDesctop("AddTreesFrame", 250, 140, dash,null,null,null,null);
		} else if (menuText.contains("Manage Trees")) {
			UtilMethodsFactory.showDialogToDesctop("ManageTreesDialog", 250, 150 + 25 * INIFilesFactory.getTreesData().size(), dash, null,null,null,null);
		} else if (menuText.contains("Update User")) {
			showConsoleLoginAccountFrame(addDashboardUser);
		} else if (menuText.contains("Open Read Only Dashboard")) {
			JScrollPane jScrollPane = (JScrollPane)dash.getTreeTabbedPane().getSelectedComponent();
			CustomTree customTree = (CustomTree)jScrollPane.getViewport().getView();
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)customTree.getTreeModel().getRoot();
			PokerStrategy pokerStrategy = (PokerStrategy)node.getUserObject();
			generateChartImages(new File(UtilMethodsFactory.getConfigPath() + "Images/" + pokerStrategy.getNodeText()), Dashboard.EDITABLE);
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
		System.out.println(node.getAbsoluteFile().getPath());
		System.out.println(UtilMethodsFactory.getConfigPath());
		System.out.println(level);
		if (level == 0) {
		} else if (level == 2) {
		} else if (level == 3) {
			if (node.getAbsoluteFile().getPath().split("\\\\")[level + UtilMethodsFactory.getConfigPath().split("/").length / 2].equals("RFI")) {
				if (!node.getName().contains("ini") && !node.getName().contains("png")) {
					generateCharts(node, editable);
				}
			} else {
			}
		} else if (level == 4) {
		} else if (level == 5) {
			if ( !node.getName().contains("png")) {
				System.out.println("Here we go");
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
			if (node.getAbsoluteFile().getPath().split("\\\\")[level + UtilMethodsFactory.getConfigPath().split("/").length / 2].equals("RFI")) {
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
		File iniFile = new File(UtilMethodsFactory.getConfigPath() + imagePath.split("\\.")[0].replaceAll("\\\\", "/") + ".ini");
		/*try {
			Boolean.parseBoolean(INIFilesFactory.getItemValueFromINI(iniFile, "Update", "latest"));
		} catch (Exception e) {
			HashMap<String, String> sectionKeys = new HashMap<String, String>();
			sectionKeys.put("latest", "false");
			INIFilesFactory.addINIFileSection(iniFile, "Update", sectionKeys);
		}*/
		//if (Boolean.parseBoolean(INIFilesFactory.getItemValueFromINI(iniFile, "Update", "latest"))) {
			ChartPanel chartPanel = new ChartPanel(imagePath, editable);
			BaseInternalFrame theFrame = new CustomTableViewInternalFrame(imagePath, chartPanel);
			JScrollableDesktopPane pane = dash.getJScrollableDesktopPane();
			UtilMethodsFactory.addInternalFrameToScrolableDesctopPane(imagePath, pane, theFrame);
			UtilMethodsFactory.tableToImage(chartPanel.getTable(), imagePath.split("\\.")[0]);
			pane.remove(theFrame);
			//INIFilesFactory.updateINIFileItems(iniFile, "Update", "false", "latest");
		//}
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
