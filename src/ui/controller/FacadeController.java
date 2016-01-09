package ui.controller;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JToolBar;

import sesion.Session;
import ui.components.EntityContextListItem;
import ui.components.Joystick;
import ui.components.RichListItem;
import ui.forms.MainForm;
import ui.interfaces.LocalToolbarType;
import bridge.transferable.EntityContextTransferWraper;
import bridge.transferable.PluginInterface;
import bridge.transferable.context.ConnectorContextInterface;
import engine.model.SheetModel;
import engine.views.ConnectionView;
import engine.views.EntityView;
import engine.views.Glyph;
import engine.views.SheetView;

public class FacadeController {
	private NestDialogController nestingDialogController;
	private ListDnDController listController;
	private ConnectSequenceController connectSequenceController;
	private JoystickUpdateController joystickUpdater;
	private ConnectorTypeChooserController connectorTypeChooserController;
	private MenuController menuController;
	private IOController ioController;
	private EditorController editorController;
	private MainWindowController mainWindowController;

	private static FacadeController instance = new FacadeController();

	private FacadeController() {
		mainWindowController = new MainWindowController();
		editorController = new EditorController();
		nestingDialogController = new NestDialogController();
		listController = new ListDnDController();
		connectSequenceController = new ConnectSequenceController();
		joystickUpdater = new JoystickUpdateController();
		connectorTypeChooserController = new ConnectorTypeChooserController();
		menuController = new MenuController();
		ioController = new IOController();
	}

	public static FacadeController getInstance() {
		return instance;
	}

	public void editor_sheetTabClosing(int sheetAtTabNr) {
		editorController.sheetTabClosing(sheetAtTabNr);
	}

	public void editor_allSheetTabsClosing() {
		editorController.allSheetTabsClosing();
	}

	public void editor_prefixStarSheetTabName(SheetView sheetInTab) {
		editorController.prefixStarSheetTabName(sheetInTab);
	}

	public void editor_addSheetToEditor(SheetView sheet) {
		editorController.addSheetToEditor(sheet);
	}

	public void editor_removePrefixStarAllSheetTabs() {
		editorController.removePrefixStarAllSheetTabs();
	}

	public void editor_removePrefixStarSheetTabName(SheetView sheetInTab) {
		editorController.removePrefixStarSheetTabName(sheetInTab);
	}

	public void menu_sheetTabClosing() {
		menuController.curretSheetTabClosing();
	}

	public void menu_sheetTabsClosing() {
		menuController.allSheetTabsClosing();
	}

	public void menu_showOpenSheetForm() {
		menuController.showOpenSheetForm();
	}

	public void menu_showCreateSheetForm() {
		menuController.showCreateSheetForm();
	}

	public void menu_saveSelectedSheet() {
		menuController.saveSelectedSheet();
	}

	public void menu_saveSelectedSheetAs(SheetView sheet) {
		menuController.showSaveSheetAsForm(sheet);
	}

	public void menu_saveAllSheets() {
		menuController.saveAllSheets();
	}

	public int menu_exit() {
		return menuController.exit();
	}

	public void menu_undo() {
		menuController.undo();
	}

	public void menu_redo() {
		menuController.redo();
	}

	public void menu_changeCanvasSize(int dxy) {
		menuController.changeCanvasSize(dxy);
	}

	public void menu_deleteSelected() {
		menuController.deleteSelected();
	}

	public void menu_showExportSheetAsImageForm(SheetView sheet) {
		menuController.showExportSheetAsImgForm(sheet);
	}

	public SheetView io_createNewSheet(String sheetName, File sheetFile, long pluginSignature) {
		return ioController.createNewSheet(sheetName, sheetFile, pluginSignature);
	}

	public void io_saveSheet(SheetModel sheet) {
		ioController.saveSheet(sheet);
	}

	public void io_saveSheetAsImage(SheetView sheet, File filename) {
		ioController.saveSheetAsImage(sheet, filename);
	}

	public void io_openSheet(String absolutePath) {
		ioController.openSheet(absolutePath);
	}

	public int mainWindow_onApplicationExit() {
		return mainWindowController.onApplicationExit();
	}

	public ActionListener nestDialog_RadioListener(Map<Glyph, Glyph> nestMap, Glyph nestable, Glyph nesting) {
		return nestingDialogController.radBtnListener(nestMap, nestable, nesting);
	}

	public ActionListener nestDialog_OkListener(Map<Glyph, Glyph> nestMap, JDialog windowToClose) {
		return nestingDialogController.okBtnListener(nestMap, windowToClose);
	}

	public ActionListener nestDialog_cancelBtnListener(JDialog windowToClose) {
		return nestingDialogController.cancelBtnListener(windowToClose);
	}

	public void updateUiFromPlugin(PluginInterface plugin) {
		((DefaultListModel<RichListItem>) MainForm.getInstance().getEntityContextJList().getModel()).clear();
		for (EntityContextTransferWraper ectw : plugin.getEntityContexts()) {
			MainForm.getInstance().getEntityContextJList().addComponent(new EntityContextListItem(ectw.getIcon(), ectw.getContext()));
		}
		System.out.println("Entity context list initialized");

		// clearing the toolbars from previous plugins
		Component[] components = MainForm.getInstance().getPluginToolbarHostPane().getComponents();
		for (Component cmp : components)
			if (!(cmp instanceof LocalToolbarType))
				MainForm.getInstance().getPluginToolbarHostPane().remove(cmp);
		// !!
		JToolBar tb;
		if ((tb = plugin.getPluginToolbar()) != null) {
			System.out.println("Plugin toolbar added");
			MainForm.getInstance().getPluginToolbarHostPane().add(tb);
		}
		else
			System.out.println("No plugin toolbar available");
	}

	public MouseAdapter getEntityListDND() {
		return listController.getEntityListDND();
	}

	public void conSeq_connectBtnListener(final EntityView source, JButton connectBtn) {
		connectSequenceController.connectBtnListener(source, connectBtn);
	}

	public void changeConnectorContext(ConnectorContextInterface context) {
		Object selectedItem = Session.getSelectedViews().get(0);
		if (selectedItem instanceof ConnectionView)
			connectorTypeChooserController.changeConnectorContext((ConnectionView) selectedItem, context);
	}

	public ComponentAdapter jstk_resEvtListener(final Joystick joystick) {
		return joystickUpdater.getUpdaterEvtListener(joystick);
	}

}
