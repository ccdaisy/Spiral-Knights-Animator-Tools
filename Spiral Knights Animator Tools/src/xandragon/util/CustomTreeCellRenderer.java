package xandragon.util;

import java.awt.Component;
import java.io.File;
import java.net.URISyntaxException;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import xandragon.core.ui.MainGui.Entry;

@SuppressWarnings("serial")
public class CustomTreeCellRenderer extends DefaultTreeCellRenderer implements TreeCellRenderer {
	
	protected ImageIcon object;
	protected ImageIcon model;
	protected ImageIcon value;
	protected ImageIcon tag;
	protected ImageIcon file;
	protected ImageIcon plug;
	protected ImageIcon gear;
	protected ImageIcon wrench;
	protected ImageIcon array;
	protected ImageIcon person;
	protected ImageIcon people;
	protected ImageIcon script;
	protected ImageIcon attachment;
	protected ImageIcon replicate;
	protected ImageIcon part;
	protected ImageIcon partgroup;
	protected ImageIcon animate;
	protected ImageIcon unknown;
	protected ImageIcon none;
	protected String ASSET_PATH;
	
	
	public CustomTreeCellRenderer() throws URISyntaxException {
		ASSET_PATH = "assets";
		object = new ImageIcon(ASSET_PATH+File.separator+"obj.png");
		model = new ImageIcon(ASSET_PATH+File.separator+"model.png");
		value = new ImageIcon(ASSET_PATH+File.separator+"value.png");
		tag = new ImageIcon(ASSET_PATH+File.separator+"tag.png");
		file = new ImageIcon(ASSET_PATH+File.separator+"file.png");
		plug = new ImageIcon(ASSET_PATH+File.separator+"plug.png");
		gear = new ImageIcon(ASSET_PATH+File.separator+"folder_gear.png");
		wrench = new ImageIcon(ASSET_PATH+File.separator+"folder_wrench.png");
		array = new ImageIcon(ASSET_PATH+File.separator+"array.png");
		person = new ImageIcon(ASSET_PATH+File.separator+"person.png");
		people = new ImageIcon(ASSET_PATH+File.separator+"people.png");
		script = new ImageIcon(ASSET_PATH+File.separator+"script.png");
		attachment = new ImageIcon(ASSET_PATH+File.separator+"hat.png");
		part = new ImageIcon(ASSET_PATH+File.separator+"part.png");
		partgroup = new ImageIcon(ASSET_PATH+File.separator+"part_group.png");
		replicate = new ImageIcon(ASSET_PATH+File.separator+"replicate.png");
		animate = new ImageIcon(ASSET_PATH+File.separator+"animate.png");
		unknown = new ImageIcon(ASSET_PATH+File.separator+"unknown.png");
		none = new ImageIcon(ASSET_PATH+File.separator+"empty.png");
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		super.setIcon(getIcon(value));
		return this;
	}
	
	protected ImageIcon getIcon(Object ent) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) ent;
		Entry entry = ((Entry) node.getUserObject());
		String name = entry.idName;
		if (name.matches("magic") || name.matches("compressed") || name.matches("version")) {
			return tag;
		} else if (name.matches("saSize") || name.matches("boneIndicesCount") || name.matches("boneWeightsCount") || name.matches("vertexArrayCount") || name.matches("normalArrayCount") || name.matches("uvArrayCount")) {
			return value;
		} else if (name.matches("faSize")) {
			return array;
		} else if (name.matches("attachments") || name.matches("attachment")) {
			return plug;
		} else if (name.matches("implementation")) {
			return getImpIcon(entry.entryName);
		}
		return model;
	}
	
	protected ImageIcon getImpIcon(String name) {
		String lower = name.toLowerCase();
		if (lower.endsWith("articulatedconfig")) {
			return person;
		} else if (lower.endsWith("animationconfig")) {
			return animate;
		} else if (lower.endsWith("compoundconfig")) {
			return object;
		} else if (lower.endsWith("generatedstaticconfig")) {
			return file;
		} else if (lower.endsWith("influenceflagconfig")) {
			return gear;
		} else if (lower.endsWith("mergedstaticconfig")) {
			return partgroup;
		} else if (lower.endsWith("scriptedconfig")) {
			return script;
		} else if (lower.endsWith("staticconfig")) {
			return part;
		} else if (lower.endsWith("staticsetconfig")) {
			return model;
		}
		return unknown;
	}
}
