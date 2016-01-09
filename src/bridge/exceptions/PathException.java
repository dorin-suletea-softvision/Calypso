package bridge.exceptions;

//happends when the directory or internet adress for plugin repos are not set properly
public class PathException extends BridgeException{
	private static final long serialVersionUID = 1L;

	public PathException(String message) {
		super(message);
	}
	public PathException(String message,Throwable cause) {
		super(message,cause);
	}

}
