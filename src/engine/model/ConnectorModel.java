package engine.model;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

import bridge.transferable.interfaces.ConnectorModelInterface;
import bridge.transferable.interfaces.EntityModelInterface;

public class ConnectorModel implements ConnectorModelInterface,Serializable{
	private static final long serialVersionUID = 1L;
	private String 						drawnType;
	private ArrayList<Point>            snaps;
	
	
	
	private String                      startText;
	private String                      midText;
	private String                      endText;
	private EntityModelInterface        from;
	private EntityModelInterface        to;
	
	
	public ConnectorModel(String type,EntityModelInterface from,EntityModelInterface to){
		this(type);
		this.from=from;
		this.to=to;
	}
	
	public ConnectorModel(EntityModelInterface from,EntityModelInterface to){
		this();
		this.from=from;
		this.to=to;
	}
	
	public ConnectorModel(String type) {
		this();
		drawnType=type;
	}
	public ConnectorModel(){
		drawnType="defaultConnectorType";
		snaps=new ArrayList<Point>();
		startText=new String();
		midText=new String();
		endText=new String();
	}
	
	
	//generated
	public void addSnap(Point snap){
		snaps.add(snap);
	}
	
	public void addSnap(Point snap , int index){
		snaps.add(index,snap);
	}
	
	public void removeSnap(Point snapLocation){
		snaps.remove(snapLocation);
	}
	
	public void addSnap(int index,Point snap){
		snaps.add(index,snap);
	}
	public void setDrawnType(String type){
		drawnType=type;
	}
	@Override
	public String toString() {
		return "ConnectorModel [connectorType=" + drawnType + ", snaps="
				+ snaps + "]";
	}
	public ArrayList<Point> getSnaps() {
		return snaps;
	}
	public int getSnapCount(){
		return snaps.size();
	}
	public void setStartText(String startText) {
		this.startText = startText;
	}
	public void setMidText(String midText) {
		this.midText = midText;
	}
	public void setEndText(String endText) {
		this.endText = endText;
	}
	public String getStartText() {
		return startText;
	}
	public String getMidText() {
		return midText;
	}
	public String getEndText() {
		return endText;
	}
	public String getDrawnType() {
		return drawnType;
	}
	
	
	public void setFrom(EntityModelInterface from) {
		this.from = from;
	}
	public void setTo(EntityModelInterface to) {
		this.to = to;
	}
	public EntityModelInterface getFrom() {
		return from;
	}
	public EntityModelInterface getTo() {
		return to;
	}
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ConnectorModel))
			return false;
		ConnectorModel objMod = (ConnectorModel) obj;
		
		if (!(objMod.drawnType.equals(drawnType)))
			return false;
		if (!(objMod.snaps.equals(snaps)))
			return false;
		if (!(objMod.getFrom().equals(from)))
			return false;
		if (!(objMod.getTo().equals(to)))
			return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		return drawnType.length()*snaps.size();
	}
}
