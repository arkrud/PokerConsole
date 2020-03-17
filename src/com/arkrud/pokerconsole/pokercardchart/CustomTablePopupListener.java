package com.arkrud.pokerconsole.pokercardchart;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.HashMap;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import com.arkrud.pokerconsole.UI.Dashboard.Dashboard;
import com.arkrud.pokerconsole.Util.INIFilesFactory;
import com.arkrud.pokerconsole.Util.UtilMethodsFactory;

public class CustomTablePopupListener extends MouseAdapter implements ActionListener, PropertyChangeListener {
	private JPopupMenu popupMenu;
	private String iniPath;
	private CustomTable table;
	private Dashboard dash;
	private String[] menus = { "Clear all", "Red", "Orange", "Blueviolet", "Green",  "Dark blue", "Yellow", "Pink",
			"Blue", "White", "Dark gray", "Light gray", "Save Chart" };

	public CustomTablePopupListener(JPopupMenu popupMenu, String iniPath, Dashboard dash) {
		this.popupMenu = popupMenu;
		this.iniPath = iniPath;
		this.dash = dash;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String menuText = ((JMenuItem) e.getSource()).getText();
		if (menuText.contains("Red")) {
			table.setCurrentSelectionColor(new Color(254, 0, 2));
		} else if (menuText.contains("Orange")) {
			table.setCurrentSelectionColor(new Color(255, 134, 0));
		} else if (menuText.contains("Green")) {
			table.setCurrentSelectionColor(new Color(0, 255, 0));
		} else if (menuText.contains("Dark blue")) {
			table.setCurrentSelectionColor(new Color(0,0,255));
		} else if (menuText.contains("Yellow")) {
			table.setCurrentSelectionColor(new Color(255, 255, 1));
		} else if (menuText.contains("Pink")) {
			table.setCurrentSelectionColor(new Color(255, 138, 255));
		} else if (menuText.contains("Blueviolet")) {
			table.setCurrentSelectionColor(new Color(138,43,226));
		} else if (menuText.contains("Blue")) {
			table.setCurrentSelectionColor(new Color(3, 255, 255));
		} else if (menuText.contains("White")) {
			table.setCurrentSelectionColor(new Color(255, 255, 255));
		} else if (menuText.contains("Dark gray")) {
			table.setCurrentSelectionColor(new Color(209, 205, 204));
		} else if (menuText.contains("Light gray")) {
			table.setCurrentSelectionColor(new Color(245, 245, 245));
		} else if (menuText.contains("Save Chart")) {
			UtilMethodsFactory.removeFromCharts(iniPath.substring(0, iniPath.length() - 3) + "jpg"); // Remove Chart object from static collection to have chart to be build from updated INI file
			File file = new File(UtilMethodsFactory.getConfigPath() + iniPath);
			UtilMethodsFactory.createChartINIFile(file); // Create chart INI file if does not exist
			updateChartINIFile(table, file); // Update file with changed coloring
			UtilMethodsFactory.generateChart(file, true, dash);
		} else if (menuText.contains("Clear all")) {
			setDefaultColor(); // Set editing color to light gray
			File file = new File(UtilMethodsFactory.getConfigPath() + iniPath);
			updateChartINIFile(table, file); // Clear all colors and have all cells light gray
		}
	}

	private void setDefaultColor() {
		table.setBackground(new Color(245, 245, 245));
		for (int i = 0; i < table.getRowCount(); i++) {
			for (int j = 0; j < table.getColumnCount(); j++) {
				((JTextField) table.getModel().getValueAt(i, j)).setBackground(new Color(245, 245, 245));
			}
		}
	}

	private void updateChartINIFile(CustomTable table, File file) {
		for (int i = 0; i < table.getRowCount(); i++) {
			for (int j = 0; j < table.getColumnCount(); j++) {
				JTextField field = (JTextField) table.getValueAt(i, j);
				HashMap<String, String> sectionKeys = new HashMap<String, String>();
				sectionKeys.put("AlphaRGB", Integer.toString(field.getBackground().getAlpha()));
				sectionKeys.put("RedRGB", Integer.toString(field.getBackground().getRed()));
				sectionKeys.put("GreenRGB", Integer.toString(field.getBackground().getGreen()));
				sectionKeys.put("BlueRGB", Integer.toString(field.getBackground().getBlue()));
				INIFilesFactory.addINIFileSection(file, field.getText(), sectionKeys);
			}
		}
		HashMap<String, String> sectionKeys = new HashMap<String, String>();
		sectionKeys.put("latest", "true");		
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		showPopup(e);
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
	}

	private void showPopup(MouseEvent e) {
		table = (CustomTable) e.getSource();
		table.clearSelection();
		if (e.isPopupTrigger()) {
			popupMenu.removeAll();
			int x = 0;
			while (x < menus.length) {
				JMenuItem menuItem = new JMenuItem(menus[x]);
				menuItem.addActionListener(this);
				popupMenu.add(menuItem);
				x++;
			}
			popupMenu.show(e.getComponent(), e.getX(), e.getY());
		}
	}
}
