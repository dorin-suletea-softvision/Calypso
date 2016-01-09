package engine.command;

import java.security.InvalidParameterException;

import sesion.Session;
import bridge.transferable.interfaces.CommandInterface;
import engine.views.ConnectionView;
import engine.views.EntityView;
import engine.views.LabeledConnectionView;

public class ConnectCommand implements CommandInterface{
	private EntityView from;
	private EntityView to;
	
	private ConnectionView createdConnection;
	
	public EntityView getFrom() {
		return from;
	}

	public EntityView getTo() {
		return to;
	}

	public ConnectionView getCreatedConnection() {
		return createdConnection;
	}

	public ConnectCommand(EntityView from, EntityView to) {
		super();
		this.from = from;
		this.to = to;
		createdConnection =new LabeledConnectionView(from, to);
	}



	@Override
	public boolean execute() throws InvalidParameterException {
		Session.getSelectedSheet().add(createdConnection);
		Session.st_triggerSelectPanUpdate();
		Session.getSelectedSheet().repaint(Session.getSelectedSheet().getVisibleRect());
		return true;
	}

	@Override
	public void undo() {
		createdConnection.delete();
	}

	@Override
	public boolean isValid() {
		return (from!=to && from!=null && to!=null);
	}
	public final void setFrom(EntityView from) {
//		this.from = from;
		System.out.println("Setters are not allowed");
	}	

	public final void setTo(EntityView to) {
//		this.to = to;
		System.out.println("Setters are not allowed");
	}

}
