package ui.components;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class RichList extends JList<RichListItem> {
	private static final long serialVersionUID = 1L;

	public RichList() {
		super(new DefaultListModel<RichListItem>());
		setCellRenderer(new ListRenderer());
	}

	public void addComponent(RichListItem item) {
		((DefaultListModel<RichListItem>) getModel()).addElement(item);
	}

	@Override
	public void setSize(Dimension d) {
		((ListRenderer) getCellRenderer()).setListWidth(d.width);
		super.setSize(d);
	}

	@Override
	public void setSize(int width, int height) {
		((ListRenderer) getCellRenderer()).setListWidth(width);
		super.setSize(width, height);
	}

}

class ListRenderer extends JLabel implements ListCellRenderer<RichListItem> {
	private static final long serialVersionUID = 1L;
	private int listWidth;

	public ListRenderer() {
		setOpaque(false);
		listWidth = 200;
		setIconTextGap(12);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends RichListItem> list, RichListItem value, int index, boolean isSelected, boolean cellHasFocus) {
		setText(value.getTitle());
		setIcon(value.getIcon());
		setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
		setPreferredSize(new Dimension(listWidth, getPreferredSize().height));
		return this;
	}

	public void setListWidth(int listWidth) {
		this.listWidth = listWidth;
	}
}
