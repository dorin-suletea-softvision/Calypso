package engine.command;

import java.awt.Point;
import java.security.InvalidParameterException;

import bridge.transferable.interfaces.CommandInterface;

import engine.views.ConnectionView;
import engine.views.SnapView;

import sesion.Session;

public class SnapCommand implements CommandInterface{
	private Point                location;
	private ConnectionView       onConnector;
	private int                  index=-1;
	private SnapView             addedSnap;
	
	public SnapCommand(Point location, ConnectionView onConnector, int index) {
		super();
		this.location = location;
		this.onConnector = onConnector;
		this.index = index;
	}
	
	public SnapCommand(Point location, ConnectionView onConnector) {
		super();
		this.location = location;
		this.onConnector = onConnector;
	}

	@Override
	public boolean execute() throws InvalidParameterException {
		if (!isValid())
			return false;
		
		if (index==-1) {
			addedSnap=onConnector.addSnap(location);
		}
		else {
			addedSnap=onConnector.addSnap(location, index);
		}

		onConnector.update();
		Session.getSelectedSheet().repaint(Session.getSelectedSheet().getVisibleRect());
		
		return true;	
	}

	@Override
	public void undo() {
		onConnector.removeSnap(addedSnap);
		onConnector.update();
		Session.getSelectedSheet().repaint(Session.getSelectedSheet().getVisibleRect());
	}

	@Override
	public boolean isValid() {
		System.out.println("Dummy isValid on Snap Command");
		return true;
	}

}
