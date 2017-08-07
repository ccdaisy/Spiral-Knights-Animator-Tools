package xandragon.converter;

public class ArrayConfig {
	public String name;
	public int size;
	public int stride;
	public int offset;
	
	public ArrayConfig(String _name, int _size, int _stride, int _offset) {
		name = _name;
		size = _size;
		stride = _stride;
		offset = _offset;
	}
}
