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
import javax.swing.SpringLayout;

import com.arkrud.Shareware.SpringUtilities;
import com.arkrud.pokerconsole.UI.Dashboard.Dashboard;
import com.arkrud.pokerconsole.Util.INIFilesFactory;
import com.arkrud.pokerconsole.Util.UtilMethodsFactory;

/**
 * @author arkrud
 *
 */
public class ManageTreesDialog extends JDialog implements ActionListener {
	private Dashboard dash;
	private JButton applyButton, cancelButton;
	private JPanel manageTreesPanel;
	private JLabel treeLabel, treeStateLabel, treeNameLabel;
	private JCheckBox treeStateCheckBox;
	private HashMap<JLabel, JCheckBox> feldsMap = new HashMap<JLabel, JCheckBox>();
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	public ManageTreesDialog(Dashboard dash) {
		this.dash = dash;
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
			treeNameLabel = new JLabel(entry.getKey());
			treeStateCheckBox = new JCheckBox();
			treeStateCheckBox.setSelected(entry.getValue());
			treeStateCheckBox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					applyButton.requestFocusInWindow();
				}
			});
			feldsMap.put(treeNameLabel, treeStateCheckBox);
			manageTreesPanel.add(treeNameLabel);
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
		if (theButton.getText().equals("Apply")) {
			Iterator<Map.Entry<JLabel, JCheckBox>> itr = feldsMap.entrySet().iterator();
			while (itr.hasNext()) {
				Map.Entry<JLabel, JCheckBox> entry = itr.next();
				if (entry.getValue().isSelected()) {
					dash.addTreeTabPaneTab(entry.getKey().getText());
				} else {
					dash.removeTreeTabPaneTab(entry.getKey().getText());
				}
				INIFilesFactory.updateINIFileItem(UtilMethodsFactory.getConsoleConfig(), "Applications", String.valueOf(entry.getValue().isSelected()) , entry.getKey().getText());
			}
			this.dispose();
		} else {
			this.dispose();
		}
	}
}
