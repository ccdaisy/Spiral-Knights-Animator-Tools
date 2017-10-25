package xandragon.converter.model;

import java.nio.ShortBuffer;
import java.util.ArrayList;

import com.threerings.opengl.renderer.config.ClientArrayConfig;

public class Geometry {
	
	public ArrayList<Float> vertices = new ArrayList<Float>();
	public ArrayList<Float> normals = new ArrayList<Float>();
	public ArrayList<Float> uvs = new ArrayList<Float>();
	public ArrayList<Float> vertexAttributes = new ArrayList<Float>();
	public ArrayList<Short> indices = new ArrayList<Short>();
	
	public Geometry(ClientArrayConfig _vertices, ClientArrayConfig _normals, ClientArrayConfig _uvs, ClientArrayConfig _vertexAttributes, ShortBuffer _indices) {
		
		int stride, offset;
		
		stride = _vertices.stride / 4;
		offset = _vertices.offset / 4;
		for (int i = offset; i < _vertices.floatArray.limit(); i+=stride) {
			float x = _vertices.floatArray.get(i);
			float y = _vertices.floatArray.get(i + 1);
			float z = _vertices.floatArray.get(i + 2);
			vertices.add(x);
			vertices.add(y);
			vertices.add(z);
		}
		
		stride = _normals.stride / 4;
		offset = _normals.offset / 4;
		for (int i = offset; i < _normals.floatArray.limit(); i+=stride) {
			float x = _normals.floatArray.get(i);
			float y = _normals.floatArray.get(i + 1);
			float z = _normals.floatArray.get(i + 2);
			normals.add(x);
			normals.add(y);
			normals.add(z);
		}
		
		if (_uvs != null) {
			stride = _uvs.stride / 4;
			offset = _uvs.offset / 4;
			for (int i = offset; i < _uvs.floatArray.limit(); i+=stride) {
				float u = _uvs.floatArray.get(i);
				float v = _uvs.floatArray.get(i + 1);
				uvs.add(u);
				uvs.add(v);
			}
		}
		
		if (_vertexAttributes != null) {
			stride = _vertexAttributes.stride / 4;
			offset = _vertexAttributes.offset / 4;
			for (int i = offset; i < _vertexAttributes.floatArray.limit(); i+=stride) {
				float x = _vertexAttributes.floatArray.get(i);
				float y = _vertexAttributes.floatArray.get(i + 1);
				float z = _vertexAttributes.floatArray.get(i + 2);
				float w = _vertexAttributes.floatArray.get(i + 3);
				vertexAttributes.add(x);
				vertexAttributes.add(y);
				vertexAttributes.add(z);
				vertexAttributes.add(w);
			}
		}
		
		for (int i = 0; i < _indices.limit(); i++) {
			indices.add(_indices.get());
		}
	}
	
	public String createVertexList() {
		String list = "";
		for (Float f : vertices) {
			list = list + f.floatValue() + " ";
		}
		list = list.trim();
		return list;
	}
	
	public String createNormalList() {
		String list = "";
		for (Float f : normals) {
			list = list + f.floatValue() + " ";
		}
		list = list.trim();
		return list;
	}
	
	public String createUVList() {
		String list = "";
		for (Float f : uvs) {
			list = list + f.floatValue() + " ";
		}
		list = list.trim();
		return list;
	}
	
	public String createIndexList() {
		String list = "";
		for (Short f : indices) {
			list = list + f.shortValue() + " ";
		}
		list = list.trim();
		return list;
	}
}
