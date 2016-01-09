package ui.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import sesion.Session;
import sesion.props.IOPropertySheet;
import sesion.props.Strings;
import ui.forms.CreateSheetForm;
import ui.forms.ExportSheetAsImageForm;
import ui.forms.MainForm;
import bridge.transferable.interfaces.CommandInterface;
import engine.command.ChangeSheetViewSize;
import engine.command.DeleteCommand;
import engine.command.SequenceCommand;
import engine.model.SheetModel;
import engine.views.SheetView;
import engine.views.interfaces.ViewFocusableInterface;

public class MenuController {
	public void showCreateSheetForm() {
		CreateSheetForm.getInstace().setVisible(true);
	}

	public void showOpenSheetForm() {
		JFileChooser fileChooser = new JFileChooser(IOPropertySheet.DEFAUL_WORKSPACE_DIR);
		fileChooser.setFileFilter(new FileFilter() {
			public String getDescription() {

				return IOPropertySheet.SHEET_EXTENSION;
			}

			public boolean accept(File arg0) {
				if (arg0.isDirectory())
					return true;
				if (arg0.getAbsolutePath().endsWith(IOPropertySheet.SHEET_EXTENSION))
					return true;
				else
					return false;
			}
		});
		int retval = fileChooser.showOpenDialog(MainForm.getInstance());
		if (retval == JFileChooser.APPROVE_OPTION) {
			FacadeController.getInstance().io_openSheet(fileChooser.getSelectedFile().getAbsolutePath());
		}
	}

	public void showExportSheetAsImgForm(SheetView sheet) {
		ExportSheetAsImageForm csf = new ExportSheetAsImageForm(Session.getSelectedSheet().getName());
		csf.setVisible(true);
	}

	public void showSaveSheetAsForm(SheetView sheet) {
		JFileChooser fileChooser = new JFileChooser(new File(IOPropertySheet.DEFAUL_WORKSPACE_DIR));

		int retval = fileChooser.showOpenDialog(MainForm.getInstance());
		if (retval == JFileChooser.APPROVE_OPTION) {
			String newFileName = new String(fileChooser.getSelectedFile().getAbsolutePath());
			if (!newFileName.endsWith(IOPropertySheet.SHEET_EXTENSION))
				newFileName += IOPropertySheet.SHEET_EXTENSION;

			boolean fileExists = false;
			for (File f : fileChooser.getSelectedFile().getParentFile().listFiles())
				if (f.getAbsolutePath().equals(newFileName))
					fileExists = true;

			if (fileExists)
				if (!(JOptionPane.showConfirmDialog(MainForm.getInstance(), Strings.FILE_EXISTS_MESSAGE + newFileName, "Overwrite?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION))
					return;
			
			
			SheetModel newModel = sheet.getModel().clone();
			newModel.setDiskFile(new File(newFileName));
			newModel.setSheetName(fileChooser.getSelectedFile().getName());

			for (SheetView sv : MainForm.getInstance().getSheetTabPane().getAllOpenedSheets())
				if (sv.getModel().equals(newModel) && (!sheet.getModel().getDiskFile().equals(newModel.getDiskFile()))) {
					JOptionPane.showMessageDialog(MainForm.getInstance(), Strings.FILE_LOCKED_MESSAGE);
					return;
				}

			System.out.println(newModel.getConnectors());
			FacadeController.getInstance().io_saveSheet(newModel);
			System.out.println("Save As operation success");
		}
	}

	public void curretSheetTabClosing() {
		int index = MainForm.getInstance().getSheetTabPane().getTabIndexBySheet(Session.getSelectedSheet());

		MainForm.getInstance().getMenu().deactivateCloseAllMit();
		MainForm.getInstance().getMenu().deactivateCloseMit();

		FacadeController.getInstance().editor_sheetTabClosing(index);
	}

	public void allSheetTabsClosing() {
		FacadeController.getInstance().editor_allSheetTabsClosing();

	}

	public void saveSelectedSheet() {
		FacadeController.getInstance().io_saveSheet(Session.getSelectedSheet().getModel());
	}

	public void saveAllSheets() {
		MainForm.getInstance().getGeneralToolbar().deactivateSaveBtn();
		MainForm.getInstance().getMenu().deactivateSaveSheetMit();
		List<SheetView> openedSheets = MainForm.getInstance().getSheetTabPane().getAllOpenedSheets();
		for (SheetView sh : openedSheets) {
			FacadeController.getInstance().io_saveSheet(sh.getModel());
		}
	}

	public int exit() {
		return FacadeController.getInstance().mainWindow_onApplicationExit();
	}

	public void undo() {
		Session.undo();
	}

	public void redo() {
		Session.redo();
	}

	public void changeCanvasSize(int dxy) {
		CommandInterface cmd = new ChangeSheetViewSize(dxy);
		if (cmd.execute())
			Session.addCommand(cmd);
	}

	public void deleteSelected() {
		int ret = JOptionPane.showConfirmDialog(MainForm.getInstance(), Strings.DELETE_MESSAGE);
		if (ret == JOptionPane.YES_OPTION) {
			ArrayList<CommandInterface> cmdList = new ArrayList<CommandInterface>();
			for (ViewFocusableInterface s : Session.getSelectedViews())
				cmdList.add(new DeleteCommand(s));

			CommandInterface cmdBat = new SequenceCommand(cmdList);
			if (cmdBat.execute())
				Session.addCommand(cmdBat);
		}
	}
}
