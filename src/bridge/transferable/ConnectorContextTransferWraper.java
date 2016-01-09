package bridge.transferable;

import javax.swing.ImageIcon;

import bridge.transferable.context.ConnectorContextInterface;

public class ConnectorContextTransferWraper {
	private final ConnectorContextInterface context;
	private final ImageIcon icon;

	public ConnectorContextInterface getContext() {
		return context;
	}

	public ImageIcon getIcon() {
		return icon;
	}

	public ConnectorContextTransferWraper(ConnectorContextInterface context, ImageIcon icon) {
		super();
		this.context = context;
		this.icon = icon;
	}
}
