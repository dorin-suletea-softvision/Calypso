package ui.controller;

import sesion.Session;
import bridge.transferable.context.ConnectorContextInterface;
import bridge.transferable.interfaces.CommandInterface;
import engine.command.ChangeConnectorContextCommand;
import engine.views.ConnectionView;

public class ConnectorTypeChooserController {
	public void changeConnectorContext(ConnectionView onView, ConnectorContextInterface context) {
		CommandInterface cmd = new ChangeConnectorContextCommand(onView, context);
		if (cmd.execute())
			Session.addCommand(cmd);
	}
}
