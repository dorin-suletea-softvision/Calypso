package ui.components;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.ImageIcon;

import sesion.props.UIPropSheet;

import bridge.transferable.PluginInterface;

public class PluginsListItem implements RichListItem{
	private ImageIcon icon;
	private PluginInterface plugin;
	
	public PluginsListItem(ImageIcon icon,PluginInterface plugin) {
		this.plugin= plugin;
		this.icon=resizeIcon(icon);
	}
	
	@Override
	public String getTitle() {
		return plugin.getPluginName();
	}

	@Override
	public ImageIcon getIcon() {
		return icon;
	}
	
	public PluginInterface getPlugin(){
		return plugin;
	}

	private ImageIcon resizeIcon(ImageIcon icon){
	    Image img = icon.getImage();  
	    Dimension newSize = UIPropSheet.RICH_LIST_SMALL_ITEM_DIM;
	    Image newimg = img.getScaledInstance(newSize.width,newSize.height,Image.SCALE_SMOOTH);  
	    return new ImageIcon(newimg);  
	}
}
