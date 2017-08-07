package xandragon.model;

import java.io.File;

/**
 * Stores material data. Has no functions otherwise.
 * @author Xan the Dragon
 */
public class Material {
	/**The texture file this material uses.*/
	public File texture = null;
	
	/**Ambient color*/
	public Color ambient = new Color();
	
	/**Diffuse color*/
	public Color diffuse = new Color(1.0f, 1.0f, 1.0f);
	
	/**Specular color*/
	public Color specular = new Color();
	
	/**Emission*/
	public Color emission = new Color();
	
	/**Blank constructor*/
	public Material() {}
	
	/**
	 * Constructor for setting data
	 * @param tex Texture file reference
	 * @param a Ambient
	 * @param d Diffuse
	 * @param s Specular
	 * @param e Emission
	 */
	public Material(File tex, Color a, Color d, Color s, Color e) {
		texture = tex;
		ambient = a;
		diffuse = d;
		specular = s;
		emission = e;
	}
}
