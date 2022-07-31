package com.arkrud.pokerconsole.UI;

import java.awt.Color;
import java.util.HashMap;

import javax.swing.JPanel;

import com.arkrud.pokerconsole.UI.Dashboard.CustomTableViewInternalFrame;
import com.arkrud.pokerconsole.UI.Dashboard.Dashboard;
import com.arkrud.pokerconsole.pokercardchart.CustomTable;

// TODO: Auto-generated Javadoc
/**
 * The Class TableChartPanel.
 */
public class TableChartPanel extends JPanel {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The table view internal frame. */
	private CustomTableViewInternalFrame theTableViewInternalFrame;
	
	/** The ini data. */
	private HashMap<String, HashMap<String, String>> iniData = new HashMap<String, HashMap<String, String>>();
	
	/** The table. */
	private CustomTable table;

	/**
	 * Instantiates a new table chart panel.
	 *
	 * @param imagePath the image path
	 * @param editable the editable
	 * @param dash the dash
	 */
	public TableChartPanel(String imagePath, boolean editable, Dashboard dash) {
		super();
		CustomTable table = new CustomTable(imagePath, this, editable, dash);
		
		this.table = table;
		add(table);
		setBackground(Color.WHITE);
	}

	/**
	 * Gets the ini data.
	 *
	 * @return the ini data
	 */
	public HashMap<String, HashMap<String, String>> getIniData() {
		return iniData;
	}

	/**
	 * Sets the ini data.
	 *
	 * @param iniData the ini data
	 */
	public void setIniData(HashMap<String, HashMap<String, String>> iniData) {
		this.iniData = iniData;
	}

	/**
	 * Gets the the table view internal frame.
	 *
	 * @return the the table view internal frame
	 */
	public CustomTableViewInternalFrame getTheTableViewInternalFrame() {
		return theTableViewInternalFrame;
	}

	/**
	 * Sets the the table view internal frame.
	 *
	 * @param theTableViewInternalFrame the new the table view internal frame
	 */
	public void setTheTableViewInternalFrame(CustomTableViewInternalFrame theTableViewInternalFrame) {
		this.theTableViewInternalFrame = theTableViewInternalFrame;
	}

	/**
	 * Gets the table.
	 *
	 * @return the table
	 */
	public CustomTable getTable() {
		return table;
	}

	/**
	 * Sets the table.
	 *
	 * @param table the new table
	 */
	public void setTable(CustomTable table) {
		this.table = table;
	}
	
	
}
