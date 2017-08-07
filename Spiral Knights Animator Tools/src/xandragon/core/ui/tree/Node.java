package xandragon.core.ui.tree;

import javax.swing.ImageIcon;

/**
 * A class storing a node for a JTree. This node has a specified display text and a specified icon.
 * @author Xan the Dragon
 */
public class Node {
	protected static Icon iconData = new Icon();
	public String displayText = "NULL";
	public ImageIcon displayIcon = iconData.none;
	
	/**
	 * Construct a new node for the tree that will display text, but have no icon.
	 * @param display The display text for this node
	 */
	public Node(String display) {
		displayText = display;
	}
	
	/**
	 * Construct a new node for the tree with a specified text and icon.
	 * @param display The display text for this node
	 * @param icon The icon for this node
	 */
	public Node(String display, ImageIcon icon) {
		displayText = display;
		displayIcon = icon;
	}
	
	@Override
	public String toString() {
		//This is used when casting to DefaultMutableTreeNode - It uses this method to get the display text.
		return displayText;
	}
	
	/**
	 * Get the icon for a specific implementation
	 * @param imp The name of the implementation
	 * @return
	 */
	public static ImageIcon getIconFromImplementation(String imp) {
		String lower = imp.toLowerCase();
		if (lower.endsWith("articulatedconfig")) {
			return iconData.person;
		} else if (lower.endsWith("animationconfig")) {
			return iconData.animation;
		} else if (lower.endsWith("compoundconfig")) {
			return iconData.object;
		} else if (lower.endsWith("generatedstaticconfig")) {
			return iconData.generatedstatic;
		} else if (lower.endsWith("influenceflagconfig")) {
			return iconData.folder_gear;
		} else if (lower.endsWith("mergedstaticconfig")) {
			return iconData.partgroup;
		} else if (lower.endsWith("scriptedconfig")) {
			return iconData.script;
		} else if (lower.endsWith("staticconfig")) {
			return iconData.part;
		} else if (lower.endsWith("staticsetconfig")) {
			return iconData.object;
		}
		return iconData.unknown;
	}
}
