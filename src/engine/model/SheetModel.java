package engine.model;

import java.awt.Dimension;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import sesion.props.ModelPropertySheet;

import bridge.transferable.interfaces.ConnectorModelInterface;
import bridge.transferable.interfaces.EntityModelInterface;
import bridge.transferable.interfaces.SheetModelInterface;

public class SheetModel implements SheetModelInterface,Serializable {
	private static final long serialVersionUID = -7404020222107648705L;
	private long pluginSignature;
	private ArrayList<EntityModelInterface> topLevelEnities;
	private ArrayList<ConnectorModelInterface> connectors;
	
	
	private File diskFile;
	private String sheetName;
	private boolean isCommited;
	private Dimension sheetSize;

	public SheetModel() {
		topLevelEnities = new ArrayList<EntityModelInterface>();
		connectors = new ArrayList<ConnectorModelInterface>();
		diskFile = null;
		isCommited=true;
		sheetSize=new Dimension(ModelPropertySheet.DEFAULT_SHEET_WIDTH,ModelPropertySheet.DEFAULT_SHEET_HEIGHT);
	}
	
	public SheetModel clone(){
		SheetModel ret= new SheetModel();
		ret.topLevelEnities = new ArrayList<EntityModelInterface>(this.getTopLevelEnities());
		ret.sheetName=this.getSheetName();
		ret.connectors = new ArrayList<ConnectorModelInterface>(this.getConnectors());
		ret.diskFile=new File(this.getDiskFile().getAbsolutePath());
		ret.setCommited(false);
		ret.sheetSize=this.getSheetSize();
		ret.pluginSignature=pluginSignature;
		return ret;
	}

	public boolean isCommited() {
		return isCommited;
	}

	public Dimension getSheetSize() {
		return sheetSize;
	}

	public void setSheetSize(Dimension sheetSize) {
		this.sheetSize = sheetSize;
	}

	public void setCommited(boolean isCommited) {
		this.isCommited = isCommited;
	}


	public void setDiskFile(File diskFile) {
		this.diskFile = diskFile;
	}

	public File getDiskFile() {
		return diskFile;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public ArrayList<EntityModelInterface> getTopLevelEnities() {
		return topLevelEnities;
	}

	public long getPluginSignature() {
		return pluginSignature;
	}

	public ArrayList<ConnectorModelInterface> getConnectors() {
		return connectors;
	}

	public void setPluginSignature(long pluginSignature) {
		this.pluginSignature = pluginSignature;
	}

	public boolean add(EntityModelInterface e) {
		return topLevelEnities.add(e);
	}

	public boolean remove(EntityModelInterface o) {
		return topLevelEnities.remove(o);
	}
	
	public boolean add(ConnectorModelInterface e) {
		return connectors.add(e);
	}

	public ConnectorModelInterface remove(int index) {
		return connectors.remove(index);
	}

	public boolean remove(ConnectorModelInterface o) {
		return connectors.remove(o);
	}

	@Override
	public boolean equals(Object arg0) {
		//2 sheets are equal if the have same physical file associated 
		if (!(arg0 instanceof SheetModel))
			return false;
		return ((SheetModel)(arg0)).diskFile.equals(this.diskFile);
	}

	@Override
	public int hashCode() {
		return diskFile.hashCode();
	}
}
