package xandragon.core.ui.tree;

import java.util.HashMap;

public class ModelData {
	protected HashMap<String, String> store = new HashMap<String, String>();
	protected HashMap<String, String> strides = new HashMap<String, String>();
	protected HashMap<String, String> offsets = new HashMap<String, String>();
			
	public void set(String index, String value) {
		store.put(index, value);
	}
	
	public void setStride(String index, String value) {
		strides.put(index, value);
	}
	
	public void setOffset(String index, String value) {
		offsets.put(index, value);
	}
	
	public void setAll(String index, String value, String stride, String offset) {
		store.put(index, value);
		strides.put(index, stride);
		offsets.put(index, offset);
	}
	
	public String get(String index) {
		if (!store.containsKey(index)) {
			return "NULL";
		}
		return store.get(index);
	}
	
	public String getStride(String index) {
		if (!strides.containsKey(index)) {
			return "NULL";
		}
		return strides.get(index);
	}
	
	public String getOffset(String index) {
		if (!offsets.containsKey(index)) {
			return "NULL";
		}
		return offsets.get(index);
	}
	
	public String formatGet(String index, String formatString) {
		return formatString.replace("<<VAL>>", get(index));
	}
}
