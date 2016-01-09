package engine.command;


import java.awt.Rectangle;
import java.security.InvalidParameterException;

import bridge.transferable.interfaces.CommandInterface;

import engine.views.interfaces.ViewDragableInterface;

import sesion.Session;

public class ResizeCommand implements CommandInterface{
	private ViewDragableInterface viewReprezentation;
	private int dx;
	private int dy;
	
	private int dw;
	private int dh;
	
	
	public ResizeCommand(ViewDragableInterface viewReprezentation,Rectangle newBounds) {
		this.viewReprezentation=viewReprezentation;
		this.dx = viewReprezentation.getX()-newBounds.x;
		this.dy= viewReprezentation.getY()-newBounds.y;
		
		this.dw=viewReprezentation.getWidth()-newBounds.width;
		this.dh=viewReprezentation.getHeight()-newBounds.height;
	}

	@Override
	public boolean execute() throws InvalidParameterException {
		if (!isValid())
			return false;
		viewReprezentation.setLocation(viewReprezentation.getX()-dx, viewReprezentation.getY()-dy);
		viewReprezentation.setSize(viewReprezentation.getWidth()-dw, viewReprezentation.getHeight()-dh);
		viewReprezentation.update();
		Session.getSelectedSheet().repaint(Session.getSelectedSheet().getVisibleRect());
		return true;
	}

	@Override
	public void undo() {
		viewReprezentation.setLocation(viewReprezentation.getX()+dx, viewReprezentation.getY()+dy);
		viewReprezentation.setSize(viewReprezentation.getWidth()+dw, viewReprezentation.getHeight()+dh);
		viewReprezentation.update();
		Session.getSelectedSheet().repaint(Session.getSelectedSheet().getVisibleRect());
	}

	@Override
	public boolean isValid() {
		System.out.println("Dummy isValid on Resize command");
		return true;
	}
}

