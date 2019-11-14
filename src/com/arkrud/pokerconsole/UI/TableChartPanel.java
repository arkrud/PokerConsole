package com.arkrud.pokerconsole.UI;

import java.awt.Color;
import java.util.HashMap;

import javax.swing.JPanel;

import com.arkrud.pokerconsole.UI.Dashboard.CustomTableViewInternalFrame;
import com.arkrud.pokerconsole.UI.Dashboard.Dashboard;
import com.arkrud.pokerconsole.pokercardchart.CustomTable;

public class TableChartPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CustomTableViewInternalFrame theTableViewInternalFrame;
	private HashMap<String, HashMap<String, String>> iniData = new HashMap<String, HashMap<String, String>>();
	private CustomTable table;

	public TableChartPanel(String imagePath, boolean editable, Dashboard dash) {
		super();
		CustomTable table = new CustomTable(imagePath, this, editable, dash);
		this.table = table;
		add(table);
		setBackground(Color.WHITE);
	}

	public HashMap<String, HashMap<String, String>> getIniData() {
		return iniData;
	}

	public void setIniData(HashMap<String, HashMap<String, String>> iniData) {
		this.iniData = iniData;
	}

	public CustomTableViewInternalFrame getTheTableViewInternalFrame() {
		return theTableViewInternalFrame;
	}

	public void setTheTableViewInternalFrame(CustomTableViewInternalFrame theTableViewInternalFrame) {
		this.theTableViewInternalFrame = theTableViewInternalFrame;
	}

	public CustomTable getTable() {
		return table;
	}

	public void setTable(CustomTable table) {
		this.table = table;
	}
	
	
}
