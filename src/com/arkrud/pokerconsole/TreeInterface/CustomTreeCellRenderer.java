package com.arkrud.pokerconsole.TreeInterface;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.arkrud.pokerconsole.Poker.PokerAction;
import com.arkrud.pokerconsole.Poker.PokerHandSizing;
import com.arkrud.pokerconsole.Poker.PokerOpponentPosition;
import com.arkrud.pokerconsole.Poker.PokerPosition;
import com.arkrud.pokerconsole.Poker.PokerStrategy;
import com.arkrud.pokerconsole.Util.UtilMethodsFactory;

public class CustomTreeCellRenderer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = 1L;
	

	// Change tree node icons and text
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		DefaultMutableTreeNode n = (DefaultMutableTreeNode) value;
		Object obj = n.getUserObject();
		if (obj instanceof PokerStrategy) {
			setText(((PokerStrategy) obj).getNodeText());
			setIcon(UtilMethodsFactory.populateInterfaceImages("interfaceimages").get("strategy"));
		} else if (obj instanceof PokerAction) {
			setText(((PokerAction) obj).getNodeText());
			setIcon(UtilMethodsFactory.populateInterfaceImages("interfaceimages").get("action"));
		} else if (obj instanceof PokerHandSizing) {
			setText(((PokerHandSizing) obj).getNodeText());
			setIcon(UtilMethodsFactory.populateInterfaceImages("interfaceimages").get("sizing"));
		} else if (obj instanceof PokerOpponentPosition) {
			setText(((PokerOpponentPosition) obj).getNodeText().substring(1));
			setIcon(UtilMethodsFactory.populateInterfaceImages("interfaceimages").get("donk"));
		} else if (obj instanceof PokerPosition) {
			setText(((PokerPosition) obj).getNodeText());
			setIcon(UtilMethodsFactory.populateInterfaceImages("interfaceimages").get("hero"));
		} else {
		}
		return this;
	}

	@Override
	public Color getBackgroundNonSelectionColor() {
		return Color.WHITE;
	}

	@Override
	public Color getBackgroundSelectionColor() {
		return Color.LIGHT_GRAY;
	}

	@Override
	public Color getBackground() {
		return Color.WHITE;
	}
}
