package com.arkrud.pokerconsole.Util;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JDialog;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimplePBEConfig;

import com.arkrud.pokerconsole.UI.AddTreeFrame;
import com.arkrud.pokerconsole.UI.ChartPanel;
import com.arkrud.pokerconsole.UI.ManageTreesDialog;
import com.arkrud.pokerconsole.UI.Dashboard.Dashboard;
import com.arkrud.pokerconsole.UI.scrollabledesktop.BaseInternalFrame;
import com.arkrud.pokerconsole.UI.scrollabledesktop.JScrollableDesktopPane;

/**
 * Static methods and constants accessed across application code.<br>
 *
 */
public class UtilMethodsFactory {
	public static String[] dropDownsNames = { "Add Group", "Refresh", "Delete", "Remove", "Rename", "Add Sizing", "Apply Template" };
	private static HashMap<String, ChartPanel> charts = new HashMap<String, ChartPanel>();

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
	
	public static void addToCharts (String path, ChartPanel chartPanel) {
		charts.put(path, chartPanel);
	}
	
	public static ChartPanel getChart (String path) {
		return charts.get(path);
	}
	
	public static boolean hasChart (String path) {
		return charts.containsKey(path);
	}

	
	/**
	 * Create image icon for tree nodes. <br>
	 *
	 * @return <code>ImageIcon</code> of tree user objects
	 * @param path reference to image file name
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

	public static void exitApp() {
		System.exit(0);
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

	private static String[] getFileNames(String location) {
		File folder = new File(getConfigPath() + location);
		String[] files = folder.list();
		return files;
	}

	public static int getIndexOfFirstIntInString(String str) {
		Matcher matcher = Pattern.compile("\\d+").matcher(str);
		matcher.find();
		return str.indexOf(matcher.group());
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

	public static void showDialogToDesctop(String frameType, int width, int height, Dashboard dash) {
		JDialog dialog = null;
		switch (frameType) {
		case "AddTreesFrame":
			dialog = new AddTreeFrame(dash);
			break;
		case "ManageTreesDialog":
			dialog = new ManageTreesDialog(dash);
			break;
		default:
			break;
		}
		dialog.setSize(width, height);
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

	public static void createChartINIFile(File file) {
		// Create the file
		try {
			if (file.createNewFile()) {
				System.out.println("File is created!");
			} else {
				// System.out.println("File already exists.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void createGRoupFolder(File file) {
		if (file.mkdir()) {
			System.out.println("Dir is created!");
		} else {
			System.out.println("Dir already exists.");
		}
	}
}
