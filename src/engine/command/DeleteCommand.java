package engine.command;

import java.security.InvalidParameterException;

import bridge.transferable.interfaces.CommandInterface;
import engine.views.Glyph;
import engine.views.interfaces.ViewDeleteableInterface;

public class DeleteCommand implements CommandInterface{
	private ViewDeleteableInterface deletedEntity;
	private Glyph  exParent;
	
	public DeleteCommand(ViewDeleteableInterface deletedEntity) {
		this.deletedEntity=deletedEntity;
		exParent=deletedEntity.getParrent();
	}
	
	@Override
	public boolean execute() throws InvalidParameterException {
		deletedEntity.delete();
		return true;
	}

	@Override
	public void undo() {
		deletedEntity.restore(exParent);
	}

	@Override
	public boolean isValid() {
		return true;
	}

}
