package xandragon.util.filedata;

import java.io.File;

/**
 * Simple command line validation system, since it decides which format to export by via output file name.
 * @author Xan the Dragon
 */
public class FileValidator {
	protected static String[] validInput = new String[]{"dat"};
	protected static String[] validOutput = new String[]{"obj", "dae", "xml"};
	protected static String inputList = null;
	protected static String outputList = null;
	
	public static String getInputList() {
		if (inputList != null) {
			return inputList;
		}
		inputList = "";
		for (String item : validInput) {
			if (item != validInput[validInput.length - 1]) {
				inputList = inputList + "\"" + item.toLowerCase() + "\", ";
			} else {
				inputList = inputList + "\"" + item.toLowerCase() + "\"";
			}
		}
		return inputList;
	}
	
	public static String getOutputList() {
		if (outputList != null) {
			return outputList;
		}
		outputList = "";
		for (String item : validOutput) {
			if (item != validOutput[validOutput.length - 1]) {
				outputList = outputList + "\"" + item.toLowerCase() + "\", ";
			} else {
				outputList = outputList + "\"" + item.toLowerCase() + "\"";
			}
		}
		return outputList;
	}
	
	public static boolean isValidInput(String ext) {
		for (String valid : validInput) {
			if (ext.toLowerCase().matches(valid)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isValidOutput(String ext) {
		for (String valid : validOutput) {
			if (ext.toLowerCase().matches(valid)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isValidInput(File f) {
		return isValidInput(getFileExtension(f));
	}
	
	public static boolean isValidOutput(File f) {
		return isValidOutput(getFileExtension(f));
	}
	
	public static String getFileExtension(File f) {
		if (!f.isDirectory()) {
			String name = f.getName();
			int lastIndex = name.lastIndexOf('.');
			if (lastIndex != -1) {
				return name.substring(lastIndex+1);
			}
		}
		return "";
	}
}
