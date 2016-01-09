package bridge.transferable.proxy;

import java.awt.Component;

import engine.views.interfaces.ViewDragableInterface;

import bridge.transferable.interfaces.EntityModelInterface;


public interface EntityViewProxy extends ViewDragableInterface{
	public EntityModelInterface getModel();
	public void setModel(EntityModelInterface model);
	public void update();
}
