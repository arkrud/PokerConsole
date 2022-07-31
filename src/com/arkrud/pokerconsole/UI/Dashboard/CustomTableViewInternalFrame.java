package com.arkrud.pokerconsole.UI.Dashboard;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import com.arkrud.pokerconsole.UI.TableChartPanel;
import com.arkrud.pokerconsole.UI.ImageChartPanel;
import com.arkrud.pokerconsole.UI.scrollabledesktop.BaseInternalFrame;

// TODO: Auto-generated Javadoc
/**
 * The Class CustomTableViewInternalFrame.
 */
public class CustomTableViewInternalFrame extends BaseInternalFrame implements InternalFrameListener {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The int frame scroll pane. */
	private JScrollPane intFrameScrollPane = null;

	/**
	 * Instantiates a new custom table view internal frame.
	 *
	 * @param title the title
	 * @param contents the contents
	 */
	public CustomTableViewInternalFrame(String title, Object contents) {
		super();
		setResizable(true);
		setClosable(true);
		setMaximizable(true);
		setIconifiable(true);
		setTitle(title);
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());
		if (contents instanceof TableChartPanel) {
			TableChartPanel chartPanel = (TableChartPanel) contents;
			chartPanel.setTheTableViewInternalFrame(this);
			contentPanel.add(getPane(title, chartPanel));
			setSize(540, 550);
		} else if (contents instanceof ImageChartPanel) {
			ImageChartPanel imageChartPanel = (ImageChartPanel) contents;
			imageChartPanel.setTheTableViewInternalFrame(this);
			contentPanel.add(getPane(title, imageChartPanel));
			setSize(540, 550);
		}
		setContentPane(contentPanel);
		setVisible(true);
		addInternalFrameListener(this);
	}

	/**
	 * Gets the pane.
	 *
	 * @param elbSpec the elb spec
	 * @param pane the pane
	 * @return the pane
	 */
	// Generating tables to set into Internal frame scrollable area
	private JScrollPane getPane(String elbSpec, Component pane) {
		intFrameScrollPane = new JScrollPane();
		intFrameScrollPane.setName(elbSpec);
		intFrameScrollPane.setViewportView(pane);
		return intFrameScrollPane;
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.InternalFrameListener#internalFrameActivated(javax.swing.event.InternalFrameEvent)
	 */
	@Override
	public void internalFrameActivated(InternalFrameEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.InternalFrameListener#internalFrameClosed(javax.swing.event.InternalFrameEvent)
	 */
	@Override
	public void internalFrameClosed(InternalFrameEvent arg0) {
		BaseInternalFrame thisFrame = (BaseInternalFrame) arg0.getSource();
		Dashboard.INTERNAL_FRAMES.remove(thisFrame.getTitle());
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.InternalFrameListener#internalFrameClosing(javax.swing.event.InternalFrameEvent)
	 */
	@Override
	public void internalFrameClosing(InternalFrameEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.InternalFrameListener#internalFrameDeactivated(javax.swing.event.InternalFrameEvent)
	 */
	@Override
	public void internalFrameDeactivated(InternalFrameEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.InternalFrameListener#internalFrameDeiconified(javax.swing.event.InternalFrameEvent)
	 */
	@Override
	public void internalFrameDeiconified(InternalFrameEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.InternalFrameListener#internalFrameIconified(javax.swing.event.InternalFrameEvent)
	 */
	@Override
	public void internalFrameIconified(InternalFrameEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.InternalFrameListener#internalFrameOpened(javax.swing.event.InternalFrameEvent)
	 */
	@Override
	public void internalFrameOpened(InternalFrameEvent arg0) {
	}
}
