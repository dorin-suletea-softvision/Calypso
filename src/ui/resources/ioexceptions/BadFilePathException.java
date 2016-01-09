package ui.resources.ioexceptions;

public class BadFilePathException extends SaveLoadIOException{
	private static final long serialVersionUID = 1L;
	
	public BadFilePathException(String path) {
		super ("The file described by the path : "+path+" can not be found");
	}
	
	public BadFilePathException(String path,Throwable cause) {
		super ("The file described by the path : "+path+" can not be found " +
				"\n cause: "+cause );
	}
}
