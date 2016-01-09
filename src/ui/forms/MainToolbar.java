package ui.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import sesion.Session;
import sesion.props.UIPropSheet;
import ui.controller.FacadeController;
import ui.interfaces.LocalToolbarType;
import ui.resources.ResourceRetriever;

public class MainToolbar extends LocalToolbarType {
	private static final long serialVersionUID = 1L;

	private JButton newBtn;
	private JButton openBtn;
	private JButton exportAsImgBtn;
	private JButton saveBtn;
	private JButton saveAllBtn;
	private JButton undoBtn;
	private JButton redoBtn;
	private JButton delBtn;

	public MainToolbar() {
		addComponents();
		addListeners();
		setAlignmentX(0);
		
		deactivateSaveBtn();
		deactivateSaveAllBtn();
		deactivateExportAsImageBtn();
		deactivateRedoBtn();
		deactivateUndoBtn();
		deactivateDelBtn();
		

	}

	private void addComponents() {
		newBtn = new JButton(ResourceRetriever.loadImage(ResourceRetriever.NEW_ICON_IMG));
		newBtn.setPreferredSize(UIPropSheet.MEDIUM_BTN_DIM);
		add(newBtn);

		openBtn = new JButton(ResourceRetriever.loadImage(ResourceRetriever.OPEN_ICON_IMG));
		openBtn.setPreferredSize(UIPropSheet.MEDIUM_BTN_DIM);
		add(openBtn);

		exportAsImgBtn = new JButton(ResourceRetriever.loadImage(ResourceRetriever.EXPORT_AS_IMAGE_ICON_IMG));
		exportAsImgBtn.setPreferredSize(UIPropSheet.MEDIUM_BTN_DIM);
		add(exportAsImgBtn);
		
		saveBtn = new JButton(ResourceRetriever.loadImage(ResourceRetriever.SAVE_ICON_IMG));
		saveBtn.setPreferredSize(UIPropSheet.MEDIUM_BTN_DIM);
		add(saveBtn);

		saveAllBtn = new JButton(ResourceRetriever.loadImage(ResourceRetriever.SAVE_ALL_ICON_IMG));
		saveAllBtn.setPreferredSize(UIPropSheet.MEDIUM_BTN_DIM);
		add(saveAllBtn);
		
		addSeparator();

		undoBtn = new JButton(ResourceRetriever.loadImage(ResourceRetriever.UNDO_ICON_IMG));
		undoBtn.setPreferredSize(UIPropSheet.MEDIUM_BTN_DIM);
		add(undoBtn);

		redoBtn = new JButton(ResourceRetriever.loadImage(ResourceRetriever.REDO_ICON_IMG));
		redoBtn.setPreferredSize(UIPropSheet.MEDIUM_BTN_DIM);
		add(redoBtn);

		addSeparator();

		delBtn = new JButton(ResourceRetriever.loadImage(ResourceRetriever.DELETE_ICON_IMG));
		delBtn.setPreferredSize(UIPropSheet.MEDIUM_BTN_DIM);
		add(delBtn);
	}

	private void addListeners() {
		newBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FacadeController.getInstance().menu_showCreateSheetForm();
			}
		});
		openBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FacadeController.getInstance().menu_showOpenSheetForm();
			}
		});
		exportAsImgBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FacadeController.getInstance().menu_showExportSheetAsImageForm(Session.getSelectedSheet());
			}
		});
		saveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FacadeController.getInstance().menu_saveSelectedSheet();
				FacadeController.getInstance().editor_removePrefixStarSheetTabName(Session.getSelectedSheet());
				
				MainForm.getInstance().getMenu().deactivateSaveSheetMit();
				MainForm.getInstance().getGeneralToolbar().deactivateSaveBtn();
				
				
				
				if (MainForm.getInstance().getSheetTabPane().allSheetsSaved()) {
					MainForm.getInstance().getGeneralToolbar().deactivateSaveAllBtn();
					MainForm.getInstance().getMenu().deactivateSaveAllSheetMit();
				}
			}
		});
		saveAllBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FacadeController.getInstance().menu_saveAllSheets();
				FacadeController.getInstance().editor_removePrefixStarAllSheetTabs();
				
				if (MainForm.getInstance().getSheetTabPane().allSheetsSaved()) {
					MainForm.getInstance().getGeneralToolbar().deactivateSaveAllBtn();
					MainForm.getInstance().getMenu().deactivateSaveAllSheetMit();
				}
			}
		});
		undoBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FacadeController.getInstance().menu_undo();
			}
		});
		redoBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FacadeController.getInstance().menu_redo();
			}
		});
		delBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FacadeController.getInstance().menu_deleteSelected();
			}
		});
	}

	public void activateSaveBtn() {
		saveBtn.setEnabled(true);
	}

	public void deactivateSaveBtn() {
		saveBtn.setEnabled(false);
	}

	public void activateSaveAllBtn() {
		saveAllBtn.setEnabled(true);
	}

	public void deactivateSaveAllBtn() {
		saveAllBtn.setEnabled(false);
	}
	
	public void activateExportAsImageBtn() {
		exportAsImgBtn.setEnabled(true);
	}

	public void deactivateExportAsImageBtn() {
		exportAsImgBtn.setEnabled(false);
	}

	public void activateUndoBtn() {
		undoBtn.setEnabled(true);
	}

	public void deactivateUndoBtn() {
		undoBtn.setEnabled(false);
	}

	public void activateRedoBtn() {
		redoBtn.setEnabled(true);
	}

	public void deactivateRedoBtn() {
		redoBtn.setEnabled(false);
	}
	
	public void activateDelBtn(){
		delBtn.setEnabled(true);
	}
	
	public void deactivateDelBtn(){
		delBtn.setEnabled(false);
	}
	
	

}
