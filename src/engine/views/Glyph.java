package engine.views;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import sesion.Session;
import bridge.transferable.interfaces.GlyphLocationInterface;
import engine.views.interfaces.ViewDragableInterface;
import engine.views.interfaces.ViewFocusableInterface;

public abstract class Glyph extends JPanel implements ViewDragableInterface, ViewFocusableInterface, GlyphLocationInterface {
	private static final long serialVersionUID = 1L;

	protected List<Glyph> nestedGlyphs;
	private boolean isResizable;
	private boolean isMovable;
	private boolean isNested;
	private boolean isNestingEnabled;

	private boolean hasFocus;

	public Glyph() {
		this.nestedGlyphs = new ArrayList<Glyph>();
		this.isResizable = true;
		this.isMovable = true;
		this.isNested = false;
		isNestingEnabled = false;
	}

	public boolean isNestingEnabled() {
		return isNestingEnabled;
	}

	public boolean isMovable() {
		return isMovable;
	}

	public void setMovable(boolean isMovable) {
		this.isMovable = isMovable;
	}

	public void setNestingEnabled(boolean isNestingEnabled) {
		this.isNestingEnabled = isNestingEnabled;
	}

	public boolean isNested() {
		return isNested;
	}

	public void setNested(boolean isNested) {
		this.isNested = isNested;
	}

	public abstract void nest(Glyph nestedGlyph);

	public abstract void deNest(Glyph neGlyph);

	private void parrentPush(int width, int height) {
		if (getParent() == null || (getParent() instanceof SheetView))
			return;
		if (width > getBounds().width)
			getParent().setSize(getParent().getWidth() + (width - getWidth()), getParent().getWidth());
		if (getHeight() > getBounds().height)
			getParent().setSize(getParent().getWidth(), getParent().getHeight() + (height - getHeight()));

	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		parrentPush(width, height);
		super.setBounds(x, y, width, height);
	}

	@Override
	public void setBounds(Rectangle r) {
		parrentPush(r.width, r.height);
		super.setBounds(r);
	}

	@Override
	public void setSize(Dimension d) {
		parrentPush(d.width, d.height);
		super.setSize(d);
	}

	@Override
	public void setSize(int width, int height) {
		parrentPush(width, height);
		super.setSize(width, height);
	}

	public boolean isResizable() {
		return isResizable;
	}

	public void setResizable(boolean isResizable) {
		this.isResizable = isResizable;
	}

	public boolean hasNested(Glyph glyph) {
		List<Glyph> allSubGlyphs = getAllGlyphs();
		return allSubGlyphs.contains(glyph);
	}

	public boolean hasFocus() {
		return hasFocus;
	}

	public void setFocus(boolean hasFocus) {
		this.hasFocus = hasFocus;
	}

	public int nestedListSize() {
		return nestedGlyphs.size();
	}

	public List<Glyph> getGlyphs() {
		return nestedGlyphs;
	}

	public List<Glyph> getAllGlyphs() {
		List<Glyph> ret = new ArrayList<Glyph>();
		ret.addAll(nestedGlyphs);
		for (Glyph g : nestedGlyphs)
			ret.addAll(g.getAllGlyphs());
		return ret;
	}

	public ArrayList<Glyph> createIntersectionList() {
		ArrayList<Glyph> intersectionList = new ArrayList<Glyph>();
		Rectangle gBounds;
		Rectangle thisBounds = SwingUtilities.convertRectangle(this.getParent(), this.getBounds(), Session.getSelectedSheet());
		for (Glyph g : Session.getSelectedSheet().getAllGlyphs()) {
			gBounds = SwingUtilities.convertRectangle(g.getParent(), g.getBounds(), Session.getSelectedSheet());
			if (!g.equals(this) && !this.hasNested(g) && !g.hasNested(this) && (thisBounds.intersects(gBounds) || thisBounds.contains(gBounds) || gBounds.contains(thisBounds)))

				intersectionList.add(g);

		}
		return intersectionList;
	}
}
