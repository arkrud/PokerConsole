package com.arkrud.pokerconsole.Poker;

public class PokerStrategy implements TreeNodeState {
	private String nodeText;
	//private String strategyName;
	private boolean selected;

	public PokerStrategy(String nodeText) {
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

	public void setNodeText(String nodeText) {
		this.nodeText = nodeText;
	}

	@Override
	public void setChartPaneTitle(String title) {
		// TODO Auto-generated method stub
	}

	@Override
	public String getChartPaneTitle() {
		// TODO Auto-generated method stub
		return null;
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

	public String getStrategyName() {
		return nodeText;
	}

	/*public void setStrategyName(String strategyName) {
		this.strategyName = nodeText;
	}*/
	
	
}
