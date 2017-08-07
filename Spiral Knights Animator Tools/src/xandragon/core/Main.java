package xandragon.core;

import java.io.File;
import java.io.IOException;

import xandragon.converter.BinaryParser;
import xandragon.core.ui.MainGui;
import xandragon.util.exception.InvalidDatException;
import xandragon.util.filedata.FileValidator;
import xandragon.util.Logger;

public class Main {
	
	/**The input directory and filename.*/
	protected static File INPUT_FILE = null;
	
	/**The output directory.*/
	protected static File OUTPUT_FILE = null;
	
	/**The output logging system.*/
	protected static Logger log = null; //This is set to null initially. It is actually set when we determine what to use.
	
	public static void main(String[] args) throws IOException {
		int ArgCount = args.length;
		BinaryParser parser = new BinaryParser(log = new Logger());
		if (ArgCount == 0) {
			//We have supplied no arguments, open the GUI.
			try {
				MainGui maingui = new MainGui(parser);
				log.setLabel(maingui.UI_Label);
				maingui.setLog(log);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (ArgCount == 1 && (args[0].matches("-?") || args[0].matches("-h"))) {
			log.AppendLn(helpText);
		} else if (ArgCount == 2) {
			//Any amount of args.
			//Input should always be first.
			//Output should always be second.
			INPUT_FILE = new File(args[0]);
			OUTPUT_FILE = new File(args[1]);
			
			if (!INPUT_FILE.exists() || INPUT_FILE.isDirectory()) {
				log.AppendLn("[ERROR] " + (INPUT_FILE.exists() ? "Input file is a directory!" : "Input file does not exist!") );
				return;
			}
			
			String outputExt = FileValidator.getFileExtension(OUTPUT_FILE);
			if (!FileValidator.isValidOutput(outputExt)) {
				log.AppendLn("[ERROR] Invalid output extension \""+outputExt+"\"! Expected the extension(s): {"+FileValidator.getInputList()+"}");
				return;
			}
			
			try {
				parser.startProcessing(INPUT_FILE, OUTPUT_FILE);
			} catch (IOException e) {
				log.AppendLn("A critical read exception occurred and conversion was not able to continue.");
			} catch (InvalidDatException e) {
				log.AppendLn(e.getMessage());
				log.AppendLn("Reading is unable to continue.");
			}
		} else {
			log.Append("Invalid argument(s)! ");
			log.AppendLn(helpText);
		}
	}
	
	/**
	 * The text used for help. (Command line -?)
	 */
	protected static final String helpText = "Expected argument format:\n (THIS JAR) \"input/file.dat\" \"output/file.dae\"";
}
