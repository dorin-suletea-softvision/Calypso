package ui.components;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import sesion.Session;
import ui.editorexceptions.EditorException;
import bridge.exceptions.ComlianceException;
import engine.views.SheetView;

public class SheetTabbedPane extends JTabbedPane {
	private static final long serialVersionUID = 1L;

	public SheetTabbedPane() {
		super();
		addListeners();
	}

	private void addListeners() {
		this.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				try {
					// if no sheets there
					if (getSelectedIndex() == -1)
						Session.removeSelectedSheet();
					else
						Session.setSelectedSheet(getSheetBytTabIndex(getSelectedIndex()));

				} catch (ComlianceException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void addSheet(SheetView sheet) {
		addTab(sheet.getName(), new JScrollPane(sheet));
		setTabComponentAt(getTabCount() - 1, new CloseTabButton(this));
	}

	public void removeSheet(int index) {
		remove(index);
	}

	public boolean isSheetInEditor(SheetView sheet) {
		if (getTabIndexBySheet(sheet) == -1)
			return false;
		return true;
	}

	public SheetView getSheetBytTabIndex(int index) {
		return (SheetView) ((JScrollPane) getComponentAt(index)).getViewport().getView();
	}

	public void prefixStarSheetTabName(SheetView sheet) throws EditorException {
		int tabIndex = getTabIndexBySheet(sheet);
		if (tabIndex == -1)
			throw new EditorException("Sheet :" + sheet + "is not in the editor");

		if (!(getTitleAt(tabIndex).startsWith("*"))) {
			setTitleAt(tabIndex, "*  " + sheet.getName());
			getTabComponentAt(tabIndex).revalidate();
			getTabComponentAt(tabIndex).repaint();
		}
	}

	public void removePrefixStarSheetTabName(SheetView sheet) throws EditorException {
		int tabIndex = getTabIndexBySheet(sheet);
		if (tabIndex == -1)
			throw new EditorException("Sheet :" + sheet + "is not in the editor");

		if (getTitleAt(tabIndex).startsWith("*")) {
			setTitleAt(tabIndex, sheet.getName());
			getTabComponentAt(tabIndex).revalidate();
			getTabComponentAt(tabIndex).repaint();
		}
	}

	// must be called only after the sheet is added
	public void showSheetTab(SheetView selectedSheet) throws EditorException {
		setSelectedIndex(getTabIndexBySheet(selectedSheet));
	}

	public int getTabIndexBySheet(SheetView sheet) {
		for (int i = 0; i < getTabCount(); i++)
			if (((JScrollPane) getComponentAt(i)).getViewport().getView().equals(sheet))
				return i;
		return -1;
	}

	public List<SheetView> getAllOpenedSheets(){
		List<SheetView> ret = new ArrayList<SheetView>();
		for (int i = 0; i < getTabCount(); i++)
			ret.add((SheetView) ((JScrollPane) getComponentAt(i)).getViewport().getView());
		return ret;
	}
	
	public boolean allSheetsSaved(){
		for (SheetView sh : getAllOpenedSheets())
			if (!sh.getModel().isCommited())
				return false;
		return true;
	}
}
