package com.arkrud.pokerconsole.pokercardchart;

import java.awt.Color;

import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTextField;

public class CustomTable extends JTable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Class<?>[] renderedClasses = { JTextField.class, Object.class };
	private Color currentSelectionColor = new Color(245, 245, 245);

	public CustomTable(String imagePath) {
		CustomTableModel tableModel = (new CustomTableModel());
		tableModel.generateTableHeaders();
		tableModel.generateTableData(imagePath);
		tableModel.generateChartINIFile(imagePath.substring(0, imagePath.length() - 3) + "ini");
		setModel(tableModel);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setFillsViewportHeight(true);
		setRowHeight(30);
		setShowGrid(false);
		createDefaultColumnsFromModel();
		CustomTableCellRenderer customTableCellRenderer = new CustomTableCellRenderer();
		for (@SuppressWarnings("rawtypes")
		Class cl : renderedClasses) {
			setDefaultRenderer(cl, customTableCellRenderer);
		}
		tableModel.adjustColumnPreferredWidths(this);
		JPopupMenu tablePopup = new JPopupMenu();
		addMouseListener(new CustomTablePopupListener(tablePopup, imagePath.substring(0, imagePath.length() - 3) + "ini"));
	}

	public Color getCurrentSelectionColor() {
		return currentSelectionColor;
	}

	public void setCurrentSelectionColor(Color currentSelectionColor) {
		this.currentSelectionColor = currentSelectionColor;
	}
}
