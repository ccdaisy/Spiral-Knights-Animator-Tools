package xandragon.util.filedata;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Main class responsible for data persistence (i.e. setting resources directory)
 * @author Xan the Dragon
 */
public class DataPersistence {
	public boolean isDevBuild = false;
	protected File thisJarLocation = null;
	
	public static File getJarDir() {
		String path = xandragon.core.Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		path = path.replaceAll("%20", " ");
		
		return new File(path);
	}
	
	public DataPersistence() {
		String OS = System.getProperty("os.name").toLowerCase();
		String path = "/";
		if (OS.indexOf("win") != -1) {
			path = System.getenv("LOCALAPPDATA") + File.separator;
		} else if (OS.indexOf("mac") != -1) {
			path = System.getProperty("user.home") + File.separator + "Library" + File.separator + "Application Support" + File.separator + "SKAnimatorTools" + File.separator;
		}
		thisJarLocation = new File(path);
	}
	
	public File getSavedResourceDirectory() {
		if (thisJarLocation == null) {
			return null;
		}
		File dataFile = new File(thisJarLocation.getPath() + File.separator + "ConverterResourceDir");
		if (dataFile.exists()) {
			return getRsrcFromFile(dataFile);
		} else {
			try {
				dataFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return thisJarLocation;
	}
	
	public void setSavedResourceDirectory(File f) {
		try {
			if (f.exists() && f.isDirectory()) {
				BufferedWriter bw = new BufferedWriter(new FileWriter(new File(thisJarLocation.getPath() + File.separator + "ConverterResourceDir")));
				bw.write(f.getPath());
				bw.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected File getRsrcFromFile(File f) {
		File returnValue = thisJarLocation;
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = br.readLine();
			returnValue = new File(line);
			if (returnValue.exists() == false || returnValue.isDirectory() == false) {
				returnValue = thisJarLocation;
			}
			br.close();
		} catch (Exception e) {
			
		}
		return returnValue;
	}
}
