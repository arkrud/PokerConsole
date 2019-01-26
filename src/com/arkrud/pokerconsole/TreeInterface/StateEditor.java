package com.arkrud.pokerconsole.TreeInterface;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;


public class StateEditor extends AbstractCellEditor implements TreeCellEditor {
	private static final long serialVersionUID = 1L;
	private JCheckBox checkBox;
	private TreeNodeState editorValue;

	public StateEditor() {
		checkBox = new JCheckBox();
		checkBox.setOpaque(false);
	}

	@Override
	public Object getCellEditorValue() {
		editorValue.setSelected(checkBox.isSelected());
		return editorValue;
	}

	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
		if (value instanceof DefaultMutableTreeNode) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			TreeNodeState state = (TreeNodeState) node.getUserObject();
			editorValue = state;
			checkBox.setText(state.getNodeText());
			checkBox.setSelected(state.isSelected());
		} else {
			checkBox.setText("??");
			checkBox.setSelected(false);
		}
		return checkBox;
	}

	@Override
	public boolean isCellEditable(EventObject event) {
		if (!super.isCellEditable(event)) {
			return false;
		}
		if (event != null && event.getSource() instanceof JTree && event instanceof MouseEvent) {
			MouseEvent mouseEvent = (MouseEvent) event;
			JTree tree = (JTree) event.getSource();
			TreePath path = tree.getPathForLocation(mouseEvent.getX(), mouseEvent.getY());
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
			if (node.getUserObject() instanceof String) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
}