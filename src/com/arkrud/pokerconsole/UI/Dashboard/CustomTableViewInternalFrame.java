package com.arkrud.pokerconsole.UI.Dashboard;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import com.arkrud.pokerconsole.UI.ChartPanel;
//import com.arkrud.TableInterface.CustomTable;
//import com.arkrud.UI.PropertiesTabbedPane;
//import com.arkrud.UI.PropertyPanel;
//import com.arkrud.TableInterface.CustomTable;
//import com.arkrud.UI.PrepareCWDashboardPanel;
//import com.arkrud.UI.PropertiesTabbedPane;
//import com.arkrud.UI.CF.CFCreateStackFrame;
//import com.arkrud.UI.ELB.ELBAddPanel;
//import com.ggl.filebrowser.view.FileBrowserMainPanel;
import com.arkrud.pokerconsole.UI.scrollabledesktop.BaseInternalFrame;

public class CustomTableViewInternalFrame extends BaseInternalFrame implements InternalFrameListener {
	private static final long serialVersionUID = 1L;
	private JScrollPane intFrameScrollPane = null;

	public CustomTableViewInternalFrame(String title, Object contents) {
		super();
		setResizable(true);
		setClosable(true);
		setMaximizable(true);
		setIconifiable(true);
		setTitle(title);
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());
		if (contents instanceof ChartPanel) {
			ChartPanel chartPanel = (ChartPanel) contents;
			chartPanel.setTheTableViewInternalFrame(this);
			contentPanel.add(getPane(title, chartPanel));
			setSize(520, 530);
		}
		setContentPane(contentPanel);
		setVisible(true);
		addInternalFrameListener(this);
	}

	// Generating tables to set into Internal frame scrollable area
	private JScrollPane getPane(String elbSpec, Component pane) {
		intFrameScrollPane = new JScrollPane();
		intFrameScrollPane.setName(elbSpec);
		intFrameScrollPane.setViewportView(pane);
		return intFrameScrollPane;
	}

	@Override
	public void internalFrameActivated(InternalFrameEvent arg0) {
	}

	@Override
	public void internalFrameClosed(InternalFrameEvent arg0) {
		BaseInternalFrame thisFrame = (BaseInternalFrame) arg0.getSource();
		Dashboard.INTERNAL_FRAMES.remove(thisFrame.getTitle());
	}

	@Override
	public void internalFrameClosing(InternalFrameEvent arg0) {
	}

	@Override
	public void internalFrameDeactivated(InternalFrameEvent arg0) {
	}

	@Override
	public void internalFrameDeiconified(InternalFrameEvent arg0) {
	}

	@Override
	public void internalFrameIconified(InternalFrameEvent arg0) {
	}

	@Override
	public void internalFrameOpened(InternalFrameEvent arg0) {
	}
}
