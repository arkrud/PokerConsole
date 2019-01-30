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

import com.arkrud.pokerconsole.Util.INIFilesFactory;
import com.arkrud.pokerconsole.Util.UtilMethodsFactory;

public class CustomTablePopupListener extends MouseAdapter implements ActionListener, PropertyChangeListener {
	private JPopupMenu popupMenu;
	private String iniPath;
	private CustomTable table;
	private String[] menus = { "Red(always bet/raise)", "Orange(Mostly bet/raise and otherwise call)", "Green(Always call)", "Yellow(Sometimes bet/raise and sometimes call)", "Purple(Mostly fold otherwise raise)", "Blue(Fold/call/raise equally)",
			"White(Sometimes call sometimes fold)", "Dark gray(Always fold)", "Light gray(Not in range)", "Save Chart" };

	public CustomTablePopupListener(JPopupMenu popupMenu, String iniPath) {
		this.popupMenu = popupMenu;
		this.iniPath = iniPath;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String menuText = ((JMenuItem) e.getSource()).getText();
		if (menuText.contains("Red(always bet/raise)")) {
			table.setCurrentSelectionColor(new Color(254, 0, 2));
		} else if (menuText.contains("Orange(Mostly bet/raise and otherwise call)")) {
			table.setCurrentSelectionColor(new Color(255, 134, 0));
		} else if (menuText.contains("Green(Always call)")) {
			table.setCurrentSelectionColor(new Color(0, 255, 0));
		} else if (menuText.contains("Yellow(Sometimes bet/raise and sometimes call)")) {
			table.setCurrentSelectionColor(new Color(255, 255, 1));
		} else if (menuText.contains("Purple(Mostly fold otherwise raise)")) {
			table.setCurrentSelectionColor(new Color(255, 138, 255));
		} else if (menuText.contains("Blue(Fold/call/raise equally)")) {
			table.setCurrentSelectionColor(new Color(3, 255, 255));
		} else if (menuText.contains("White(Sometimes call sometimes fold)")) {
			table.setCurrentSelectionColor(new Color(255, 255, 255));
		} else if (menuText.contains("Dark gray(Always fold)")) {
			table.setCurrentSelectionColor(new Color(209, 205, 204));
		} else if (menuText.contains("Light gray(Not in range)")) {
			table.setCurrentSelectionColor(new Color(245, 245, 245));
		} else if (menuText.contains("Save Chart")) {
			File file = new File(UtilMethodsFactory.getConfigPath() + iniPath);
			UtilMethodsFactory.createChartINIFile(file);
			updateChartINIFile(table, file);
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
