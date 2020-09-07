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
import javax.swing.UIManager;

import org.jasypt.properties.PropertyValueEncryptionUtils;

import com.arkrud.Shareware.SpringUtilities;
import com.arkrud.pokerconsole.Util.INIFilesFactory;
import com.arkrud.pokerconsole.Util.UtilMethodsFactory;
import com.arkrud.pokerconsole.licensing.LicenseKeyGUI;
import com.license4j.License;
import com.license4j.ValidationStatus;

/**
 * Main Class For Poker Console.<br>
 * Opens login screen if password protected user is added. <br>
 * Opens the dashboard with layout from previous session.
 */
@SuppressWarnings("serial")
public class LoginFrame extends JFrame implements ActionListener { // NO_UCD (unused code)
	private JButton submitButton, cancelButton;
	private JPanel loginDialogPanel, imagePanel, overPanel;
	private JLabel userNameLabel, passwordLabel, iconLabel;
	private JTextField userNameTextField, passwordTextField;
	private static LicenseKeyGUI licenseKeyGUI;

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
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowOpened(java.awt.event.WindowEvent evt) {
				formWindowOpened(evt);
			}
		});
		licenseKeyGUI = new LicenseKeyGUI(this, true);
	}

	public LicenseKeyGUI getLicenseKeyGUI() {
		return licenseKeyGUI;
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
				// showDashboard();
				UtilMethodsFactory.showDialogToDesctop("AddUser", 350, 140, null, null, null, null, null, null, null);
				// this.dispose();
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
			java.awt.EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					// Show login prompt if security enabled or open Dashboard directly
					// if (INIFilesFactory.hasINIFileSection(UtilMethodsFactory.getConsoleConfig(), "Security")) { // Check in INI file if secure login is enabled
					showLoginFrame();
					// } else {
					// showDashboard();
					// }
				}
			});
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Something Went Wrong!!!");
		}
	}

	private static void showLoginFrame() {
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
	}

	private void formWindowOpened(java.awt.event.WindowEvent evt) {// GEN-FIRST:event_formWindowOpened
		/**
		 * COPY THIS METHOD.
		 *
		 *
		 * We will check license here in "formWindowOpened" so that license window will be displayed after user will see main product window.
		 *
		 * Depending license status, we will display license window.
		 *
		 * If license on disk is not valid, display license window. ALSO it is good to disable some features or menu items like below; so that user will not be able to use product without a valid license. OR
		 * software may be directly closed with an error.
		 */
		License license = licenseKeyGUI.checkLicense();
		if (license != null) {
			if (license.getValidationStatus() == ValidationStatus.LICENSE_VALID) {
				/**
				 * License is valid, so run your software product.
				 */
				/**
				 * But If license require activation, check if license is activated. If license is not activated check the activation period. If allowed activation period is expired but user still did not complete
				 * activation, display license GUI for user to complete activation.
				 */
				if (license.isActivationRequired() && license.getLicenseActivationDaysRemaining(null) == 0) {
					JOptionPane.showMessageDialog(null, "Your license activation period is over, activate on the next window.", "License Activation", WIDTH);
					// This is an example, and we just disable main file menu.
					// filejMenu.setEnabled(false);
					licenseKeyGUI.setVisible(true);
				}
			} else {
				/**
				 * If license status is not valid, display message to display license status; and disable some software features etc.
				 */
				JOptionPane.showMessageDialog(null, "Your license is not valid (" + license.getValidationStatus() + ")", "License Error", WIDTH);
				// This is an example, and we just disable main file menu.
				// filejMenu.setEnabled(false);
				licenseKeyGUI.setVisible(true);
			}
		} else {
			JOptionPane.showMessageDialog(null, "You should have a valid license to run this software.", "License Error", JOptionPane.ERROR_MESSAGE);
			// This is an example, and we just disable main file menu.
			// filejMenu.setEnabled(false);
			licenseKeyGUI.setVisible(true);
		}
	}// GEN-LAST:event_formWindowOpened
}
