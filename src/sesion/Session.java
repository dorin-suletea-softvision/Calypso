package sesion;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import sesion.props.ModelPropertySheet;
import ui.components.EmptyControlPann;
import ui.components.RichListItem;
import ui.controller.FacadeController;
import ui.forms.MainForm;
import bridge.exceptions.ComlianceException;
import bridge.transferable.ConnectorContextTransferWraper;
import bridge.transferable.EntityContextTransferWraper;
import bridge.transferable.PluginInterface;
import bridge.transferable.context.ConnectorContextInterface;
import bridge.transferable.context.EntityContextInterface;
import bridge.transferable.interfaces.CommandInterface;
import bridge.transferable.proxy.EntityViewProxy;
import bridge.transferable.proxy.SessionProxy;
import bridge.transferable.proxy.SheetViewProxy;
import engine.exceptions.IntersectionAlgorithmInvalidId;
import engine.geometry.IntersectionAlgorithmFactory;
import engine.geometry.IntersectionAlgorithmFamily;
import engine.views.DefaultConnectorContext;
import engine.views.SheetView;
import engine.views.interfaces.ViewDragableInterface;
import engine.views.interfaces.ViewFocusableInterface;

public class Session implements SessionProxy {
	private static IntersectionAlgorithmFamily intersectionAlgorithm;
	private static SheetView selectedSheet;
	private static List<ViewFocusableInterface> selectedViews;
	private static Set<ViewDragableInterface> selectedDragableViews;
	private static List<PluginInterface> loadedPlugins;

	static {
		selectedViews = new ArrayList<ViewFocusableInterface>();
		selectedDragableViews = new HashSet<ViewDragableInterface>();
		loadedPlugins = new ArrayList<PluginInterface>();
		try {
			intersectionAlgorithm = IntersectionAlgorithmFactory.createAlgorithm(ModelPropertySheet.getInstance().getIntersectionAlgorithmID());
		} catch (IntersectionAlgorithmInvalidId e) {
			e.printStackTrace();
		}
	}

	// delegate for udo segment
	public static void undo() {
		selectedSheet.undo();
	}

	public static void undoAndRemove() {// undo operation and delete it from
										// queue
		selectedSheet.undoAndRemove();
	}

	public static void redo() {
		selectedSheet.redo();
	}

	public static void addCommand(CommandInterface c) {
		selectedSheet.addCommand(c);
	}

	// !delegate for udo segment

	// delegate for selected views
	public static List<ViewFocusableInterface> getSelectedViews() {
		return selectedViews;
	}

	public static Set<ViewDragableInterface> getSelectedDragableViews() {
		return selectedDragableViews;
	}

	public static void deselectAll() {
		while (!selectedViews.isEmpty()) {
			selectedViews.get(0).releaseFocus();
		}
		Session.st_triggerSelectPanUpdate();
	}

	public static SheetView getSelectedSheet() {
		return selectedSheet;
	}

	@Override
	public SheetViewProxy getSelectedSheetProxy() {
		return selectedSheet;
	}

	public static IntersectionAlgorithmFamily getIntersectionAlgorithm() {
		return intersectionAlgorithm;
	}

	public static void setSelectedSheet(SheetView selectedSheet) throws ComlianceException {
		removeSelectedSheet();
		PluginInterface requiredPlugin = null;
		selectedViews.clear();
		selectedDragableViews.clear();

		for (PluginInterface p : loadedPlugins) {
			if (p.getSignature() == selectedSheet.getPluginSignature())
				requiredPlugin = p;
		}
		if (requiredPlugin == null)
			throw new ComlianceException("Sheet " + selectedSheet.getName() + " can not ne selected due to compatibility issue (the plugin required to load it is not installed");
		FacadeController.getInstance().updateUiFromPlugin(requiredPlugin);
		requiredPlugin.setSheetView(selectedSheet);
		Session.selectedSheet = selectedSheet;
		FacadeController.getInstance().editor_addSheetToEditor(selectedSheet);
		if (!Session.selectedSheet.getModel().isCommited()) {
			MainForm.getInstance().getMenu().activateSaveSheetMit();
			MainForm.getInstance().getGeneralToolbar().activateSaveBtn();
		}
		else {
			MainForm.getInstance().getMenu().deactivateSaveSheetMit();
			MainForm.getInstance().getGeneralToolbar().deactivateSaveBtn();
		}

		MainForm.getInstance().getMenu().activateSaveSheetAsMit();

		MainForm.getInstance().getMenu().activateIncreaseCanvasMit();
		MainForm.getInstance().getMenu().activateShrinkCanvasMit();

		MainForm.getInstance().getMenu().activateExportAsImageMit();
		MainForm.getInstance().getGeneralToolbar().activateExportAsImageBtn();

		if (selectedSheet.canUndo()) {
			MainForm.getInstance().getMenu().activateUndoMit();
			MainForm.getInstance().getGeneralToolbar().activateUndoBtn();
		}
		if (selectedSheet.canRedo()) {
			MainForm.getInstance().getMenu().activateRedoMit();
			MainForm.getInstance().getGeneralToolbar().activateRedoBtn();
		}

	}

	public static void removeSelectedSheet() {
		for (ViewFocusableInterface sdv : new ArrayList<ViewFocusableInterface>(selectedViews))
			sdv.releaseFocus();
		selectedSheet = null;

		MainForm.getInstance().getMenu().deactivateSaveSheetMit();
		MainForm.getInstance().getGeneralToolbar().deactivateSaveBtn();

		MainForm.getInstance().getMenu().deactivateSaveSheetAsMit();

		MainForm.getInstance().getMenu().deactivateExportAsImageMit();
		MainForm.getInstance().getGeneralToolbar().deactivateExportAsImageBtn();

		((DefaultListModel<RichListItem>) MainForm.getInstance().getEntityContextJList().getModel()).clear();
		MainForm.getInstance().updateOnSelectPan(new EmptyControlPann());
		MainForm.getInstance().getMenu().deactivateIncreaseCanvasMit();
		MainForm.getInstance().getMenu().deactivateShrinkCanvasMit();

		MainForm.getInstance().getMenu().deactivateUndoMit();
		MainForm.getInstance().getMenu().deactivateRedoMit();

		MainForm.getInstance().getGeneralToolbar().deactivateUndoBtn();
		MainForm.getInstance().getGeneralToolbar().deactivateRedoBtn();

		if (MainForm.getInstance().getSheetTabPane().getTabCount() == 0) {
			MainForm.getInstance().getGeneralToolbar().deactivateSaveAllBtn();
			MainForm.getInstance().getMenu().deactivateSaveAllSheetMit();
		}
	}

	public static void addPlugin(PluginInterface plugin) {
		loadedPlugins.add(plugin);
	}

	public static List<PluginInterface> getPlugins() {
		return loadedPlugins;
	}

	public static PluginInterface getPluginBySignature(long signature) {
		for (PluginInterface p : loadedPlugins)
			if (p.getSignature() == signature)
				return p;
		throw new RuntimeException("Plugin not Found");
	}

	@Override
	public List<EntityViewProxy> getSelectedEntities() {
		List<EntityViewProxy> ret = new ArrayList<EntityViewProxy>();
		for (ViewFocusableInterface vs : selectedViews)
			if (vs instanceof EntityViewProxy)
				ret.add((EntityViewProxy) vs);
		return ret;
	}

	public static EntityContextInterface getEntityContextByType(String type) throws ComlianceException {
		for (PluginInterface p : loadedPlugins)
			for (EntityContextTransferWraper ectw : p.getEntityContexts())
				if (ectw.getContext().getDrawnType().equals(type))
					return ectw.getContext();
		throw new ComlianceException("Context of the entity nor found in the plugin list");
	}

	public static ConnectorContextInterface getConnectorContextByType(String type) throws ComlianceException {
		for (PluginInterface p : loadedPlugins)
			for (ConnectorContextTransferWraper ectw : p.getConnectorContexts())
				if (ectw.getContext().getDrawnType().equals(type))
					return ectw.getContext();
		// maybe is default
		DefaultConnectorContext ret = new DefaultConnectorContext();
		if (ret.getDrawnType().equals(type))
			return ret;
		throw new ComlianceException("Context of the connector nor found in the plugin list");
	}

	public static void st_triggerSelectPanUpdate() {
		if (Session.getSelectedViews().size() == 1)
			MainForm.getInstance().updateOnSelectPan(Session.getSelectedViews().get(0).generateControlPan());
		else
			MainForm.getInstance().updateOnSelectPan(Session.getSelectedSheet().generateControlPan());
	}

	@Override
	public void triggerSelectPanUpdate() {
		Session.st_triggerSelectPanUpdate();
	}

	public static void st_postErrorMessage(String message) {
		JOptionPane.showMessageDialog(MainForm.getInstance(), message);
	}

	@Override
	public void postErrorMessage(String message) {
		JOptionPane.showMessageDialog(MainForm.getInstance(), message);
	}

	public static void st_repaintSelectedSheet() {
		selectedSheet.repaint(Session.getSelectedSheet().getVisibleRect());
	}

	public void repaintSelectedSheet() {
		Session.st_repaintSelectedSheet();
	}

}
