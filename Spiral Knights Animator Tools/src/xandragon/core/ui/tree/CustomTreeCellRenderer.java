package xandragon.core.ui.tree;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

@SuppressWarnings("serial")
public class CustomTreeCellRenderer extends DefaultTreeCellRenderer implements TreeCellRenderer {
	
	protected Icon iconData = new Icon();
	
	public CustomTreeCellRenderer() {
		
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		super.setIcon(getIcon(value));
		return this;
	}
	
	protected ImageIcon getIcon(Object ent) {
		DefaultMutableTreeNode tree_node = (DefaultMutableTreeNode) ent;
		TreeNode node = ((TreeNode) tree_node.getUserObject());
		return node.displayIcon != null ? node.displayIcon : iconData.none;
	}
}
