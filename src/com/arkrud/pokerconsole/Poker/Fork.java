package com.arkrud.pokerconsole.Poker;

// TODO: Auto-generated Javadoc
/**
 * The Class Fork.
 */
public class Fork implements TreeNodeState {
	
	/** The node text. */
	private String nodeText;
	
	/** The selected. */
	private boolean selected;
	
	/** The chart pane title. */
	private String chartPaneTitle;
	
	/**
	 * Instantiates a new fork.
	 *
	 * @param nodeText the node text
	 */
	public Fork(String nodeText) {
		super();
		this.nodeText = nodeText;
	}

	/* (non-Javadoc)
	 * @see com.arkrud.pokerconsole.Poker.TreeNodeState#getNodeText()
	 */
	@Override
	public String getNodeText() {
		// TODO Auto-generated method stub
		return nodeText;
	}

	/* (non-Javadoc)
	 * @see com.arkrud.pokerconsole.Poker.TreeNodeState#getNodeScreenName()
	 */
	@Override
	public String getNodeScreenName() {
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
