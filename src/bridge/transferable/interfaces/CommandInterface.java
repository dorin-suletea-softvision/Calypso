package bridge.transferable.interfaces;

import java.security.InvalidParameterException;

public interface CommandInterface {
	public boolean execute()throws InvalidParameterException;
	public void undo();
	public boolean isValid();
}
