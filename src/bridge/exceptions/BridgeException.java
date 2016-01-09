package bridge.exceptions;

public abstract class BridgeException extends Throwable{
	private static final long serialVersionUID = 1L;
	
	public BridgeException(String message) {
		super(message);
	}
	public BridgeException(String msg,Throwable cause){
		super (msg,cause);
	}
}
