package com.arkrud.pokerconsole.Poker;

public class PokerHandSizing implements TreeNodeState {
	private String nodeText;
	private PokerAction pokerAction;
	private boolean selected;

	public PokerHandSizing(String nodeText, PokerAction pokerAction) {
		super();
		this.nodeText = nodeText;
		this.pokerAction = pokerAction;
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

	public PokerAction getPokerAction() {
		return pokerAction;
	}

	public void setPokerAction(PokerAction pokerAction) {
		this.pokerAction = pokerAction;
	}




}
