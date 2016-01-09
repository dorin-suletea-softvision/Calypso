package engine.views;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import sesion.Session;
import ui.components.Joystick;
import ui.components.ResizableBorder;
import ui.controller.FacadeController;
import ui.forms.MainForm;
import bridge.exceptions.ComlianceException;
import bridge.transferable.context.EntityContextInterface;
import bridge.transferable.interfaces.AbstractControlJPanel;
import bridge.transferable.interfaces.EntityModelInterface;
import bridge.transferable.interfaces.GlyphLocationInterface;
import bridge.transferable.proxy.EntityViewProxy;
import engine.model.EntityModel;
import engine.views.interfaces.ViewDragableInterface;
import engine.views.interfaces.ViewFocusableInterface;

public class EntityView extends Glyph implements  ViewFocusableInterface, EntityViewProxy {
	private static final long serialVersionUID = 1L;
	/**
	 * this is an auxiliary storage for connectors linked to this entity, used
	 * in case of delete->restore operations, is inited on delete and cleared on
	 * restore for mem usage reasons
	 */
	private List<ConnectionView> linkedConnectors;

	private EntityContextInterface context;
	private EntityModelInterface model;
	private Joystick joystick;
	private AbstractControlJPanel controlPannel;

	/**
	 * Constructor used for deserialization , which get's the deserialized model
	 * and draws the entity accordingly
	 * 
	 * @param model
	 *            = the deserialized model from file passed by the
	 *            ResourceRetriever
	 * @throws ComlianceException
	 *             thrown if the required context type is not found in the
	 *             loaded plugin list
	 */
	public EntityView(EntityModelInterface model) throws ComlianceException {
		Point location = model.getLocation();
		this.model = model;
		setLayout(null);
		this.setSize(model.getSize());
		this.setLocation(location);
		this.context = Session.getEntityContextByType(model.getType());
		this.setBorder(new ResizableBorder(this));
		this.linkedConnectors = new ArrayList<ConnectionView>();
		createJoystick();
		addListeners();
		update();
	}

	public EntityView(EntityContextInterface context) {
		super();
		setLayout(null);
		this.context = context;
		this.model = new EntityModel(context.getDrawnType(), context.getDataRowIdentifiers());
		this.setSize(context.getDefaultSize());
		this.setBorder(new ResizableBorder(this));
		this.setMinimumSize(context.getMinimumSize(model, getFontMetrics(getFont())));
		this.linkedConnectors = new ArrayList<ConnectionView>();
		createJoystick();
		addListeners();
		update();
	}

	public EntityView(EntityContextInterface context, Point location) {
		this(context);
		this.setLocation(location);

	}

	private void createJoystick() {
		this.joystick = new Joystick(this);
		this.add(joystick);
		this.joystick.setVisible(false);
		this.addComponentListener(FacadeController.getInstance().jstk_resEvtListener(joystick));
	}

	private void addListeners() {
		MouseListener listener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					if (e.isShiftDown()) {
						if (!EntityView.this.hasFocus())
							getFocus();
						else
							releaseFocus();
					}
					else {
						Session.deselectAll();
						Session.getSelectedDragableViews().clear();
						getFocus();
						MainForm.getInstance().updateOnSelectPan(generateControlPan());
					}

				}
			}
		};
		addMouseListener(listener);
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		context.drawShape(g, model.getSize().width, model.getSize().height);
		context.drawIdentifiers(g, model.getSize().width, model.getSize().height, model.getName(), model.getType());
		context.drawData(g, model.getSize().width, model.getSize().height, model.getDataRows());
	}

	@Override
	public void update() {
		this.setMinimumSize(context.getMinimumSize(model, getFontMetrics(getFont())));

		// enforcing the size of the component to be biger than min size
		if (getMinimumSize().width > getWidth())
			setSize(getMinimumSize().width, getHeight());
		if (getMinimumSize().height > getHeight())
			setSize(getWidth(), getMinimumSize().height);
		// !!

		for (Glyph g : nestedGlyphs) {
			g.update();
		}

		List<ConnectionView> connections = new ArrayList<ConnectionView>();
		if (Session.getSelectedSheet() != null) {
			connections.addAll(Session.getSelectedSheet().getOutgoingConnectors(this));
			connections.addAll(Session.getSelectedSheet().getIncomingConnectors(this));
		}
		for (ConnectionView c : connections) {
			c.update();
		}
	}

	// generated
	public void setLocation(Point location) {
		super.setLocation(location);
		model.setLocation(location);
	}

	public void setSize(Dimension size) {
		super.setSize(size);
		model.setSize(size);
	}

	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		model.setLocation(new Point(x, y));
		model.setSize(new Dimension(width, height));
	}

	public EntityModelInterface getModel() {
		return model;
	}

	public void setModel(EntityModelInterface model) {
		this.model = model;
	}

	@Override
	public void nest(Glyph nestedGlyph) {
		((Glyph) nestedGlyph.getParent()).deNest(nestedGlyph);
		super.nestedGlyphs.add(nestedGlyph);
		// casting the list
		List<GlyphLocationInterface> nest = new ArrayList<GlyphLocationInterface>();
		for (Glyph g : nestedGlyphs)
			nest.add((GlyphLocationInterface) g);
		// casting the list
		context.setNestedPositions(nest, this);
		this.add(nestedGlyph);
		nestedGlyph.setResizable(false);
		nestedGlyph.setMovable(false);
		nestedGlyph.setNested(true);
	}

	@Override
	public void deNest(Glyph nestedGlyph) {
		super.nestedGlyphs.remove(nestedGlyph);
		this.remove(nestedGlyph);
		nestedGlyph.setResizable(true);
		nestedGlyph.setMovable(true);
		nestedGlyph.setNested(false);

	}

	// !generated

	@Override
	public void getFocus() {
		MainForm.getInstance().getMenu().activateDeleteMit();
		MainForm.getInstance().getGeneralToolbar().activateDelBtn();
		MainForm.getInstance().onSelectPaneToFront();

		joystick.setVisible(true);
		Session.getSelectedViews().add(this);
		Session.getSelectedDragableViews().addAll(getDragableItems());
		super.setFocus(true);
		MouseEvent me = new MouseEvent(this, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, 10, 10, 0, false);
		this.dispatchEvent(me);
		Session.getSelectedSheet().repaint(Session.getSelectedSheet().getVisibleRect());
		Session.st_triggerSelectPanUpdate();
	}

	@Override
	public void releaseFocus() {
		joystick.setVisible(false);
		Session.getSelectedViews().remove(this);

		if (Session.getSelectedViews().isEmpty()) {
			MainForm.getInstance().getMenu().deactivateDeleteMit();
			MainForm.getInstance().getGeneralToolbar().deactivateDelBtn();
		}

		Session.getSelectedDragableViews().removeAll(getDragableItems());
		super.setFocus(false);

		MouseEvent me = new MouseEvent(this, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, 10, 10, 0, false);
		this.dispatchEvent(me);

		Session.getSelectedSheet().repaint(Session.getSelectedSheet().getVisibleRect());
		Session.st_triggerSelectPanUpdate();
	}

	public boolean hasFocus() {
		return super.hasFocus();
	}

	@Override
	public ArrayList<ViewDragableInterface> getDragableItems() {
		ArrayList<ViewDragableInterface> ret = new ArrayList<ViewDragableInterface>();
		ret.add(this);
		return ret;
	}

	@Override
	public boolean validNewPosition(int dx, int dy) {
		int threashhol=10;
		if (getX() + dx < threashhol || getY() + dy < threashhol || getX()+dx+getWidth()>getParent().getWidth()-threashhol || getY()+dy+getHeight()>getParent().getHeight()-threashhol)
			return false;
		return true;
	}

	@Override
	public AbstractControlJPanel generateControlPan() {
		if (controlPannel==null)
			controlPannel = context.generateControlPannel(this);
		else
			controlPannel.initComponents();
		return controlPannel;
	}

	@Override
	public String toString() {
		return model.getName();
	}

	public void delete() {
		this.releaseFocus();
		linkedConnectors = Session.getSelectedSheet().getConnectorsOf(this);

		for (ConnectionView cv : linkedConnectors)
			cv.delete();

		if (this.getParent() != null) {
			if (this.getParent() instanceof SheetView)
				((SheetView) this.getParent()).remove(this);
		}
	}

	@Override
	public void restore(Glyph parent) {
		for (ConnectionView cv : linkedConnectors)
			cv.restore(parent);
		if (parent instanceof SheetView)
			((SheetView) parent).add(this);
		// interesting fact , sheetView is a a glyph that's a jcomponent, it has
		// ovverided add for entities
		// but if not casting it to sheet view it calls the method from
		// jcomponent not for sheet view
		// since it has to virtualize method call for parrent to sheet view ,
		// and autobox EntityView.this
		// parent.add(this)- vs -((SheetView)parent).add(this);
	}

	@Override
	public Glyph getParrent() {
		return (Glyph) super.getParent();
	}
}
