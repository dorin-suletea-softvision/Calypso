package bridge.transferable.proxy;

import bridge.transferable.interfaces.CommandInterface;
import bridge.transferable.interfaces.SheetModelInterface;


//used to transfer to the plugin
public interface SheetViewProxy {
	public void undo();
	public void undoAndRemove();
	public void redo();
	public void addCommand(CommandInterface c);
	public SheetModelInterface getModel();
}
