package com.arkrud.pokerconsole.UI.Dashboard;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

import org.jasypt.properties.PropertyValueEncryptionUtils;

import com.arkrud.Shareware.SpringUtilities;
import com.arkrud.pokerconsole.UI.Dashboard.Dashboard;
import com.arkrud.pokerconsole.Util.INIFilesFactory;
import com.arkrud.pokerconsole.Util.UtilMethodsFactory;
import com.arkrud.pokerconsole.licensing.LicenseJDialog;
import com.arkrud.pokerconsole.licensing.ProductLicense;
import com.license4j.License;
import com.license4j.LicenseValidator;

/**
 * Main Class For Java based Fat client for AWS Cloud.<br>
 * Opens login screen if password protected user is added. <br>
 * Opens the dashboard with top tree element collapsed.
 */
@SuppressWarnings("serial")
public class LoginFrame extends JFrame implements ActionListener { // NO_UCD (unused code)
	private JButton submitButton, cancelButton;
	private JPanel loginDialogPanel, imagePanel, overPanel;
	private JLabel userNameLabel, passwordLabel, iconLabel;
	private JTextField userNameTextField, passwordTextField;
	// ProductLicense class
    private static ProductLicense productLicense;
    // License object
    private static License license;

	/**
	 * Sole constructor of <code>LoginFrame</code> object.
	 *
	 */
	public LoginFrame() {
		// AWS image panel
		ImageIcon icon = UtilMethodsFactory.createImageIcon("images/aws-big.jpg");
		iconLabel = new JLabel();
		iconLabel.setIcon(icon);
		imagePanel = new JPanel();
		imagePanel.add(iconLabel);
		// Login credentials and controls panel
		loginDialogPanel = new JPanel(new SpringLayout());
		userNameLabel = new JLabel();
		userNameLabel.setText("Username:");
		userNameTextField = new JTextField(15);
		userNameTextField.setMaximumSize(userNameTextField.getMinimumSize());
		userNameTextField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				passwordTextField.requestFocusInWindow();
			}
		});
		passwordLabel = new JLabel();
		passwordLabel.setText("Password:");
		passwordTextField = new JPasswordField(15);
		passwordTextField.setMaximumSize(passwordTextField.getMinimumSize());
		loginDialogPanel.add(userNameLabel);
		loginDialogPanel.add(userNameTextField);
		loginDialogPanel.add(passwordLabel);
		loginDialogPanel.add(passwordTextField);
		submitButton = new JButton("Login");
		cancelButton = new JButton("Cancel");
		submitButton.addActionListener(this);
		cancelButton.addActionListener(this);
		loginDialogPanel.add(submitButton);
		loginDialogPanel.add(cancelButton);
		SpringUtilities.makeCompactGrid(loginDialogPanel, 3, 2, 10, 10, 10, 10);
		passwordTextField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				submitButton.requestFocusInWindow();
				submitButton.doClick();
			}
		});
		// Login Window panel
		overPanel = new JPanel(new SpringLayout());
		overPanel.add(loginDialogPanel);
		overPanel.add(imagePanel);
		SpringUtilities.makeCompactGrid(overPanel, 1, 2, 10, 10, 10, 10);
		// Add Login Window panel to Login Window
		setTitle("Login To AWS Console");
		add(overPanel, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		JButton theButton = (JButton) ae.getSource();
		if (theButton.getText().equals("Login")) {
			String value1 = userNameTextField.getText();
			String value2 = passwordTextField.getText();
			if (INIFilesFactory.readINI(UtilMethodsFactory.getConsoleConfig()).hasSection("Security")) {
				String decryptedPassword = PropertyValueEncryptionUtils.decrypt(INIFilesFactory.getItemValueFromINI(UtilMethodsFactory.getConsoleConfig(), "Security", "password"), UtilMethodsFactory.getEncryptor());
				if (value1.contains(INIFilesFactory.getItemValueFromINI(UtilMethodsFactory.getConsoleConfig(), "Security", "user")) && value2.contains(decryptedPassword)) {
					showDashboard();
					this.dispose();
				} else {
					JOptionPane.showMessageDialog(this, "Incorrect login or password", "Error", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				showDashboard();
				this.dispose();
			}
		} else {
			this.dispose();
		}
	}

	/**
	 * Show Dashboard on the screen. <br>
	 *
	 */
	private static void showDashboard() {
		Dashboard dash = null;
		try {
			dash = new Dashboard(Boolean.parseBoolean(INIFilesFactory.getItemValueFromINI(UtilMethodsFactory.getConsoleConfig(), "Config", "editable")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		dash.setVisible(true);
	}

	/**
	 * Main method to launch application.
	 *
	 */
	public static void main(String[] args) throws Exception {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			// Show login prompt if security enabled or open Dashboard directly
			 // load settings and/or license from config file.
	        /*productLicense = new ProductLicense();
	        if (productLicense.loadLicense()) {
	            license = productLicense.validateLicense(false);
	        }

	        if (license == null) {
	            // license object is null, either there is no config file, or config
	            // file does not include a license, so display licensing window.
	        	 new LicenseJDialog(null, true).setVisible(true);
	        } else {
	            // license found
	            switch (license.getValidationStatus()) {
	                case LICENSE_VALID:
	                    // license is valid, so continue running your software

	                    if (license.isActivationRequired()) {
	                        // if it requires activation, but not activated yet, display activation days remaining.
	                        JOptionPane.showMessageDialog(null, "Activation required, days left: " + license.getLicenseActivationDaysRemaining(null), "Activation Requires", JOptionPane.ERROR_MESSAGE);
	                    }

	                    if (license.getLicenseText() != null && license.getLicenseText().getLicenseExpireDaysRemaining(null) > 0 && license.getLicenseText().getLicenseExpireDaysRemaining(null) < 30) {
	                        // expiration date is set, and less than 30 days remaining, SO if you like display a message?
	                        JOptionPane.showMessageDialog(null, "License will expire soon, days left: " + license.getLicenseText().getLicenseExpireDaysRemaining(null), "License Expiration", JOptionPane.ERROR_MESSAGE);
	                    }

	                    // Here check for license availability (blacklist). Method checks for license on server, 
	                    // also it checks for activated licenses. If it returns -1 you can be sure that given
	                    // license is deleted from server, so notify customer and close software because of illegal
	                    // license usage.
	                    // This check runs in a thread so it will not block software.
	                    SwingWorker worker = new SwingWorker() {
	                        int blacklistCheck;

	                        @Override
	                        protected void done() {
	                            if (blacklistCheck == -1) {
	                                System.err.println(blacklistCheck);
	                                JOptionPane.showMessageDialog(null, "This is a blacklisted license. You are using an illegal license.\n\nSoftware will be closed.", "License Error", JOptionPane.ERROR_MESSAGE);
	                                System.exit(-1);
	                            }
	                        }

	                        @Override
	                        protected License doInBackground() {
	                            blacklistCheck = LicenseValidator.checkOnlineAvailability(productLicense.publicKey, license, 3000);

	                            return null;
	                        }
	                    };
	                    worker.execute();

	                    break;
	                default:
	                    // ValidationStatus is not LICENSE_VALID, display a message dialog and display licensing window.

	                    // YOU CAN CHECK FOR OTHER VALIDATION STATUS HERE LIKE EXPIRED, USAGE LIMIT REACHED ETC,
	                    // THEN MAKE ANY OTHER THINGS.
	                    JOptionPane.showMessageDialog(null, "License error: " + license.getValidationStatus(), "License Error", JOptionPane.ERROR_MESSAGE);

	                    new LicenseJDialog(null, true).setVisible(true);
	            }
	        }
	        
	        
	        java.awt.EventQueue.invokeLater(new Runnable() {
	            @Override
	            public void run() {*/
	            	if (INIFilesFactory.hasINIFileSection(UtilMethodsFactory.getConsoleConfig(), "Security")) { // Check in INI file if secure login is enabled 
	    				LoginFrame frame = new LoginFrame();
	    				frame.setSize(400, 150);
	    				frame.setResizable(false);
	    				// Set login prompt window in the center of the desktop
	    				Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    				int w = frame.getSize().width;
	    				int h = frame.getSize().height;
	    				int x = (dim.width - w) / 2;
	    				int y = (dim.height - h) / 2;
	    				frame.setLocation(x, y);
	    				frame.setVisible(true);
	    			} else {
	    				showDashboard();
	    			}
	            //}
	        //});
	        
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Something Went Wrong!!!");
		}
	}
}
