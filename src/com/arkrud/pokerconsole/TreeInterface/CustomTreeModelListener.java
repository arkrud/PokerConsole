package com.arkrud.pokerconsole.TreeInterface;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving customTreeModel events.
 * The class that is interested in processing a customTreeModel
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addCustomTreeModelListener<code> method. When
 * the customTreeModel event occurs, that object's appropriate
 * method is invoked.
 *
 * @see CustomTreeModelEvent
 */
public class CustomTreeModelListener implements TreeModelListener {
	
	/* (non-Javadoc)
	 * @see javax.swing.event.TreeModelListener#treeNodesChanged(javax.swing.event.TreeModelEvent)
	 */
	public void treeNodesChanged(TreeModelEvent e) {
		DefaultMutableTreeNode node;
		node = (DefaultMutableTreeNode) (e.getTreePath().getLastPathComponent());
		/*
		 * If the event lists children, then the changed node is the child
		 * of the node we've already gotten. Otherwise, the changed node and
		 * the specified node are the same.
		 */
		int index = e.getChildIndices()[0];
		node = (DefaultMutableTreeNode) (node.getChildAt(index));
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.TreeModelListener#treeNodesInserted(javax.swing.event.TreeModelEvent)
	 */
	public void treeNodesInserted(TreeModelEvent e) {
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.TreeModelListener#treeNodesRemoved(javax.swing.event.TreeModelEvent)
	 */
	public void treeNodesRemoved(TreeModelEvent e) {
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.TreeModelListener#treeStructureChanged(javax.swing.event.TreeModelEvent)
	 */
	public void treeStructureChanged(TreeModelEvent e) {
	}
}
