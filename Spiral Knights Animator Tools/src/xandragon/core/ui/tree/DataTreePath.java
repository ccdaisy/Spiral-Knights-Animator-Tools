package xandragon.core.ui.tree;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class DataTreePath implements TreeModel {
	protected DefaultMutableTreeNode root = null;
	
	public DataTreePath(DefaultMutableTreeNode _root) {
		root = _root;
	}
	
	@Override
	public void addTreeModelListener(TreeModelListener l) {}

	@Override
	public Object getChild(Object parent, int index) {
		DefaultMutableTreeNode par = (DefaultMutableTreeNode) parent;
		return par.getChildAt(index);
	}

	@Override
	public int getChildCount(Object parent) {
		DefaultMutableTreeNode par = (DefaultMutableTreeNode) parent;
		return par.getChildCount();
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		DefaultMutableTreeNode par = (DefaultMutableTreeNode) parent;
		return par.getIndex((TreeNode) child);
	}

	@Override
	public Object getRoot() {
		return root;
	}

	@Override
	public boolean isLeaf(Object node) {
		return ((DefaultMutableTreeNode) node).isLeaf();
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {}
}