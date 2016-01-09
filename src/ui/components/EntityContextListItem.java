package ui.components;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import sesion.props.UIPropSheet;
import bridge.transferable.context.EntityContextInterface;

public class EntityContextListItem extends JComponent implements RichListItem{
	private static final long                 serialVersionUID = 1L;
	private ImageIcon                         icon;
	private final EntityContextInterface      context;

	public EntityContextListItem(ImageIcon icon,EntityContextInterface context) {
		super();
		this.context=context;
		this.icon=resizeIcon(icon);
	}

	public String getTitle() {
		return context.getDrawnType();
	}

	public ImageIcon getIcon() {
		return icon;
	}
	
	public EntityContextInterface getContext(){
		return context;
	}
	
	private ImageIcon resizeIcon(ImageIcon icon){
	    Image img = icon.getImage();  
	    Dimension newSize = UIPropSheet.RICH_LIST_BIG_ITEM_DIM;
	    Image newimg = img.getScaledInstance(newSize.width,newSize.height,Image.SCALE_SMOOTH);  
	    return new ImageIcon(newimg);  
	}
}