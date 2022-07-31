package com.arkrud.pokerconsole.Poker;

// TODO: Auto-generated Javadoc
/**
 * The Class PokerPosition.
 */
public class PokerPosition implements TreeNodeState {

	/** The node text. */
	private String nodeText;
	
	/** The chart pane title. */
	private String chartPaneTitle;
	
	/** The chart image path. */
	private String chartImagePath;
	
	/** The selected. */
	private boolean selected;

	/**
	 * Instantiates a new poker position.
	 *
	 * @param nodeText the node text
	 */
	public PokerPosition(String nodeText) {
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
}
