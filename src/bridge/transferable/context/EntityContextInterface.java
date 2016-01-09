package bridge.transferable.context;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import bridge.transferable.interfaces.AbstractControlJPanel;
import bridge.transferable.interfaces.DataModelInterface;
import bridge.transferable.interfaces.EntityModelInterface;
import bridge.transferable.interfaces.GlyphLocationInterface;
import bridge.transferable.proxy.EntityViewProxy;

public interface EntityContextInterface {
	public String getDrawnType();
	public ArrayList<String> getDataRowIdentifiers();
	public Dimension getDefaultSize();
	public Dimension getMinimumSize(EntityModelInterface entityModel,FontMetrics fontMetrics);
	public void drawShape(Graphics g, int width, int height);
	public void drawData(Graphics g, int width, int height, List<DataModelInterface> data);
	public void drawIdentifiers(Graphics g, int width, int height, String name, String type);
	public void setNestedPositions(List<GlyphLocationInterface> nested, GlyphLocationInterface hostView);
	/**
	 * 
	 * @param entity - generate pannel for this element
	 * @return the ControlPannel of the element
	 * <<This method should be called only from View classes and never otherwise>>
	 */
	public AbstractControlJPanel generateControlPannel(EntityViewProxy entity);
}
