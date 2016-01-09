package engine.views;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import sesion.Session;
import sesion.UndoSegment;
import ui.components.EmptyControlPann;
import ui.components.MultilineLabel;
import ui.components.SheetGlassPane;
import ui.controller.FacadeController;
import ui.forms.MainForm;
import bridge.exceptions.ComlianceException;
import bridge.transferable.interfaces.AbstractControlJPanel;
import bridge.transferable.interfaces.CommandInterface;
import bridge.transferable.interfaces.ConnectorModelInterface;
import bridge.transferable.interfaces.EntityModelInterface;
import bridge.transferable.proxy.SheetViewProxy;
import engine.command.SnapCommand;
import engine.model.SheetModel;
import engine.views.interfaces.ViewDragableInterface;

public class SheetView extends Glyph implements SheetViewProxy {
	private static final long serialVersionUID = 1L;
	// moved here from session so each view has it's own undo segment and the
	// plugin can register
	// custom comands into the undo segment
	private UndoSegment undoSegment;

	public ArrayList<ConnectionView> connections;
	private ArrayList<SnapView> snaps;
	private SheetGlassPane glassPane;

	private Rectangle selectionRectangle;
	private Point selectionRectangleStartPt;

	private SheetModel model;

	/**
	 * Constructor used for deserialization of the sheet view
	 * 
	 * @param model
	 *            the deserialized model from file passed by the
	 *            ResourceRetriever
	 */
	public SheetView(SheetModel model) {
		super();
		this.undoSegment = new UndoSegment();
		this.model = model;
		model.getPluginSignature();
		this.connections = new ArrayList<ConnectionView>();
		this.snaps = new ArrayList<SnapView>();
		this.glassPane = new SheetGlassPane();
		this.setName(model.getSheetName());
		setLayout(null);
		selectionRectangle = new Rectangle();
		selectionRectangleStartPt = new Point();
		setPreferredSize(model.getSheetSize());
		addListeners();
		update();

	}

	/**
	 * Pair method of the SheetView deserialize constructor While the
	 * constructor sets up the sheet , the actual initialization of the entities
	 * and connectors from it must occur after the Seet is set as selected sheet
	 * in Session , <b>use only after Session.setSelectedSheet(SheetView shv)<b>
	 * 
	 * @throws ComlianceException
	 *             occurs in case of the incompatibility between the drawn types
	 *             and the plugin types, is a second check that might occur if
	 *             the project is an old plugin plugin version and the the app
	 *             is runing on a new plugin version (this 2 plugins have the
	 *             same signature but the newer version doesnt have a context
	 *             type that the first version had) <b>\nOpen point<b> this
	 *             method's code is more fit in the update() , but due to the
	 *             high computation of the code and high callability pf the
	 *             SheetView.this.update() it was left here , initComponents
	 *             should be called only once when deserializing the sheet
	 */
	@SuppressWarnings("unchecked")
	public void initComponents() throws ComlianceException {

		// buggy code left for teaching proposes
		// the add(EntityView view) method in SheetView adds the EntityView's
		// model to the model
		// of this SheetView , and this is the cause of a concurency exception
		// for the same array list we get an element in this loop and add one
		// element to the list
		// is add(EntityView ev) :
		// for (EntityModelInterface em: model.getTopLevelEnities())
		// add(new EntityView(em));

		// adding entity's at view level\
		ArrayList<EntityModelInterface> auxModels = (ArrayList<EntityModelInterface>) model.getTopLevelEnities().clone();
		model.getTopLevelEnities().clear();
		for (EntityModelInterface em : auxModels) {
			EntityView ev = new EntityView(em);
			add(ev);
		}

		ArrayList<ConnectorModelInterface> auxConModels = (ArrayList<ConnectorModelInterface>) model.getConnectors().clone();
		model.getConnectors().clear();
		for (ConnectorModelInterface cm : auxConModels) {
			ConnectionView cv = new LabeledConnectionView(cm, this);
			add(cv);
		}
		// !!

	}

	public SheetView(long pluginSignature) {
		super();
		this.undoSegment = new UndoSegment();
		this.model = new SheetModel();
		this.model.setPluginSignature(pluginSignature);
		this.connections = new ArrayList<ConnectionView>();
		this.snaps = new ArrayList<SnapView>();
		this.glassPane = new SheetGlassPane();
		setName("Default_Sheet_Name");
		setLayout(null);
		setPreferredSize(model.getSheetSize());
		selectionRectangle = new Rectangle();
		selectionRectangleStartPt = new Point();
		addListeners();
	}

	public long getPluginSignature() {
		return model.getPluginSignature();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		glassPane.paintComponent(g);
		for (ConnectionView c : connections) {
			c.paintComponent(g);
		}
	}

	private void addListeners() {
		MouseAdapter mouseListener = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				getFocus();
				if (e.getButton() == MouseEvent.BUTTON1) {
					selectionRectangleStartPt = e.getPoint();
					selectionRectangle = new Rectangle();

					if (!e.isShiftDown()) {
						Session.deselectAll();
						Session.getSelectedDragableViews().clear();
					}
					for (ConnectionView c : connections)
						if (c.isSelected(e)) {
							if (e.isShiftDown() && c.hasFocus())
								c.releaseFocus();
							else
								c.getFocus();
						}
				}
				if (e.getButton() == MouseEvent.BUTTON3) {
					for (ConnectionView c : connections)
						if (c.isSelected(e)) {
							CommandInterface command = new SnapCommand(e.getPoint(), c);
							if (command.execute())
								Session.addCommand(command);
						}
				}
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				if (selectionRectangle != null) {
					generateSelectionRectangle(e);
					glassPane.setMultipleSelectionRectangle(selectionRectangle);
					repaint();
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (selectionRectangle == null)
					return;
				selectViews();
				selectionRectangle = null;
				glassPane.setMultipleSelectionRectangle(null);

			}
		};
		ComponentListener resizeListener = new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				setPreferredSize(getSize());

			}

		};

		addComponentListener(resizeListener);
		addMouseListener(mouseListener);
		addMouseMotionListener(mouseListener);
	}

	private void generateSelectionRectangle(MouseEvent e) {
		if (e.getX() >= selectionRectangleStartPt.x) {
			selectionRectangle.width = e.getX() - selectionRectangleStartPt.x;
			selectionRectangle.x = selectionRectangleStartPt.x;
		}
		else {
			selectionRectangle.x = e.getX();
			selectionRectangle.width = selectionRectangleStartPt.x - e.getX();
		}
		if (e.getY() >= selectionRectangleStartPt.y) {
			selectionRectangle.height = e.getY() - selectionRectangleStartPt.y;
			selectionRectangle.y = selectionRectangleStartPt.y;
		}
		else {
			selectionRectangle.y = e.getY();
			selectionRectangle.height = selectionRectangleStartPt.y - e.getY();
		}
	}

	private void selectViews() {
		for (Glyph v : super.nestedGlyphs)
			if (selectionRectangle.contains(v.getBounds()) || selectionRectangle.intersects(v.getBounds()))
				v.getFocus();
		for (ConnectionView c : connections) {
			for (int i = 0; i < c.getSnapCount(); i++)
				if (selectionRectangle.contains(c.getSnap(i).getLocation()) || selectionRectangle.contains(c.getStartPoint()) || selectionRectangle.contains(c.getEndPoint())) {
					c.getFocus();
					break;
				}
		}
		Session.st_triggerSelectPanUpdate();
		repaint();
	}

	// generated

	@Override
	public void update() {
		for (ConnectionView c : connections)
			c.update();
	}

	public Component add(EntityView entity) {
		super.nestedGlyphs.add(entity);
		model.add(entity.getModel());
		return super.add(entity);
	}

	public Component add(SnapView snap) {
		snaps.add(snap);
		return super.add(snap);
	}

	public void add(ConnectionView connection) {
		model.add(connection.getModel());
		connections.add(connection);
	}

	public void remove(ConnectionView connection) {
		model.getConnectors().remove(connection.getModel());
		connections.remove(connection);
	}

	public void remove(EntityView entity) {
		super.nestedGlyphs.remove(entity);
		model.remove(entity.getModel());
		super.remove(entity);
	}

	public ArrayList<SnapView> getSnapsOf(ConnectionView connector) {
		ArrayList<SnapView> ret = new ArrayList<SnapView>();
		for (SnapView s : snaps)
			if (s.onConnector().equals(connector))
				ret.add(s);
		return ret;
	}

	public SheetModel getModel() {
		return model;
	}

	public SheetGlassPane getGlassPane() {
		return glassPane;
	}

	public ArrayList<ConnectionView> getOutgoingConnectors(ViewDragableInterface entity) {
		ArrayList<ConnectionView> ret = new ArrayList<ConnectionView>();
		for (ConnectionView c : connections)
			if (c.getOriginEntity().equals(entity))
				ret.add(c);
		return ret;
	}

	public ArrayList<ConnectionView> getIncomingConnectors(ViewDragableInterface entity) {
		ArrayList<ConnectionView> ret = new ArrayList<ConnectionView>();
		for (ConnectionView c : connections)
			if (c.getEndEntity().equals(entity))
				ret.add(c);
		return ret;
	}

	public void updateConnectorsOf(ViewDragableInterface entity) {
		ArrayList<ConnectionView> allConnectors = getOutgoingConnectors(entity);
		allConnectors.addAll(getIncomingConnectors(entity));
		for (ConnectionView c : allConnectors) {
			c.update();
		}
	}

	public ArrayList<ConnectionView> getConnectorsOf(ViewDragableInterface entity) {
		ArrayList<ConnectionView> allConnectors = getOutgoingConnectors(entity);
		allConnectors.addAll(getIncomingConnectors(entity));
		return allConnectors;
	}

	// !generated

	@Override
	public void nest(Glyph nestedGlyph) {
		((Glyph) nestedGlyph.getParent()).deNest(nestedGlyph);
		super.nestedGlyphs.add((EntityView) nestedGlyph);
		super.add(nestedGlyph);
		nestedGlyph.setResizable(true);
		nestedGlyph.setMovable(true);

	}

	@Override
	public void deNest(Glyph nestedGlyph) {
		super.nestedGlyphs.remove(nestedGlyph);
		this.remove(nestedGlyph);
		nestedGlyph.setResizable(true);
	}

	public Glyph getGlyphUnderMouse(Point globalMouseLocation) {
		Rectangle globalBounds = new Rectangle();

		for (Glyph e : super.getAllGlyphs()) {
			globalBounds = new Rectangle(e.getLocationOnScreen().x, e.getLocationOnScreen().y, e.getWidth(), e.getHeight());
			if (globalBounds.contains(globalMouseLocation))
				return e;
		}
		return this;
	}

	@Override
	public void getFocus() {
		MainForm.getInstance().createEntPaneToFront();
	}

	@Override
	public void releaseFocus() {
		System.out.println("Dummy releaseFocus from SheetView");

	}

	@Override
	public ArrayList<ViewDragableInterface> getDragableItems() {
		System.out.println("Dummy getFocus from SheetView");
		return new ArrayList<ViewDragableInterface>();
	}

	@Override
	public boolean validNewPosition(int dx, int dy) {
		System.out.println("Dummy valid new pos on SheetView");
		return false;
	}

	// can be optimized
	public boolean componentsOverlap(MultilineLabel label) {
		for (Component c : getComponents()) {
			if (c instanceof MultilineLabel && c != label && label.overlapes((MultilineLabel) c))
				return true;
			if (c instanceof EntityView && c != label && label.overlapes((EntityView) c))
				return true;
		}
		return false;
	}

	@Override
	public AbstractControlJPanel generateControlPan() {
		return new EmptyControlPann();
	}

	// delegate for udo segment
	public void undo() {
		if (undoSegment.getCursorIndex() == 0) {
			MainForm.getInstance().getMenu().deactivateUndoMit();
			MainForm.getInstance().getGeneralToolbar().deactivateUndoBtn();
		}
		MainForm.getInstance().getMenu().activateRedoMit();
		MainForm.getInstance().getGeneralToolbar().activateRedoBtn();

		if (!MainForm.getInstance().getSheetTabPane().allSheetsSaved()) {
			MainForm.getInstance().getGeneralToolbar().activateSaveAllBtn();
			MainForm.getInstance().getMenu().activateSaveAllSheetMit();
		}

		getModel().setCommited(false);
		MainForm.getInstance().getMenu().activateSaveSheetMit();
		MainForm.getInstance().getGeneralToolbar().activateSaveBtn();
		FacadeController.getInstance().editor_prefixStarSheetTabName(this);
		undoSegment.undo();
	}

	public void undoAndRemove() {// undo operation and delete it for queue
		undoSegment.undoAndRemove();
	}

	public boolean canUndo() {
		return undoSegment.canUndo();
	}

	public boolean canRedo() {
		return undoSegment.canRedo();
	}

	public void redo() {
		if (undoSegment.getCursorIndex() == undoSegment.getSegmentLength() - 2) {
			MainForm.getInstance().getMenu().deactivateRedoMit();
			MainForm.getInstance().getGeneralToolbar().deactivateRedoBtn();
		}

		MainForm.getInstance().getMenu().activateUndoMit();
		MainForm.getInstance().getGeneralToolbar().activateUndoBtn();

		getModel().setCommited(false);
		MainForm.getInstance().getMenu().activateSaveSheetMit();
		MainForm.getInstance().getGeneralToolbar().activateSaveBtn();

		if (!MainForm.getInstance().getSheetTabPane().allSheetsSaved()) {
			MainForm.getInstance().getGeneralToolbar().activateSaveAllBtn();
			MainForm.getInstance().getMenu().activateSaveAllSheetMit();
		}

		FacadeController.getInstance().editor_prefixStarSheetTabName(this);
		undoSegment.redo();
	}

	public void addCommand(CommandInterface c) {
		getModel().setCommited(false);
		MainForm.getInstance().getMenu().activateUndoMit();

		MainForm.getInstance().getMenu().activateSaveSheetMit();

		MainForm.getInstance().getGeneralToolbar().activateUndoBtn();
		MainForm.getInstance().getGeneralToolbar().activateSaveBtn();

		if (!MainForm.getInstance().getSheetTabPane().allSheetsSaved()) {
			MainForm.getInstance().getGeneralToolbar().activateSaveAllBtn();
			MainForm.getInstance().getMenu().activateSaveAllSheetMit();
		}

		FacadeController.getInstance().editor_prefixStarSheetTabName(this);

		undoSegment.addCommand(c);
	}

	public void setName(String name) {
		model.setSheetName(name);
	}

	public String getName() {
		return model.getSheetName();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof SheetView))
			return false;
		return ((SheetView) obj).getModel().equals(model);
	}

	@Override
	public int hashCode() {
		return model.hashCode();
	}

	@Override
	public void setPreferredSize(Dimension preferredSize) {
		model.setSheetSize(preferredSize);
		super.setPreferredSize(preferredSize);
	}

	/**
	 * Returns the connector sibling from this sheet view In case of connector
	 * related commands , in the sequence: create connector->do operation->undo
	 * operation->undo create->redo create connector->redo operation the inner
	 * ConnectionView from the command will be different from the one added in
	 * the first operation create connection(1) and redo create connector(2)
	 * will point to different connectors but with the same contents
	 * 
	 * @return the ConnectorView reference of the logical equal(with the
	 *         ConnectorView passed as argument) connector from
	 *         SeetView.connectors
	 */
	public ConnectionView getEqualConnector(ConnectionView connector) {
		return connections.get(connections.indexOf(connector));
	}

	public EntityView getEntityViewByModel(EntityModelInterface entityModel) throws ComlianceException {
		for (Glyph g : getAllGlyphs())
			if (g instanceof EntityView && ((EntityView) g).getModel().equals(entityModel))
				return (EntityView) g;
		throw new ComlianceException("The connector origin or end is not defined");
	}

	@Override
	public void delete() {
		System.out.println("Dummy delete on sheet");
	}

	@Override
	public void restore(Glyph parrent) {
		System.out.println("Dummy restore on sheet");
	}

	@Override
	public Glyph getParrent() {
		System.out.println("Dummy getParrent on SheetView");
		return null;
	}
}
