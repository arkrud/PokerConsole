package com.arkrud.pokerconsole.UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.arkrud.pokerconsole.UI.Dashboard.CustomTableViewInternalFrame;
import com.arkrud.pokerconsole.Util.UtilMethodsFactory;
import com.arkrud.pokerconsole.pokercardchart.CustomTable;

public class ChartPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CustomTableViewInternalFrame theTableViewInternalFrame;

	public ChartPanel(String imagePath) {
		super();
		CustomTable table = new CustomTable(imagePath);
		add(table);
		setBackground(Color.WHITE);
	}

	public CustomTableViewInternalFrame getTheTableViewInternalFrame() {
		return theTableViewInternalFrame;
	}

	public void setTheTableViewInternalFrame(CustomTableViewInternalFrame theTableViewInternalFrame) {
		this.theTableViewInternalFrame = theTableViewInternalFrame;
	}
}
