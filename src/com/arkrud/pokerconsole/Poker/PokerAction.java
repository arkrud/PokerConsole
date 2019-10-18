package com.arkrud.pokerconsole.Poker;

import java.util.ArrayList;

public class PokerAction implements TreeNodeState {
	private String nodeText;
	private boolean sizing;
	private boolean selected;
	private ArrayList<PokerHandSizing> sizings;

	public PokerAction(String nodeText) {
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

	public boolean isSizing() {
		return sizing;
	}

	public void setSizing(boolean sizing) {
		this.sizing = sizing;
	}

	public void addSizing (PokerHandSizing theSizing) {
		sizings.add(theSizing);
	}

	public ArrayList<PokerHandSizing> getSizings() {
		return sizings;
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
}
