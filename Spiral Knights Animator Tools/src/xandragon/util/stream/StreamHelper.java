package xandragon.util.stream;

import java.io.IOException;
import xandragon.converter.SeekInputStream;
import xandragon.util.StringTool;
import xandragon.util.exception.InvalidDatException;

/**
 * A very very quick utility to make reading go by a bit faster.
 * @author Xan the Dragon
 */
public class StreamHelper {
	public SeekInputStream stream;
	
	public StreamHelper(SeekInputStream s) {
		stream = s;
	}
	
	/**
	 * Will read all data and compile it into a string until read() returns a value that is a control character.
	 * @return A string of all the characters read.
	 */
	public String getDataUntilControl() throws IOException, InvalidDatException {
		String ret = "";
		while (true) {
			int b = stream.read();
			if (b >= 32) {
				//Not a control.
				ret = ret + String.valueOf((char) b);
			} else if (b == -1) {
				throw new InvalidDatException();
			} else {
				break;
			}
		}
		return ret;
	}
	
	/**
	 * Will read through the data stream until it finds a specific sequence of bytes
	 * @param sequence A sequence of integer values that will be recognized.
	 */
	public void skipUntilSequenceMet(int[] sequence) throws IOException, InvalidDatException {
		if (sequence.length == 0) {
			return;
		}
	
		int len = sequence.length;
		boolean similar = compare(stream.readAhead(len), sequence);
		while (!similar) {
			similar = compare(stream.readAhead(len), sequence);
		}
	}
	
	/**
	 * Will read through the data stream until it finds a specific sequence of bytes.
	 * @param stringSequence The text to find.
	 * @throws IOException 
	 * @throws InvalidDatException 
	 */
	public void skipUntilTextMet(String stringSequence) throws InvalidDatException, IOException {
		skipUntilSequenceMet(StringTool.stringToIntArray(stringSequence));
	}
	
	/**
	 * Will read through the data stream until it finds the specified character.
	 * @param c The character to find.
	 */
	public void skipUntilCharacterMet(char c) throws IOException, InvalidDatException {
		char r = (char) stream.read();
		while (r != c) {
			r = (char) stream.read();
		}
	}
	
	/**
	 * Reads the next value, checks if it's a control character. If it is, it will skip that and test the next value. Otherwise it will stop skipping.
	 * @throws IOException
	 */
	public void skipNextControlCharacters() throws IOException, InvalidDatException {
		while (true) {
			int b = stream.read();
			if (b >= 32) {
				stream.skipBackwards(1);
				break; //Then stop.
			} else if (b == -1) {
				throw new InvalidDatException();
			}
		}
	}
	
	protected boolean compare(int[] a, int[] b) {
		if (a.length != b.length) {
			return false;
		}
		
		boolean ret = true;
		for (int i = 0; i < a.length; i++) {
			if (a[i] != b[i]) {
				ret = false;
			}
		}
		return ret;
	}
}
