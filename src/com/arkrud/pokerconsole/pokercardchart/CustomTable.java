package com.arkrud.pokerconsole.pokercardchart;

import java.awt.Color;

import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.arkrud.pokerconsole.UI.TableChartPanel;
import com.arkrud.pokerconsole.UI.Dashboard.Dashboard;

// TODO: Auto-generated Javadoc
/**
 * The Class CustomTable.
 */
public class CustomTable extends JTable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The rendered classes. */
	private static Class<?>[] renderedClasses = { JTextField.class, Object.class };
	
	/** The current selection color. */
	private Color currentSelectionColor = new Color(245, 245, 245);

	/**
	 * Instantiates a new custom table.
	 *
	 * @param imagePath the image path
	 * @param chart the chart
	 * @param editable the editable
	 * @param dash the dash
	 */
	public CustomTable(String imagePath, TableChartPanel chart, boolean editable, Dashboard dash) {
		CustomTableModel tableModel = (new CustomTableModel(chart));
		
		tableModel.generateTableHeaders();
		tableModel.generateTableData(imagePath);
		String path = imagePath.substring(0, imagePath.length() - 3) + "ini";
		tableModel.generateChartINIFile(path);
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
		if (editable) {
		addMouseListener(new CustomTablePopupListener(tablePopup, path, dash));
		}
	}

	/**
	 * Gets the current selection color.
	 *
	 * @return the current selection color
	 */
	public Color getCurrentSelectionColor() {
		return currentSelectionColor;
	}

	/**
	 * Sets the current selection color.
	 *
	 * @param currentSelectionColor the new current selection color
	 */
	public void setCurrentSelectionColor(Color currentSelectionColor) {
		this.currentSelectionColor = currentSelectionColor;
	}
}
