/*
 *
 */
package com.arkrud.pokerconsole.UI.Dashboard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;
import javax.swing.tree.DefaultMutableTreeNode;

import com.arkrud.pokerconsole.Poker.PokerStrategy;
import com.arkrud.pokerconsole.TreeInterface.CustomTree;
import com.arkrud.pokerconsole.UI.CustomProgressBar;
import com.arkrud.pokerconsole.UI.TableChartPanel;
import com.arkrud.pokerconsole.UI.scrollabledesktop.BaseInternalFrame;
import com.arkrud.pokerconsole.UI.scrollabledesktop.JScrollableDesktopPane;
import com.arkrud.pokerconsole.Util.INIFilesFactory;
import com.arkrud.pokerconsole.Util.MongoDBFactory;
import com.arkrud.pokerconsole.Util.UtilMethodsFactory;
import com.arkrud.pokerconsole.licensing.LicenseKeyGUI;

/**
 * Class to build dashboard drop-down menu.<br>
 */
public class DashboardMenu extends JMenu implements ActionListener, PropertyChangeListener {
	private static final long serialVersionUID = 1L;
	/**
	 * Menu item.
	 */
	private JMenuItem exit, license, addDashboardUser, clearUser, addTree, loadSolution, manageTrees,  openReadOnlyDash, populateChartDB, dataSourceSelection,
			manualSolutionNaming, backupConsoleLayoutAndData, restoreConsoleLayoutAndData;
	/**
	 * Reference to dashboard object
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
	String solutionPackagePath = "";
	final JFileChooser fc = new JFileChooser();
	
	

	/**
	 * Sole constructor of Dashboard object. <br>
	 * Adding Window Listener and initializing graphics controls
	 *
	 * @param dash
	 *            dashboard object reference
	 * @param editable
	 *            flag to define the editable state of the Poker hand charts
	 */
	public DashboardMenu(Dashboard dash, boolean editable) {
		super();
		this.dash = dash;
		this.editable = editable;
		setText("Edit");
		exit = new JMenuItem("Exit");
		license = new JMenuItem("License Info");
		addTree = new JMenuItem("Add Solution");
		loadSolution = new JMenuItem("Load Solution");
		backupConsoleLayoutAndData = new JMenuItem("Backup Console Layout And Data");
		restoreConsoleLayoutAndData = new JMenuItem("Restore Console Layout And Data");
		manualSolutionNaming = new JMenuItem("Enable Manual Solution Copy Naming");
		manualSolutionNaming.setEnabled(false);
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
		license.addActionListener(this);
		addTree.addActionListener(this);
		loadSolution.addActionListener(this);
		addDashboardUser.addActionListener(this);
		clearUser.addActionListener(this);
		manageTrees.addActionListener(this);
		openReadOnlyDash.addActionListener(this);
		populateChartDB.addActionListener(this);
		dataSourceSelection.addActionListener(this);
		manualSolutionNaming.addActionListener(this);
		backupConsoleLayoutAndData.addActionListener(this);
		restoreConsoleLayoutAndData.addActionListener(this);
		if (editable) {
			add(addDashboardUser);
			//add(clearUser);
			add(addTree);
			add(loadSolution);
			add(manualSolutionNaming);
			add(manageTrees);
			add(openReadOnlyDash);
			add(backupConsoleLayoutAndData);
			add(restoreConsoleLayoutAndData);
			// add(dataSourceSelection);
			// add(populateChartDB);
		}
		add(license);
		add(exit);
		
	}

	/**
	 * Initiates methods to perform menu actions on menu items selection. <br>
	 *
	 *
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String menuText = ((JMenuItem) e.getSource()).getText();
		if (menuText.contains("Exit")) {
			UtilMethodsFactory.exitApp();
		} else if (menuText.contains("License Info")) {
			//UtilMethodsFactory.showDialogToDesctop("LicenseInfo", 300, 350, dash, null, null, null, null, null, null);
			LicenseKeyGUI licenseKeyGUI = new LicenseKeyGUI(dash, true);
			licenseKeyGUI.checkLicense();
			licenseKeyGUI.updateGUIFieldsWithLicenseObject();
			licenseKeyGUI.setVisible(true);
		} else if (menuText.contains("Add User")) {
			UtilMethodsFactory.showDialogToDesctop("AddUser", 350, 140, null, null, null, null, null, null, addDashboardUser);
		} else if (menuText.contains("Add Solution")) {
			String solutiosCountString = INIFilesFactory.getItemValueFromINI(UtilMethodsFactory.getConsoleConfig(), "Config", "solutionsinuse");
			String multiSolutioModeString = INIFilesFactory.getItemValueFromINI(UtilMethodsFactory.getConsoleConfig(), "Config", "multisolution");
			if (Integer.parseInt(solutiosCountString) > 0 && !Boolean.parseBoolean(multiSolutioModeString)) {
				JOptionPane.showMessageDialog(null, "You are in Single Solution-Mode", "Single Solution-Mode Warning", JOptionPane.ERROR_MESSAGE);
			} else {
			UtilMethodsFactory.showDialogToDesctop("AddTreesFrame", 250, 140, dash, null, null, null, null, null, null);
			}
		} else if (menuText.contains("Load Solution")) {
			loadSolution();
		} else if (menuText.contains("Backup Console")) {
			backupConsoleData();
		} else if (menuText.contains("Restore Console")) {
			restoreConsoleData();
		} else if (menuText.contains("Enable Manual Solution Copy Naming")) {
			enableManualNaming();
		} else if (menuText.contains("Disable Manual Solution Copy Naming")) {
			disableManualNaming();
		} else if (menuText.contains("Hide/Show Trees")) {
			UtilMethodsFactory.showDialogToDesctop("ManageTreesDialog", 250, 150 + 25 * INIFilesFactory.getTreesData().size(), dash, null, null, null, null,
					null, null);
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
	 * Allows to have previously selected tree node to be selected and Poker hand charts placed into scrollable desktop on clicking on the header of tab pane
	 * tabs. <br>
	 * <ul>
	 * <li>Calculates the level on the node in the tree independent from application deployment location in file system.
	 * <li>Generates charts at specific levels if filesystem object is file.
	 * </ul>
	 *
	 * @param node
	 *            INI file object to be loaded into Poker hands chart
	 * @param editable
	 *            flag to define the editable state of the Poker hand charts
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
						File testFile = new File(node.getParent() + "\\" + node.getName().split("\\.")[0] + ".png");
						if (!testFile.exists()) {
							generateChart(node, editable);
						}
					}
				} else {
				}
			} else if (level == 4) {
				if (!node.getName().contains("png")) {
					File testFile = new File(node.getParent() + "\\" + node.getName().split("\\.")[0] + ".png");
					if (!testFile.exists()) {
						generateChart(node, editable);
					}
				}
			} else if (level == 5) {
				if (!node.getName().contains("png")) {
					File testFile = new File(node.getParent() + "\\" + node.getName().split("\\.")[0] + ".png");
					if (!testFile.exists()) {
						generateChart(node, editable);
					}
				}
			} else {
			}
		}
	}

	/**
	 * Generates chart from INI file to produce PNG image and removes chart. <br>
	 * <ul>
	 * <li>Get absolute file path.
	 * <li>Strip the path before application Images directory to produce relative path string.
	 * <li>Generate ChartPanel object using relative path and according editable flag value.
	 * <li>Instantiate BaseInternalFrame to hold the ChartPanel and using relative path as a title.
	 * <li>Get reference to JScrollableDesktopPane from Dashboard.
	 * <li>Add frame to the scrollable desktop using relative path as a frame title.
	 * <li>Generate chart PNG image from chart table in the ChartPanel in the same location as initial INI file.
	 * <li>Remove the frame from the JScrollableDesktopPane.
	 * </ul>
	 *
	 * @param node
	 *            INI file object to be loaded into Poker hands chart
	 * @param editable
	 *            flag to define the editable state of the Poker hand charts
	 */
	private void generateChart(File node, boolean editable) {
		String absolutePath = node.getAbsoluteFile().getPath();
		String imagePath = absolutePath.substring(absolutePath.indexOf("Images"), absolutePath.length());
		TableChartPanel chartPanel = new TableChartPanel(imagePath, editable, dash);
		BaseInternalFrame theFrame = new CustomTableViewInternalFrame(imagePath, chartPanel);
		theFrame.setName(imagePath);
		JScrollableDesktopPane pane = dash.getJScrollableDesktopPane();
		UtilMethodsFactory.addInternalFrameToScrolableDesctopPane(imagePath, pane, theFrame);
		UtilMethodsFactory.tableToImage(chartPanel.getTable(), imagePath.split("\\.")[0]);
		pane.remove(theFrame);
	}

	/**
	 * Opens read-only or editable Dashboard. <br>
	 * <ul>
	 * <li>Instantiate new dashboard.
	 * <li>Make dashboard visible.
	 * <li>Dispose current dashboard.
	 * </ul>
	 *
	 * @param editable
	 *            flag to define the editable state of the Poker hand charts
	 */
	private void showDashboard(boolean editable) {
		Dashboard readOnlyDash = null;
		try {
			readOnlyDash = new Dashboard(editable);
		} catch (Exception e) {
			e.printStackTrace();
		}
		readOnlyDash.setVisible(true);
		dash.dispose();
	}

	/**
	 * Imports solution from solution package ZIP file.<br>
	 * <ul>
	 * <li>Opens File dialog to select solution package ZIP file from filesystem.
	 * <li>Add solution name item to INI file Application section with status true visible.
	 * <li>Add solution name item to INI file Autonaming section with status false visible.
	 * <li>Create strategy directory.
	 * <li>Instantiate new tree and add it to new solution tree tab.
	 * <li>Unzip solution files to the solution directory.
	 * <li>Refresh tree to read and reflect new solution charts.
	 * <li>Select the imported tab with new solution tree.
	 * </ul>
	 *
	 */
	private void loadSolution() {
		File file = null;
		fc.setCurrentDirectory(new File(UtilMethodsFactory.getConfigPath() + "Images/"));
		fc.setDialogTitle("Load Solution Package");
		int returnVal = fc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();
			solutionPackagePath = file.getAbsolutePath().replace("\\", "/");
			String strategyName = file.getName().split("\\.")[0];
			INIFilesFactory.addINIFileItemToSection(UtilMethodsFactory.getConsoleConfig(), "Solutions", strategyName, "true");
			INIFilesFactory.addINIFileItemToSection(UtilMethodsFactory.getConsoleConfig(), "Autonaming", strategyName, "true");
			File strategyDir = new File(UtilMethodsFactory.getConfigPath() + "Images/" + strategyName);
			UtilMethodsFactory.createFolder(strategyDir);
			CustomTree tree = dash.addTreeTabPaneTab(strategyName);
			String destDirectory = (UtilMethodsFactory.getConfigPath() + "Images/").substring(1);
			final CustomProgressBar progFrame = new CustomProgressBar(true, false, "Retrieving Instances Info");
			progFrame.getPb().setIndeterminate(true);
			SwingWorker<Void, Void> w = new SwingWorker<Void, Void>() {
				@Override
				protected Void doInBackground() throws Exception {
					UtilMethodsFactory.unZipUpdate(solutionPackagePath, destDirectory);
					tree.refreshTreeNode((DefaultMutableTreeNode) tree.getTreeModel().getRoot(), strategyName, editable);
					dash.getTreeTabbedPane().setSelectedIndex(dash.getTreeTabbedPane().indexOfTab(strategyName));
					tree.setSelection((DefaultMutableTreeNode) tree.getTreeModel().getRoot(), tree.getTheTree(), true);
					return null;
				};

				// this is called when the SwingWorker's doInBackground finishes
				@Override
				protected void done() {
					progFrame.getPb().setIndeterminate(false);
					progFrame.setVisible(false); // hide my progress bar JFrame
				};
			};
			w.addPropertyChangeListener(this);
			w.execute();
			progFrame.setVisible(true);
		} else {
		}
	}

	private void backupConsoleData() {
		String applicationRoot = UtilMethodsFactory.getConfigPath();
		String backupDirName = applicationRoot + "Backup/";
		File backupDefaultDir = new File(backupDirName);
		UtilMethodsFactory.createFolder(backupDefaultDir);
		fc.setCurrentDirectory(backupDefaultDir);
		fc.setDialogTitle("Backup Console Layout And Data");
		fc.setSelectedFile(new File(composeBackupFolderName()));
		int returnVal = fc.showSaveDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String backupName = fc.getSelectedFile().getName();
			UtilMethodsFactory.createFolder(new File(backupDirName + backupName));
			File source = new File(applicationRoot + "config.ini");
			File destination = new File(backupDirName + backupName + "/config.ini");
			try {
				UtilMethodsFactory.copyFile(source, destination);
			} catch (IOException e) {
				e.printStackTrace();
			}
			UtilMethodsFactory.createFolder(new File(backupDirName + backupName + "/Images"));
			Path sourcePath = new File(applicationRoot + "Images").toPath();
			Path destinationPath = new File(backupDirName + backupName + "/Images").toPath();
			try {
				UtilMethodsFactory.copyDir(sourcePath, destinationPath);
				JOptionPane.showMessageDialog(dash, "Backup Successful", "Info", JOptionPane.INFORMATION_MESSAGE);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
		}
	}

	/**
	 * Restore console data and layout from backup.<br>
	 * <ul>
	 * <li>Define name of application root folder.
	 * <li>Define name of backup folder.
	 * <li>Set file chooser (FC) current directory to backup folder.
	 * <li>Set FC title.
	 * <li>Set FC mode to select directories.
	 * <li>Show FC.
	 * <li>After selecting the desired backup folder on save button click bring confirmation dialog (CD).
	 * <li>On CD OK option define backup name variable as selected directory.
	 * <li>Define source file as config.ini file the backup directory.
	 * <li>Define destination file as console config.ini file in the root of teh application.
	 * <li>Restore INI file.
	 * <li>Define source directory as backup Images directory.
	 * <li>Define destination directory Images sub-directory of the application.
	 * <li>Delete application Images directory.
	 * <li>Restore Images directory.
	 * <li>Reopen application with restored configuration.
	 * </ul>
	 *
	 */
	private void restoreConsoleData() {
		String applicationRoot = UtilMethodsFactory.getConfigPath();
		String backupDirName = applicationRoot + "Backup/";
		File backupDefaultDir = new File(backupDirName);
		fc.setCurrentDirectory(backupDefaultDir);
		fc.setDialogTitle("Restore Console Layout And Data");
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			int response = JOptionPane.showConfirmDialog(null, "Do you really want to owerwrite the console data?", "Restore Console Data Warning",
					JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
			if (response == JOptionPane.NO_OPTION) {
			} else if (response == JOptionPane.YES_OPTION) {
				String backupName = fc.getSelectedFile().getName();
				File source = new File(backupDirName + backupName + "/config.ini");
				File destination = new File(applicationRoot + "config.ini");
				try {
					UtilMethodsFactory.copyFile(source, destination);
				} catch (IOException e) {
					e.printStackTrace();
				}
				Path sourcePath = new File(backupDirName + backupName + "/Images").toPath();
				Path destinationPath = new File(applicationRoot + "Images").toPath();
				try {
					UtilMethodsFactory.deleteDirectory(new File(applicationRoot + "Images"));
					UtilMethodsFactory.copyDir(sourcePath, destinationPath);
					showDashboard(true);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (response == JOptionPane.CLOSED_OPTION) {
			}
		} else {
		}
	}

	/**
	 * Open read only dashboard.
	 * <ul>
	 * <li>Generates chart images if not yet generated.
	 * <li>Update INI configuration file to set dashboard flag to false (not editable).
	 * <li>Show read-only and close editable dashboard.
	 * </ul>
	 */
	private void openReadOnlyDashboard() {
		JScrollPane jScrollPane = (JScrollPane) dash.getTreeTabbedPane().getSelectedComponent();
		CustomTree customTree = (CustomTree) jScrollPane.getViewport().getView();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) customTree.getTreeModel().getRoot();
		PokerStrategy pokerStrategy = (PokerStrategy) node.getUserObject();
		generateChartImages(new File(UtilMethodsFactory.getConfigPath() + "Images/" + pokerStrategy.getNodeText()), false);
		INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Config", "false", "editable");
		showDashboard(false);
	}

	/**
	 * Enable manual naming.
	 * <ul>
	 * <li>Retrieve the currently selected tab.
	 * <li>Update INI configuration file to set manual naming of the tree tab headers flag to true (editable).
	 * <li>Set appropriate menu item text.
	 * </ul>
	 */
	private void enableManualNaming() {
		String selectedTabName = dash.getTreeTabbedPane().getTitleAt(dash.getTreeTabbedPane().getSelectedIndex());
		INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Autonaming", "false", selectedTabName);
		manualSolutionNaming.setText("Disable Manual Solution Copy Naming");
	}

	/**
	 * Disable manual naming.
	 * <ul>
	 * <li>Update INI configuration file to set manual naming of the tree tab headers flag to false (auto naming).
	 * <li>Set appropriate menu item text.
	 * </ul>
	 */
	public void disableManualNaming() {
		String selectedTabName = dash.getTreeTabbedPane().getTitleAt(dash.getTreeTabbedPane().getSelectedIndex());
		INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Autonaming", "true", selectedTabName);
		manualSolutionNaming.setText("Enable Manual Solution Copy Naming");
	}
	
	
	/**
	 * Load Charts To Mongo DB.
	 * <ul>
	 * <li>Open Mongo DB connection.
	 * <li>Add chart images to Mongo DB table.
	 * <li>Close Mongo DB connection.
	 * </ul>
	 */
	private void loadChartsToMongo() {
		MongoDBFactory.crateMongoConnection();
		addDocuments(new File(UtilMethodsFactory.getConfigPath() + "Images"));
		MongoDBFactory.closeMongoConnection();
	}

	/**
	 * Set dashboard to use Mongo DB to populate charts.
	 * <ul>
	 * <li>Update INI configuration file to set use Mongo DB flag to true.
	 * <li>Update INI configuration file to set use INI files flag to false.
	 * <li>Show editable dashboard with chart images loaded from Mongo DB.
	 * </ul>
	 */
	private void useMongo() {
		INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Config", "true", "mongo");
		INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Config", "false", "ini");
		showDashboard(true);
	}

	/**
	 * Set dashboard to use INI files to populate charts.
	 * <ul>
	 * <li>Update INI configuration file to set use Mongo DB flag to false.
	 * <li>Update INI configuration file to set use INI files flag to true.
	 * <li>Show editable dashboard with chart images loaded from file system INI files.
	 * </ul>
	 */
	private void useINI() {
		INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Config", "false", "mongo");
		INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Config", "true", "ini");
		showDashboard(true);
	}

	/**
	 * Clear security.
	 * <ul>
	 * <li>Bring up confirmation dialog for user to approve the action.
	 * <li>If approved remove Security section from INI configuration file.
	 * </ul>
	 */
	private void clearSecurity() {
		int response = JOptionPane.showConfirmDialog(null, "Do you want to disable security?", "Disable security", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.NO_OPTION) {
		} else if (response == JOptionPane.YES_OPTION) {
			INIFilesFactory.removeINIFileSection(UtilMethodsFactory.getConsoleConfig(), "Security");
		} else if (response == JOptionPane.CLOSED_OPTION) {
		}
	}

	/**
	 * Recursive Crawl file system to add chart images to Mongo DB.
	 * <ul>
	 * <li>Calculate how deep the file structure is.
	 * <li>If file object is directory run this function recursively.
	 * <li>If file object is file add image object document to Mongo.
	 * </ul>
	 *
	 * @param node
	 *            the node
	 */
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

	/**
	 * Add and update mongo document.
	 * <ul>
	 * <li>Get image path string from the file.
	 * <li>Add file object document to Mongo.
	 * <li>Generate PNG image file.
	 * <li>Update created Mongo document with PNG file.
	 * </ul>
	 *
	 * @param node
	 *            the node
	 */
	private void updateMongoDocument(File node) {
		String absolutePath = node.getAbsoluteFile().getPath();
		String imagePath = absolutePath.substring(absolutePath.indexOf("Images"), absolutePath.length()).split("\\.")[0].replaceAll("\\\\", "/");
		MongoDBFactory.addDocument(INIFilesFactory.getItemValuesFromINI(node), imagePath);
		File pngfile = new File(UtilMethodsFactory.getConfigPath() + imagePath + ".png");
		MongoDBFactory.updateDocuments(imagePath, pngfile);
	}

	private String composeBackupFolderName() {
		Date date = new Date();
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		List<String> strings = new LinkedList<>();
		strings.add(Integer.toString(year));
		strings.add(Integer.toString(month));
		strings.add(Integer.toString(day));
		strings.add(Integer.toString(hour));
		strings.add(Integer.toString(minute));
		return String.join("-", strings);
	}

	public void setManualEditingMenu(boolean state) {
		manualSolutionNaming.setEnabled(true);
		if (state) {
			manualSolutionNaming.setText("Disable Manual Solution Copy Naming");
		} else {
			manualSolutionNaming.setText("Enable Manual Solution Copy Naming");
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		// TODO Auto-generated method stub
	}

	
	
	
}
