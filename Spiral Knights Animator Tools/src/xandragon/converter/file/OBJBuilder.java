package xandragon.converter.file;

import java.io.*;
import java.util.ArrayList;

import xandragon.converter.ArrayConfig;

public class OBJBuilder {
	
	protected File outputFile;
	protected FileWriter fw;
	protected BufferedWriter bw;
	
	protected String verts;
	protected String norms;
	protected String uvs;
	protected String indices;
	
	public OBJBuilder(File f) throws IOException {
		outputFile = f;
		outputFile.createNewFile();
		fw = new FileWriter(outputFile);
		bw = new BufferedWriter(fw);
		
		bw.write("#Generated by Spiral Knights Animator Tools - Prototype 1\n");
		bw.write("o model\n\n");
	}
	
	public void writeVertex(float x, float y, float z) throws IOException {
		bw.write("v "+x+" "+y+" "+z+"\n");
	}
	
	public void writeNormal(float x, float y, float z) throws IOException {
		bw.write("vn "+x+" "+y+" "+z+"\n");
	}
	
	public void writeUV(float x, float y) throws IOException {
		bw.write("vt "+x+" "+y+"\n");
	}
	
	public void writeIndices(ArrayList<Short> indices) throws IOException {
		int j = 0;
		bw.write("\nf ");
		for (int i = 0; i < indices.size(); i++) {
			if (j == 3) {
				j = 0;
				bw.write("\nf ");
			}
			int val = indices.get(i).shortValue() + 1;
			bw.write(val+"/"+val+"/"+val+" ");
			j++;
		}
	}
	
	public void close() throws IOException {
		bw.flush();
		bw.close();
	}
	
	public void autoWrite(ArrayList<Float> floatArray, ArrayConfig vertexArray, ArrayConfig normalArray, ArrayConfig uvArray, ArrayList<Short> inds) throws IOException {
		if (vertexArray != null) {
			for (int i = vertexArray.offset / 4; i < floatArray.size(); i+=vertexArray.stride / 4) {
				try {
					writeVertex(
						floatArray.get(i+0),
						floatArray.get(i+1),
						floatArray.get(i+2)
					);
				} catch (Exception e) {
					
				}
			}
		}
		
		if (normalArray != null) {
			for (int i = normalArray.offset / 4; i < floatArray.size(); i+=normalArray.stride / 4) {
				try {
					writeNormal(
						floatArray.get(i+0),
						floatArray.get(i+1),
						floatArray.get(i+2)
					);
				} catch (Exception e) {
					
				}
			}
		}
		
		if (uvArray != null) {
			for (int i = uvArray.offset / 4; i < floatArray.size(); i+=uvArray.stride / 4) {
				try {
					writeUV(
						floatArray.get(i+0),
						floatArray.get(i+1)
					);
				} catch (Exception e) {
					
				}
			}
		}
		
		writeIndices(inds);
	}
}
