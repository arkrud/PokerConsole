package com.arkrud.pokerconsole.Poker;

public class Fork implements TreeNodeState {
	private String nodeText;
	private boolean selected;
	private String chartPaneTitle;
	public Fork(String nodeText) {
		super();
		this.nodeText = nodeText;
	}

	@Override
	public String getNodeText() {
		// TODO Auto-generated method stub
		return nodeText;
	}

	@Override
	public String getNodeScreenName() {
		return null;
	}

	@Override
	public boolean isSelected() {
		return selected;
	}

	@Override
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public void setChartPaneTitle(String chartPaneTitle) {
		this.chartPaneTitle = chartPaneTitle;
	}

	@Override
	public String getChartPaneTitle() {
		return chartPaneTitle;
	}

	@Override
	public void setChartImagePath(String path) {
		// TODO Auto-generated method stub
	}

	@Override
	public String getChartImagePath() {
		// TODO Auto-generated method stub
		return null;
	}
}
