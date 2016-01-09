package sesion.props;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

public class ModelPropertySheet {
	private static final  ModelPropertySheet     instance = new ModelPropertySheet();
	private final int                            conectorSelectionArea=10;
	private final int                            connectorArrowPtsDistance=14;
	private final int                            intersectionAlgorithmID;
	
	public static final int                      DEFAULT_SHEET_WIDTH=1000;
	public static final int                      DEFAULT_SHEET_HEIGHT=600;
	
	private final  Color                		 moveViewColor = Color.green;
	private final  Color               			 selectionColor    = Color.red;
//	private final  Color 			   			 drawColor = Color.darkGray;
//	private final  Color 			  			 fillColor = new Color(170,170,170,15);
		
	private final  Font                          sheetFont = new Font("Serif", Font.PLAIN, 10);
	private final  Dimension                     defaultSheetSize=new Dimension(1000,1000);  
	
	
	public static final Color                    OPAQUE_DARK_GRAY = Color.darkGray;
	public static final Color                    OPAQUE_LIGHT_GRAY = Color.lightGray;
	public static final Color                    TRANSPARENT_LIGHT_GRAY = new Color(190,190,190,150);
	

	
	
	public static ModelPropertySheet getInstance(){
		return instance;
	}
	
	private ModelPropertySheet() {
		intersectionAlgorithmID=1;
	}

	public int getIntersectionAlgorithmID() {
		return intersectionAlgorithmID;
	}

	public int getConectorSelectionArea() {
		return conectorSelectionArea;
	}

	public Color getMoveViewColor() {
		return moveViewColor;
	}

	public Color getSelectionColor() {
		return selectionColor;
	}

//	public Color getDrawcolor() {
//		return drawColor;
//	}
//
//	public Color getFillcolor() {
//		return fillColor;
//	}
	public int getConnectorArrowPtsDistance() {
		return connectorArrowPtsDistance;
	}
	
	public Font getSheetFont(){
		return sheetFont;
	}

	public Dimension getDefaultSheetSize() {
		return defaultSheetSize;
	}
}	
