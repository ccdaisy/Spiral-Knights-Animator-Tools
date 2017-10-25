package xandragon.converter;

import java.io.File;
import java.io.FileInputStream;

import com.threerings.export.BinaryImporter;
import com.threerings.opengl.model.config.ArticulatedConfig.Node;
import com.threerings.opengl.model.config.ModelConfig.VisibleMesh;

import xandragon.converter.file.DAEBuilder;
import xandragon.converter.model.Model;
import xandragon.core.ui.tree.Icon;
import xandragon.core.ui.tree.TreeNode;
import xandragon.core.ui.tree.TreeRenderer;
import xandragon.util.Logger;

public class BinaryParser {
	protected static Icon iconData = new Icon();
	
	/**The output logging system.*/
	protected static Logger log = null;
	
	/** The rsrc directory */
	protected String resourceDir = "";
	
	/** The model created from loading the DAT.*/
	protected Model processedModel = null;
	
	/** The tree node renderer for passing into the converter.*/
	public TreeRenderer treeRen = null;
	
	/** Visible meshes*/
	public VisibleMesh[] visibleMeshes;
	
	/** Attachments */
	public VisibleMesh[] attachments;
	
	/** Bones*/
	public Node boneData;
	
	/**
	 * Instantiate a new BinaryParser, inputting the file to read as well as a logger for output.
	 * @param _log The log to write to.
	 */
	public BinaryParser(Logger _log) {
		log = _log;
		CommonConfig.setLogger(_log);
	}
	
	/**
	 * Set the logger of this parser.
	 * @param _log The log to write to.
	 */
	public void setLog(Logger _log) {
		log = _log;
		CommonConfig.setLogger(_log);
	}
	
	public void setResourceDir(String dir) {
		resourceDir = dir;
		CommonConfig.setResourceDir(resourceDir);
	}
	
	/**
	 * Process a DAT file, storing all of the information about it.
	 * @param INPUT_FILE The file.
	 */
	public void process(File INPUT_FILE) throws Exception {
		FileInputStream fileIn = new FileInputStream(INPUT_FILE);
		BinaryImporter stockImporter = new BinaryImporter(fileIn); //Standard ThreeRings binary importer. Same kind used in Spiral Spy.
		treeRen = new TreeRenderer(new TreeNode(INPUT_FILE.getName(), iconData.model));
		Object rawModel = stockImporter.readObject();
		stockImporter.close();
		
		CommonConfig com = new CommonConfig(rawModel, INPUT_FILE.getName(), treeRen);
		visibleMeshes = com.visibleMeshes;
		attachments = com.attachments;
		boneData = com.boneData;
		
		processedModel = new Model(visibleMeshes, attachments, boneData);
	}
	
	/**
	 * Save a DAE
	 * @param OUTPUT_FILE The file to write to.
	 * @throws Exception
	 */
	public void saveDAE(File OUTPUT_FILE) throws Exception {
		DAEBuilder builder = new DAEBuilder(OUTPUT_FILE);
		
		for (int i = 0; i < processedModel.geometry.size(); i++) {
			builder.appendNewGeometry(processedModel.geometry.get(i), "Model", i);
			builder.appendNewBoneData("Model", i);
			builder.appendNewMaterial("Material", i);
		}
		
		builder.createDAE();
	}
}