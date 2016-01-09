package engine.command;

import java.security.InvalidParameterException;

import sesion.Session;
import bridge.transferable.context.ConnectorContextInterface;
import bridge.transferable.interfaces.CommandInterface;
import engine.views.ConnectionView;

public class ChangeConnectorContextCommand implements CommandInterface{
	private ConnectionView view;
	private ConnectorContextInterface exContext;
	private ConnectorContextInterface newContext;
	
	
	
	public ChangeConnectorContextCommand(ConnectionView view, ConnectorContextInterface newContext) {
		super();
		this.view = view;
		this.newContext = newContext;
		this.exContext=view.getContext();
	}

	@Override
	public boolean execute() throws InvalidParameterException {
		ConnectionView auxView = Session.getSelectedSheet().getEqualConnector(view);
		view=auxView;
		auxView.update();
		auxView.repaint();
		auxView.setContext(newContext);
		Session.st_triggerSelectPanUpdate();
		return true;
	}

	@Override
	public void undo() {
		view.setContext(exContext);
		view.update();
		view.repaint();
		Session.st_triggerSelectPanUpdate();
	}

	@Override
	public boolean isValid() {
		if (newContext.equals(exContext))
			return false;
		return true;
	}

}
