package com.arkrud.pokerconsole.Poker;

// TODO: Auto-generated Javadoc
/**
 * The Interface TreeNodeState.
 */
public interface TreeNodeState {
	
	/**
	 * Gets the node text.
	 *
	 * @return the node text
	 */
	String getNodeText();

	/**
	 * Gets the node screen name.
	 *
	 * @return the node screen name
	 */
	String getNodeScreenName();

	/**
	 * Checks if is selected.
	 *
	 * @return true, if is selected
	 */
	boolean isSelected();

	/**
	 * Sets the selected.
	 *
	 * @param selected the new selected
	 */
	void setSelected(boolean selected);

	/**
	 * Sets the chart pane title.
	 *
	 * @param title the new chart pane title
	 */
	void setChartPaneTitle(String title);

	/**
	 * Gets the chart pane title.
	 *
	 * @return the chart pane title
	 */
	String getChartPaneTitle();

	/**
	 * Sets the chart image path.
	 *
	 * @param path the new chart image path
	 */
	void setChartImagePath(String path);

	/**
	 * Gets the chart image path.
	 *
	 * @return the chart image path
	 */
	String getChartImagePath();
}