package xandragon.util;

//import java.awt.Label;
import java.awt.TextArea;

/**
 * @author Xan the Dragon
 * 
 * TestLogger is a class designed to be used in prototyping.
 * When tinkering with formats and various attributes, it is handy to be able to output information into the console (i.e. print the fields and methods of something imported)
 *
 */
public class Logger {
	
	/**A label that is used in the UI's log. Will be null if the UI is not being used.*/
	protected TextArea UI_Label = null;
	
	/**
	 * Append text + a new line to both the system output and, the UI's log window.<br>
	 * @param objects A tuple of any objects, preferably strings. If objects are not strings, it will call toString() on them.
	 */
	public void AppendLn(Object... objects) {
		//Special case: there can be no arguments. If it's a length of 0, we just append newline and that's it.
		if (objects.length == 0) {
			Append("\n");
			return;
		}
		for (int idx = 0; idx < objects.length; idx++) {
			Object obj = objects[idx];
			if (obj instanceof String) {
				Append((String) obj + "\n");
			} else {
				Append(obj.toString() + "\n");
			}
		}
	}
	
	/**
	 * Append text to both the system output and, if it exists, the UI's log window.<br>
	 * @param objects A tuple of any objects, preferably strings. If objects are not strings, it will call toString() on them.
	 */
	public void Append(Object... objects) {
		for (int idx = 0; idx < objects.length; idx++) {
			Object obj = objects[idx];
			if (obj instanceof String) {
				Append((String) obj);
			} else {
				Append(obj.toString());
			}
		}
	}
	
	/**
	 * Clear the log.<br>
	 * <strong>This will only work if a GUI element has been passed in.</strong>
	 */
	public void ClearLog() {
		if (UI_Label != null) {
			UI_Label.setText(null);
		}
	}
	
	/**
	 * Set the gui label to print to.
	 * @param label The label to print to.
	 */
	public void setLabel(TextArea label) {
		UI_Label = label;
	}
	
	/**
	 * Append a list of text entries to the log. This was made for specific use cases for helping with debugging.
	 * @param texts The text to append.
	 */
	public void AppendList(String... texts) {
		for (int idx = 0; idx < texts.length; idx++) {
			//We'll automatically put newlines.
			Append(" - " + texts[idx].replaceAll("\\n", "")  + "\n");
		}
	}
	
	/**
	 * Append text to the log with special formatting, and print.<br>
	 * <strong>Do not get this confused with the public Append function! This is the internal function that directly applies text. All append functions go here.</strong>
	 * @param text The text to append.
	 */
	protected void Append(String text) {
		System.out.print(text); //Text will have a newline if it needs to.
		if (UI_Label != null) {
			UI_Label.setText(UI_Label.getText() + text);
		}
	}
}
