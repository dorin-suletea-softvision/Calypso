package bridge.transferable;

import javax.swing.ImageIcon;

import bridge.transferable.context.EntityContextInterface;


public class EntityContextTransferWraper {
	private final EntityContextInterface context;
	private final ImageIcon              icon;
	
	public EntityContextInterface getContext() {
		return context;
	}

	public ImageIcon getIcon() {
		return icon;
	}

	public EntityContextTransferWraper(EntityContextInterface context, ImageIcon icon) {
		super();
		this.context = context;
		this.icon = icon;
	}
}
