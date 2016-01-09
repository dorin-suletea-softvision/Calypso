package bridge.transferable;

import java.util.List;

import javax.swing.JToolBar;

import bridge.transferable.proxy.MainWindowProxy;
import bridge.transferable.proxy.SessionProxy;
import bridge.transferable.proxy.SheetViewProxy;


public interface PluginInterface {
	public long   getSignature();
	public void   setSheetView(SheetViewProxy associatedSheet);
	public void   setMainWindow(MainWindowProxy assoctiatedMainWindow); 
	public void   setSession(SessionProxy associatedSession);
	public String getPluginName();
	public String getAuthor();
	public String getReleaseDate();
	public List<EntityContextTransferWraper> getEntityContexts();
	public List<ConnectorContextTransferWraper> getConnectorContexts();
	public JToolBar getPluginToolbar();
}
