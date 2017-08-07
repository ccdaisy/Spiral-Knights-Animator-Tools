package xandragon.model;

public class Color {
	/**The red component.*/
	public float r = 0;
	
	/**The green component.*/
	public float g = 0;
	
	/**The blue component.*/
	public float b = 0;
	
	/**Create an empty (black) color.*/
	public Color() {}
	
	/**
	 * Create a new Color from floating point RGB values ranging from 0-1.
	 * @param r The red component.
	 * @param g The green component.
	 * @param b The blue component.
	 */
	public Color(float _r, float _g, float _b) {
		r = _r;
		g = _g;
		b = _b;
	}
	
	/**
	 * Create a new Color from integer RGB values ranging from 0-255.
	 * @param r The red component.
	 * @param g The green component.
	 * @param b The blue component.
	 */
	public Color (int _r, int _g, int _b) {
		r = _r / 255;
		g = _g / 255;
		b = _b / 255;
	}
	
	public String toDAEFormat() {
		return String.valueOf(r) + " " + String.valueOf(g) + " " + String.valueOf(b) + "  0";
	}
}
