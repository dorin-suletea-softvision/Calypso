package bridge.transferable.interfaces;

import java.awt.Point;
import java.util.ArrayList;

public interface ConnectorModelInterface {
	public void addSnap(Point snap);
	public void addSnap(int index,Point snap);
	public void setDrawnType(String type);
	public String getDrawnType();
	public ArrayList<Point> getSnaps();
	public int getSnapCount();
	public EntityModelInterface getFrom();
	public EntityModelInterface getTo();
	public void removeSnap(Point snapLocation);
	
	public void setStartText(String startText);
	public void setMidText(String midText);
	public void setEndText(String endText);
	public String getStartText();
	public String getMidText();
	public String getEndText();
}
