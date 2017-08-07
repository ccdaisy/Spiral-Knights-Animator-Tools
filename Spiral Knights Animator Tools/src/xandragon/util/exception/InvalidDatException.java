package xandragon.util.exception;

@SuppressWarnings("serial")
public class InvalidDatException extends RuntimeException {
	public InvalidDatException() {
		super("DAT File invalid. Reason unknown.");
	}
	
	public InvalidDatException(String message) {
		super(message);
	}
}
