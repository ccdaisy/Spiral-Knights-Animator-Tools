package xandragon.converter.file;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

@SuppressWarnings("unused")
public class DAEBuilder {
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
	
	public DAEBuilder() throws ParserConfigurationException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		DOCUMENT = docBuilder.newDocument();
		
		/////////////////////////////////////
		//PART 1: CREATE ROOT AND BASE DATA//
		/////////////////////////////////////
		
		//////Create the root element.
		ROOT = DOCUMENT.createElement("COLLADA");
		ROOT.setAttribute("xmlns", "http://www.collada.org/2005/11/COLLADASchema");
		ROOT.setAttribute("version", "1.4.1");
		DOCUMENT.appendChild(ROOT);
		
		
		//////CREATE METADATA
		Element Asset = DOCUMENT.createElement("asset");
		Element Contributor = DOCUMENT.createElement("contributor");
		Element AuthorElement = DOCUMENT.createElement("author");
		Element AuthoringTool = DOCUMENT.createElement("authoring_tool");
		Element Unit = DOCUMENT.createElement("unit");
		Element UpAxis = DOCUMENT.createElement("up_axis");
		
		Text Author = DOCUMENT.createTextNode("Brent \"Xan the Dragon\" D.");
		Text Tool = DOCUMENT.createTextNode("Spiral Knights Model Converter");
		Text UpAxisTxt = DOCUMENT.createTextNode("Z_UP");
		
		AuthorElement.appendChild(Author);
		AuthoringTool.appendChild(Tool);
		
		Contributor.appendChild(AuthorElement);
		Contributor.appendChild(AuthoringTool);
				
		UpAxis.appendChild(UpAxisTxt);
		
		Unit.setAttribute("meter", "1");
		Unit.setAttribute("name", "meter");
		
		Asset.appendChild(Contributor);
		Asset.appendChild(UpAxis);
		Asset.appendChild(Unit);
		ROOT.appendChild(Asset);
		
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
		
		
	}
	
	public void AppendNewMaterial() {
		/////////////////////////
		//PART 3: MATERIAL DATA//
		/////////////////////////
		Element MatEffect = DOCUMENT.createElement("effect");
		MatEffect.setAttribute("id", "Material-effect");
		lib_effects.appendChild(MatEffect);
		
		Element MatProfile = DOCUMENT.createElement("profile_COMMON");
		Element MatTechnique = DOCUMENT.createElement("technique");
		Element ShadeType = DOCUMENT.createElement("phong");
		
		MatTechnique.setAttribute("sid", "common");
		
		MatEffect.appendChild(MatProfile);
		MatProfile.appendChild(MatTechnique);
		MatTechnique.appendChild(ShadeType);
		
		Element Emission = DOCUMENT.createElement("emission");
		Element Ambient = DOCUMENT.createElement("ambient");
		Element Diffuse = DOCUMENT.createElement("diffuse");
		Element Specular = DOCUMENT.createElement("specular");
		Element Shininess = DOCUMENT.createElement("shininess");
		Element Refraction = DOCUMENT.createElement("index_of_refraction");
		ShadeType.appendChild(Emission);
		ShadeType.appendChild(Ambient);
		ShadeType.appendChild(Diffuse);
		ShadeType.appendChild(Specular);
		ShadeType.appendChild(Shininess);
		ShadeType.appendChild(Refraction);
		
		Element Color_Emission = DOCUMENT.createElement("color");
		Color_Emission.setAttribute("sid", "emission");
		Emission.appendChild(Color_Emission);
		Text Value_Emission = DOCUMENT.createTextNode("0 0 0 1");
		Color_Emission.appendChild(Value_Emission);
		
		
		Element Color_Ambient = DOCUMENT.createElement("color");
		Color_Ambient.setAttribute("sid", "ambient");
		Ambient.appendChild(Color_Ambient);
		Text Value_Ambient = DOCUMENT.createTextNode("0 0 0 1");
		Color_Ambient.appendChild(Value_Ambient);
		
		
		Element Color_Diffuse = DOCUMENT.createElement("color");
		Color_Diffuse.setAttribute("sid", "diffuse");
		Diffuse.appendChild(Color_Diffuse);
		Text Value_Diffuse = DOCUMENT.createTextNode("1 1 1 1");
		Color_Diffuse.appendChild(Value_Diffuse);
		
		
		Element Color_Specular = DOCUMENT.createElement("color");
		Color_Specular.setAttribute("sid", "specular");
		Specular.appendChild(Color_Specular);
		Text Value_Specular = DOCUMENT.createTextNode("0 0 0 1");
		Color_Specular.appendChild(Value_Specular);
		
		
		Element Number_Shininess = DOCUMENT.createElement("color");
		Number_Shininess.setAttribute("sid", "shininess");
		Shininess.appendChild(Number_Shininess);
		Text Value_Shininess = DOCUMENT.createTextNode("0");
		Number_Shininess.appendChild(Value_Shininess);
		
		
		Element FloatValue = DOCUMENT.createElement("float");
		FloatValue.setAttribute("sid", "index_of_refraction");
		Refraction.appendChild(FloatValue);
		Text Value_Refraction = DOCUMENT.createTextNode("1");
		FloatValue.appendChild(Value_Refraction);
		
		//Now that the material is set up, create the reference itself.
		Element Material = DOCUMENT.createElement("material");
		Material.setAttribute("id", "Material-material");
		Material.setAttribute("name", "material");
		lib_materials.appendChild(Material);
		Element InstanceEffect = DOCUMENT.createElement("instance_effect");
		InstanceEffect.setAttribute("url", "#Material-effect");
		Material.appendChild(InstanceEffect);
	}
	
	public void AppendBoneData() {
		//TODO: Write this. Still debating on what to make it take in.
	}
	
	public void AppendGeometry() {
		//TODO: Create some class capable of storing all of the geometric data. (The old method was absolutely terrible. Passed in 50 different arrays.)
	}
}
