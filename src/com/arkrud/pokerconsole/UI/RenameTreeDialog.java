package com.arkrud.pokerconsole.UI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import com.arkrud.Shareware.SpringUtilities;
import com.arkrud.pokerconsole.UI.Dashboard.Dashboard;
import com.arkrud.pokerconsole.Util.INIFilesFactory;
import com.arkrud.pokerconsole.Util.UtilMethodsFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class RenameTreeDialog.
 */
public class RenameTreeDialog extends JDialog implements ActionListener {
	
	/** The Constant serialVersionUID. */
	static final long serialVersionUID = 1L;
	
	/** The cancel button. */
	private JButton renameButton, cancelButton;
	
	/** The buttons panel. */
	private JPanel renameTabPanel, tabNamePanel, buttonsPanel;
	
	/** The solution name. */
	private JLabel solutionName;
	
	/** The solution copy name. */
	private final JTextField solutionCopyName;
	
	/** The tabbed pane. */
	private JTabbedPane tabbedPane;
	
	/** The dash. */
	private Dashboard dash;

	/**
	 * Instantiates a new rename tree dialog.
	 *
	 * @param dash the dash
	 * @param tabbedPane the tabbed pane
	 */
	public RenameTreeDialog(Dashboard dash, JTabbedPane tabbedPane) {
		this.tabbedPane = tabbedPane;
		this.dash = dash;
		setModal(true);
		setTitle("Rename Solution Tab");
		String tabText = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
		solutionName = new JLabel(tabText.split("-")[0] + "-");
		solutionCopyName = new JTextField(15);
		solutionCopyName.setText(tabText.substring(tabText.indexOf("-") + 1, tabText.length()));
		renameButton = new JButton("Rename");
		cancelButton = new JButton("Cancel");
		renameButton.addActionListener(this);
		cancelButton.addActionListener(this);
		solutionCopyName.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				renameButton.requestFocusInWindow();
			}
		});
		tabNamePanel = new JPanel(new SpringLayout());
		tabNamePanel.add(solutionName);
		tabNamePanel.add(solutionCopyName);
		SpringUtilities.makeCompactGrid(tabNamePanel, 1, 2, 10, 10, 10, 10);
		buttonsPanel = new JPanel(new SpringLayout());
		buttonsPanel.add(renameButton);
		buttonsPanel.add(cancelButton);
		SpringUtilities.makeCompactGrid(buttonsPanel, 1, 2, 10, 10, 10, 10);
		renameTabPanel = new JPanel();
		renameTabPanel.add(tabNamePanel);
		renameTabPanel.add(buttonsPanel);
		add(renameTabPanel, BorderLayout.CENTER);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		JButton theButton = (JButton) ae.getSource();
		if (theButton.getText().equals("Rename")) {
			String tabName = solutionName.getText() + solutionCopyName.getText();
			String oldTreeName = dash.getTreeTabbedPane().getTitleAt(dash.getTreeTabbedPane().getSelectedIndex());
			String newSolutionCopyName = solutionName.getText() + solutionCopyName.getText();
			String oldItemValue = INIFilesFactory.getItemValueFromINI(UtilMethodsFactory.getConsoleConfig(), "Selections", oldTreeName);
			INIFilesFactory.updateINIFileItemName(UtilMethodsFactory.getConsoleConfig(), "Selections", newSolutionCopyName, oldTreeName);
			INIFilesFactory.updateINIFileItemName(UtilMethodsFactory.getConsoleConfig(), "Solutions", newSolutionCopyName, oldTreeName);
			INIFilesFactory.updateINIFileItemName(UtilMethodsFactory.getConsoleConfig(), "Autonaming", newSolutionCopyName, oldTreeName);
			INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Selections", oldItemValue, newSolutionCopyName);
			// INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Autonaming", "false", newSolutionCopyName);
			tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(), tabName);
			this.dispose();
		} else {
			this.dispose();
		}
	}
}
