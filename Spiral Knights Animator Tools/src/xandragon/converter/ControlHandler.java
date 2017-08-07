package xandragon.converter;

import java.io.DataInputStream;
import java.util.HashMap;

public class ControlHandler {
	public static enum HandleType {
		IGNORE,
		READ,
		TEXT,
		UNKNOWN
	}
	
	public static enum DatSection {
		VECTOR3,
		VECTOR2,
		QUATERNION,
		FLOATARRAY
	}
	
	public static final int NUL = 0x00;	//Null.
	public static final int SOH = 0x01;	//Start of Header, first character of a message header.
    public static final int STX = 0x02;	//Start of Text, first character of message text. Can be used to terminate SOH.
    public static final int ETX = 0x03;	//End of Text, used as a "break" to terminate a program or process.
    public static final int EOT = 0x04;	//End of Transmission, signify EOF (End Of File) - Used to mark the end of the bytes used to store a value.
    public static final int ENQ = 0x05;	//Enquiry - Send a signal to see if something is still present (Unused in this?)
    public static final int ACK = 0x06;	//Acknowledge - Respond to ENQ with "Yes" (Unused?)
    public static final int BEL = 0x07;	//Bell - Play a bell sound or a buzzer, like when you mess up in command prompt. (Unused?)
    public static final int BS = 0x08;	//Backspace (Moves cursor backwards once.) (Unused?)
    public static final int HT = 0x09;	//Horizontal tab. Position to next tabstop (Go forward until POSITION % 2 == 0) (I think)
    public static final int LF = 0x0A;	//Line feed, move down exactly one line. (k)
    public static final int VT = 0x0B;	//Vertical tab. Position to the next line's tabstop. (k)
    public static final int FF = 0x0C;	//Form Feed. Whitespace, or to separate logical divisions ("parts") of code
    public static final int CR = 0x0D;	//Carriage return. Goes to next line, sets cursor to 0 (on its line)
    public static final int SO = 0x0E;	//Shift out, go to an alternative charset.
    public static final int SI = 0x0F;	//Shift in, return to the regular set.
    public static final int DLE = 0x10;	//Data Link Escape, cause the following octets, not as control codes (Octet = Group of 8 objects, likely bits.)
    public static final int DC1 = 0x11; //Device Control 1. Turn something on
    public static final int DC2 = 0x12;	//Device Control 2. Turn something on
    public static final int DC3 = 0x13;	//Device Control 3. Turn something off
    public static final int DC4 = 0x14; //Device Control 4. Turn something off
    public static final int NAK = 0x15; //Negative acknowledge (See ACK and ENQ)
    public static final int SYN = 0x16;	//Sync'd idle. (?)
    public static final int ETB = 0x17; //End Transmission Block - Used to split apart blocks of the same bit of data.
    public static final int CAN = 0x18;	//Cancel, indicates the following data is in error or is to be disregarded. (Unused?)
    public static final int EM = 0x19;	//End of Medium. Shows end of a magnetic tape on a computer. Likely unused.
    public static final int SUB = 0x1A;	//Substitute. Similar to EOT? Edit: Seems to be used to START transmission data.
    public static final int ESC = 0x1B;	//Escape. Signifies that what follows is a command sequence rather than normal text.
    public static final int FS = 0x1C;	//File Separator. This and the three following are used to mark fields of data structures. Highest if used in a hierarchy.
    public static final int GS = 0x1D;	//Group Separator. Second highest.
    public static final int RS = 0x1E;	//Record Separator. Third highest.
    public static final int US = 0x1F;	//Unit Separator. Lowest.
    
    protected HashMap<Integer, Boolean> currentSections = new HashMap<Integer, Boolean>();
    protected boolean shouldProcessControl = true;
    protected DataInputStream stream;
    
    public ControlHandler(DataInputStream _stream) {
    	stream = _stream;
    	for (int i = 0; i < 32; i++) {
    		currentSections.put(i, false);
    	}
    }
    
    public HandleType processByte(int readValue, int currentIndex) {
    	//First check: Is the byte a control character?
    	if (!currentSections.containsKey(readValue)) {
    		if (readValue < 255) {
    			return HandleType.TEXT;
    		} else {
    			return HandleType.IGNORE;
    		}
    	}
    	
    	//Second check: Are we being affected by some value that prevents reading (i.e. DLE)?
    	//Represented by shouldProcess.
    	
    	//if (!shouldProcessControl)
    	//	return HandleType.IGNORE;
    	
    	if (readValue == NUL) {
    		
    	} else if (readValue == SOH) {
    		
    	} else if (readValue == STX) {
    		
    	} else if (readValue == ETX) {
    		
    	} else if (readValue == EOT) {
    		
    	} else if (readValue == ENQ) {
    		
    	} else if (readValue == ACK) {
    		
    	} else if (readValue == BEL) {
    		
    	} else if (readValue == BS) {
    		
    	} else if (readValue == HT) {
    		
    	} else if (readValue == LF) {
    		
    	} else if (readValue == VT) {
    		
    	} else if (readValue == FF) {
    		
    	} else if (readValue == CR) {
    		
    	} else if (readValue == SO) {
    		
    	} else if (readValue == SI) {
    		
    	} else if (readValue == DLE) {
    		
    	} else if (readValue == DC1) {
    		
    	} else if (readValue == DC2) {
    		
    	} else if (readValue == DC3) {
    		
    	} else if (readValue == DC4) {
    		
    	} else if (readValue == NAK) {
    		
    	} else if (readValue == SYN) {
    		
    	} else if (readValue == ETB) {
    		
    	} else if (readValue == CAN) {
    		
    	} else if (readValue == EM) {
    		
    	} else if (readValue == SUB) {
    		
    	} else if (readValue == ESC) {
    		
    	} else if (readValue == FS) {
    		
    	} else if (readValue == GS) {
    		
    	} else if (readValue == RS) {
    		
    	} else if (readValue == US) {
    		
    	}
    	
    	return HandleType.UNKNOWN;
    }
    
    protected boolean in(int val) {
    	return currentSections.get(val).booleanValue();
    }
    
    protected void set(int ct, boolean val) {
    	currentSections.put(ct, val);
    }
    
    
    public static String controlToString(int readValue) {
    	if (readValue == NUL) {
    		return "NUL";
    	} else if (readValue == SOH) {
    		return "SOH";
    	} else if (readValue == STX) {
    		return "STX";
    	} else if (readValue == ETX) {
    		return "ETX";
    	} else if (readValue == EOT) {
    		return "EOT";
    	} else if (readValue == ENQ) {
    		return "ENQ";
    	} else if (readValue == ACK) {
    		return "ACK";
    	} else if (readValue == BEL) {
    		return "BEL";
    	} else if (readValue == BS) {
    		return "BS";
    	} else if (readValue == HT) {
    		return "HT";
    	} else if (readValue == LF) {
    		return "LF";
    	} else if (readValue == VT) {
    		return "VT";
    	} else if (readValue == FF) {
    		return "FF";
    	} else if (readValue == CR) {
    		return "CR";
    	} else if (readValue == SO) {
    		return "SO";
    	} else if (readValue == SI) {
    		return "SI";
    	} else if (readValue == DLE) {
    		return "DLE";
    	} else if (readValue == DC1) {
    		return "DC1";
    	} else if (readValue == DC2) {
    		return "DC2";
    	} else if (readValue == DC3) {
    		return "DC3";
    	} else if (readValue == DC4) {
    		return "DC4";
    	} else if (readValue == NAK) {
    		return "NAK";
    	} else if (readValue == SYN) {
    		return "SYN";
    	} else if (readValue == ETB) {
    		return "ETB";
    	} else if (readValue == CAN) {
    		return "CAN";
    	} else if (readValue == EM) {
    		return "EM";
    	} else if (readValue == SUB) {
    		return "SUB";
    	} else if (readValue == ESC) {
    		return "ESC";
    	} else if (readValue == FS) {
    		return "FS";
    	} else if (readValue == GS) {
    		return "GS";
    	} else if (readValue == RS) {
    		return "RS";
    	} else if (readValue == US) {
    		return "US";
    	}
    	return "?";
    }
}
