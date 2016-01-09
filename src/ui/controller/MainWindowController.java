package ui.controller;

import javax.swing.JOptionPane;

import sesion.props.Strings;
import ui.forms.MainForm;

public class MainWindowController {
	/**
	 * 
	 * @return 1 the exit code
	 *         0 the no_exit
	 */
	public int onApplicationExit() {
		if (!MainForm.getInstance().getSheetTabPane().allSheetsSaved()) {
			int ret = JOptionPane.showConfirmDialog(MainForm.getInstance(), Strings.EXIT_AND_SAVE_MESSAGE);
			if (ret == JOptionPane.YES_OPTION){
				FacadeController.getInstance().menu_saveAllSheets();
				MainForm.getInstance().dispose();
				return 1;//
			}else if(ret==JOptionPane.NO_OPTION){
				MainForm.getInstance().dispose();
				return 1;
			}
			return 0;
		}
		return 1;
	}
}
