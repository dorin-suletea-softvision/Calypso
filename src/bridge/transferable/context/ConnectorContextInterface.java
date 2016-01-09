package bridge.transferable.context;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;


import bridge.transferable.interfaces.AbstractControlJPanel;
import bridge.transferable.proxy.ConnectorViewProxy;


public interface ConnectorContextInterface {
	public String getDrawnType(); //unique ID
	public boolean isLabeled();
	public void drawEndArrow(Graphics g,ArrayList<Point> points);
	public void drawStartArrow(Graphics g,ArrayList<Point> points);
	public void drawLine(Graphics g, ArrayList<Point> snaps, Point start, Point end);
	public AbstractControlJPanel generateUiPan(ConnectorViewProxy connector);

}
