package com.arkrud.pokerconsole.Poker;

import com.arkrud.pokerconsole.TreeInterface.TreeNodeState;

public class PokerGroup implements TreeNodeState {
	private String nodeText;
	private String iniFilePath;

	public PokerGroup(String nodeText) {
		super();
		this.nodeText = nodeText;
	}

	@Override
	public String getNodeText() {
		return nodeText;
	}

	public void setNodeText(String nodeText) {
		this.nodeText = nodeText;
	}

	@Override
	public String getNodeScreenName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSelected() {
		return false;
	}

	@Override
	public void setSelected(boolean selected) {
		// TODO Auto-generated method stub
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

	public String getIniFilePath() {
		return iniFilePath;
	}

	public void setIniFilePath(String iniFilePath) {
		this.iniFilePath = iniFilePath;
	}
	
	
}
