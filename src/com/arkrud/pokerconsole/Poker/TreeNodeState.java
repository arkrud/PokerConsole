package com.arkrud.pokerconsole.Poker;

public interface TreeNodeState {
	String getNodeText();

	String getNodeScreenName();

	boolean isSelected();

	void setSelected(boolean selected);

	void setChartPaneTitle(String title);

	String getChartPaneTitle();

	void setChartImagePath(String path);

	String getChartImagePath();
}