package com.arkrud.pokerconsole.UI;

import java.awt.Color;
import java.util.HashMap;

import javax.swing.JPanel;

import com.arkrud.pokerconsole.UI.Dashboard.CustomTableViewInternalFrame;
import com.arkrud.pokerconsole.UI.Dashboard.Dashboard;
import com.arkrud.pokerconsole.pokercardchart.CustomTable;

public class ChartPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CustomTableViewInternalFrame theTableViewInternalFrame;
	private HashMap<String, HashMap<String, String>> iniData = new HashMap<String, HashMap<String, String>>();

	public ChartPanel(String imagePath) {
		super();
		CustomTable table = new CustomTable(imagePath,this);
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
}
