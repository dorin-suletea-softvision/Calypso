package engine.model;

import java.awt.Dimension;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import bridge.transferable.interfaces.DataModelInterface;
import bridge.transferable.interfaces.EntityModelInterface;
import engine.exceptions.InvalidDataRow;

public class EntityModel implements EntityModelInterface, Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * type is PK
	 */
	private final String type;
	private String name;
	private List<EntityModel> nestedEntities;
	private List<DataModelInterface> dataRows;

	private Point location;
	private Dimension size;

	public EntityModel(String type, List<String> identifiers) {
		this.type = type;
		this.name = "Default_" + type + "_name";
		nestedEntities = new ArrayList<EntityModel>();
		dataRows = new ArrayList<DataModelInterface>();
		location = new Point();
		size = new Dimension();
		createDataRows(identifiers);
	}

	public EntityModel(String type, List<String> identifiers, List<EntityModel> nestedEntities) {
		this(type, identifiers);
		this.nestedEntities.addAll(new ArrayList<EntityModel>(nestedEntities));
	}

	@Override
	public EntityModelInterface duplicate() {
		List<String> identifiers = new ArrayList<String>();
		for (DataModelInterface df : dataRows)
			identifiers.add(df.getIdentifier());
		
		EntityModelInterface ret = new EntityModel(this.type, identifiers, this.nestedEntities);

		ret.setLocation(getLocation());
		ret.setSize(getSize());
		ret.setName(getName());
		return ret;
	}

	private void createDataRows(List<String> identifiers) {
		for (String s : identifiers)
			dataRows.add(new DataModel(s));
	}

	public void addDataString(int dataRowIndex, String data) throws InvalidDataRow {
		if (dataRowIndex > dataRows.size())
			throw new InvalidDataRow(dataRowIndex, dataRows.size(), this);
		else
			dataRows.get(dataRowIndex).add(data);
	}

	@Override
	public String toString() {
		return "EntityModel [type=" + type + ", name=" + name + ", nestedEntities=" + nestedEntities + ", dataRows=" + dataRows + ", location=" + location + ", size=" + size + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public Dimension getSize() {
		return size;
	}

	public void setSize(Dimension size) {
		this.size = size;
	}

	public List<DataModelInterface> getDataRows() {
		return dataRows;
	}

	public String getType() {
		return type;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof EntityModel))
			return false;
		EntityModel modelObj = (EntityModel) obj;
		if (!(modelObj.getName().equals(name)) || (!(modelObj.getType().equals(type))))
			return false;
		if (!(dataRows.containsAll(modelObj.getDataRows()) && modelObj.getDataRows().containsAll(dataRows)))
			return false;
		if (location.x!=modelObj.location.x || location.y!=modelObj.location.y)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		return type.length() * name.length() + dataRows.size() * nestedEntities.size()+location.x+location.y;
	}
}
