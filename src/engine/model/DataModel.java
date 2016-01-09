package engine.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import bridge.transferable.interfaces.DataModelInterface;

public class DataModel implements DataModelInterface,Serializable{
	private static final long serialVersionUID = 1L;
	private ArrayList<String>      dataLine;
	private String                 identifier;

	public DataModel(String identifier) {
		dataLine=new ArrayList<String>();
		this.identifier=identifier;
	}

	public void add(String data){
		this.dataLine.add(data);
	}
	
	@Override
	public String toString() {
		return "DataModel [dataLine=" + dataLine + ", identifier=" + identifier
				+ "]";
	}
	public ArrayList<String> getDataLine() {
		return dataLine;
	}

	public int entryNr(){
		return dataLine.size();
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DataModelInterface))
			return false;
		DataModelInterface modelObj = (DataModelInterface) obj;
		
		if (!(modelObj.getIdentifier().equals(identifier)))
			return false;
		
		List<String> thisString = new ArrayList<String>();
		List<String> modelObjString = new ArrayList<String>();
		
		for (int i=0;i<dataLine.size();i++)
			thisString.add(dataLine.get(i));
		
		for (int i=0;i<getDataLine().size();i++)
			modelObjString.add(getDataLine().get(i));
		
		if (modelObjString.size()!=thisString.size())
			return false;
		
		for (int i=0;i<modelObjString.size();i++)
			if (!(thisString.get(i).equals(modelObjString.get(i))))
				return false;
		return true;
	}
	//!generated

	
	
}
