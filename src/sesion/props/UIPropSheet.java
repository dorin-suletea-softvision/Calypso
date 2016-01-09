package sesion.props;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

public class UIPropSheet {
	public static final  UIPropSheet instance = new UIPropSheet();
	
	public static final float       CONTROL_PAN_WITH_RATIO=0.25f;
	public static final String      SHEET_TAB_DATA_FLAV = "SHEET";
	
	public static final Dimension   RICH_LIST_BIG_ITEM_DIM=new Dimension(50,40);
	public static final Dimension   RICH_LIST_DIM=new Dimension(120,300);
	public static final Dimension   RICH_LIST_SMALL_ITEM_DIM=new Dimension(20,20);
	public static final Color       RICH_LIST_HIGHLIGHT_COL=Color.LIGHT_GRAY;
	
	public static final Dimension   VERY_SMALL_BTN_DIM = new Dimension(17,17) ;
	public static final Dimension   SMALL_BTN_DIM = new Dimension(20,20) ;
	public static final Dimension   MEDIUM_BTN_DIM = new Dimension(30,30) ;
	
	public static final int 		SMALL_BTN_PAD = 5;
	public static final Dimension   BIG_BTN_DIM = new Dimension(90,20);
	
	
	public static final int         SCROLL_BAR_W = 20;
	public static final Dimension   CONNECTOR_TYPE_BTN_DIM=new Dimension(20,20);
	public static final int         CONNECTOR_TYPE_SP_PREF_H = 120;
	public static final int         CONNECTOR_TYPE_PAN_GRID_H_CELLCOUNT=3;
	public static final int         CONNECTOR_TYPE_BTN_PAD=3;
	
	public static final Dimension   OPTIONS_DIALOG_FORM_DIM=new Dimension(550,450);
	
	public static final Color       LINE_LABEL_BG_COLOR = Color.black;
	public static final int         LINE_LABEL_H = 20;
	

	public static final Dimension   DIAG_TF_DIM = new Dimension(170,20);
	public static final Dimension   DIAG_HEADER_FOOTER_DIM = new Dimension(OPTIONS_DIALOG_FORM_DIM.width,100);
	public static final Dimension   DIAG_IMG_DIM = new Dimension(50,50);
	public static final float       DIAG_TITLE_FONT_SIZE = 20;
	public static final int         DIAG_TITLE_FONT_STYLE = Font.BOLD;
	public static final int         DIAG_TITLE_H = (int) (DIAG_TITLE_FONT_SIZE+DIAG_TITLE_FONT_SIZE/4);// + 5 because of the text that goes below the line
	public static final int         DIAG_CMP_PAD_V = 30;
	public static final int         DIAG_CMP_PAD_H = 20;

	

	
	
}
