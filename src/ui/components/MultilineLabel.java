package ui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

import engine.views.EntityView;

import sesion.Session;
import sesion.props.ModelPropertySheet;

public class MultilineLabel extends JComponent {
	private static final long serialVersionUID = 1L;
	private static final int defaultVTextSpacing = 5;
	private static final int defaultHTextSpacing = 5;
	private static final String endl = "\n";

	private int textVerticalSpacing;
	private int textHorizontalSpacing;

	private Point virtualLocation;
	private Dimension virtaulSize;

	private boolean minimized;

	private ArrayList<String> text;

	public MultilineLabel() {
		this("", defaultVTextSpacing, defaultHTextSpacing);
	}

	public MultilineLabel(String text) {
		this(text, defaultVTextSpacing, defaultHTextSpacing);
	}

	public MultilineLabel(String text, int textVerticalSpacing, int textHorizontalLeftSpacing) {
		super();
		this.setFont(ModelPropertySheet.getInstance().getSheetFont());
		this.textVerticalSpacing = textVerticalSpacing;
		this.textHorizontalSpacing = textHorizontalLeftSpacing;
		this.setText(text);
		virtualLocation = getLocation();
		virtaulSize = computeSize();
		setBorder(BorderFactory.createLineBorder(Color.black));
		setToolTipText(text);
		setOpaque(false);

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// draw background
		Color c = g.getColor();
		g.setColor(ModelPropertySheet.TRANSPARENT_LIGHT_GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(c);

		// the text's base line is given to drawString, so the string to be
		// visible you must
		// leave room for text height
		int x = textHorizontalSpacing;
		int y = getFont().getSize();
		if (!minimized)
			for (String st : text) {
				g.drawString(st, x, y);
				y += getFont().getSize() + textVerticalSpacing;
			}
		else
			g.drawString("- - -", textHorizontalSpacing, getFont().getSize());

	}

	private ArrayList<String> parseText(String text) {
		ArrayList<String> ret = new ArrayList<String>();
		StringTokenizer tk = new StringTokenizer(text, endl);
		while (tk.hasMoreTokens())
			ret.add(tk.nextToken());

		return ret;
	}

	private Dimension computeSize() {
		Dimension ret = new Dimension();
		String maxLenStr = new String();
		for (String te : text) {
			if (te.length() > maxLenStr.length())
				maxLenStr = te;
		}
		FontMetrics fm = getFontMetrics(getFont());
		int width = fm.stringWidth(maxLenStr) + textHorizontalSpacing * 2;
		int height = fm.getHeight() * text.size() + textVerticalSpacing * (text.size() - 1);
		// text.size()-1 because there is no spacing on line 1
		ret = new Dimension(width, height);
		virtaulSize = ret;
		return ret;
	}

	public void setText(String text) {
		setToolTipText(text);
		this.text = parseText(text);
		if (!minimized)
			setSize(computeSize());
	}

	public String getText() {
		String ret = new String();
		for (String st : text) {
			ret += st;
		}
		return ret;
	}

	public void minimize() {
		minimized = true;
		FontMetrics fm = getFontMetrics(getFont());
		int width = fm.stringWidth("- - -") + textHorizontalSpacing;
		int height = fm.getHeight();
		setSize(new Dimension(width + textHorizontalSpacing, height));
	}

	public void maximize() {
		minimized = false;
		getParent().setComponentZOrder(this, getParent().getComponentCount() - 1);// make
																					// the
																					// lowest
																					// ZOrder
																					// possible,
																					// so
																					// it
																					// does'nt
																					// hide
																					// the
																					// snapViews

		setLocation(virtualLocation);
		setSize(virtaulSize);
	}

	public boolean overlapes(MultilineLabel lbl) {
		if (new Rectangle(virtualLocation.x, virtualLocation.y, virtaulSize.width, virtaulSize.height).intersects(new Rectangle(lbl.virtualLocation.x, lbl.virtualLocation.y, lbl.virtaulSize.width,
				lbl.virtaulSize.height)))
			return true;
		else
			return false;
	}

	public boolean overlapes(EntityView entityView) {
		if (new Rectangle(virtualLocation.x, virtualLocation.y, virtaulSize.width, virtaulSize.height).intersects(entityView.getBounds()))
			return true;
		else
			return false;
	}

	public Point getVirtaulLocation() {
		return virtualLocation;
	}

	public void setVirtaulLocation(Point virtaulLocation) {
		this.virtualLocation = virtaulLocation;
	}

	@Override
	public void setLocation(int x, int y) {
		setVirtaulLocation(new Point(x, y));
		super.setLocation(x, y);
	}

	@Override
	public void setLocation(Point p) {
		setVirtaulLocation(p);
		super.setLocation(p);
	}

	public boolean isMinimized() {
		return minimized;
	}

	public void update() {
		if (Session.getSelectedSheet().componentsOverlap(this))
			minimize();
		else
			maximize();
	}

}
