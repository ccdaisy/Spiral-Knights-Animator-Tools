package xandragon.converter.model;

import java.nio.ShortBuffer;
import java.util.ArrayList;

import com.threerings.opengl.geometry.config.GeometryConfig;
import com.threerings.opengl.model.config.ArticulatedConfig.MeshNode;
import com.threerings.opengl.model.config.ArticulatedConfig.Node;
import com.threerings.opengl.model.config.ModelConfig.VisibleMesh;
import com.threerings.opengl.renderer.config.ClientArrayConfig;

public class Model {
	
	public String name = "model";
	public ArrayList<Geometry> geometry = new ArrayList<Geometry>();
	public ArrayList<BoneDepth> bonelist = new ArrayList<BoneDepth>();
	public ArrayList<Skin> skin = new ArrayList<Skin>();
	
	public Model(VisibleMesh[] meshes, VisibleMesh[] attachments, Node bones) {
		if (meshes != null) {
			for (VisibleMesh geometryCfg : meshes) {
				handleGeometry(geometryCfg);
			}
		}
		
		if (attachments != null) {
			for (VisibleMesh geometryCfg : attachments) {
				handleGeometry(geometryCfg);
			}
		}
		
		if (bones != null) {
			int depth = 0;
			//Note to self: "bones" is just the root bone. Not a set of bones.
			bonelist.add(new BoneDepth(depth, bones));
			search(bones, depth);
			
			//From here we should have an entire hierarchy of bones.
			for (BoneDepth boneDepth : bonelist) {
				Node bone = boneDepth.bone;
				//int dep = boneDepth.depth;
				if (bone instanceof MeshNode) {
					MeshNode meshBone = (MeshNode) bone;
					handleGeometry(meshBone.visible);
				}
			}
		}
	}
	
	protected void handleGeometry(VisibleMesh geometryCfg) {
		GeometryConfig.Stored geo = (GeometryConfig.Stored) geometryCfg.geometry;
		GeometryConfig.IndexedStored idgeo = new GeometryConfig.IndexedStored();
		GeometryConfig.IndexedStored sidgeo = new GeometryConfig.SkinnedIndexedStored();
		if (geometryCfg.geometry instanceof GeometryConfig.IndexedStored) {
			idgeo = (GeometryConfig.IndexedStored) geometryCfg.geometry;
		}
		if (geometryCfg.geometry instanceof GeometryConfig.SkinnedIndexedStored) {
			sidgeo = (GeometryConfig.SkinnedIndexedStored) geometryCfg.geometry;
		}
		
		ClientArrayConfig vertices = geo.vertexArray;
		ClientArrayConfig normals = geo.normalArray;
		ClientArrayConfig uvs = geo.texCoordArrays != null ? geo.texCoordArrays[0] : (idgeo.texCoordArrays != null ? idgeo.texCoordArrays[0] : (sidgeo.texCoordArrays != null ? sidgeo.texCoordArrays[0] : null));
		ClientArrayConfig vertexAttributes = geo.vertexAttribArrays != null ? geo.vertexAttribArrays[0] : (idgeo.vertexAttribArrays != null ? idgeo.vertexAttribArrays[0] : (sidgeo.vertexAttribArrays != null ? sidgeo.vertexAttribArrays[0] : null));
		ShortBuffer indices = idgeo.indices != null ? idgeo.indices : sidgeo.indices;
		
		geometry.add(new Geometry(vertices, normals, uvs, vertexAttributes, indices));
	}
	
	protected void search(Node b, int d) {
		for (Node bone : b.children) {
			bonelist.add(new BoneDepth(d + 1, bone));
			search(bone, d + 1);
		}
	}
	
}
