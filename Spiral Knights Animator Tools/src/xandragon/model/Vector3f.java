package xandragon.model;

/**
 * Vector3 stored with floats.
 * @author Xan the Dragon
 */
public class Vector3f {
	
	/**The X component.*/
	public float x = 0;
	
	/**The Y component.*/
	public float y = 0;
	
	/**The Z component.*/
	public float z = 0;
	
	/**Create a blank Vector3f*/
	public Vector3f() {}
	
	/**Create a Vector3f*/
	public Vector3f(float _x, float _y, float _z) {
		x = _x;
		y = _y;
		z = _z;
	}
}
