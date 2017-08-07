package xandragon.model;

import java.util.ArrayList;

import xandragon.model.math.Vector2f;
import xandragon.model.math.Vector3f;

/**
 * Stores geometry data.
 * Has handy functions to automatically compile the text for DAE formatting.
 * @author Xan the Dragon
 */
public class Geometry {
	/** The vertices of this geometry */
	public ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
	
	/** The normals of this geometry */
	public ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
	
	/** The normals of this geometry */
	public ArrayList<Vector2f> uvs = new ArrayList<Vector2f>();
	
	/** The index array of this geometry*/
	public ArrayList<Short> indices = new ArrayList<Short>();
	
	/**
	 * Constructor for creation
	 * @param v Vertices
	 * @param n Normals
	 * @param u UVs
	 * @param i Indices
	 */
	public Geometry(ArrayList<Vector3f> v, ArrayList<Vector3f> n, ArrayList<Vector2f> u, ArrayList<Short> i) {
		vertices = v;
		normals = n;
		uvs = u;
		indices = i;
	}
	
	/**
	 * Create a vertex list
	 * @return A long string of every vertex for the DAE.
	 */
	public String createVertexList() {
		String ret = "";
		for (int idx = 0; idx < vertices.size(); idx++) {
			Vector3f val = vertices.get(idx);
			ret = ret + val.x + " " + val.y + " " + val.z + ((idx != (vertices.size() - 1)) ? " " : "");
		}
		return ret;
	}
	
	/**
	 * Create a normal list
	 * @return A long string of every normal for the DAE.
	 */
	public String createNormalList() {
		String ret = "";
		for (int idx = 0; idx < normals.size(); idx++) {
			Vector3f val = normals.get(idx);
			ret = ret + val.x + " " + val.y + " " + val.z + ((idx != (normals.size() - 1)) ? " " : "");
		}
		return ret;
	}
	
	/**
	 * Create a uv list
	 * @return A long string of every uv for the DAE.
	 */
	public String createUVList() {
		String ret = "";
		for (int idx = 0; idx < uvs.size(); idx++) {
			Vector2f val = uvs.get(idx);
			ret = ret + val.x + " " + val.y + ((idx != (uvs.size() - 1)) ? " " : "");
		}
		return ret;
	}
	
	/**
	 * Create an index
	 * @return A long string of every index for the DAE.
	 */
	public String createIndexList() {
		String ret = "";
		for (int idx = 0; idx < indices.size(); idx++) {
			ret = ret + indices.get(idx) + ((idx != (indices.size() - 1)) ? " " : "");
		}
		return ret;
	}
}
