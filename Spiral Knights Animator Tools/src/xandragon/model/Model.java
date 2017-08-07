package xandragon.model;

import java.util.ArrayList;

import xandragon.converter.ArrayConfig;

/**
 * The class responsible for storing model data.
 * @author Xan the Dragon
 */
public class Model {
	
	/**The FloatArray.*/
	public ArrayList<Float> floatArray;
	
	/*Other data*/
	public ArrayConfig boneIndices;
	public ArrayConfig boneWeights;
	public ArrayConfig vertices;
	public ArrayConfig normals;
	public ArrayConfig uvs;
	
	/**
	 * Create an empty model, initializing the ArrayConfigs to a null value.
	 */
	public Model() {
		boneIndices = null;
		boneWeights = null;
		vertices = null;
		normals = null;
		uvs = null;
	}
}
