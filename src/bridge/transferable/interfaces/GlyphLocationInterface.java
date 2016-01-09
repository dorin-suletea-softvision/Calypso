package bridge.transferable.interfaces;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

public interface GlyphLocationInterface {
	public void setLocation(Point location);
	public void setBounds(Rectangle r);
	public void setBounds(int x, int y, int w, int h);
	public Rectangle getBounds();
	public Point getLocation();
	public void setSize(Dimension d);
	public void setSize(int w, int h);
	public void setMinimumSize(Dimension minimumSize);
	public Dimension getMinimumSize();
}
