/**
 *
 */
package com.arkrud.pokerconsole.UI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.SpringLayout;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.arkrud.Shareware.SpringUtilities;
import com.arkrud.pokerconsole.Poker.PokerAction;
import com.arkrud.pokerconsole.Poker.PokerHandSizing;
import com.arkrud.pokerconsole.Poker.PokerPosition;
import com.arkrud.pokerconsole.Poker.PokerStrategy;
import com.arkrud.pokerconsole.TreeInterface.CustomTree;
import com.arkrud.pokerconsole.Util.UtilMethodsFactory;

/**
 * @author arkrud
 *
 */
public class AddHandsDialog extends JDialog implements ActionListener {
	private JTree tree;
	private CustomTree theTree;
	private Object obj;
	private DefaultMutableTreeNode node;
	private JButton okButton, cancelButton;
	private JPanel handsPanel;
	private String[] handNames = { "Big Blind", "Small Blind", "Button", "Cutoff", "HiJack", "LoJack", "Undeer The Gun", "Undeer The Gun + 1", "Undeer The Gun + 2" };
	private String[] handScreenNames = { "BB", "SB", "BU", "CO", "HJ", "LJ", "UTG", "UTG1", "UTG2" };
	Map<String, JCheckBox> feldsMap = new TreeMap<String, JCheckBox>(Collections.reverseOrder());
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	public AddHandsDialog(JTree tree, CustomTree theTree, Object obj, DefaultMutableTreeNode node) {
		this.tree = tree;
		this.obj = obj;
		this.node = node;
		this.theTree = theTree;
		setModal(true);
		ArrayList<String> alreadyAdded = new ArrayList<String>();
		Enumeration<?> en = node.children();
		@SuppressWarnings("unchecked")
		List<DefaultMutableTreeNode> list = (List<DefaultMutableTreeNode>) Collections.list(en);
		for (DefaultMutableTreeNode s : list) {
			if (s.getUserObject() instanceof PokerPosition) {
				PokerPosition pokerPosition = (PokerPosition) s.getUserObject();
				alreadyAdded.add(pokerPosition.getNodeText());
			}
		}
		handsPanel = new JPanel(new SpringLayout());
		int x = 0;
		while (x < handNames.length) {
			handsPanel.add(new JLabel(handNames[x]));
			JCheckBox checkBox = new JCheckBox();
			checkBox.setName(handScreenNames[x]);
			handsPanel.add(checkBox);
			if (alreadyAdded.contains(handScreenNames[x])) {
				checkBox.setSelected(true);
				checkBox.setEnabled(false);
			}
			feldsMap.put(handScreenNames[x], checkBox);
			x++;
		}
		okButton = new JButton("Add");
		cancelButton = new JButton("Cancel");
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
		handsPanel.add(okButton);
		handsPanel.add(cancelButton);
		add(handsPanel, BorderLayout.CENTER);
		setTitle("Add Hands");
		SpringUtilities.makeCompactGrid(handsPanel, handNames.length + 1, 2, 10, 10, 10, 10);
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		JButton theButton = (JButton) ae.getSource();
		if (theButton.getText().equals("Add")) {
			Set<Map.Entry<String, JCheckBox>> set = feldsMap.entrySet();
			Iterator<Map.Entry<String, JCheckBox>> itr = set.iterator();
			while (itr.hasNext()) {
				JCheckBox jCheckBox = itr.next().getValue();
				if (jCheckBox.isSelected() && jCheckBox.isEnabled()) {
					PokerPosition pokerPosition = new PokerPosition(jCheckBox.getName());
					DefaultMutableTreeNode top = (DefaultMutableTreeNode) tree.getModel().getRoot();
					if (tree.getSelectionPath().getPath().length == 3) {
						File pokerHandDir = new File(UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/" + ((PokerHandSizing) obj).getPokerAction().getNodeText() + "/"
								+ ((PokerHandSizing) obj).getNodeText() + "/" + jCheckBox.getName());
						UtilMethodsFactory.createFolder(pokerHandDir);
					} else if (tree.getSelectionPath().getPath().length == 2) {
						File pokerHandDir = new File(UtilMethodsFactory.getConfigPath() + "Images/" + ((PokerStrategy) top.getUserObject()).getNodeText() + "/" + ((PokerAction) obj).getNodeText() + "/" + jCheckBox.getName());
						UtilMethodsFactory.createFolder(pokerHandDir);
					}
					DefaultMutableTreeNode pokerPositionNode = new DefaultMutableTreeNode(pokerPosition);
					((DefaultTreeModel) tree.getModel()).insertNodeInto(pokerPositionNode, node, pokerPositionNode.getChildCount());
				}
			}
			theTree.expandNodesBelow(node, tree);
			this.dispose();
		} else {
			this.dispose();
		}
	}
}
