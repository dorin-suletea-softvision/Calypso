package bridge.transferable.interfaces;

import java.awt.Dimension;
import java.awt.Point;
import java.util.List;


public interface EntityModelInterface {
	public void addDataString(int dataRowIndex,String data) throws Throwable;
	public String getName();
	public void setName(String name);
	public Point getLocation();
	public void setLocation(Point location);
	public Dimension getSize();
	public void setSize(Dimension size);
	public List<DataModelInterface> getDataRows();
	public String getType();
	public EntityModelInterface duplicate();
}
