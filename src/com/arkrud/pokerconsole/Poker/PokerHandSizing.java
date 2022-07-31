package com.arkrud.pokerconsole.Poker;

// TODO: Auto-generated Javadoc
/**
 * The Class PokerHandSizing.
 */
public class PokerHandSizing implements TreeNodeState {
	
	/** The node text. */
	private String nodeText;
	
	/** The poker action. */
	private PokerAction pokerAction;
	
	/** The selected. */
	private boolean selected;

	/**
	 * Instantiates a new poker hand sizing.
	 *
	 * @param nodeText the node text
	 * @param pokerAction the poker action
	 */
	public PokerHandSizing(String nodeText, PokerAction pokerAction) {
		super();
		this.nodeText = nodeText;
		this.pokerAction = pokerAction;
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

	/**
	 * Gets the poker action.
	 *
	 * @return the poker action
	 */
	public PokerAction getPokerAction() {
		return pokerAction;
	}

	/**
	 * Sets the poker action.
	 *
	 * @param pokerAction the new poker action
	 */
	public void setPokerAction(PokerAction pokerAction) {
		this.pokerAction = pokerAction;
	}




}
