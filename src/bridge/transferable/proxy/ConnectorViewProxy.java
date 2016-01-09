package bridge.transferable.proxy;

import bridge.transferable.interfaces.ConnectorModelInterface;


public interface ConnectorViewProxy {
	public void setStartText(String text);
	public void setMidText(String mid);
	public void setEndText(String end);
	
	public String getStartText();
	public String getMidText();
	public String getEndText();
	
	
	public String getType();
	public String getSourceEntityName();
	public String getEndEntityName();
	public ConnectorModelInterface getModel();
	
}
