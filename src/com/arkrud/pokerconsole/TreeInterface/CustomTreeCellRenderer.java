package com.arkrud.pokerconsole.TreeInterface;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.arkrud.pokerconsole.Poker.Fork;
import com.arkrud.pokerconsole.Poker.PokerAction;
import com.arkrud.pokerconsole.Poker.PokerHandSizing;
import com.arkrud.pokerconsole.Poker.PokerOpponentPosition;
import com.arkrud.pokerconsole.Poker.PokerPosition;
import com.arkrud.pokerconsole.Poker.PokerStrategy;
import com.arkrud.pokerconsole.Util.UtilMethodsFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class CustomTreeCellRenderer.
 */
public class CustomTreeCellRenderer extends DefaultTreeCellRenderer {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
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
			if (Character.isDigit(((PokerOpponentPosition) obj).getNodeText().charAt(0)) & Character.isDigit(((PokerOpponentPosition) obj).getNodeText().charAt(1))) {
				setText(((PokerOpponentPosition) obj).getNodeText().substring(2));
			} else {
				setText(((PokerOpponentPosition) obj).getNodeText().substring(1));
			}
			// setText(((PokerOpponentPosition) obj).getNodeText().substring(1));
			setIcon(UtilMethodsFactory.populateInterfaceImages("interfaceimages").get("donk"));
		} else if (obj instanceof PokerPosition) {
			setText(((PokerPosition) obj).getNodeText());
			setIcon(UtilMethodsFactory.populateInterfaceImages("interfaceimages").get("hero"));
		} else if (obj instanceof Fork) {
			setText(((Fork) obj).getNodeText());
			setIcon(UtilMethodsFactory.populateInterfaceImages("interfaceimages").get("fork"));
		} else {
		}
		return this;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultTreeCellRenderer#getBackgroundNonSelectionColor()
	 */
	@Override
	public Color getBackgroundNonSelectionColor() {
		return Color.WHITE;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultTreeCellRenderer#getBackgroundSelectionColor()
	 */
	@Override
	public Color getBackgroundSelectionColor() {
		return Color.LIGHT_GRAY;
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#getBackground()
	 */
	@Override
	public Color getBackground() {
		return Color.WHITE;
	}
}
