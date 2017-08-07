package xandragon.model;

import java.io.File;

/**
 * Stores materials. Has no functions otherwise.
 * @author Xan the Dragon
 *
 */
public class Material {
	/**The texture file this material uses.*/
	public File texture = null;
	
	/**Ambient color*/
	public Color ambient = new Color();
	
	/**Diffuse color*/
	public Color diffuse = new Color(1, 1, 1);
	
	/**Specular color*/
	public Color specular = new Color();
}
