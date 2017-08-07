package xandragon.model;

import java.util.ArrayList;

/**
 * Stores bone data.
 * Has handy functions to automatically compile the text for DAE formatting.
 * @author Xan the Dragon
 */
public class Skin {
	/**An array of the stored bone indices in this model. Note: This must be cast to Integer when used, even though it's stored as a float.*/
	public ArrayList<BoneIndex4i> boneIndices = null;
	
	/**An array of modified bone indices*/
	public ArrayList<Integer> boneIndicesModded = null;
	
	/**An array of the stored bone weights in this model*/
	public ArrayList<BoneIndex4i> boneWeights = null;
	
	/**An array of the stored bone names in this model.*/
	public ArrayList<Bone> boneData = null;
	
	/**Constructor for ease of creation.*/
	public Skin(ArrayList<BoneIndex4i> _boneIndices, ArrayList<BoneIndex4i> _boneWeights, ArrayList<Bone> _boneData) {
		boneIndices = _boneIndices;
		boneWeights = _boneWeights;
		boneData = _boneData;
		
		//So to get indices now, I'm going to go through each hierarchy level on its own.
		//...TODO.
	}
	
	public String getBoneNameList() {
		String ret = "";
		/*
		 * Bone indices use a really weird hierarchy system.
		 * 
		 * A root bone will only use the first of its four values (the size of the indices and weighs values are 4)
		 * However, a bone that's connected as a child of a root bone will use the first value and have the first value the same as the root's value, but it will also
		 * have its own second value set. Because of this, I must rewrite the indices on my own.
		 */
		for (int i = 0; i < boneIndices.size(); i++) {
			
		}
		return ret;
	}
}
