package xandragon.converter.model;

import com.threerings.opengl.model.config.ArticulatedConfig.Node;

public class BoneDepth {
	public int depth = 0;
	public Node bone = null;
	
	public BoneDepth(int d, Node b) {
		depth = d;
		bone = b;
	}
}
