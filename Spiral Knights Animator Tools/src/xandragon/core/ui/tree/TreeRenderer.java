package xandragon.core.ui.tree;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * A class dedicated to constructing a new hierarchy for a JTree with ease.
 * @author Xan the Dragon
 */
public class TreeRenderer {
	
	protected DefaultMutableTreeNode root;
	
	/**
	 * Construct a new TreeRenderer
	 * @param rootText The text for the root node to display.
	 */
	public TreeRenderer(String rootText) {
		root = createNode(new TreeNode(rootText));
	}
	
	/**
	 * Construct a new TreeRenderer with a specific icon.
	 * @param rootText The text for the root node to display.
	 * @param Node the node object
	 */
	public TreeRenderer(TreeNode node) {
		root = createNode(node);
	}
	
	/**
	 * Get a DataTreePath from this renderer.
	 * @return A compiled DataTreePath.
	 */
	public DataTreePath getDataTreePath() {
		return new DataTreePath(root);
	}
	
	/**
	 * @return the root node
	 */
	public DefaultMutableTreeNode getRoot() {
		return root;
	}
	
	/**
	 * Create a new tree element from a Node
	 * @param entry The Node that holds the display data
	 * @return
	 */
	public DefaultMutableTreeNode createNode(TreeNode entry) {
		return new DefaultMutableTreeNode(entry);
	}
	
	/**
	 * Add a Node to a specific part of a tree.
	 * @param in The tree item to add this under.
	 * @param entry The Node that holds the display data
	 * @return
	 */
	public DefaultMutableTreeNode addNode(DefaultMutableTreeNode in, TreeNode entry) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(entry);
		node.setUserObject(entry);
		in.add(node);
		return node;
	}
	
	/**
	 * Add a new node under the root node of this renderer.
	 * @param entry The node to add
	 * @return
	 */
	public DefaultMutableTreeNode addNodeRoot(TreeNode entry) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(entry);
		node.setUserObject(entry);
		root.add(node);
		return node;
	}
	
	/**
	 * Create a blank JTree with the default text.
	 * @return a new JTree
	 */
	public static JTree createBlankTree() {
		return new JTree(new DefaultMutableTreeNode(new TreeNode("(no model to view)")));
	}
	
	/**
	 * Create a DataTreePath with the default text.
	 * @return
	 */
	public static DataTreePath createBlankTreePath() {
		return new DataTreePath(new DefaultMutableTreeNode(new TreeNode("(no model to view)")));
	}
}
