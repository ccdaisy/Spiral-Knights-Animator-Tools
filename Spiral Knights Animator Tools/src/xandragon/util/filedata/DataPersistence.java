package xandragon.util.filedata;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Main class responsible for data persistence (i.e. setting resources directory)
 * @author Xan the Dragon
 */
public class DataPersistence {
	public boolean isDevBuild = false;
	protected File thisJarLocation;
	protected File thisJar;
	
	public DataPersistence() throws URISyntaxException {
		thisJar = new File(xandragon.core.Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		if (thisJar.isDirectory()) {
			isDevBuild = true; //This will point to the bin folder if it's a development build.
		}
		thisJarLocation = thisJar.getParentFile();
	}
	
	public File getSavedResourceDirectory() {
		if (thisJarLocation == null) {
			return null;
		}
		File dataFile = new File(thisJarLocation.getPath() + File.separator + "converterResourceDir");
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
	
	public void setRsrcDir(File f) {
		try {
			if (f.exists() && f.isDirectory()) {
				BufferedWriter bw = new BufferedWriter(new FileWriter(new File(thisJarLocation.getPath() + File.separator + "converterResourceDir")));
				bw.write(f.getPath());
				bw.close();
			}
		} catch (Exception e) {
			
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
