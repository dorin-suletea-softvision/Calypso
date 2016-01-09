package engine.command;

import java.security.InvalidParameterException;

import bridge.transferable.interfaces.CommandInterface;

import sesion.Session;
import engine.views.EntityView;

public class AddEntityCommand implements CommandInterface {
	private EntityView entity;
	static int index;
	
	public AddEntityCommand(EntityView entity) {
		super();
		this.entity = entity;
	}

	@Override
	public boolean execute() throws InvalidParameterException {
		Session.getSelectedSheet().add(entity);
		Session.getSelectedSheet().repaint();
		return true;
	}

	@Override
	public void undo() {
		entity.delete();
		Session.getSelectedSheet().remove(entity);
		Session.getSelectedSheet().repaint();
	}

	@Override
	public boolean isValid() {
		System.out.println("Dummy is valid on AddEntityCommand");
		return true;
	}

}
