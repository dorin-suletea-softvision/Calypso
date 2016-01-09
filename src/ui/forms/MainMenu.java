package ui.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import sesion.Session;
import sesion.props.MenuAcceleratorPropertySheet;
import ui.controller.FacadeController;

public class MainMenu extends JMenuBar {
	private static final long serialVersionUID = 1L;
	private JMenu fileMenu;
	private JMenu editMenu;

	private JMenuItem newSheetMit;
	private JMenuItem openSheetMit;

	private JMenuItem closeSheetMit;
	private JMenuItem closeAllMit;

	private JMenuItem saveSheetMit;
	private JMenuItem saveSheetAsMit;
	private JMenuItem saveAllSheetMit;
	private JMenuItem exportAsImageMit;

	private JMenuItem exitMit;

	private JMenuItem undoMit;
	private JMenuItem redoMit;
	private JMenuItem deleteMit;

	private JMenu increaseCanvasM;
	private JMenu shrinkCanvasM;

	public MainMenu() {
		super();
		addFileTabComponents();
		addEditTabComponents();
		addListeners();
		deactivateCloseMit();
		deactivateCloseAllMit();
		deactivateSaveSheetMit();
		deactivateSaveSheetAsMit();
		deactivateSaveAllSheetMit();
		deactivateExportAsImageMit();
		deactivateDeleteMit();
		deactivateIncreaseCanvasMit();
		deactivateShrinkCanvasMit();
		deactivateUndoMit();
		deactivateRedoMit();
	}

	private void addFileTabComponents() {
		fileMenu = new JMenu("File");
		fileMenu.setMnemonic(MenuAcceleratorPropertySheet.FILE_MNEMONIC);

		// group 1
		JMenu newDocM = new JMenu("New");
		newDocM.setMnemonic(MenuAcceleratorPropertySheet.NEW_DOC_GROUP_MNEMONIC);
		fileMenu.add(newDocM);

		// TODO no project support yet
		// JMenuItem newProject = new JMenuItem("Project");
		// newProject.setAccelerator(KeyStroke.getKeyStroke(MenuAcceleratorPropertySheet.NEW_PROJECT));
		// newDocM.add(newProject);

		newSheetMit = new JMenuItem("Sheet");
		newSheetMit.setAccelerator(KeyStroke.getKeyStroke(MenuAcceleratorPropertySheet.NEW_SHEET));
		newDocM.add(newSheetMit);

		JMenu openDocM = new JMenu("Open");
		newDocM.setMnemonic(MenuAcceleratorPropertySheet.OPEN_DOC_GROUP_MNEMONIC);
		fileMenu.add(openDocM);

		// TODO no project support yet
		// JMenuItem openProject = new JMenuItem("Project");
		// openProject.setAccelerator(KeyStroke.getKeyStroke(MenuAcceleratorPropertySheet.OPEN_PROJECT));
		// openDocM.add(openProject);

		openSheetMit = new JMenuItem("Sheet");
		openSheetMit.setAccelerator(KeyStroke.getKeyStroke(MenuAcceleratorPropertySheet.OPEN_SHEET));
		openDocM.add(openSheetMit);

		fileMenu.addSeparator();

		// group 2
		closeAllMit = new JMenuItem("Close All");
		closeAllMit.setAccelerator(KeyStroke.getKeyStroke(MenuAcceleratorPropertySheet.CLOSE_ALL));
		fileMenu.add(closeAllMit);

		closeSheetMit = new JMenuItem("Close");
		closeSheetMit.setAccelerator(KeyStroke.getKeyStroke(MenuAcceleratorPropertySheet.CLOSE_SHEET));
		fileMenu.add(closeSheetMit);

		fileMenu.addSeparator();

		// group 3
		saveSheetMit = new JMenuItem("Save");
		saveSheetMit.setAccelerator(KeyStroke.getKeyStroke(MenuAcceleratorPropertySheet.SAVE_SHEET));
		fileMenu.add(saveSheetMit);

		saveSheetAsMit = new JMenuItem("Save As");
		saveSheetAsMit.setAccelerator(KeyStroke.getKeyStroke(MenuAcceleratorPropertySheet.SAVE_AS_SHEET));
		fileMenu.add(saveSheetAsMit);

		saveAllSheetMit = new JMenuItem("Save all");
		saveAllSheetMit.setAccelerator(KeyStroke.getKeyStroke(MenuAcceleratorPropertySheet.SAVE_ALL_SHEET));
		fileMenu.add(saveAllSheetMit);

		exportAsImageMit = new JMenuItem("Export as image");
		exportAsImageMit.setAccelerator(KeyStroke.getKeyStroke(MenuAcceleratorPropertySheet.EXPORT_SHEET_AS_IMAGE));
		fileMenu.add(exportAsImageMit);

		fileMenu.addSeparator();

		// group 4
		exitMit = new JMenuItem("Exit");
		exitMit.setAccelerator(KeyStroke.getKeyStroke(MenuAcceleratorPropertySheet.EXIT));
		fileMenu.add(exitMit);

		this.add(fileMenu);
	}

	private void addEditTabComponents() {
		editMenu = new JMenu("Edit");
		editMenu.setMnemonic(MenuAcceleratorPropertySheet.EDIT_MNEMONIC);

		undoMit = new JMenuItem("Undo");
		undoMit.setAccelerator(KeyStroke.getKeyStroke(MenuAcceleratorPropertySheet.UNDO));
		editMenu.add(undoMit);

		redoMit = new JMenuItem("Redo");
		redoMit.setAccelerator(KeyStroke.getKeyStroke(MenuAcceleratorPropertySheet.REDO));
		editMenu.add(redoMit);

		fileMenu.addSeparator();

		increaseCanvasM = new JMenu("Expand canvas");
		increaseCanvasM.setMnemonic(MenuAcceleratorPropertySheet.EXPAND_CANVAS_MNEMONIC);
		editMenu.add(increaseCanvasM);

		class CanvasSizeListener implements ActionListener {
			private int dxy;

			public CanvasSizeListener(final int dxy) {
				this.dxy = dxy;
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				FacadeController.getInstance().menu_changeCanvasSize(dxy);
			}
		}

		JMenuItem _p_100Px = new JMenuItem("100Px");
		increaseCanvasM.add(_p_100Px);
		_p_100Px.addActionListener(new CanvasSizeListener(100));
		JMenuItem _p_250Px = new JMenuItem("250Px");
		increaseCanvasM.add(_p_250Px);
		_p_250Px.addActionListener(new CanvasSizeListener(250));
		JMenuItem _p_500Px = new JMenuItem("500Px");
		increaseCanvasM.add(_p_500Px);
		_p_500Px.addActionListener(new CanvasSizeListener(500));
		JMenuItem _p_1000Px = new JMenuItem("1000Px");
		increaseCanvasM.add(_p_1000Px);
		_p_1000Px.addActionListener(new CanvasSizeListener(1000));
		JMenuItem _p_1500Px = new JMenuItem("1500Px");
		increaseCanvasM.add(_p_1500Px);
		_p_1500Px.addActionListener(new CanvasSizeListener(1500));

		shrinkCanvasM = new JMenu("Shrink canvas");
		increaseCanvasM.setMnemonic(MenuAcceleratorPropertySheet.SHRINK_CANVAS_MNEMONIC);
		editMenu.add(shrinkCanvasM);

		JMenuItem _100Px = new JMenuItem("100Px");
		shrinkCanvasM.add(_100Px);
		_100Px.addActionListener(new CanvasSizeListener(-100));
		JMenuItem _250Px = new JMenuItem("250Px");
		shrinkCanvasM.add(_250Px);
		_250Px.addActionListener(new CanvasSizeListener(-250));
		JMenuItem _500Px = new JMenuItem("500Px");
		shrinkCanvasM.add(_500Px);
		_500Px.addActionListener(new CanvasSizeListener(-500));
		JMenuItem _1000Px = new JMenuItem("1000Px");
		shrinkCanvasM.add(_1000Px);
		_1000Px.addActionListener(new CanvasSizeListener(-1000));
		JMenuItem _1500Px = new JMenuItem("1500Px");
		shrinkCanvasM.add(_1500Px);
		_1500Px.addActionListener(new CanvasSizeListener(-1500));

		fileMenu.addSeparator();

		deleteMit = new JMenuItem("Delete selected");
		deleteMit.setAccelerator(KeyStroke.getKeyStroke(MenuAcceleratorPropertySheet.DELETE_SELECTED));
		editMenu.add(deleteMit);

		this.add(editMenu);
	}

	private void addListeners() {
		newSheetMit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FacadeController.getInstance().menu_showCreateSheetForm();
			}
		});
		openSheetMit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FacadeController.getInstance().menu_showOpenSheetForm();
			}
		});
		closeSheetMit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FacadeController.getInstance().menu_sheetTabClosing();
			}
		});
		closeAllMit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FacadeController.getInstance().menu_sheetTabsClosing();
			}
		});
		saveSheetMit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
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
		saveSheetAsMit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FacadeController.getInstance().menu_saveSelectedSheetAs(Session.getSelectedSheet());
			}
		});

		saveAllSheetMit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FacadeController.getInstance().menu_saveAllSheets();
				FacadeController.getInstance().editor_removePrefixStarAllSheetTabs();

				if (MainForm.getInstance().getSheetTabPane().allSheetsSaved()) {
					MainForm.getInstance().getGeneralToolbar().deactivateSaveAllBtn();
					MainForm.getInstance().getMenu().deactivateSaveAllSheetMit();
				}
			}
		});
		exportAsImageMit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				FacadeController.getInstance().menu_showExportSheetAsImageForm(Session.getSelectedSheet());
			}
		});
		exitMit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FacadeController.getInstance().menu_exit();
			}
		});
		undoMit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FacadeController.getInstance().menu_undo();
			}
		});
		redoMit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FacadeController.getInstance().menu_redo();
			}
		});
		deleteMit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FacadeController.getInstance().menu_deleteSelected();
			}
		});
	}

	public void activateCloseMit() {
		closeSheetMit.setEnabled(true);
	}

	public void deactivateCloseMit() {
		closeSheetMit.setEnabled(false);
	}

	public void activateCloseAllMit() {
		closeAllMit.setEnabled(true);
	}

	public void deactivateCloseAllMit() {
		closeAllMit.setEnabled(false);
	}

	public void activateSaveSheetMit() {
		saveSheetMit.setEnabled(true);
	}

	public void deactivateSaveSheetMit() {
		saveSheetMit.setEnabled(false);
	}

	public void activateSaveSheetAsMit() {
		saveSheetAsMit.setEnabled(true);
	}

	public void deactivateSaveSheetAsMit() {
		saveSheetAsMit.setEnabled(false);
	}

	public void activateSaveAllSheetMit() {
		saveAllSheetMit.setEnabled(true);
	}

	public void deactivateSaveAllSheetMit() {
		saveAllSheetMit.setEnabled(false);
	}

	public void activateExportAsImageMit() {
		exportAsImageMit.setEnabled(true);
	}

	public void deactivateExportAsImageMit() {
		exportAsImageMit.setEnabled(false);
	}

	public void activateDeleteMit() {
		deleteMit.setEnabled(true);
	}

	public void deactivateDeleteMit() {
		deleteMit.setEnabled(false);
	}

	public void activateIncreaseCanvasMit() {
		increaseCanvasM.setEnabled(true);
	}

	public void deactivateIncreaseCanvasMit() {
		increaseCanvasM.setEnabled(false);
	}

	public void activateShrinkCanvasMit() {
		shrinkCanvasM.setEnabled(true);
	}

	public void deactivateShrinkCanvasMit() {
		shrinkCanvasM.setEnabled(false);
	}

	public void activateUndoMit() {
		undoMit.setEnabled(true);
	}

	public void deactivateUndoMit() {
		undoMit.setEnabled(false);
	}

	public void activateRedoMit() {
		redoMit.setEnabled(true);
	}

	public void deactivateRedoMit() {
		redoMit.setEnabled(false);
	}

}
