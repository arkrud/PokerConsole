package com.arkrud.pokerconsole.Util;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimplePBEConfig;

import com.arkrud.pokerconsole.TreeInterface.CustomTree;
import com.arkrud.pokerconsole.UI.AddHandsDialog;
import com.arkrud.pokerconsole.UI.AddTreeFrame;
import com.arkrud.pokerconsole.UI.ChnageChartsOrderDialog;
import com.arkrud.pokerconsole.UI.ManageTreesDialog;
import com.arkrud.pokerconsole.UI.RenameTreeDialog;
import com.arkrud.pokerconsole.UI.TableChartPanel;
import com.arkrud.pokerconsole.UI.Dashboard.AddUserDialog;
import com.arkrud.pokerconsole.UI.Dashboard.CustomTableViewInternalFrame;
import com.arkrud.pokerconsole.UI.Dashboard.Dashboard;
import com.arkrud.pokerconsole.UI.scrollabledesktop.BaseInternalFrame;
import com.arkrud.pokerconsole.UI.scrollabledesktop.JScrollableDesktopPane;
import com.arkrud.pokerconsole.pokercardchart.CustomTable;

/**
 * Static methods and constants accessed across application code.<br>
 *
 */
public class UtilMethodsFactory {
	public static String[] dropDownsNames = { "Add Group", "Refresh", "Delete", "Remove", "Rename", "Add Sizing", "Delete Sizing", "Apply Template",
			"Add Action", "Add Hero Position", "Add Opponents Position / Hero Range", "Duplicate", "Change Charts Order" };
	private static HashMap<String, TableChartPanel> charts = new HashMap<String, TableChartPanel>();

	public static void addChartFrameToScrolableDesctop(String chartImagePath, String chartFrameTitle, boolean editable, Dashboard dash) {
		JScrollableDesktopPane jScrollableDesktopPane = dash.getJScrollableDesktopPane();
		TableChartPanel chartPanel = new TableChartPanel(chartImagePath, editable, dash);
		BaseInternalFrame theFrame = new CustomTableViewInternalFrame(chartFrameTitle, chartPanel);
		theFrame.setName(chartImagePath);
		UtilMethodsFactory.addInternalFrameToScrolableDesctopPane(chartFrameTitle, jScrollableDesktopPane, theFrame);
	}

	public static void addInternalFrameToScrolableDesctopPane(String frameTitle, JScrollableDesktopPane jScrollableDesktopPan, BaseInternalFrame theFrame) {
		if (Dashboard.INTERNAL_FRAMES.get(frameTitle) == null) {
			jScrollableDesktopPan.add(theFrame);
			Dashboard.INTERNAL_FRAMES.put(frameTitle, theFrame);
		} else {
			jScrollableDesktopPan.remove(Dashboard.INTERNAL_FRAMES.get(frameTitle));
			jScrollableDesktopPan.add(theFrame);
			jScrollableDesktopPan.setSelectedFrame(theFrame);
			Dashboard.INTERNAL_FRAMES.put(frameTitle, theFrame);
		}
	}

	public static void addToCharts(String path, TableChartPanel chartPanel) {
		charts.put(path, chartPanel);
	}

	public static boolean checkIfGroupNameHasLettersOnly(String name) {
		return name.matches("[a-zA-Z]+");
	}

	public static void copyDir(Path src, Path dest) throws IOException {
		List<Path> sources = Files.walk(src).collect(Collectors.toList());
		for (Path source: sources) {
			Files.copy(source, dest.resolve(src.relativize(source)),
						StandardCopyOption.REPLACE_EXISTING);
		}
	}
	
	public static void copyFile(File source, File dest) throws IOException {
		Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
	}

	public static void createChartINIFile(File file) {
		// Create the file
		try {
			if (file.createNewFile()) {
				// System.out.println("File is created!");
			} else {
				// System.out.println("File already exists.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void createFolder(File file) {
		if (file.mkdir()) {
			//System.out.println("Dir is created!");
		} else {
			System.out.println("Dir already exists.");
		}
	}

	/**
	 * Create image icon for tree nodes. <br>
	 *
	 * @return <code>ImageIcon</code> of tree user objects
	 * @param path
	 *            reference to image file name
	 */
	public static ImageIcon createImageIcon(String path) {
		URL imgURL = null;
		try {
			imgURL = new URL("file:" + getConfigPath() + path);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	public static boolean deleteDirectory(File directoryToBeDeleted) {
		File[] allContents = directoryToBeDeleted.listFiles();
		if (allContents != null) {
			for (File file : allContents) {
				deleteDirectory(file);
			}
		}
		return directoryToBeDeleted.delete();
	}

	public static void deleteFile(String fileName) {
		try {
			File file = new File(fileName);
			if (file.delete()) {
				System.out.println(file.getName() + " is deleted!");
			} else {
				System.out.println("Delete operation is failed.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void exitApp() {
		INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Config", "true", "editable");
		System.exit(0);
	}

	public static String firstLetterOccurence(String text) {
		String matchPosition = "";
		Matcher m = Pattern.compile("[^a-zA-Z]*([a-zA-Z]+).*").matcher(text);
		if (m.matches()) {
			matchPosition = m.group(1);
		}
		return matchPosition;
	}

	public static void generateChart(File node, boolean editable, Dashboard dash) {
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

	// Retrieve the path of the root of the solution - src\
	public static String getConfigPath() {
		String binPath = UtilMethodsFactory.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String configPath = binPath.substring(0, binPath.indexOf(binPath.split("/")[binPath.split("/").length - 1]));
		return configPath;
	}

	/**
	 * Retrieve path for this console config - src\config.ini <br>
	 *
	 * @return <code>File</code> object representing Dashboard configuration file
	 */
	public static File getConsoleConfig() {
		String confPath = getConfigPath() + "config.ini";
		File configINIFile = new File(confPath);
		return configINIFile;
	}

	public static StandardPBEStringEncryptor getEncryptor() {
		SimplePBEConfig config = new SimplePBEConfig();
		config.setAlgorithm("PBEWithMD5AndTripleDES");
		config.setKeyObtentionIterations(1000);
		config.setPassword("propertiesFilePassword");
		StandardPBEStringEncryptor encryptor = new org.jasypt.encryption.pbe.StandardPBEStringEncryptor();
		encryptor.setConfig(config);
		encryptor.initialize();
		return encryptor;
	}

	public static int getIndexOfFirstIntInString(String str) {
		Matcher matcher = Pattern.compile("\\d+").matcher(str);
		matcher.find();
		return str.indexOf(matcher.group());
	}

	public static int getIndexOfFirstLiteralInString(String str) {
		Matcher matcher = Pattern.compile("\\D+").matcher(str);
		matcher.find();
		return str.indexOf(matcher.group());
	}

	public static boolean hasPNGFile(List<String> filesList) {
		Iterator<String> it = filesList.iterator();
		boolean hasPNG = false;
		while (it.hasNext()) {
			String fileName = it.next();
			if (fileName.contains("png")) {
				hasPNG = true;
				break;
			}
		}
		return hasPNG;
	}

	public static List<String> listFiles(String path) {
		List<String> result = new ArrayList<String>();
		try (Stream<Path> walk = Files.walk(Paths.get(path))) {
			result = walk.filter(Files::isRegularFile).map(x -> x.toString()).collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	// Populate tree nodes with respective icons
	public static HashMap<String, ImageIcon> populateInterfaceImages(String location) {
		HashMap<String, ImageIcon> images = new HashMap<String, ImageIcon>();
		String[] objectIcons = getFileNames(location);
		for (String objectIcon : objectIcons) {
			ImageIcon icon = createImageIcon(location + "/" + objectIcon);
			images.put(objectIcon.split("\\.")[0], icon);
		}
		return images;
	}

	public static void printList(List<?> list) {
		int y = 0;
		while (y < list.size()) {
			System.out.println(list.get(y));
			y++;
		}
	}

	public static void removeFromCharts(String path) {
		charts.remove(path);
	}

	public static void renameFile(String oldFileName, String newFileName) {
		File oldFile = new File(oldFileName);
		File newFile = new File(newFileName);
		try {
			Files.move(oldFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static <T> Reversed<T> reversed(List<T> original) {
		return new Reversed<T>(original);
	}

	public static void showDialogToDesctop(String frameType, int invalid, int invalid2, Dashboard dash, JTree tree, CustomTree theTree, Object obj,
			DefaultMutableTreeNode node, JTabbedPane tabbedPane, JMenuItem addUser) {
		JDialog dialog = null;
		switch (frameType) {
		case "AddTreesFrame":
			dialog = new AddTreeFrame(dash);
			break;
		case "RenameSolutioTab":
			dialog = new RenameTreeDialog(dash, tabbedPane);
			break;
		case "ManageTreesDialog":
			dialog = new ManageTreesDialog(dash);
			break;
		case "AddHandsDialog":
			dialog = new AddHandsDialog(tree, theTree, obj, node);
			break;
		case "AddUser":
			dialog = new AddUserDialog(addUser);
			break;
		case "ChnageChartsOrderDialog":
			dialog = new ChnageChartsOrderDialog(tree, node, dash, theTree);
			break;
		default:
			break;
		}
		dialog.setSize(invalid, invalid2);
		// Get the size of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		// Determine the new location of the window
		int w = dialog.getSize().width;
		int h = dialog.getSize().height;
		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;
		// Move the window
		dialog.setLocation(x, y);
		dialog.setVisible(true);
	}

	public static void tableToImage(CustomTable table, String imagePath) {
		int w = Math.max(table.getWidth(), table.getTableHeader().getWidth());
		int h = table.getHeight() + table.getTableHeader().getHeight();
		if (w > 0 && h > 0) {
			BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2 = bi.createGraphics();
			table.getTableHeader().paint(g2);
			g2.translate(0, table.getTableHeader().getHeight());
			table.paint(g2);
			g2.dispose();
			try {
				ImageIO.write(bi, "png", new File(imagePath + ".png"));
			} catch (IOException ioe) {
				System.out.println("write: " + ioe.getMessage());
			}
		}
	}

	public static void unZipUpdate(String pathToUpdateZip, String destinationPath) {
		byte[] byteBuffer = new byte[1024];
		try {
			ZipInputStream inZip = new ZipInputStream(new FileInputStream(pathToUpdateZip));
			ZipEntry inZipEntry = inZip.getNextEntry();
			while (inZipEntry != null) {
				String fileName = inZipEntry.getName();
				File unZippedFile = new File(destinationPath + File.separator + fileName);
				if (inZipEntry.isDirectory()) {
					unZippedFile.mkdirs();
				} else {
					new File(unZippedFile.getParent()).mkdirs();
					unZippedFile.createNewFile();
					FileOutputStream unZippedFileOutputStream = new FileOutputStream(unZippedFile);
					int length;
					while ((length = inZip.read(byteBuffer)) > 0) {
						unZippedFileOutputStream.write(byteBuffer, 0, length);
					}
					unZippedFileOutputStream.close();
				}
				inZipEntry = inZip.getNextEntry();
			}
			inZip.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String[] getFileNames(String location) {
		File folder = new File(getConfigPath() + location);
		String[] files = folder.list();
		return files;
	}
	
}
