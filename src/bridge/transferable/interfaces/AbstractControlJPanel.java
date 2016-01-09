package bridge.transferable.interfaces;

import javax.swing.JPanel;

public abstract class AbstractControlJPanel extends JPanel {
	private static final long serialVersionUID = 5342199451965391700L;
	public abstract void cleanup();
	public abstract Object getModelObject();
	public boolean equals(Object obj){
		if (!(obj instanceof AbstractControlJPanel))
			return false;
		if (!((AbstractControlJPanel)obj).getModelObject().equals(getModelObject()))
			return false;
		return true;
	}
	public abstract void initComponents();
}
