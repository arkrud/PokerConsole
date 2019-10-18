package com.arkrud.pokerconsole.Poker;

public class PokerOpponentPosition implements TreeNodeState {

	private String nodeText;
	private String chartPaneTitle;
	private String chartImagePath;
	private boolean selected;
	private String pokerAction;
	private String pokerHandSizing;
	private String pokerPosition;

	public PokerOpponentPosition(String nodeText) {
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

	public String getPokerAction() {
		return pokerAction;
	}

	public void setPokerAction(String pokerAction) {
		this.pokerAction = pokerAction;
	}

	public String getPokerHandSizing() {
		return pokerHandSizing;
	}

	public void setPokerHandSizing(String pokerHandSizing) {
		this.pokerHandSizing = pokerHandSizing;
	}

	public String getPokerPosition() {
		return pokerPosition;
	}

	public void setPokerPosition(String pokerPosition) {
		this.pokerPosition = pokerPosition;
	}

	public void setNodeText(String nodeText) {
		this.nodeText = nodeText;
	}



}
