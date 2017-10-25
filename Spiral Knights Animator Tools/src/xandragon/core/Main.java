package xandragon.core;

import java.io.*;

import xandragon.converter.BinaryParser;
import xandragon.core.ui.ErrorGui;
import xandragon.core.ui.MainGui;
import xandragon.util.Logger;
import xandragon.util.filedata.DataPersistence;

public class Main {
	
	/**The input directory and filename.*/
	protected static File INPUT_FILE = null;
	
	/**The output directory.*/
	protected static File OUTPUT_FILE = null;
	
	/**The output logging system.*/
	protected static Logger log = null; //This is set to null initially. It is actually set when we determine what to use (console vs ui).
	
	public static void main(String[] args) throws Exception { //I'm so lazy. "throws Exception". Wow.
		int ArgCount = args.length;
		BinaryParser parser = new BinaryParser(log = new Logger());
		
		//File jarDir = DataPersistence.getJarDir().getParentFile();
		//String path = jarDir.getPath() + File.separator + "code";
		//boolean exists = new File(path).exists();
		
		/*
		if (ArgCount == 2) {
			//if (!exists) {
			//	log.AppendLn("ERROR! ProjectX Code cannot be found!\nPlease put this JAR into the Spiral Knights root directory.");
			//	return;
			//}
			
			INPUT_FILE = new File(args[0]);
			OUTPUT_FILE = new File(args[1]);
			File t = INPUT_FILE;
			while (t.getName().matches("rsrc") == false) {
				t = t.getParentFile();
			}
			parser.setResourceDir(t.getPath());
			parser.process(INPUT_FILE);
			
			String ext = OUTPUT_FILE.getName().substring(OUTPUT_FILE.getName().lastIndexOf('.'));
			if (ext.matches(".dae")) {
				parser.saveDAE(OUTPUT_FILE);
			}
		} else {
		*/
			//if (!exists) {
			//	new ErrorGui();
			//	return;
			//}
			MainGui maingui = new MainGui(parser);
			log.setLabel(maingui.UI_Label);
			maingui.setLog(log);
		//}
	}
	
}
