package xandragon.converter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.tree.DefaultMutableTreeNode;

import com.threerings.export.BinaryImporter;
import com.threerings.opengl.model.config.*;
import com.threerings.opengl.model.config.ArticulatedConfig.Attachment;
import com.threerings.opengl.model.config.ArticulatedConfig.Node;
import com.threerings.opengl.scene.config.*;
import com.threerings.opengl.model.config.ModelConfig.MeshSet;
import com.threerings.opengl.model.config.ModelConfig.VisibleMesh;

import xandragon.core.ui.tree.*;
import xandragon.util.Logger;

/**
 * A common form of ThreeRings's ModelConfig. This class will be a single class storing all necessary information to form a model from SK.
 * It will also handle the bulk of the reading.
 * @author Xan the Dragon
 */
public class CommonConfig {
	protected static Icon iconData = new Icon();
	
	/** The logger. */
	protected static Logger log;
	
	/** The main configuration of the model being imported.*/
	protected ModelConfig mainConfig;
	
	/** The raw class of the configuration, which can be used to find its type.*/
	protected Object baseModelClass;
	
	/** The rsrc directory. */
	protected static String resourceDir = "";
	
	/** The tree renderer. */
	public static TreeRenderer treeRenderer = null;
	
	/** The name of the model class being used*/
	public String modelClassName;
	
	/** The name of the subclass (if applicable) being used*/
	public String subClassName;
	
	/** Visible meshes*/
	public VisibleMesh[] visibleMeshes;
	
	/** Attachments */
	public VisibleMesh[] attachments;
	
	/** Bones*/
	public Node boneData;
	
	public CommonConfig(Object raw, String fileName, TreeRenderer ren) throws IOException {
		treeRenderer = ren;
		handleModelMain(raw, fileName);
	}
	
	protected void handleModelMain(Object raw, String fileName, DefaultMutableTreeNode lastTreeNode, boolean isAttachment) throws IOException {
		if (raw instanceof ModelConfig) {
			mainConfig = (ModelConfig) raw;
		} else if (raw instanceof AnimationConfig) {
			log.AppendLn("An error occurred! An AnimationConfig (the file imported was an animation) was imported (If you didn't do this, a Compound / Merged model may have called for it). Import a model instead.");
			return;
		} else {
			log.AppendLn("An error occurred! The input model's class is unknown. All that is known is that it's NOT a model (If you didn't do this, a Compound / Merged model may have called for it). Import a model instead.");
			return;
		}
		
		baseModelClass = mainConfig.implementation.copy(null);
		String name = baseModelClass.getClass().getName();
		
		//To what I know, models will either end in "." (blah.blah.blah.ModelName) so I can substring everything to that last . to take out ModelName
		//Though if it's a subclass i.e. Derived it will end in $.
		boolean isSubclass = name.contains((CharSequence) "$");
		if (!isSubclass) {
			modelClassName = name.substring(name.lastIndexOf('.') + 1);
			subClassName = null;
		} else {
			modelClassName = name.substring(name.lastIndexOf('.') + 1, name.lastIndexOf("$"));
			subClassName = name.substring(name.lastIndexOf("$") + 1);
		}
		
		TreeNode typeTreeNode = new TreeNode("Type: "+modelClassName);
		
		if (modelClassName.matches("ModelConfig") && subClassName != null) {
			typeTreeNode.displayText = "Type: "+modelClassName+"$"+subClassName;
			
			if (subClassName.matches("Imported")) {
				typeTreeNode.displayIcon = iconData.unknown;
				//ModelConfig.Imported cfg = (ModelConfig.Imported) mainConfig.implementation;
				
			} else if (subClassName.matches("Derived")) {
				typeTreeNode.displayIcon = iconData.derived;
				ModelConfig.Derived cfg = (ModelConfig.Derived) mainConfig.implementation;
				
				TreeNode refTreeNode = new TreeNode("Model references...", iconData.object);
				
				DefaultMutableTreeNode refTreeNodeTree;
				DefaultMutableTreeNode newModelTreeNodeTree;
				
				if (lastTreeNode == null) {
					//Parent to root.
					treeRenderer.addNodeRoot(typeTreeNode);
					refTreeNodeTree = treeRenderer.addNodeRoot(refTreeNode);
				} else {
					//Parent to the reference folder, aka lastTreeNode
					//Here, we need to create the modelTreeNode
					TreeNode modelTreeNode = new TreeNode(fileName, iconData.model);
					newModelTreeNodeTree = treeRenderer.addNode(lastTreeNode, modelTreeNode);
					//Mode TreeNode made. Now the type + ref go into the new model TreeNode.
					treeRenderer.addNode(newModelTreeNodeTree, typeTreeNode);
					refTreeNodeTree = treeRenderer.addNode(newModelTreeNodeTree, refTreeNode);
				}
				
				openReferenceFromPath(cfg.model.getName(), refTreeNodeTree, isAttachment);
			} else if (subClassName.matches("Schemed")) {
				typeTreeNode.displayIcon = iconData.part;
				ModelConfig.Schemed cfg = (ModelConfig.Schemed) mainConfig.implementation;
				
				TreeNode refTreeNode = new TreeNode("Model references...", iconData.object);
				
				DefaultMutableTreeNode refTreeNodeTree;
				DefaultMutableTreeNode newModelTreeNodeTree;
				
				if (lastTreeNode == null) {
					//Parent to root.
					treeRenderer.addNodeRoot(typeTreeNode);
					refTreeNodeTree = treeRenderer.addNodeRoot(refTreeNode);
				} else {
					//Parent to the reference folder, aka lastTreeNode
					//Here, we need to create the modelTreeNode
					TreeNode modelTreeNode = new TreeNode(fileName, iconData.model);
					newModelTreeNodeTree = treeRenderer.addNode(lastTreeNode, modelTreeNode);
					//Mode TreeNode made. Now the type + ref go into the new model TreeNode.
					treeRenderer.addNode(newModelTreeNodeTree, typeTreeNode);
					refTreeNodeTree = treeRenderer.addNode(newModelTreeNodeTree, refTreeNode);
				}
				
				openReference(cfg, refTreeNodeTree, isAttachment);
			}
			
		} else if (modelClassName.matches("ArticulatedConfig")) {
			ArticulatedConfig cfg = (ArticulatedConfig) mainConfig.implementation;
			typeTreeNode.displayIcon = iconData.person;
			TreeNode refTreeNode = new TreeNode("Attachments", iconData.object);
			
			DefaultMutableTreeNode refTreeNodeTree;
			DefaultMutableTreeNode newModelTreeNodeTree;
			
			if (lastTreeNode == null) {
				//Parent to root.
				treeRenderer.addNodeRoot(typeTreeNode);
				refTreeNodeTree = treeRenderer.addNodeRoot(refTreeNode);
			} else {
				//Parent to the reference folder, aka lastTreeNode
				//Here, we need to create the modelTreeNode
				TreeNode modelTreeNode = new TreeNode(fileName, iconData.model);
				newModelTreeNodeTree = treeRenderer.addNode(lastTreeNode, modelTreeNode);
				//Mode TreeNode made. Now the type + ref go into the new model TreeNode.
				treeRenderer.addNode(newModelTreeNodeTree, typeTreeNode);
				refTreeNodeTree = treeRenderer.addNode(newModelTreeNodeTree, refTreeNode);
			}
			
			if (!isAttachment) {
				visibleMeshes = cfg.skin.visible;
			} else {
				attachments = cfg.skin.visible;
			}
			boneData = cfg.root;
			for (Attachment a : cfg.attachments) {
				if (a.model != null) {
					openReferenceFromPath(a.model.getName(), refTreeNodeTree, true);
				}
			}
			
		} else if (modelClassName.matches("CompoundConfig")) {
			CompoundConfig cfg = (CompoundConfig) mainConfig.implementation;
			
			typeTreeNode.displayIcon = iconData.partgroup;
			TreeNode refTreeNode = new TreeNode("Model references...", iconData.object);
			
			DefaultMutableTreeNode refTreeNodeTree;
			DefaultMutableTreeNode newModelTreeNodeTree;
			
			if (lastTreeNode == null) {
				//Parent to root.
				treeRenderer.addNodeRoot(typeTreeNode);
				refTreeNodeTree = treeRenderer.addNodeRoot(refTreeNode);
			} else {
				//Parent to the reference folder, aka lastTreeNode
				//Here, we need to create the modelTreeNode
				TreeNode modelTreeNode = new TreeNode(fileName, iconData.model);
				newModelTreeNodeTree = treeRenderer.addNode(lastTreeNode, modelTreeNode);
				//Mode TreeNode made. Now the type + ref go into the new model TreeNode.
				treeRenderer.addNode(newModelTreeNodeTree, typeTreeNode);
				refTreeNodeTree = treeRenderer.addNode(newModelTreeNodeTree, refTreeNode);
			}
			
			openReference(cfg, refTreeNodeTree, isAttachment);
			
		} else if (modelClassName.matches("GeneratedStaticConfig")) {
			//GeneratedStaticConfig cfg = (GeneratedStaticConfig) mainConfig.implementation;
			typeTreeNode.displayIcon = iconData.unknown;
			if (lastTreeNode == null) {
				treeRenderer.addNodeRoot(typeTreeNode);
			} else {
				TreeNode modelTreeNode = new TreeNode(fileName, iconData.model);
				DefaultMutableTreeNode newModelTreeNodeTree = treeRenderer.addNode(lastTreeNode, modelTreeNode);
				//Mode TreeNode made. Now the type + ref go into the new model TreeNode.
				treeRenderer.addNode(newModelTreeNodeTree, typeTreeNode);
			}
			//There's no way to read this.
		} else if (modelClassName.matches("MergedStaticConfig")) {
			MergedStaticConfig cfg = (MergedStaticConfig) mainConfig.implementation;

			typeTreeNode.displayIcon = iconData.partgroup;
			TreeNode refTreeNode = new TreeNode("Model references...", iconData.object);
			
			DefaultMutableTreeNode refTreeNodeTree;
			DefaultMutableTreeNode newModelTreeNodeTree;
			
			if (lastTreeNode == null) {
				//Parent to root.
				treeRenderer.addNodeRoot(typeTreeNode);
				refTreeNodeTree = treeRenderer.addNodeRoot(refTreeNode);
			} else {
				//Parent to the reference folder, aka lastTreeNode
				//Here, we need to create the modelTreeNode
				TreeNode modelTreeNode = new TreeNode(fileName, iconData.model);
				newModelTreeNodeTree = treeRenderer.addNode(lastTreeNode, modelTreeNode);
				//Mode TreeNode made. Now the type + ref go into the new model TreeNode.
				treeRenderer.addNode(newModelTreeNodeTree, typeTreeNode);
				refTreeNodeTree = treeRenderer.addNode(newModelTreeNodeTree, refTreeNode);
			}
			openReference(cfg, refTreeNodeTree, isAttachment);
			
		} else if (modelClassName.matches("StaticConfig")) {
			StaticConfig cfg = (StaticConfig) mainConfig.implementation;
			typeTreeNode.displayIcon = iconData.part;
			if (lastTreeNode == null) {
				treeRenderer.addNodeRoot(typeTreeNode);
			} else {
				TreeNode modelTreeNode = new TreeNode(fileName, iconData.model);
				DefaultMutableTreeNode newModelTreeNodeTree = treeRenderer.addNode(lastTreeNode, modelTreeNode);
				//Mode TreeNode made. Now the type + ref go into the new model TreeNode.
				treeRenderer.addNode(newModelTreeNodeTree, typeTreeNode);
			}
			visibleMeshes = cfg.meshes.visible;
			boneData = null;
			attachments = null;
			
		} else if (modelClassName.matches("StaticSetConfig")) {
			StaticSetConfig cfg = (StaticSetConfig) mainConfig.implementation;
			typeTreeNode.displayIcon = iconData.partgroup;
			if (lastTreeNode == null) {
				treeRenderer.addNodeRoot(typeTreeNode);
			} else {
				TreeNode modelTreeNode = new TreeNode(fileName, iconData.model);
				DefaultMutableTreeNode newModelTreeNodeTree = treeRenderer.addNode(lastTreeNode, modelTreeNode);
				//Mode TreeNode made. Now the type + ref go into the new model TreeNode.
				treeRenderer.addNode(newModelTreeNodeTree, typeTreeNode);
			}
			
			MeshSet set = cfg.meshes.get(cfg.model);
			visibleMeshes = set.visible;
			
		} else if (modelClassName.matches("ProjectXModelConfig")) {
			/*
			ProjectXModelConfig cfg = (ProjectXModelConfig) mainConfig.implementation;
			
			typeTreeNode.displayIcon = iconData.person;
			TreeNode refTreeNode = new TreeNode("Attachments", iconData.object);
			
			DefaultMutableTreeNode refTreeNodeTree;
			DefaultMutableTreeNode newModelTreeNodeTree;
			
			if (lastTreeNode == null) {
				//Parent to root.
				treeRenderer.addNodeRoot(typeTreeNode);
				refTreeNodeTree = treeRenderer.addNodeRoot(refTreeNode);
			} else {
				//Parent to the reference folder, aka lastTreeNode
				//Here, we need to create the modelTreeNode
				TreeNode modelTreeNode = new TreeNode(fileName, iconData.model);
				newModelTreeNodeTree = treeRenderer.addNode(lastTreeNode, modelTreeNode);
				//Mode TreeNode made. Now the type + ref go into the new model TreeNode.
				treeRenderer.addNode(newModelTreeNodeTree, typeTreeNode);
				refTreeNodeTree = treeRenderer.addNode(newModelTreeNodeTree, refTreeNode);
			}
			
			if (!isAttachment) {
				visibleMeshes = cfg.skin.visible;
			} else {
				attachments = cfg.skin.visible;
			}
			boneData = cfg.root;
			for (Attachment a : cfg.attachments) {
				if (a.model != null) {
					openReferenceFromPath(a.model.getName(), refTreeNodeTree, true);
				}
			}
			*/
			DefaultMutableTreeNode typeTreeNodeTree;
			typeTreeNode.displayIcon = iconData.unknown;
			if (lastTreeNode == null) {
				typeTreeNodeTree = treeRenderer.addNodeRoot(typeTreeNode);
			} else {
				typeTreeNodeTree = treeRenderer.addNode(lastTreeNode, typeTreeNode);
			}
			TreeNode info = new TreeNode("ProjectXModelConfigs (Knights) cannot be read.", iconData.info);
			TreeNode info2 = new TreeNode("This feature will be implemented soon.", iconData.info);
			treeRenderer.addNode(typeTreeNodeTree, info);
			treeRenderer.addNode(typeTreeNodeTree, info2);
			
		} else if (modelClassName.matches("ViewerAffecterConfig")) {
			//Phew. This is gonna be crazy.
			//Viewer Affecters are things that... well, affect the view. This includes things like skyboxes, particle effects (like the snow on icy levels), ambient sounds, and ambient light.
			//To get the information out of this I have to create raw object of it and see if it casts to any specific subclasses, then handle accordingly.
			ViewerAffecterConfig cfg = (ViewerAffecterConfig) mainConfig.implementation;
			ViewerEffectConfig view_cfg = cfg.effect;
			Object raw_view = view_cfg.copy(null);
			
			typeTreeNode.displayText = "Type: ViewerAffecterConfig"+raw_view.getClass().getName().substring(raw_view.getClass().getName().lastIndexOf('$'));
			if (raw_view instanceof ViewerEffectConfig.Skybox) {
				//Type is skybox.
				
				ViewerEffectConfig.Skybox sky = (ViewerEffectConfig.Skybox) raw_view;
				
				if (sky.model != null) {
					typeTreeNode.displayIcon = iconData.sky;
					TreeNode refTreeNode = new TreeNode("Model references...", iconData.object);
					
					DefaultMutableTreeNode refTreeNodeTree;
					DefaultMutableTreeNode newModelTreeNodeTree;
					
					if (lastTreeNode == null) {
						//Parent to root.
						treeRenderer.addNodeRoot(typeTreeNode);
						refTreeNodeTree = treeRenderer.addNodeRoot(refTreeNode);
					} else {
						//Parent to the reference folder, aka lastTreeNode
						//Here, we need to create the modelTreeNode
						TreeNode modelTreeNode = new TreeNode(fileName, iconData.model);
						newModelTreeNodeTree = treeRenderer.addNode(lastTreeNode, modelTreeNode);
						//Mode TreeNode made. Now the type + ref go into the new model TreeNode.
						treeRenderer.addNode(newModelTreeNodeTree, typeTreeNode);
						refTreeNodeTree = treeRenderer.addNode(newModelTreeNodeTree, refTreeNode);
					}
					openReferenceFromPath(sky.model.getName(), refTreeNodeTree, isAttachment);
					
				} else {
					DefaultMutableTreeNode typeTreeNodeTree;
					typeTreeNode.displayIcon = iconData.unknown;
					if (lastTreeNode == null) {
						typeTreeNodeTree = treeRenderer.addNodeRoot(typeTreeNode);
					} else {
						typeTreeNodeTree = treeRenderer.addNode(lastTreeNode, typeTreeNode);
					}
					TreeNode info = new TreeNode("The file this references is an internal data reference.", iconData.info);
					TreeNode info2 = new TreeNode("Any models of this type inherit its properties.", iconData.info);
					treeRenderer.addNode(typeTreeNodeTree, info);
					treeRenderer.addNode(typeTreeNodeTree, info2);
				}
			} else {
				typeTreeNode.displayIcon = iconData.unknown;
				if (lastTreeNode == null) {
					treeRenderer.addNodeRoot(typeTreeNode);
				} else {
					treeRenderer.addNode(lastTreeNode, typeTreeNode);
				}
			}
		} else {
			typeTreeNode.displayIcon = iconData.unknown;
			if (lastTreeNode == null) {
				treeRenderer.addNodeRoot(typeTreeNode);
			} else {
				TreeNode modelTreeNode = new TreeNode(fileName, iconData.model);
				DefaultMutableTreeNode newModelTreeNodeTree = treeRenderer.addNode(lastTreeNode, modelTreeNode);
				//Mode TreeNode made. Now the type + ref go into the new model TreeNode.
				treeRenderer.addNode(newModelTreeNodeTree, typeTreeNode);
			}
		}
	}
	
	protected void handleModelMain(Object raw, String fileName) throws IOException {
		handleModelMain(raw, fileName, null, false);
	}
	
	protected void openReference(CompoundConfig cfg, DefaultMutableTreeNode parent, boolean isAttachment) throws IOException {
		for (int i = 0; i < cfg.models.length; i++) {
			if (cfg.models[i].model != null) {
				String path = cfg.models[i].model.getName();
				openReferenceFromPath(path, parent, isAttachment);
			}
		}
	}
	
	protected void openReference(MergedStaticConfig cfg, DefaultMutableTreeNode parent, boolean isAttachment) throws IOException {
		for (int i = 0; i < cfg.models.length; i++) {
			if (cfg.models[i].model != null) {
				String path = cfg.models[i].model.getName();
				openReferenceFromPath(path, parent, isAttachment);
			}
		}
	}
	
	protected void openReference(ModelConfig.Schemed cfg, DefaultMutableTreeNode parent, boolean isAttachment) throws IOException {
		for (int i = 0; i < cfg.models.length; i++) {
			if (cfg.models[i].model != null) {
				String path = cfg.models[i].model.getName();
				openReferenceFromPath(path, parent, isAttachment);
			}
		}
	}
	
	protected void openReferenceFromPath(String path, DefaultMutableTreeNode parent, boolean isAttachment) throws IOException {
		File ref = new File(resourceDir + path);
		FileInputStream fileIn = new FileInputStream(ref);
		BinaryImporter stockImporter = new BinaryImporter(fileIn);
		handleModelMain(stockImporter.readObject(), ref.getName(), parent, isAttachment);
		stockImporter.close();
	}
	
	public static void setLogger(Logger l) {
		log = l;
	}
	
	public static void setResourceDir(String dir) {
		resourceDir = dir;
	}
}
