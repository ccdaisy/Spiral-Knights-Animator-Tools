package xandragon.model;

import java.util.ArrayList;

/**
 * Stores bone data.
 * Has handy functions to automatically compile the text for DAE formatting.
 * @author Xan the Dragon
 */
public class Skin {
	/**An array of the stored bone indices in this model. Note: This must be cast to Integer when used, even though it's stored as a float.*/
	public ArrayList<Float> boneIndices = null;
	
	/**An array of the stored bone weights in this model*/
	public ArrayList<Float> boneWeights = null;
	
	/**Constructor for ease of creation.*/
	public Skin(ArrayList<Float> bi, ArrayList<Float> bw) {
		boneIndices = bi;
		boneWeights = bw;
	}
}
