package engine.command;

import java.awt.Rectangle;
import java.security.InvalidParameterException;

import bridge.transferable.interfaces.CommandInterface;

import sesion.Session;
import engine.views.Glyph;

public class NestCommand implements CommandInterface{
	private Glyph       newParent;
	private Glyph       nestable;
	private Glyph       exParent;
	
	private Rectangle nestableExBounds;
	private Rectangle exParentExBounds;
	private Rectangle newParentExBounds;
	

	public NestCommand(Glyph newParent, Glyph nestable, Glyph exParent) {
		super();
		this.newParent = newParent;
		this.nestable = nestable;
		this.exParent = exParent;
		
		this.nestableExBounds=nestable.getBounds();
		this.exParentExBounds=exParent.getBounds();
		this.newParentExBounds=newParent.getBounds();
	}

	@Override
	public boolean execute() throws InvalidParameterException {
		if (!isValid())
			return false;
		newParent.nest(nestable);
		newParent.update();
		exParent.update();
		Session.getSelectedSheet().repaint(Session.getSelectedSheet().getVisibleRect());
		return true;
	}

	@Override
	public void undo() {
		nestable.setBounds(nestableExBounds);
		newParent.setBounds(newParentExBounds);
		exParent.setBounds(exParentExBounds);
		
		exParent.nest(nestable);
		newParent.update();
		exParent.update();
		Session.getSelectedSheet().repaint(Session.getSelectedSheet().getVisibleRect());
	}

	@Override
	public boolean isValid() {
		System.out.println("Dummy isValid on NestCommand");
		return true;
	}



}
