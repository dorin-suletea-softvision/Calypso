package engine.exceptions;

public class InvalidConnector extends Throwable{
	private static final long	serialVersionUID	= 1L;

	public InvalidConnector() {
		super("The Entity you want to connect doest exist in current sheet");
	}
}
