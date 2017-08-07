package xandragon.model;

import java.util.ArrayList;

import xandragon.converter.ArrayConfig;

/**
 * The class responsible for storing model data.
 * @author Xan the Dragon
 */
public class Model {
	
	/**The FloatArray.*/
	public ArrayList<Float> floatArray = null;
	
	/**The index array.*/
	public ArrayList<Short> indices = null;
	
	/**An array of the stored vertices in this model*/
	public ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
	
	/**An array of the stored normals in this model*/
	public ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
	
	/**An array of the stored texture coordinates in this model*/
	public ArrayList<Vector2f> uvs = new ArrayList<Vector2f>();
	
	/**An array of the stored bone indices in this model. Note: This must be cast to Integer when used, even though it's stored as a float.*/
	public ArrayList<Float> boneIndices = new ArrayList<Float>();
	
	/**An array of the stored bone weights in this model*/
	public ArrayList<Float> boneWeights = new ArrayList<Float>();
	
	/**The geometry reference.*/
	public Geometry geometry;
	
	/**The material reference*/
	public Material material;
	
	/**The bone skin reference*/
	public Skin skin;
	
	/**The name of this model.*/
	public String name;
	
	/*Other data*/
	public ArrayConfig boneIndexArray;
	public ArrayConfig boneWeightArray;
	public ArrayConfig vertexArray;
	public ArrayConfig normalArray;
	public ArrayConfig uvArray;
	
	////////////////
	//CONSTRUCTORS//
	////////////////
	
	/**
	 * Create an empty model, initializing the ArrayConfigs to a null value.
	 */
	public Model() {
		name = "model";
		
		floatArray = null;
		indices = null;
		boneIndexArray = null;
		boneWeightArray = null;
		vertexArray = null;
		normalArray = null;
		uvArray = null;
		geometry = null;
		material = null;
		skin = null;
	}
	
	/**
	 * Create a new model with the specified ArrayConfigs
	 * @param fa The float array
	 * @param ind The index array
	 * @param bi Bone indices array
	 * @param bw Bone weights array
	 * @param v Vertex array
	 * @param n Normal array
	 * @param u UV array
	 */
	public Model(String modelName, ArrayList<Float> fa, ArrayList<Short> ind, ArrayConfig bi, ArrayConfig bw, ArrayConfig v, ArrayConfig n, ArrayConfig u) {
		name = modelName;
		floatArray = fa;
		indices = ind;
		boneIndexArray = bi;
		boneWeightArray = bw;
		vertexArray = v;
		normalArray = n;
		uvArray = u;
		packArrays();
		
		geometry = new Geometry(vertices, normals, uvs, indices);
		material = new Material();
		skin = new Skin(new ArrayList<Float>(), new ArrayList<Float>()); //Should be boneIndices, boneWeights. I'm overriding this because it's not quite ready.
	}
	
	////////////////////////
	//CONSTRUCTION METHODS//
	////////////////////////
	
	/**
	 * Pack an array list with values specified by the config being used.
	 * @param toPack The ArrayList to pack
	 * @param cfg The ArrayConfig to use
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" }) //I need to do this so that I can append without it complaining about types, because I know I will do it right.
	//TECHNICALLY I shouldn't have to idiot-proof this method but I might as well.
	protected void pack(ArrayList toPack, ArrayConfig cfg) {
		try {
			for (int count = cfg.offset / 4; count < floatArray.size(); count += cfg.stride / 4) {
				//Get each data point in the FloatArray.
				//Also get each of the values after its size.
				//Here I'll need to check which ArrayConfig it is...
				if (cfg == boneIndexArray || cfg == boneWeightArray) {
					for (int i = 0; i < cfg.size; i++) {
						toPack.add(floatArray.get(count + i));
					}
				} else if (cfg == vertexArray || cfg == normalArray) {
					Vector3f val = new Vector3f(floatArray.get(count + 0), floatArray.get(count + 1), floatArray.get(count + 2));
					toPack.add(val);
				} else if (cfg == uvArray) {
					Vector2f val = new Vector2f(floatArray.get(count + 0), floatArray.get(count + 1));
					toPack.add(val);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Construct the arrays for the model data automatically.
	 */
	protected void packArrays() {
		if (floatArray == null || indices == null) return;
		
		if (boneIndexArray != null) {
			pack(boneIndices, boneIndexArray);
		}
		if (boneWeightArray != null) {
			pack(boneWeights, boneWeightArray);
		}
		if (vertexArray != null) {
			pack(vertices, vertexArray);
		}
		if (normalArray != null) {
			pack(normals, normalArray);
		}
		if (uvArray != null) {
			pack(uvs, uvArray);
		}
	}
}
