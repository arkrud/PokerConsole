package com.arkrud.pokerconsole.Poker;

// TODO: Auto-generated Javadoc
/**
 * The Class PokerStrategy.
 */
public class PokerStrategy implements TreeNodeState {
	
	/** The node text. */
	private String nodeText;
	
	/** The selected. */
	//private String strategyName;
	private boolean selected;

	/**
	 * Instantiates a new poker strategy.
	 *
	 * @param nodeText the node text
	 */
	public PokerStrategy(String nodeText) {
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
	 * Sets the node text.
	 *
	 * @param nodeText the new node text
	 */
	public void setNodeText(String nodeText) {
		this.nodeText = nodeText;
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
	 * Gets the strategy name.
	 *
	 * @return the strategy name
	 */
	public String getStrategyName() {
		return nodeText;
	}

	/*public void setStrategyName(String strategyName) {
		this.strategyName = nodeText;
	}*/
	
	
}
