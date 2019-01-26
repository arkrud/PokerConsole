package com.arkrud.pokerconsole.TreeInterface;

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