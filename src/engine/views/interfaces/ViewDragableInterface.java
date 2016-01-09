package engine.views.interfaces;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

public interface ViewDragableInterface {
	public void setLocation(Point location);
	public void setLocation(int x,int y);
	public void setSize(int width,int height);
	public void setSize(Dimension size);
	public void setBounds(int x, int y,int width,int height);
	public Rectangle getBounds();
	public int getX();
	public int getY();
	public int getWidth();
	public int getHeight();
	public void setMinimumSize(Dimension minimumSize);
	public Dimension getMinimumSize();
	public Container getParent();
	public void update();
	public boolean validNewPosition(int dx,int dy);
}
