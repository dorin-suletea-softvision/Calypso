package bridge.transferable.interfaces;

import java.util.ArrayList;

public interface DataModelInterface {
	public ArrayList<String> getDataLine();
	public String getIdentifier();
	public int entryNr();
	public void add(String data);
	public boolean equals(Object obj);
}
