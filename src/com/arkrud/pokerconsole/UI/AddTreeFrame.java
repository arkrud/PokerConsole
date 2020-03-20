package com.arkrud.pokerconsole.UI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.tree.DefaultMutableTreeNode;

import com.arkrud.Shareware.SpringUtilities;
import com.arkrud.pokerconsole.TreeInterface.CustomTree;
import com.arkrud.pokerconsole.UI.Dashboard.Dashboard;
import com.arkrud.pokerconsole.Util.INIFilesFactory;
import com.arkrud.pokerconsole.Util.UtilMethodsFactory;

public class AddTreeFrame extends JDialog implements ActionListener {
	static final long serialVersionUID = 1L;
	private JButton addButton, cancelButton;
	private JPanel addAPPTreePanel;
	private JLabel appLabel, treeStateLabel;
	private final JTextField appTreeTextField;
	private JCheckBox treeStateCheckBox;
	private Dashboard dash;

	public AddTreeFrame(Dashboard dash) {
		this.dash = dash;
		setModal(true);
		appLabel = new JLabel();
		appLabel.setText("Solution:");
		treeStateLabel = new JLabel();
		treeStateLabel.setText("Visible");
		appTreeTextField = new JTextField(15);
		appTreeTextField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				treeStateCheckBox.requestFocusInWindow();
			}
		});
		treeStateCheckBox = new JCheckBox();
		treeStateCheckBox.setSelected(true);
		treeStateCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addButton.requestFocusInWindow();
			}
		});
		addButton = new JButton("Add");
		cancelButton = new JButton("Cancel");
		addAPPTreePanel = new JPanel(new SpringLayout());
		addAPPTreePanel.add(appLabel);
		addAPPTreePanel.add(treeStateLabel);
		addAPPTreePanel.add(appTreeTextField);
		addAPPTreePanel.add(treeStateCheckBox);
		addAPPTreePanel.add(addButton);
		add(addAPPTreePanel, BorderLayout.CENTER);
		addButton.addActionListener(this);
		addAPPTreePanel.add(cancelButton);
		cancelButton.addActionListener(this);
		setTitle("Add Application Tree Account");
		SpringUtilities.makeCompactGrid(addAPPTreePanel, 3, 2, 10, 10, 10, 10);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		JButton theButton = (JButton) ae.getSource();
		if (theButton.getText().equals("Add")) {
			String solution = appTreeTextField.getText();
			Boolean visibility = treeStateCheckBox.isSelected();
			INIFilesFactory.addINIFileItemToSection(UtilMethodsFactory.getConsoleConfig(), "Solutions", solution, visibility);
			INIFilesFactory.addINIFileItemToSection(UtilMethodsFactory.getConsoleConfig(), "Autonaming", solution, "true");
			INIFilesFactory.addINIFileItemToSection(UtilMethodsFactory.getConsoleConfig(), "Selections", solution, solution);
			File sizingDir = new File(UtilMethodsFactory.getConfigPath() + "Images/" + solution);
			UtilMethodsFactory.createFolder(sizingDir);
			dash.addTreeTabPaneTab(solution);
			dash.getTreeTabbedPane().setSelectedIndex(dash.getTreeTabbedPane().indexOfTab(solution));
			JScrollPane scroll = (JScrollPane) (dash.getTreeTabbedPane().getSelectedComponent());
			CustomTree tree = (CustomTree) scroll.getViewport().getView();
			tree.setSelection((DefaultMutableTreeNode) tree.getTreeModel().getRoot(),tree.getTheTree(), true);
			this.dispose();
		} else {
			this.dispose();
		}
	}
}
