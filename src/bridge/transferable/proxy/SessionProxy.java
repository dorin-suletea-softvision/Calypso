package bridge.transferable.proxy;

import java.util.List;

public interface SessionProxy {
	public void triggerSelectPanUpdate();
	public void postErrorMessage(String message);
	public void repaintSelectedSheet();
	public SheetViewProxy getSelectedSheetProxy();
	public List<EntityViewProxy> getSelectedEntities();
}
