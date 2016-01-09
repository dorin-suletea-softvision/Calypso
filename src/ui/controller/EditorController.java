package ui.controller;

import javax.swing.JOptionPane;

import sesion.props.Strings;
import ui.editorexceptions.EditorException;
import ui.forms.MainForm;
import engine.views.SheetView;

public class EditorController {

	public void sheetTabClosing(int sheetAtTabNr) {
		SheetView targetSheet = MainForm.getInstance().getSheetTabPane().getSheetBytTabIndex(sheetAtTabNr);
		if (!targetSheet.getModel().isCommited()) {
			int ret = JOptionPane.showConfirmDialog(MainForm.getInstance(), Strings.FILE_NOT_SAVED_MESSAGE);
			switch (ret) {
			case JOptionPane.YES_OPTION:
				FacadeController.getInstance().io_saveSheet(targetSheet.getModel());
				MainForm.getInstance().getSheetTabPane().removeSheet(sheetAtTabNr);
				break;
			case JOptionPane.NO_OPTION:
				MainForm.getInstance().getSheetTabPane().removeSheet(sheetAtTabNr);
				break;
			case JOptionPane.CANCEL_OPTION:
				break;
			default:
				break;
			}
		}
		else {
			targetSheet.getModel().setCommited(true);
			MainForm.getInstance().getSheetTabPane().removeSheet(sheetAtTabNr);
		}
	}

	public void allSheetTabsClosing() {
		int index = MainForm.getInstance().getSheetTabPane().getTabCount() ;
		while (MainForm.getInstance().getSheetTabPane().getTabCount() > 0) {
			index--;
			SheetView targetSheet = MainForm.getInstance().getSheetTabPane().getSheetBytTabIndex(index);
			if (!targetSheet.getModel().isCommited()) {
				int ret = JOptionPane.showConfirmDialog(MainForm.getInstance(), Strings.FILE_NOT_SAVED_MESSAGE);
				switch (ret) {
				case JOptionPane.YES_OPTION:
					FacadeController.getInstance().io_saveSheet(targetSheet.getModel());
					MainForm.getInstance().getSheetTabPane().remove(index);
					break;
				case JOptionPane.NO_OPTION:
					MainForm.getInstance().getSheetTabPane().remove(index);
					break;
				case JOptionPane.CANCEL_OPTION:
					return;
				default:
					break;
				}
			}
			else {
				targetSheet.getModel().setCommited(true);
				MainForm.getInstance().getSheetTabPane().remove(index);
			}
		}
	}
	
	public void prefixStarSheetTabName(SheetView sheetInTab){
		try {
			MainForm.getInstance().getSheetTabPane().prefixStarSheetTabName(sheetInTab);
		} catch (EditorException e) {
			e.printStackTrace();
		}
	}
	
	public void removePrefixStarSheetTabName(SheetView sheetInTab){
		try {
	
			MainForm.getInstance().getSheetTabPane().removePrefixStarSheetTabName(sheetInTab);
		} catch (EditorException e) {
			e.printStackTrace();
		}
	}
	
	public void removePrefixStarAllSheetTabs(){
		try {
			for (SheetView sh : MainForm.getInstance().getSheetTabPane().getAllOpenedSheets())
				MainForm.getInstance().getSheetTabPane().removePrefixStarSheetTabName(sh);
		} catch (EditorException e) {
			e.printStackTrace();
		}
	}
	
	public void addSheetToEditor(SheetView sheet){
		if (!MainForm.getInstance().getSheetTabPane().isSheetInEditor(sheet))
			MainForm.getInstance().getSheetTabPane().addSheet(sheet);
		try {
			MainForm.getInstance().getSheetTabPane().showSheetTab(sheet);
			MainForm.getInstance().getMenu().activateCloseMit();
			MainForm.getInstance().getMenu().activateCloseAllMit();
		} catch (EditorException e) {
			e.printStackTrace();
		}
	}
}
