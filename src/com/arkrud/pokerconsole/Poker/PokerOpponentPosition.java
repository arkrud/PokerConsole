package com.arkrud.pokerconsole.Poker;

// TODO: Auto-generated Javadoc
/**
 * The Class PokerOpponentPosition.
 */
public class PokerOpponentPosition implements TreeNodeState {

	/** The node text. */
	private String nodeText;
	
	/** The chart pane title. */
	private String chartPaneTitle;
	
	/** The chart image path. */
	private String chartImagePath;
	
	/** The selected. */
	private boolean selected;
	
	/** The poker action. */
	private String pokerAction;
	
	/** The poker hand sizing. */
	private String pokerHandSizing;
	
	/** The poker position. */
	private String pokerPosition;

	/**
	 * Instantiates a new poker opponent position.
	 *
	 * @param nodeText the node text
	 */
	public PokerOpponentPosition(String nodeText) {
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

	/* (non-Javadoc)
	 * @see com.arkrud.pokerconsole.Poker.TreeNodeState#setChartPaneTitle(java.lang.String)
	 */
	@Override
	public void setChartPaneTitle(String chartPaneTitle) {
		this.chartPaneTitle = chartPaneTitle;
	}

	/* (non-Javadoc)
	 * @see com.arkrud.pokerconsole.Poker.TreeNodeState#getChartPaneTitle()
	 */
	@Override
	public String getChartPaneTitle() {
		return chartPaneTitle;
	}

	/* (non-Javadoc)
	 * @see com.arkrud.pokerconsole.Poker.TreeNodeState#setChartImagePath(java.lang.String)
	 */
	@Override
	public void setChartImagePath(String chartImagePath) {
		this.chartImagePath = chartImagePath;
	}

	/* (non-Javadoc)
	 * @see com.arkrud.pokerconsole.Poker.TreeNodeState#getChartImagePath()
	 */
	@Override
	public String getChartImagePath() {
		return chartImagePath;
	}

	/**
	 * Gets the poker action.
	 *
	 * @return the poker action
	 */
	public String getPokerAction() {
		return pokerAction;
	}

	/**
	 * Sets the poker action.
	 *
	 * @param pokerAction the new poker action
	 */
	public void setPokerAction(String pokerAction) {
		this.pokerAction = pokerAction;
	}

	/**
	 * Gets the poker hand sizing.
	 *
	 * @return the poker hand sizing
	 */
	public String getPokerHandSizing() {
		return pokerHandSizing;
	}

	/**
	 * Sets the poker hand sizing.
	 *
	 * @param pokerHandSizing the new poker hand sizing
	 */
	public void setPokerHandSizing(String pokerHandSizing) {
		this.pokerHandSizing = pokerHandSizing;
	}

	/**
	 * Gets the poker position.
	 *
	 * @return the poker position
	 */
	public String getPokerPosition() {
		return pokerPosition;
	}

	/**
	 * Sets the poker position.
	 *
	 * @param pokerPosition the new poker position
	 */
	public void setPokerPosition(String pokerPosition) {
		this.pokerPosition = pokerPosition;
	}

	/**
	 * Sets the node text.
	 *
	 * @param nodeText the new node text
	 */
	public void setNodeText(String nodeText) {
		this.nodeText = nodeText;
	}



}
