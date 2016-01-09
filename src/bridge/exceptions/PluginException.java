package bridge.exceptions;

public class PluginException extends BridgeException {
	private static final long serialVersionUID = 1L;

	public PluginException(String message) {
		super(message);
	}

	public PluginException(String message, Throwable cause) {
		super(message, cause);
	}

}
