package bridge.transferable.interfaces;

import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;

public interface SheetModelInterface {
	public Dimension getSheetSize();
	public void setSheetSize(Dimension sheetSize);
	public void setCommited(boolean isCommited);
	public File getDiskFile();
	public String getSheetName();
	public void setSheetName(String sheetName);
	public ArrayList<EntityModelInterface> getTopLevelEnities();
	public long getPluginSignature();
	public ArrayList<ConnectorModelInterface> getConnectors();
	public boolean add(EntityModelInterface e);
	public boolean remove(EntityModelInterface o);
	public boolean add(ConnectorModelInterface e);
	public ConnectorModelInterface remove(int index);
	public boolean remove(ConnectorModelInterface o);
}
