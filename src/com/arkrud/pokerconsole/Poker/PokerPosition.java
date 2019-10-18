package com.arkrud.pokerconsole.Poker;

public class PokerPosition implements TreeNodeState {

	private String nodeText;
	private String chartPaneTitle;
	private String chartImagePath;
	private boolean selected;

	public PokerPosition(String nodeText) {
		super();
		this.nodeText = nodeText;
	}

	@Override
	public String getNodeText() {
		return nodeText;
	}

	@Override
	public String getNodeScreenName() {
		// TODO Auto-generated method stub
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
	public void setChartImagePath(String chartImagePath) {
		this.chartImagePath = chartImagePath;
	}

	@Override
	public String getChartImagePath() {
		return chartImagePath;
	}
}
