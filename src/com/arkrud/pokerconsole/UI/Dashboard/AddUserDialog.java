package com.arkrud.pokerconsole.UI.Dashboard;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.jasypt.properties.PropertyValueEncryptionUtils;

import com.arkrud.Shareware.SpringUtilities;
import com.arkrud.pokerconsole.Util.INIFilesFactory;
import com.arkrud.pokerconsole.Util.UtilMethodsFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class AddUserDialog.
 */
public class AddUserDialog extends JDialog implements ActionListener {
	
	/** The Constant serialVersionUID. */
	static final long serialVersionUID = 1L;
	
	/** The cancel button. */
	private JButton accountManageButton, cancelButton;
	
	/** The console login account panel. */
	private JPanel consoleLoginAccountPanel;
	
	/** The password label. */
	private JLabel userLabel, passwordLabel;
	
	/** The password text field. */
	private final JTextField userTextField, passwordTextField;
	
	/** The add user. */
	private JMenuItem addUser;

	/**
	 * Instantiates a new adds the user dialog.
	 *
	 * @param addUser the add user
	 */
	public AddUserDialog(JMenuItem addUser) {
		this.addUser = addUser;
		setModal(true);
		userLabel = new JLabel();
		userLabel.setText("User:");
		userTextField = new JTextField(6);
		userTextField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				passwordTextField.requestFocusInWindow();
			}
		});
		passwordLabel = new JLabel();
		passwordLabel.setText("Password:");
		passwordTextField = new JPasswordField(15);
		accountManageButton = new JButton();
		if (INIFilesFactory.readINI(UtilMethodsFactory.getConsoleConfig()).hasSection("Security")) {
			accountManageButton.setText("Edit");
			userTextField.setText(INIFilesFactory.getItemValueFromINI(UtilMethodsFactory.getConsoleConfig(), "Security", "user"));
			passwordTextField.setText(INIFilesFactory.getItemValueFromINI(UtilMethodsFactory.getConsoleConfig(), "Security", "password"));
		} else {
			accountManageButton.setText("Add ");
		}
		cancelButton = new JButton("Cancel");
		consoleLoginAccountPanel = new JPanel(new SpringLayout());
		consoleLoginAccountPanel.add(userLabel);
		consoleLoginAccountPanel.add(userTextField);
		consoleLoginAccountPanel.add(passwordLabel);
		consoleLoginAccountPanel.add(passwordTextField);
		consoleLoginAccountPanel.add(cancelButton);
		consoleLoginAccountPanel.add(accountManageButton);
		accountManageButton.addActionListener(this);
		cancelButton.addActionListener(this);
		add(consoleLoginAccountPanel, BorderLayout.CENTER);
		setTitle("Manage AWS Account");
		SpringUtilities.makeCompactGrid(consoleLoginAccountPanel, 3, 2, 10, 10, 10, 10);
		passwordTextField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				accountManageButton.requestFocusInWindow();
				accountManageButton.doClick();
			}
		});
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		JButton theButton = (JButton) ae.getSource();
		String user = userTextField.getText();
		String password = passwordTextField.getText();
		if (theButton.getText().equals("Add ")) {
			HashMap<String, String> credentials = new HashMap<String, String>();
			credentials.put("user", user);
			credentials.put("password", PropertyValueEncryptionUtils.encrypt(password, UtilMethodsFactory.getEncryptor()));
			INIFilesFactory.addINIFileSection(UtilMethodsFactory.getConsoleConfig(), "Security", credentials);
			if (addUser != null) {
			addUser.setText("Update User");
			} else {
				INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Security", user, "user");
				INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Security", PropertyValueEncryptionUtils.encrypt(password, UtilMethodsFactory.getEncryptor()), "password");
			}
			this.dispose();
		} else if (theButton.getText().equals("Edit")) {
			INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Security", user, "user");
			INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Security", PropertyValueEncryptionUtils.encrypt(password, UtilMethodsFactory.getEncryptor()), "password");
			this.dispose();
		} else {
			this.dispose();
		}
	}
}
