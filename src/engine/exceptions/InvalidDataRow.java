package engine.exceptions;

import engine.model.EntityModel;

public class InvalidDataRow extends Throwable{
	private static final long	serialVersionUID	= 1L;

	public InvalidDataRow(int sheetRowIndex,int maxRowIndex, EntityModel trigger) {
		super ("The specified sheet index : "+sheetRowIndex+"\n"+
				  "is bigger that the sheet index accepted :"+maxRowIndex+"\n"+
				  "for the entity + "+trigger);
	}

}
