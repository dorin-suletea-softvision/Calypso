package engine.views.interfaces;

import engine.views.Glyph;

public interface ViewDeleteableInterface {
	public void delete();
	public void restore(Glyph exParent);
	public Glyph getParrent();
}
