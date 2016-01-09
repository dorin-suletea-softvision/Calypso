package engine.command;

import java.awt.Dimension;
import java.security.InvalidParameterException;

import sesion.Session;

import bridge.transferable.interfaces.CommandInterface;

public class ChangeSheetViewSize implements CommandInterface{
	private int dxy;

	public ChangeSheetViewSize(int dxy) {
		this.dxy=dxy;
	}
	
	@Override
	public boolean execute() throws InvalidParameterException {
		if (!isValid())
			return false;
		Session.getSelectedSheet().setPreferredSize(new Dimension(Session.getSelectedSheet().getPreferredSize().width+dxy,
																  Session.getSelectedSheet().getPreferredSize().height+dxy));
		Session.getSelectedSheet().getParent().revalidate();
		return true;
	}

	@Override
	public void undo() {
		Session.getSelectedSheet().setPreferredSize(new Dimension(Session.getSelectedSheet().getPreferredSize().width-dxy,
				  Session.getSelectedSheet().getPreferredSize().height-dxy));
		Session.getSelectedSheet().getParent().revalidate();
		
	}

	@Override
	public boolean isValid() {
		if (Session.getSelectedSheet().getWidth()+dxy>1 && Session.getSelectedSheet().getHeight()+dxy>1)
			return true;
		return false;
	}

}
