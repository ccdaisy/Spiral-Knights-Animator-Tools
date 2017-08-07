package xandragon.core.ui.tree;

import java.net.URL;

import javax.swing.ImageIcon;

public class Icon {
	
	protected ImageIcon loadImage(String name) {
		URL url = getClass().getClassLoader().getResource("assets/"+name);
		return new ImageIcon(url);
	}
	
	public ImageIcon object = loadImage("object.png");
	public ImageIcon model = loadImage("model.png");
	public ImageIcon value = loadImage("value.png");
	public ImageIcon tag = loadImage("tag.png");
	public ImageIcon generatedstatic = loadImage("generatedstatic.png");
	public ImageIcon plug = loadImage("plug.png");
	public ImageIcon folder_gear = loadImage("folder_gear.png");
	public ImageIcon folder_wrench = loadImage("folder_wrench.png");
	public ImageIcon array = loadImage("array.png");
	public ImageIcon person = loadImage("person.png");
	public ImageIcon people = loadImage("people.png");
	public ImageIcon script = loadImage("script.png");
	public ImageIcon attachment = loadImage("hat.png");
	public ImageIcon derived = loadImage("derived.png");
	public ImageIcon part = loadImage("part.png");
	public ImageIcon partgroup = loadImage("partgroup.png");
	public ImageIcon animation = loadImage("animation.png");
	public ImageIcon unknown = loadImage("unknown.png");
	public ImageIcon none = loadImage("empty.png");
}
