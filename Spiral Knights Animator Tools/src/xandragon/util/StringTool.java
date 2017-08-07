package xandragon.util;

public class StringTool {
	public static String shortToHex(short input, int leading) {
		String s = Integer.toHexString(Short.toUnsignedInt(input)).toUpperCase();
		String pre = "0x";
		int length = leading - s.length();
		for (int i = 0; i < length; i++) {
			pre = pre + "0";
		}
		return pre + s;
	}
	
	public static String intToHex(int input, int leading) {
		String s = Integer.toHexString(input).toUpperCase();
		String pre = "0x";
		int length = leading - s.length();
		for (int i = 0; i < length; i++) {
			pre = pre + "0";
		}
		return pre + s;
	}
	
	public static String longToHex(long input, int leading) {
		String s = Long.toHexString(input).toUpperCase();
		String pre = "0x";
		int length = leading - s.length();
		for (int i = 0; i < length; i++) {
			pre = pre + "0";
		}
		return pre + s;
	}
	
	public static int[] stringToIntArray(String in) {
		char[] c = in.toCharArray();
		int[] v = new int[c.length];
		for (int i = 0; i < c.length; i++) {
			v[i] = (int) c[i];
		}
		return v;
	}
	
	public static String op(int opid) {
		return String.valueOf((char) opid);
	}
}
