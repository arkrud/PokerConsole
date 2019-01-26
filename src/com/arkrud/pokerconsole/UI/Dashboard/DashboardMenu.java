package com.arkrud.pokerconsole.UI.Dashboard;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.arkrud.pokerconsole.Util.INIFilesFactory;
import com.arkrud.pokerconsole.Util.UtilMethodsFactory;

public class DashboardMenu extends JMenu implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JMenuItem exit, addDashboardUser, clearUser, addTree, manageTrees;
	private Dashboard dash;

	public DashboardMenu(Dashboard dash) {
		super();
		this.dash = dash;
		setText("Edit");
		exit = new JMenuItem("Exit");
		addTree = new JMenuItem("Add Tree");
		manageTrees = new JMenuItem("Manage Trees");
		addDashboardUser = new JMenuItem();
		clearUser = new JMenuItem("Clear Security");
		if (INIFilesFactory.readINI(UtilMethodsFactory.getConsoleConfig()).hasSection("Security")) {
			addDashboardUser.setText("Update User");
		} else {
			addDashboardUser.setText("Add User");
		}
		if (INIFilesFactory.readINI(UtilMethodsFactory.getConsoleConfig()).hasSection("Security")) {
			clearUser.setEnabled(true);
		} else {
			clearUser.setEnabled(false);
		}
		exit.addActionListener(this);
		addTree.addActionListener(this);
		addDashboardUser.addActionListener(this);
		clearUser.addActionListener(this);
		manageTrees.addActionListener(this);
		add(addDashboardUser);
		add(clearUser);
		add(addTree);
		add(manageTrees);
		add(exit);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String menuText = ((JMenuItem) e.getSource()).getText();
		if (menuText.contains("Exit")) {
			UtilMethodsFactory.exitApp();
		} else if (menuText.contains("Add User")) {
			showConsoleLoginAccountFrame(addDashboardUser);
		} else if (menuText.contains("Add Tree")) {
			UtilMethodsFactory.showDialogToDesctop("AddTreesFrame", 250, 140, dash);
		} else if (menuText.contains("Manage Trees")) {
			UtilMethodsFactory.showDialogToDesctop("ManageTreesDialog", 250, 150 + 25 * INIFilesFactory.getTreesData().size(), null);
		} else if (menuText.contains("Update User")) {
			showConsoleLoginAccountFrame(addDashboardUser);
		} else if (menuText.contains("Clear Security")) {
			int response = JOptionPane.showConfirmDialog(null, "Do you want to disable security?", "Disable security", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (response == JOptionPane.NO_OPTION) {
			} else if (response == JOptionPane.YES_OPTION) {
				INIFilesFactory.removeINIFileSection(UtilMethodsFactory.getConsoleConfig(), "Security");
			} else if (response == JOptionPane.CLOSED_OPTION) {
			}
		}
	}

	private void showConsoleLoginAccountFrame(JMenuItem addUser) {
		ConsoleLoginAccountFrame consoleLoginAccountFrame = new ConsoleLoginAccountFrame(addUser);
		consoleLoginAccountFrame.setSize(350, 140);
		// Get the size of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		// Determine the new location of the window
		int w = consoleLoginAccountFrame.getSize().width;
		int h = consoleLoginAccountFrame.getSize().height;
		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;
		// Move the window
		consoleLoginAccountFrame.setLocation(x, y);
		consoleLoginAccountFrame.setVisible(true);
	}
	
}
