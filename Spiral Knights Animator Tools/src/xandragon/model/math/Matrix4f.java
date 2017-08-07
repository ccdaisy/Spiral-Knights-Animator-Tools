package xandragon.model.math;

/**
 * It's not actually a matrix thing. It's got a single use case, and was big enough for its own class.
 * @author Xan the Dragon
 */
public class Matrix4f {
	
    /** The values of the matrix. */
    public float m00, m10, m20, m30, m01, m11, m21, m31, m02, m12, m22, m32, m03, m13, m23, m33;
    
    public Matrix4f() {
    	set(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
    }
    
    public Matrix4f setFromRotAndPos(Quaternion quat, Vector3f pos) {
    	return setToRotation(quat).setToTranslation(pos);
    }
    
	public Matrix4f setToRotation (Quaternion quat) {
		float xx = quat.x*quat.x, yy = quat.y*quat.y, zz = quat.z*quat.z;
		float xy = quat.x*quat.y, xz = quat.x*quat.z, xw = quat.x*quat.w;
		float yz = quat.y*quat.z, yw = quat.y*quat.w, zw = quat.z*quat.w;
		return set(
				1f - 2f*(yy + zz),	2f*(xy - zw),			2f*(xz + yw),		0f,
				2f*(xy + zw),		1f - 2f*(xx + zz),		2f*(yz - xw),		0f,
				2f*(xz - yw),		2f*(yz + xw),			1f - 2f*(xx + yy),	0f,
				0f,					0f,						0f,					1f
		);
    }
	
	public Matrix4f setToTranslation (Vector3f pos) {
		return set(
				1f, 0f, 0f, pos.x,
	            0f, 1f, 0f, pos.y,
	            0f, 0f, 1f, pos.z,
	            0f, 0f, 0f, 1f
		);
	}
	
	
	public Matrix4f set (
						float m00, float m10, float m20, float m30,
				        float m01, float m11, float m21, float m31,
				        float m02, float m12, float m22, float m32,
				        float m03, float m13, float m23, float m33) {
			this.m00 = m00; this.m01 = m01; this.m02 = m02; this.m03 = m03;
	    	this.m10 = m10; this.m11 = m11; this.m12 = m12; this.m13 = m13;
	    	this.m20 = m20; this.m21 = m21; this.m22 = m22; this.m23 = m23;
	    	this.m30 = m30; this.m31 = m31; this.m32 = m32; this.m33 = m33;
	    return this;
	}
}
