package ui.resources.ioexceptions;

import java.io.File;

public class BrokeFileException extends SaveLoadIOException{
	private static final long serialVersionUID = 1L;
	
	public BrokeFileException(File  f) {
		super ("The file :"+f+" is broken and can not be imported/exported");
	}
	
	public BrokeFileException(File  f,Throwable cause) {
		super ("The file :"+f+" is broken and can not be imported/exported " +
				"\n cause: "+cause );
	}

}
