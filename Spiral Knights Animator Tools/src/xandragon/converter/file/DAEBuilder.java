package xandragon.converter.file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.threerings.opengl.model.Model;
import com.threerings.opengl.model.config.ArticulatedConfig.Attachment;
import com.threerings.opengl.model.config.ArticulatedConfig.Node;
import com.threerings.opengl.model.config.ModelConfig.VisibleMesh;

import xandragon.converter.model.Geometry;

@SuppressWarnings("unused")

public class DAEBuilder {

	protected File OUTPUT_FILE;
	protected Document DOCUMENT;
	protected Element ROOT;
	private Element lib_images;
	private Element lib_effects;
	private Element lib_materials;
	private Element lib_geometry;
	private Element lib_control;
	private Element lib_nodes;
	private Element lib_visual_scene;
	private Element base_scene;
	
	public DAEBuilder(File out) throws ParserConfigurationException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		DOCUMENT = docBuilder.newDocument();
		OUTPUT_FILE = out;
		
		/////////////////////////////////////
		//PART 1: CREATE ROOT AND BASE DATA//
		/////////////////////////////////////
		
		//////Create the root element.
		ROOT = DOCUMENT.createElement("COLLADA");
		ROOT.setAttribute("xmlns", "http://www.collada.org/2005/11/COLLADASchema");
		ROOT.setAttribute("version", "1.4.1");
		DOCUMENT.appendChild(ROOT);
		
		
		//////CREATE METADATA
		Element asset = DOCUMENT.createElement("asset");
		Element contributor = DOCUMENT.createElement("contributor");
		Element authorElement = DOCUMENT.createElement("author");
		Element authoringTool = DOCUMENT.createElement("authoring_tool");
		Element unit = DOCUMENT.createElement("unit");
		Element upAxis = DOCUMENT.createElement("up_axis");
		
		Text author = DOCUMENT.createTextNode("Xan the Dragon");
		Text tool = DOCUMENT.createTextNode("Spiral Knights Animator Tools");
		Text upAxisTxt = DOCUMENT.createTextNode("Z_UP");
		
		authorElement.appendChild(author);
		authoringTool.appendChild(tool);
		
		contributor.appendChild(authorElement);
		contributor.appendChild(authoringTool);
				
		upAxis.appendChild(upAxisTxt);
		
		unit.setAttribute("meter", "1");
		unit.setAttribute("name", "meter");
		
		asset.appendChild(contributor);
		asset.appendChild(upAxis);
		asset.appendChild(unit);
		ROOT.appendChild(asset);
		
		//////////////////////////////////
		//PART 2: CREATE DATA CONTAINERS//
		//////////////////////////////////
		
		//Create library_images element
		lib_images = DOCUMENT.createElement("library_images");
		
		//Create library_effects element
		lib_effects = DOCUMENT.createElement("library_effects");
		
		//Create library_materials element
		lib_materials = DOCUMENT.createElement("library_materials");
		
		//Create library_geometries element
		lib_geometry = DOCUMENT.createElement("library_geometries");
		
		//Create library_nodes element
		lib_nodes = DOCUMENT.createElement("library_nodes");
		
		//Create library_controllers element
		lib_control = DOCUMENT.createElement("library_controllers");
		
		//Create library_visual_scenes element
		lib_visual_scene = DOCUMENT.createElement("library_visual_scenes");
		
		base_scene = DOCUMENT.createElement("visual_scene");
 		base_scene.setAttribute("id", "Scene");
 		base_scene.setAttribute("name", "Scene");
 		lib_visual_scene.appendChild(base_scene);
	}
	
	@SuppressWarnings("rawtypes")
	protected Element appendGeometryData(Geometry geo, ArrayList data, String type, String name) {
		String list = "";
		if (data == geo.vertices) {
			list = geo.createVertexList();
		} else if (data == geo.normals) {
			list = geo.createNormalList();
		} else if (data == geo.uvs) {
			list = geo.createUVList();
		}
		
		int sizeVal = data.size();
		
		//Source
		Element src = DOCUMENT.createElement("source");
		src.setAttribute("id", name+"-mesh-"+type);
		
		//Array
		Element arrayHeading = DOCUMENT.createElement("float_array");
		arrayHeading.setAttribute("id", name+"-mesh-"+type+"-array");
		arrayHeading.setAttribute("count", String.valueOf(sizeVal));
		Text arrayText = DOCUMENT.createTextNode(list);
		arrayHeading.appendChild(arrayText);
		src.appendChild(arrayHeading);
		
		//Technique
		Element technique = DOCUMENT.createElement("technique_common");
		src.appendChild(technique);
		
		//Accessor
		Element accessor = DOCUMENT.createElement("accessor");
		accessor.setAttribute("source", "#"+name+"-mesh-"+type+"-array");
		accessor.setAttribute("count", String.valueOf(sizeVal / (data == geo.uvs ? 2 : 3)));
		accessor.setAttribute("stride", data == geo.uvs ? "2" : "3");
		technique.appendChild(accessor);
		
		//Parameters
		if (data == geo.uvs) {
			Element paramS = DOCUMENT.createElement("param");
			Element paramT = DOCUMENT.createElement("param");
			
			paramS.setAttribute("name", "S");
			paramT.setAttribute("name", "T");
			paramS.setAttribute("type", "float");
			paramT.setAttribute("type", "float");
			
			accessor.appendChild(paramS);
			accessor.appendChild(paramT);
		} else {
			Element paramX = DOCUMENT.createElement("param");
			Element paramY = DOCUMENT.createElement("param");
			Element paramZ = DOCUMENT.createElement("param");
			
			paramX.setAttribute("name", "X");
			paramY.setAttribute("name", "Y");
			paramZ.setAttribute("name", "Z");
			paramX.setAttribute("type", "float");
			paramY.setAttribute("type", "float");
			paramZ.setAttribute("type", "float");
			
			accessor.appendChild(paramX);
			accessor.appendChild(paramY);
			accessor.appendChild(paramZ);
		}
		
		return src;
	}
	
	public void appendNewGeometry(Geometry geo, String name, int ID) {
		//PRE-WRITE: GET VALUES FROM GEOMETRY.
		int vertexSize = geo.vertices.size();
		int normalSize = geo.normals.size();
		int uvSize = geo.uvs.size();
		
		///////////////////
		//PART 1: HEADING//
		///////////////////	
		Element geometryId = DOCUMENT.createElement("geometry");
		geometryId.setAttribute("id", name + ID +"-mesh");
		geometryId.setAttribute("name", name + ID);
		Element mesh = DOCUMENT.createElement("mesh");
		geometryId.appendChild(mesh);
		
		////////////////////
		//PART 2: RAW DATA//
		////////////////////	
		mesh.appendChild(appendGeometryData(geo, geo.vertices, "positions", name + ID));
		mesh.appendChild(appendGeometryData(geo, geo.normals, "normals", name + ID));
		mesh.appendChild(appendGeometryData(geo, geo.uvs, "map-0", name + ID));
		
		//////////////////////
		//PART 3: REFERENCES//
		//////////////////////
		Element vertices = DOCUMENT.createElement("vertices");
 		vertices.setAttribute("id", name + ID +"-mesh-vertices");
 		mesh.appendChild(vertices);
 		
 		Element posInput = DOCUMENT.createElement("input");
 		posInput.setAttribute("semantic", "POSITION");
 		posInput.setAttribute("source", "#"+ name + ID +"-mesh-positions");
 		vertices.appendChild(posInput);
 		
 		Element triangleList = DOCUMENT.createElement("triangles");
 		triangleList.setAttribute("count", String.valueOf(geo.indices.size()));
 		triangleList.setAttribute("material", name + ID + "-material");
 		mesh.appendChild(triangleList);
 		
 		Element vtxInput = DOCUMENT.createElement("input");
 		vtxInput.setAttribute("semantic", "VERTEX");
 		vtxInput.setAttribute("source", "#"+ name + ID +"-mesh-vertices");
 		vtxInput.setAttribute("offset", "0");
 		triangleList.appendChild(vtxInput);
 		
 		Element nrmInput = DOCUMENT.createElement("input");
 		nrmInput.setAttribute("semantic", "NORMAL");
 		nrmInput.setAttribute("source", "#"+ name + ID +"-mesh-normals");
 		nrmInput.setAttribute("offset", "0");
 		triangleList.appendChild(nrmInput);
 		
 		Element uvInput = DOCUMENT.createElement("input");
 		uvInput.setAttribute("semantic", "TEXCOORD");
 		uvInput.setAttribute("source", "#"+ name + ID +"-mesh-map-0");
 		uvInput.setAttribute("offset", "0");
 		uvInput.setAttribute("set", "0");
 		triangleList.appendChild(uvInput);
 		
 		////////////////////
 		//PART 4: INDEXING//
 		////////////////////
 		Element idxElement = DOCUMENT.createElement("p");
 		Text idxValue = DOCUMENT.createTextNode(geo.createIndexList());
 		idxElement.appendChild(idxValue);
 		triangleList.appendChild(idxElement);
 		
 		//Complete:
 		lib_geometry.appendChild(geometryId);
	}
	
	public void appendNewBoneData(String name, int ID) {
		//Append the base scene data.
		Element scNode = DOCUMENT.createElement("node");
		scNode.setAttribute("id", name + ID);
		scNode.setAttribute("name", name + ID);
		scNode.setAttribute("type", "NODE");
		base_scene.appendChild(scNode);
		
		//Then...
		
		//NOTE: I'M COMMENTING THESE OUT SO THAT IT ALWAYS WORKS.
		
		//if (skin.boneIndices.size() == 0 || skin.boneWeights.size() == 0) {
			//Bone data is not present in this model.
			//Append the closing data.
			Element trsMatrix = DOCUMENT.createElement("matrix");
			trsMatrix.setAttribute("sid", "transform");
			Text mtxVal = DOCUMENT.createTextNode("1 0 0 0 0 1 0 0 0 0 1 0 0 0 0 1"); //Identity matrix.
			trsMatrix.appendChild(mtxVal);
			scNode.appendChild(trsMatrix);
			
			Element geoInstance = DOCUMENT.createElement("instance_geometry");
			geoInstance.setAttribute("url", "#"+ name + ID +"-mesh");
			geoInstance.setAttribute("name", name + ID);
			scNode.appendChild(geoInstance);
			
			Element mtlBinding = DOCUMENT.createElement("bind_material");
			geoInstance.appendChild(mtlBinding);
			Element technique = DOCUMENT.createElement("technique_common");
			mtlBinding.appendChild(technique);
			
			Element mtlInstance = DOCUMENT.createElement("instance_material");
			mtlInstance.setAttribute("symbol", name + ID + "-material");
			mtlInstance.setAttribute("target", "#" + name + ID + "-material");
			technique.appendChild(mtlInstance);
			
			//And then be done with it.
			return;
		//}
		
		//Continue
		
		
	}
	
	public void appendNewMaterial(String name, int ID) {
		////////////////////////////
		//PART 1: MATERIAL HEADING//
		////////////////////////////
		Element materialId = DOCUMENT.createElement("material");
		materialId.setAttribute("id", name + ID +"-material");
		materialId.setAttribute("name", name + ID);
		
		Element mtlEffect = DOCUMENT.createElement("effect");
		mtlEffect.setAttribute("id", name + ID + "-effect");
		
		Element mtlProfile = DOCUMENT.createElement("profile_COMMON");
		Element mtlTechnique = DOCUMENT.createElement("technique");
		Element shadeType = DOCUMENT.createElement("phong");
		
		mtlTechnique.setAttribute("sid", "common");
		
		mtlEffect.appendChild(mtlProfile);
		mtlProfile.appendChild(mtlTechnique);
		mtlTechnique.appendChild(shadeType);
		
		/////////////////////////
		//PART 2: MATERIAL DATA//
		/////////////////////////
		
		Element emission = DOCUMENT.createElement("emission");
		Element ambient = DOCUMENT.createElement("ambient");
		Element diffuse = DOCUMENT.createElement("diffuse");
		Element specular = DOCUMENT.createElement("specular");
		Element shininess = DOCUMENT.createElement("shininess");
		Element refraction = DOCUMENT.createElement("index_of_refraction");
		shadeType.appendChild(emission);
		shadeType.appendChild(ambient);
		shadeType.appendChild(diffuse);
		shadeType.appendChild(specular);
		shadeType.appendChild(shininess);
		shadeType.appendChild(refraction);
		
		Element color_Emission = DOCUMENT.createElement("color");
		color_Emission.setAttribute("sid", "emission");
		emission.appendChild(color_Emission);
		Text value_Emission = DOCUMENT.createTextNode("0 0 0 1");
		color_Emission.appendChild(value_Emission);
		
		
		Element color_Ambient = DOCUMENT.createElement("color");
		color_Ambient.setAttribute("sid", "ambient");
		ambient.appendChild(color_Ambient);
		Text value_Ambient = DOCUMENT.createTextNode("0 0 0 1");
		color_Ambient.appendChild(value_Ambient);
		
		
		Element color_Diffuse = DOCUMENT.createElement("color");
		color_Diffuse.setAttribute("sid", "diffuse");
		diffuse.appendChild(color_Diffuse);
		Text value_Diffuse = DOCUMENT.createTextNode("1 1 1 1");
		color_Diffuse.appendChild(value_Diffuse);
		
		
		Element color_Specular = DOCUMENT.createElement("color");
		color_Specular.setAttribute("sid", "specular");
		specular.appendChild(color_Specular);
		Text Value_Specular = DOCUMENT.createTextNode("0 0 0 1");
		color_Specular.appendChild(Value_Specular);
		
		
		Element number_Shininess = DOCUMENT.createElement("color");
		number_Shininess.setAttribute("sid", "shininess");
		shininess.appendChild(number_Shininess);
		Text value_Shininess = DOCUMENT.createTextNode("0");
		number_Shininess.appendChild(value_Shininess);
		
		
		Element float_Refraction = DOCUMENT.createElement("float");
		float_Refraction.setAttribute("sid", "index_of_refraction");
		refraction.appendChild(float_Refraction);
		Text value_Refraction = DOCUMENT.createTextNode("1");
		float_Refraction.appendChild(value_Refraction);
		
		Element instanceEffect = DOCUMENT.createElement("instance_effect");
		instanceEffect.setAttribute("url", "#" + name + ID + "-effect");
		materialId.appendChild(instanceEffect);
		
		//And finally append
		lib_materials.appendChild(materialId);
		lib_effects.appendChild(mtlEffect);
	}
	
	public void createDAE() throws TransformerException {
		//Finish up the whole thing.
		ROOT.appendChild(lib_images);
		ROOT.appendChild(lib_effects);
		ROOT.appendChild(lib_materials);
		ROOT.appendChild(lib_geometry);
		ROOT.appendChild(lib_control);
		ROOT.appendChild(lib_nodes);
		ROOT.appendChild(lib_visual_scene);
		lib_visual_scene.appendChild(base_scene);
		
		Element scene = DOCUMENT.createElement("scene");
		Element visScene = DOCUMENT.createElement("instance_visual_scene");
		visScene.setAttribute("url", "#scene");
		scene.appendChild(visScene);
		ROOT.appendChild(scene);
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		
		DOMSource source = new DOMSource(DOCUMENT);
		StreamResult result = new StreamResult(new StringWriter());//new StreamResult(System.out);//new StreamResult(new FileOutputStream(_out));
		
		transformer.transform(source, result);
		
		String xmlString = result.getWriter().toString();
    	
    	FileWriter fileWriter;
    	try {
    		fileWriter = new FileWriter(OUTPUT_FILE);
            fileWriter.write(xmlString);
            fileWriter.flush();
            fileWriter.close();
    	} catch (IOException e) {
    	 
        }
	}
}
