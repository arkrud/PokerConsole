/**
 * 
 */
package com.arkrud.pokerconsole.UI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import com.arkrud.Shareware.SpringUtilities;
import com.arkrud.pokerconsole.Util.INIFilesFactory;
import com.arkrud.pokerconsole.Util.UtilMethodsFactory;

/**
 * @author arkrud
 *
 */
public class ManageTreesDialog extends JDialog implements ActionListener {
	private JButton applyButton, cancelButton;
	private JPanel manageTreesPanel;
	private JLabel treeLabel, treeStateLabel;
	private JTextField treeTextField;
	private JCheckBox treeStateCheckBox;
	private HashMap<JTextField, JCheckBox> feldsMap= new HashMap<JTextField, JCheckBox>();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public ManageTreesDialog() {
		setModal(true);
		manageTreesPanel = new JPanel(new SpringLayout());
		treeLabel = new JLabel();
		treeLabel.setText("Application:");
		treeStateLabel = new JLabel();
		treeStateLabel.setText("Visible");
		manageTreesPanel.add(treeLabel);
		manageTreesPanel.add(treeStateLabel);
		Iterator<Map.Entry<String, Boolean>> itr = INIFilesFactory.getTreesData().entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry<String, Boolean> entry = itr.next();
			treeTextField = new JTextField(entry.getKey());
			treeTextField.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					treeStateCheckBox.requestFocusInWindow();
				}
				
			});
			treeStateCheckBox = new JCheckBox();
			treeStateCheckBox.setSelected(entry.getValue());
			treeStateCheckBox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					applyButton.requestFocusInWindow();
				}
			});
			feldsMap.put(treeTextField, treeStateCheckBox);
			manageTreesPanel.add(treeTextField);
			manageTreesPanel.add(treeStateCheckBox);
		}
		applyButton = new JButton("Apply");
		cancelButton = new JButton("Cancel");
		applyButton.addActionListener(this);
		cancelButton.addActionListener(this);
		manageTreesPanel.add(applyButton);
		manageTreesPanel.add(cancelButton);
		add(manageTreesPanel, BorderLayout.CENTER);
		setTitle("Add Application Tree Account");
		SpringUtilities.makeCompactGrid(manageTreesPanel, INIFilesFactory.getTreesData().size() + 2, 2, 10, 10, 10, 10);
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		JButton theButton = (JButton) ae.getSource();
		String[] itemValues = {"true", "false"};
		if (theButton.getText().equals("Apply")) {
			Iterator<Map.Entry<JTextField, JCheckBox>> itr = feldsMap.entrySet().iterator();
			while (itr.hasNext()) {
				Map.Entry<JTextField, JCheckBox> entry = itr.next();
				System.out.println(((JTextField)entry.getKey()).getText() + " - " + ((JCheckBox)entry.getValue()).isSelected());
				INIFilesFactory.removeINIItemsWithValues(UtilMethodsFactory.getConsoleConfig(), "Applications", itemValues);
			}
			while (itr.hasNext()) {
				Map.Entry<JTextField, JCheckBox> entry = itr.next();
				INIFilesFactory.addINIFileItemToSection(UtilMethodsFactory.getConsoleConfig(), "Applications", ((JTextField)entry.getKey()).getText(), String.valueOf(((JCheckBox)entry.getValue()).isSelected()));
			}
			
			this.dispose();
		} else {
			this.dispose();
		}
	}
}
