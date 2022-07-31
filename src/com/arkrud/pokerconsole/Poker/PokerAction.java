package com.arkrud.pokerconsole.Poker;

import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class PokerAction.
 */
public class PokerAction implements TreeNodeState {
	
	/** The node text. */
	private String nodeText;
	
	/** The sizing. */
	private boolean sizing;
	
	/** The selected. */
	private boolean selected;
	
	/** The sizings. */
	private ArrayList<PokerHandSizing> sizings;

	/**
	 * Instantiates a new poker action.
	 *
	 * @param nodeText the node text
	 */
	public PokerAction(String nodeText) {
		super();
		this.nodeText = nodeText;
	}

	/* (non-Javadoc)
	 * @see com.arkrud.pokerconsole.Poker.TreeNodeState#getNodeText()
	 */
	@Override
	public String getNodeText() {
		return nodeText;
	}

	/* (non-Javadoc)
	 * @see com.arkrud.pokerconsole.Poker.TreeNodeState#getNodeScreenName()
	 */
	@Override
	public String getNodeScreenName() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.arkrud.pokerconsole.Poker.TreeNodeState#isSelected()
	 */
	@Override
	public boolean isSelected() {
		return selected;
	}

	/* (non-Javadoc)
	 * @see com.arkrud.pokerconsole.Poker.TreeNodeState#setSelected(boolean)
	 */
	@Override
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * Checks if is sizing.
	 *
	 * @return true, if is sizing
	 */
	public boolean isSizing() {
		return sizing;
	}

	/**
	 * Sets the sizing.
	 *
	 * @param sizing the new sizing
	 */
	public void setSizing(boolean sizing) {
		this.sizing = sizing;
	}

	/**
	 * Adds the sizing.
	 *
	 * @param theSizing the the sizing
	 */
	public void addSizing (PokerHandSizing theSizing) {
		sizings.add(theSizing);
	}

	/**
	 * Gets the sizings.
	 *
	 * @return the sizings
	 */
	public ArrayList<PokerHandSizing> getSizings() {
		return sizings;
	}

	/* (non-Javadoc)
	 * @see com.arkrud.pokerconsole.Poker.TreeNodeState#setChartPaneTitle(java.lang.String)
	 */
	@Override
	public void setChartPaneTitle(String title) {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.arkrud.pokerconsole.Poker.TreeNodeState#getChartPaneTitle()
	 */
	@Override
	public String getChartPaneTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.arkrud.pokerconsole.Poker.TreeNodeState#setChartImagePath(java.lang.String)
	 */
	@Override
	public void setChartImagePath(String path) {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.arkrud.pokerconsole.Poker.TreeNodeState#getChartImagePath()
	 */
	@Override
	public String getChartImagePath() {
		// TODO Auto-generated method stub
		return null;
	}
}
