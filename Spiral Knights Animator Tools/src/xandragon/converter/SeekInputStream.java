package xandragon.converter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.io.DataInputStream;
import java.io.EOFException;

/**
 * A rewritten implementation of a DataInputStream that allows for seeking through what you've already read.<br>
 * <br>
 * This was written for personal usage in instances when I needed to rewind, especially in an instance where I'm skipping a set of specific characters.<br>
 * In order to check if the character is one of those specific ones, I need to call read() which causes it to move ahead. In the instance that it's NOT<br>
 * one of those specific characters, this can cause issues because I've just cut off the first bit of what I may need to read.<br>
 * <br>
 * Unfortunately, this only does its magic with the read(). You can wrap your own version of things like readFloat() and such with a {@link java.nio.ByteBuffer}
 * 
 * @author Xan the Dragon
 */
public class SeekInputStream {
	
	/**The current position of the stream*/
	protected int position = 0;
	
	/**The current stream.*/
	protected DataInputStream stream;
	
	/**The current cache of read values*/
	protected ArrayList<Integer> readValues = new ArrayList<Integer>();
	
	/**
	 * Construct a new SeekInputStream
	 * @param in A DataInputStream to read from.
	 */
	public SeekInputStream(DataInputStream in) {
		stream = in;
	}
	
	/**
	 * Reads the next value relative to the current set position in the stream.
	 * @returns The value read, or -1 if the end of file is met;
	 */
	public int read() throws IOException {
		if (position == readValues.size()) {
			//If the position is equal to the size of the list, that means we're at the end and need to read a new value.
			int readValue = stream.read();
			if (readValue != -1) {
				//Not EOF
				readValues.add(readValue);
				position++;
			}
			return readValue;
		} else if (position < readValues.size()) {
			//Otherwise we need to read from something we already got.
			int readValue = readValues.get(position).intValue();
			position++;
			return readValue;
		}
		return -1;
	}
	
	/**
	 * Read the next amount of bytes, but only move forward by one byte. Useful for telling what bytes might be ahead without skipping anything.
	 * @param bytes The amount of bytes to read ahead by
	 * @return A list of the read values
	 */
	public int[] readAhead(int bytes) throws IOException, EOFException {
		int[] values = new int[bytes];
		int actuallyMovedBy = 0;
		for (int i = 0; i < bytes; i++) {
			int readValue = read();
			values[i] = readValue;
			if (readValue != -1) {
				actuallyMovedBy++;
			} else {
				throw new EOFException();
			}
		}
		skipBackwards(actuallyMovedBy-1); //Subtract 1 so it still actually goes forward.
		return values;
	}
	
	/**
	 * Read a byte value.
	 * @returns The value read.
	 */
	public byte readByte() throws IOException {
		return (byte) read();
	}
	
	
	/**
	 * Read a short value.
	 * @return The short value being read.
	 */
	public short readShort() throws IOException {
		byte[] b = new byte[]{readByte(), readByte()};
		return ByteBuffer.wrap(b).getShort();
	}
	
	/**
	 * Skips ahead by a specified length of bytes.<br>
	 * @param n The amount of bytes to skip by.
	 * @return Returns -1 if seeking failed. Will otherwise return how many bytes were skipped.
	 */
	public long skip(long n) throws IOException {
		for (long i = 0; i < n; i++) {
			int v = read();
			if (v == -1) {
				return -1;
			}
		}
		return n;
	}
	
	/** 
	 * Seeks the stream backwards by forcing it to read cached data that has already been read.
	 * @param amount The amount of bytes to back by.
	 * @return The amount of bytes moved backwards, or -1 if moving by that amount of bytes was not possible (due to going before the start of the file)
	 */
	public int skipBackwards(int amount) {
		if (amount > position) {
			return -1;
		}
		position-= amount; //Just change the position value. See the read function for why.
		return amount;
	}
	
	/**
	 * Seek to a specific point in the stream.<br>
	 * <strong>This will forcefully read from the stream when attempting to seek past a point that has already been read.</strong>
	 * @param index The byte to go to.
	 * @returns -1 if seeking failed. Will otherwise return the index that the stream is currently located at.
	 */
	public int seek(int index) throws IOException {
		if (index > readValues.size()) {
			//Trying to read past what we can.
			position = readValues.size(); //Push the position to the end of what we've read.
			for (int i = position; i < index; i++) {
				int v = read(); //Continue reading until we hit our goal.
				if (v == -1) {
					position = i; //Set the position value to the end of the stream.
					return -1;
				}
			}
		}
		position = index; //This is common between if we read past what we have already read and if we're just reading normally.
		return index;
	}
	
	/**
	 * @return The current location in the stream.
	 */
	public int at() {
		return position;
	}
	
	/**
	 * Rewind the stream back to its start
	 */
	public void rewind() {
		position = 0;
	}
}
