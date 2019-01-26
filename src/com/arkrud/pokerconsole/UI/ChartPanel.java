package com.arkrud.pokerconsole.UI;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.arkrud.pokerconsole.UI.Dashboard.CustomTableViewInternalFrame;
import com.arkrud.pokerconsole.Util.UtilMethodsFactory;

public class ChartPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage image;
	private CustomTableViewInternalFrame theTableViewInternalFrame;

	public ChartPanel(String imagePath) {
		super();
		try {
			String binPath = UtilMethodsFactory.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			String configPath = binPath.substring(0, binPath.indexOf(binPath.split("/")[binPath.split("/").length - 1]))  + imagePath;
			image = ImageIO.read(new File(configPath));
		} catch (IOException ex) {
			// handle exception...
		}
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, this); // see javadoc for more info on the parameters
	}

	public CustomTableViewInternalFrame getTheTableViewInternalFrame() {
		return theTableViewInternalFrame;
	}

	public void setTheTableViewInternalFrame(CustomTableViewInternalFrame theTableViewInternalFrame) {
		this.theTableViewInternalFrame = theTableViewInternalFrame;
	}
}
