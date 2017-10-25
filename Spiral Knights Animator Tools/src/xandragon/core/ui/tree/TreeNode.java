package xandragon.core.ui.tree;

import javax.swing.ImageIcon;

/**
 * A class storing a node for a JTree. This node has a specified display text and a specified icon.
 * @author Xan the Dragon
 */
public class TreeNode {
	protected static Icon iconData = new Icon();
	public String displayText = "NULL";
	public ImageIcon displayIcon = iconData.none;
	
	/**
	 * Create a blank node. Manually set the display values later.
	 */
	public TreeNode() {
		
	}
	
	/**
	 * Construct a new node for the tree that will display text, but have no icon.
	 * @param display The display text for this node
	 */
	public TreeNode(String display) {
		displayText = display;
	}
	
	/**
	 * Construct a new node for the tree with a specified text and icon.
	 * @param display The display text for this node
	 * @param icon The icon for this node
	 */
	public TreeNode(String display, ImageIcon icon) {
		displayText = display;
		displayIcon = icon;
	}
	
	@Override
	public String toString() {
		//This is used when casting to DefaultMutableTreeNode - It uses this method to get the display text.
		return displayText;
	}
}
