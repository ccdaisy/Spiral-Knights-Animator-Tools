package xandragon.util.filedata;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class OpenFileFilter extends FileFilter {
	
	protected boolean isDir = false;
	protected String description = "";
	public String fileExt = "";

	public OpenFileFilter(boolean dir) {
		this("", "", dir);
	}
	
	public OpenFileFilter(String extension) {
		this(extension, "", false);
	}

	public OpenFileFilter(String extension, String typeDescription) {
		this(extension, typeDescription, false);
	}
	
	public OpenFileFilter(String extension, String typeDescription, boolean dir) {
		fileExt = extension;
		description = typeDescription;
		isDir = dir;
	}

	public boolean accept(File f) {
		if (f.isDirectory())
			return true;
		if (isDir)
			return true; //just do this so we don't ever compare extension
		return (f.getName().toLowerCase().endsWith(fileExt.toLowerCase()));
	}

	public String getDescription() {
		if (isDir) {
			return "Directories";
		}
		return "." + fileExt.toUpperCase() + " ("+description+")";
	}
}