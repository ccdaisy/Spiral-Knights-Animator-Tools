package xandragon.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputHelper {
	public static String getSystemInput(String prompt) throws IOException {
		System.out.print(prompt + " > ");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String input = br.readLine();
		System.out.println();
		return input;
	}
	
	public static String getSystemInput() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String input = br.readLine();
		System.out.println();
		return input;
	}
}
