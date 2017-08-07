package xandragon.converter;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.zip.InflaterInputStream;

import javax.swing.tree.DefaultMutableTreeNode;

import xandragon.converter.file.OBJBuilder;
import xandragon.core.ui.tree.DataTreePath;
import xandragon.core.ui.tree.Icon;
import xandragon.core.ui.tree.Node;
import xandragon.core.ui.tree.TreeRenderer;
import xandragon.util.exception.InvalidDatException;
import xandragon.util.stream.StreamHelper;
import xandragon.util.Logger;
import xandragon.util.StringTool;

public class BinaryParser {
	
	/**The output logging system.*/
	protected static Logger log = null;
	
	/** Identifies the file type. */
    public static final int MAGIC_NUMBER = 0xFACEAF0E;

    /** Latest version of the format. */
    public static final short DEFAULT_VERSION = 0x1002;
    
    /** Intermediate ID version of the format. */
    public static final short INTERMEDIATE_VERSION = 0x1001;
    
    /** Classic version of the format. */
    public static final short CLASSIC_VERSION = 0x1000;

    /** The compressed format flag. */
    public static final short COMPRESSED_FORMAT_FLAG = 0x1000;
    
    /** The icon database*/
    protected static final Icon iconData = new Icon();
    
	/**
	 * Instantiate a new BinaryParser, inputting the file to read as well as a logger for output.
	 * @param _log The log to write to.
	 */
	public BinaryParser(Logger _log) {
		log = _log;
	}
	
	/**
	 * Set the logger of this parser.
	 * @param _log The log to write to.
	 */
	public void setLog(Logger _log) {
		log = _log;
	}
	
	/**
	 * Pre-process data. This is only used by MainGui so that it can show the data tree before actually parsing the model data.
	 * @param input_file The opened file.
	 */
	public DataTreePath preProcess(File input_file) throws IOException, InvalidDatException {
		if ((input_file == null) || (input_file != null && input_file.exists() == false)) {
			throw new FileNotFoundException("The file was not found!");
		}
		
		TreeRenderer renderer = new TreeRenderer(new Node("Model \""+input_file.getName()+"\"", iconData.model));
		
		FileInputStream fileInput = new FileInputStream(input_file);
		DataInputStream stream = new DataInputStream(fileInput);
		
		int magic = stream.readInt();
		
		if (magic != MAGIC_NUMBER) {
			log.AppendLn("InvalidDatException thrown!");
			log.AppendLn("This means the DAT file you've opened is malformed in some way. Here's the supplied reason:");
			log.AppendLn();
			log.AppendLn("[INVALID ID] Expected ID 0xFACEAF0E, got "+ StringTool.intToHex(magic, 0) + " - This is not a valid DAT file.");
			log.AppendLn();
			log.AppendLn("Conversion Failed.");
			stream.close();
			return null;
		}
		
		short version = stream.readShort();
		short compressedFlag = stream.readShort();
		boolean isCompressed = (compressedFlag & COMPRESSED_FORMAT_FLAG) != 0;
		
		renderer.addNodeRoot(new Node("Model tag: "+StringTool.intToHex(magic, 0) + " (Valid)", iconData.tag));
		renderer.addNodeRoot(new Node("Compressed: "+(isCompressed ? "True" : "False") + " ["+StringTool.intToHex(compressedFlag, 0)+"]", iconData.tag));
		
		//ModelData(String _magicnumber, String _version, String _compressed, String _implementation, String _faSize, String _saSize)
		
		if (version == DEFAULT_VERSION) {
			renderer.addNodeRoot(new Node("Version: DEFAULT_VERSION [0x1002]", iconData.tag));
		} else if (version == INTERMEDIATE_VERSION) {
			renderer.addNodeRoot(new Node("Version: INTERMEDIATE_VERSION [0x1001]", iconData.tag));
		} else if (version == CLASSIC_VERSION) {
			renderer.addNodeRoot(new Node("Version: CLASSIC_VERSION [0x1000]", iconData.tag));
		} else {
			renderer.addNodeRoot(new Node("Version: UNKNOWN_VERSION ["+StringTool.intToHex(compressedFlag, 0)+"]", iconData.tag));
		}
		
		if (isCompressed) {
			stream = new DataInputStream(new InflaterInputStream(stream));
		}
		
		SeekInputStream data = new SeekInputStream(stream);
		StreamHelper helper = new StreamHelper(data);
		
		data.skip(3);											//Skip the first 3 header values.
		helper.getDataUntilControl();							//Get the root class.
		helper.skipNextControlCharacters();						//Skip the following control characters
		helper.getDataUntilControl();							//Run this but don't worry about the data. Using it to skip more stuff.
		helper.skipNextControlCharacters();						//Skip the following control characters.
		helper.getDataUntilControl();							//Again
		helper.skipNextControlCharacters();						//...
		helper.getDataUntilControl();							//This is just a copy/paste. Scroll down to see what I am doing.
		helper.skipNextControlCharacters();
		data.skip(1);
		helper.getDataUntilControl();
		helper.skipNextControlCharacters();
		data.skip(1);
		String imp = helper.getDataUntilControl();
		imp = imp.substring(imp.lastIndexOf(".") + 1);
		renderer.addNodeRoot(new Node("Type: "+imp, Node.getIconFromImplementation(imp)));
		
		data.rewind();
		int[] sequenceStartInit = StringTool.stringToIntArray("floatArray");
		helper.skipUntilSequenceMet(sequenceStartInit);
		data.skip(sequenceStartInit.length-1); //Skip that -1 so I can read and store.
		int lookingFor = data.read();
		
		int[] sequenceStartMain = StringTool.stringToIntArray("java.nio.FloatBuffer");
		helper.skipUntilSequenceMet(sequenceStartMain);
		data.skip(sequenceStartMain.length);
		
		helper.skipUntilCharacterMet((char) lookingFor);
		data.skip(4);
		ArrayList<Float> floatArray = GetFloatArray(data);
		int floatCount = floatArray.size();
		
		data.rewind();
		int[] sequenceShort = StringTool.stringToIntArray("java.nio.ShortBuffer");
		helper.skipUntilSequenceMet(sequenceShort);
		data.skip(sequenceShort.length);
		
		//The count value is just before two NULs.
		int[] next = data.readAhead(2);
		while (next[0] != 0x00 && next[1] != 0x00) {
			next = data.readAhead(2);
		}
		data.skip(2); //so skip those NULs
		//Now we need to find the first short value. This value will be the length of the array.
		short arrLen = data.readShort();
		renderer.addNodeRoot(new Node("Indices: "+String.valueOf(arrLen), iconData.value));
		
		DefaultMutableTreeNode fArray = renderer.addNodeRoot(new Node("FloatArray (size="+String.valueOf(floatCount)+")", iconData.array));
				
		int allSize = 0;
		int stride = 0;
		int nextOffset = 0;
		
		allSize += (canFindData(helper, "boneIndices") ? 4 : 0) + (canFindData(helper, "boneWeights") ? 4 : 0) + (canFindData(helper, "texCoordArray") ? 2 : 0) + (canFindData(helper, "normalArray") ? 3 : 0) + (canFindData(helper, "vertexArray") ? 3 : 0);
		stride = allSize * 4;
		
		if (canFindData(helper, "boneIndices")) {
			DefaultMutableTreeNode n = renderer.addNode(fArray, new Node("Bone indices: "+String.valueOf((int) floatCount / stride * 4), iconData.value));
			renderer.addNode(n, new Node("Stride: "+String.valueOf(stride), iconData.value));
			renderer.addNode(n, new Node("Offset: "+String.valueOf(nextOffset), iconData.value));
			nextOffset += (4 * 4);
		} else {
			DefaultMutableTreeNode n = renderer.addNode(fArray, new Node("Bone indices: NONE", iconData.value));
			renderer.addNode(n, new Node("Stride: --", iconData.value));
			renderer.addNode(n, new Node("Offset: --", iconData.value));
		}
		if (canFindData(helper, "boneWeights")) {
			DefaultMutableTreeNode n = renderer.addNode(fArray, new Node("Bone weights: "+String.valueOf((int) floatCount / stride * 4), iconData.value));
			renderer.addNode(n, new Node("Stride: "+String.valueOf(stride), iconData.value));
			renderer.addNode(n, new Node("Offset: "+String.valueOf(nextOffset), iconData.value));
			nextOffset += (4 * 4);
		} else {
			DefaultMutableTreeNode n = renderer.addNode(fArray, new Node("Bone weights: NONE", iconData.value));
			renderer.addNode(n, new Node("Stride: --", iconData.value));
			renderer.addNode(n, new Node("Offset: --", iconData.value));
		}
		if (canFindData(helper, "texCoordArray")) {
			DefaultMutableTreeNode n = renderer.addNode(fArray, new Node("UV Coords: "+String.valueOf((int) floatCount / stride * 4), iconData.value));
			renderer.addNode(n, new Node("Stride: "+String.valueOf(stride), iconData.value));
			renderer.addNode(n, new Node("Offset: "+String.valueOf(nextOffset), iconData.value));
			nextOffset += (4 * 2);
		} else {
			DefaultMutableTreeNode n = renderer.addNode(fArray, new Node("UV Coords: NONE", iconData.value));
			renderer.addNode(n, new Node("Stride: --", iconData.value));
			renderer.addNode(n, new Node("Offset: --", iconData.value));
		}
		if (canFindData(helper, "normalArray")) {
			DefaultMutableTreeNode n = renderer.addNode(fArray, new Node("Normals: "+String.valueOf((int) floatCount / stride * 4), iconData.value));
			renderer.addNode(n, new Node("Stride: "+String.valueOf(stride), iconData.value));
			renderer.addNode(n, new Node("Offset: "+String.valueOf(nextOffset), iconData.value));
			nextOffset += (4 * 3);
		} else {
			DefaultMutableTreeNode n = renderer.addNode(fArray, new Node("Normals: NONE", iconData.value));
			renderer.addNode(n, new Node("Stride: --", iconData.value));
			renderer.addNode(n, new Node("Offset: --", iconData.value));
		}
		if (canFindData(helper, "vertexArray")) {
			DefaultMutableTreeNode n = renderer.addNode(fArray, new Node("Vertices: "+String.valueOf((int) floatCount / stride * 4), iconData.value));
			renderer.addNode(n, new Node("Stride: "+String.valueOf(stride), iconData.value));
			renderer.addNode(n, new Node("Offset: "+String.valueOf(nextOffset), iconData.value));
			nextOffset += (4 * 3);
		} else {
			DefaultMutableTreeNode n = renderer.addNode(fArray, new Node("Vertices: NONE", iconData.value));
			renderer.addNode(n, new Node("Stride: --", iconData.value));
			renderer.addNode(n, new Node("Offset: --", iconData.value));
		}
		//modelData.set("attachments", "Attachments: ");
		
		return renderer.getDataTreePath();
	}
	
	
	@SuppressWarnings({ "resource", "unused" })
	/**
	 * Start the reader, validating or getting the following information: <ul>
	 * <li>The DAT file is using the Spiral Knights format</li>
	 * <li>The version of the DAT file</li>
	 * <li>If the DAT is compressed.</li>
	 * </ul>
	 * If all of these conditions are met, it will continue to read.
	 * @throws FileNotFoundException The reference to the file doesn't exist.
	 * @throws IOException An error occurred while trying to read.
	 * @throws InvalidDatException The DAT file is malformed.
	 */
	public void startProcessing(File input_file, File output_file) throws FileNotFoundException, IOException, InvalidDatException {
		if ((input_file == null) || (input_file != null && input_file.exists() == false)) {
			throw new FileNotFoundException("The input file does not exist!");
		}
		
		//////////////////////////////
		//PART 1: VALIDATE FILE DATA//
		//////////////////////////////
		log.AppendLn("Reading file "+input_file.getName()+" from directory "+input_file.getParent()+"...\n");
		
		FileInputStream fileInput = new FileInputStream(input_file);
		DataInputStream stream = new DataInputStream(fileInput);
		int magic = stream.readInt();
		if (magic != MAGIC_NUMBER) { //0xFACEAF0E
			throw new InvalidDatException("[INVALID ID] Expected ID 0xFACEAF0E, got "+ StringTool.intToHex(magic, 0) + " - This is not a valid DAT file.");
		}
		
		short version = stream.readShort();
		short compressedFlag = stream.readShort();
		boolean isCompressed = (compressedFlag & COMPRESSED_FORMAT_FLAG) != 0;
		
		if (version == DEFAULT_VERSION) {
			//log.AppendLn("File version: DEFAULT_VERSION [0x1002]");
		} else if (version == INTERMEDIATE_VERSION) {
			//log.AppendLn("File version: INTERMEDIATE_VERSION [0x1001]");
		} else if (version == CLASSIC_VERSION) {
			//log.AppendLn("File version: CLASSIC_VERSION [0x1000]");
		} else {
			throw new InvalidDatException("[INVALID VERSION] The version of this DAT file is malformed. The file can not be read.");
		}
		
		if (isCompressed) {
			stream = new DataInputStream(new InflaterInputStream(stream));
			log.AppendLn("DAT File was compressed, decompression successful.");
		}
			
		//////////////////////////
		//PART 2: READING BINARY//
		//////////////////////////
		log.AppendLn("Beginning data collection process...\n");
		SeekInputStream data = new SeekInputStream(stream);
		StreamHelper helper = new StreamHelper(data);
		
		data.skip(3);											//Skip the first 3 header values.
		helper.getDataUntilControl();			//Get the root class.		
		helper.skipNextControlCharacters();						//Skip the following control characters
		helper.getDataUntilControl();							//Run this but don't worry about the data. Using it to skip more stuff.
		//Skipped: "configs"
		helper.skipNextControlCharacters();						//Skip the following control characters.
		helper.getDataUntilControl();							//Again
		//Skipped: "#com.threerings.config.ConfigManager"
		helper.skipNextControlCharacters();						//...
		helper.getDataUntilControl();
		//Skipped: "implementation"
		helper.skipNextControlCharacters();
		data.skip(1);
		//Skipped: "="
		helper.getDataUntilControl();
		//Skipped: "threerings.opengl.model.config.ModelConfig$Implementation"
		helper.skipNextControlCharacters();
		data.skip(1);
		//Skipped a number as a character.
		String implementation = helper.getDataUntilControl();
		implementation = implementation.substring(implementation.lastIndexOf(".") + 1);
		//log.AppendLn("Model implementation: "+implementation);
		
		////////////////////
		//READ FLOAT ARRAY//
		////////////////////
		
		//TODO: Find a reliable way to locate the FloatArray's starting value. The time from when it has the string "java.nio.FloatBuffer" and to the first value is always different.
		//UPD: I THINK I've found a really strange method that's reliably giving me the start of the array.
		//In the hex view, you see the following:
		/*
		 * floatArray~..java.nio.floatBuffer(blahblahblah)~
		 * That tilde (~) is what I'm focusing on.
		 * What I've noticed is that after it defines the floatArray it has a character of any type. After passing java.nio.floatBuffer, that
		 * character can be found again. When it's found (read() is called on it and it's the same), you skip exactly 4 bytes and then you're on the
		 * first value of the array. That's so strange... 
		 */
		
		//Well, here goes nothing.
		
		data.rewind();
		int[] sequenceStartInit = StringTool.stringToIntArray("floatArray");
		helper.skipUntilSequenceMet(sequenceStartInit);
		data.skip(sequenceStartInit.length-1); //Skip that -1 so I can read and store.
		int lookingFor = data.read();
		
		int[] sequenceStartMain = StringTool.stringToIntArray("java.nio.FloatBuffer");
		helper.skipUntilSequenceMet(sequenceStartMain);
		data.skip(sequenceStartMain.length);
		
		helper.skipUntilCharacterMet((char) lookingFor);
		data.skip(4);
		//And now I should theoretically be on the start of the array.
		
		ArrayList<Float> floatArray = GetFloatArray(data);
		//log.AppendLn("Model data length: "+floatArray.size()+"\n");
		
		//Float array is over.
		//Now, read along.
		//Should we encounter 00 0B or 00 0E, we have hit the name of a value referencing the float array
		//Alternatively, hitting 00 07 means I have hit the index array, which is stored via short values.
		//This is where I can use the readAhead method.
		
		//What's really interesting about this is that the format is always sorted in this order:
		//boneIndices, boneWeights, Indices, normals, vertices, texture coords
		//It has never changed in all of the models I've read from.
		//log.AppendLn("Searching for array configs (Bones)...");
		
		//Now I have to manually search. Rewind after every read, find specific data names.
		
		data.rewind();
		int[] sequenceShort = StringTool.stringToIntArray("java.nio.ShortBuffer");
		helper.skipUntilSequenceMet(sequenceShort);
		data.skip(sequenceShort.length);
		int[] next = data.readAhead(2);
		while (next[0] != 0x00 && next[1] != 0x00) {
			next = data.readAhead(2);
		}
		data.skip(2); //so skip those NULs
		short arrLen = data.readShort();
		ArrayList<Short> indices = GetIndices(data, arrLen);
		
		ArrayConfig boneIndices = null;
		ArrayConfig boneWeights = null;
		ArrayConfig vertexArray = null;
		ArrayConfig normalArray = null;
		ArrayConfig uvArray = null;
		
		int allSize = 0;
		int stride = 0;
		int nextOffset = 0;
		
		allSize += (canFindData(helper, "boneIndices") ? 4 : 0) + (canFindData(helper, "boneWeights") ? 4 : 0) + (canFindData(helper, "texCoordArray") ? 2 : 0) + (canFindData(helper, "normalArray") ? 3 : 0) + (canFindData(helper, "vertexArray") ? 3 : 0);
		stride = allSize * 4;
		
		if (canFindData(helper, "boneIndices")) {
			boneIndices = new ArrayConfig("boneIndices", 4, stride, nextOffset);
			nextOffset += (4 * 4);
		}
		if (canFindData(helper, "boneWeights")) {
			boneWeights = new ArrayConfig("boneWeights", 4, stride, nextOffset);
			nextOffset += (4 * 4);
		}
		if (canFindData(helper, "texCoordArray")) {
			uvArray = new ArrayConfig("uvArray", 2, stride, nextOffset);
			nextOffset += (2 * 4);
		}
		if (canFindData(helper, "normalArray")) {
			normalArray = new ArrayConfig("normalArray", 3, stride, nextOffset);
			nextOffset += (3 * 4);
		}
		if (canFindData(helper, "vertexArray")) {
			vertexArray = new ArrayConfig("vertexArray", 3, stride, nextOffset);
			nextOffset += (3 * 4);
		}
		
		log.AppendLn("Data collection complete! Generating file...\n");
		
		output_file.createNewFile();
		OBJBuilder obj = new OBJBuilder(output_file);
		obj.autoWrite(floatArray, vertexArray, normalArray, uvArray, indices);
		obj.close();
		
		log.AppendLn("Conversion complete.");
	}
	
	protected ArrayList<Short> GetIndices(SeekInputStream stream, short amount) throws IOException {
		ArrayList<Short> shortArray = new ArrayList<Short>();
		for (short i = 0; i < amount; i++) {
			shortArray.add(stream.readShort());
		}
		return shortArray;
	}
	
	protected boolean canFindData(StreamHelper helper, String searchFor) throws IOException {
		boolean ok = true;
		int at = helper.stream.at();
		helper.stream.rewind();
		try {
			helper.skipUntilTextMet(searchFor);
		} catch (EOFException e) {
			ok = false;
		} finally {
			helper.stream.seek(at);
		}
		return ok;
	}
	
	protected ArrayList<Float> GetFloatArray(SeekInputStream stream) {
		ArrayList<Float> floatArray = new ArrayList<Float>();
		try {
			while (true) {
				byte b1 = stream.readByte();
				byte b2 = stream.readByte();
				byte b3 = stream.readByte();
				byte b4 = stream.readByte();
				
				if (!(b1 > 0 && b1 < 32)) {
					ByteBuffer wrapped = ByteBuffer.wrap(new byte[]{b1, b2, b3, b4});
					//Not a control character. Weird that all of the values in the floatarray never start on a control character.
							//They can start on NUL but that's not used for specific identity like SOH or STX
					floatArray.add(wrapped.getFloat(0));
				} else {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return floatArray;
	}
}