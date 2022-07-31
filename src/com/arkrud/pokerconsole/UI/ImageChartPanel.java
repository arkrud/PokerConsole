package com.arkrud.pokerconsole.UI;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.arkrud.pokerconsole.UI.Dashboard.CustomTableViewInternalFrame;
import com.arkrud.pokerconsole.Util.UtilMethodsFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class ImageChartPanel.
 */
public class ImageChartPanel extends JPanel {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The image. */
	private BufferedImage image;
	
	/** The table view internal frame. */
	private CustomTableViewInternalFrame theTableViewInternalFrame;

	/**
	 * Instantiates a new image chart panel.
	 *
	 * @param imagePath the image path
	 */
	public ImageChartPanel(String imagePath) {
		super();
		try {
			String binPath = UtilMethodsFactory.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			imagePath = imagePath.split("\\.")[0] + ".png";
			String configPath = binPath.substring(0, binPath.indexOf(binPath.split("/")[binPath.split("/").length - 1])) + imagePath;
			image = ImageIO.read(new File(configPath));
		} catch (IOException ex) {
			// handle exception...
		}
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, this); // see javadoc for more info on the parameters
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
}
