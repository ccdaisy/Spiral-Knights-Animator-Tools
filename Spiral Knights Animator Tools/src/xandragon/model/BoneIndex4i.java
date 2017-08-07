package xandragon.model;

/**
 * The system of quad bone indices stored with ints.
 * Bone indices are interesting in how they work.
 * The four values are composed as so: ROOT_IDX, CHILD_1_IDX, CHILD_2_IDX, CHILD_3_IDX
 * Where each CHILD_X value is used for a depth of children. For instance, I have a shoulder root bone, and an arm bone as a child of the shoulder - The arm would have its index on CHILD_1_INDEX.
 * @author Xan the Dragon
 */
public class BoneIndex4i {
	
	public int a = 0;
	public int b = 0;
	public int c = 0;
	public int d = 0;
	
	/**Create a blank BoneIndex4i*/
	public BoneIndex4i() {}
	
	/**Create a BoneIndex4i*/
	public BoneIndex4i(int _a, int _b, int _c, int _d) {
		a = _a;
		b = _b;
		c = _c;
		d = _d;
	}
	
	/**Create a BoneIndex4i from default float values*/
	public BoneIndex4i(float _a, float _b, float _c, float _d) {
		a = (int) _a;
		b = (int) _b;
		c = (int) _c;
		d = (int) _d;
	}
}
