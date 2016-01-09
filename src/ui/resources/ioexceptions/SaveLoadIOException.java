package ui.resources.ioexceptions;

public class SaveLoadIOException extends Throwable{
	private static final long serialVersionUID = 1L;
	
	public SaveLoadIOException() {
		super();
	}
	
	public SaveLoadIOException(String message) {
		super(message);
	}
	
	public SaveLoadIOException(String message,Throwable cause) {
		super(message);
	}
	
	

}
