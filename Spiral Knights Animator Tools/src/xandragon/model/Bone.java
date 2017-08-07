package xandragon.model;

import xandragon.model.math.Matrix4f;

public class Bone {
	public Matrix4f boneTransform;
	public BoneIndex4i index;
	public String name;
	
	public Bone(String n) {
		this(n, new Matrix4f(), null);
	}
	
	public Bone(String n, Matrix4f trs) {
		this(n, trs, null);
	}
	
	public Bone(String n, Matrix4f trs, BoneIndex4i ind) {
		name = n;
		boneTransform = trs;
		index = ind;
	}
}
