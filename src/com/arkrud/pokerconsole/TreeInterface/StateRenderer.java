package com.arkrud.pokerconsole.TreeInterface;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

import com.arkrud.pokerconsole.Poker.PokerAction;
import com.arkrud.pokerconsole.Poker.PokerGroup;
import com.arkrud.pokerconsole.Poker.PokerHandSizing;
import com.arkrud.pokerconsole.Poker.PokerOpponentPosition;
import com.arkrud.pokerconsole.Poker.PokerPosition;
import com.arkrud.pokerconsole.Poker.PokerStrategy;
import com.arkrud.pokerconsole.Util.UtilMethodsFactory;

public class StateRenderer implements TreeCellRenderer {
	private JCheckBox checkBox;
	private Icon origIcon;

	public StateRenderer() {
		checkBox = new JCheckBox();
		checkBox.setOpaque(false);
		origIcon = checkBox.getIcon();
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		if (value instanceof DefaultMutableTreeNode) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			TreeNodeState state = (TreeNodeState) node.getUserObject();
			checkBox.setText(state.getNodeText());
			checkBox.setSelected(state.isSelected());
			if (node.getUserObject() instanceof PokerGroup) {
				checkBox.setIcon(UtilMethodsFactory.populateInterfaceImages("interfaceimages").get("filter"));
			} else if (node.getUserObject() instanceof PokerStrategy){
				checkBox.setIcon(UtilMethodsFactory.populateInterfaceImages("interfaceimages").get("configuration"));
			} else if (node.getUserObject() instanceof PokerAction){
				checkBox.setIcon(UtilMethodsFactory.populateInterfaceImages("interfaceimages").get("action"));
			} else if (node.getUserObject() instanceof PokerPosition){
				checkBox.setIcon(UtilMethodsFactory.populateInterfaceImages("interfaceimages").get("hero"));
			} else if (node.getUserObject() instanceof PokerHandSizing){
				checkBox.setIcon(UtilMethodsFactory.populateInterfaceImages("interfaceimages").get("sizing"));
			} else if (node.getUserObject() instanceof PokerOpponentPosition){
				checkBox.setIcon(origIcon);
			} else {
				checkBox.setIcon(origIcon);
			}
		} else {
			checkBox.setText("??");
			checkBox.setSelected(false);
		}
		if (selected) {
			checkBox.setBackground(UIManager.getColor("Tree.selectionBackground"));
			checkBox.setForeground(UIManager.getColor("Tree.selectionForeground"));
		} else {
			checkBox.setForeground(tree.getForeground());
		}
		checkBox.setOpaque(selected);
		return checkBox;
	}
}
