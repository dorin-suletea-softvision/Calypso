package engine.views;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.SwingUtilities;

import sesion.Session;
import sesion.props.ModelPropertySheet;
import ui.forms.MainForm;
import bridge.exceptions.ComlianceException;
import bridge.transferable.context.ConnectorContextInterface;
import bridge.transferable.interfaces.AbstractControlJPanel;
import bridge.transferable.interfaces.ConnectorModelInterface;
import engine.model.ConnectorModel;
import engine.views.interfaces.ViewDragableInterface;
import engine.views.interfaces.ViewFocusableInterface;

public abstract class ConnectionView implements ViewFocusableInterface {
	private boolean hasFocus;
	private boolean hideArrows;

	private EntityView origin;
	private EntityView end;

	private Point startPoint;
	private Point endPoint;

	private ArrayList<Point> startArrwoPoints;
	public ArrayList<Point> endArrwoPoints;

	private ArrayList<SnapView> snaps;
	private ArrayList<Polygon> selectableAreaPolygons;

	private ConnectorModelInterface model;
	private ConnectorContextInterface context;
	
	@SuppressWarnings("unchecked")
	public ConnectionView(ConnectorModelInterface model, SheetView hostSheet) throws ComlianceException {
		this.hideArrows = false;
		this.hasFocus = false;
		this.context = Session.getConnectorContextByType(model.getDrawnType());
		this.origin = hostSheet.getEntityViewByModel(model.getFrom());
		this.end = hostSheet.getEntityViewByModel(model.getTo());
		this.startArrwoPoints = new ArrayList<Point>();
		this.endArrwoPoints = new ArrayList<Point>();
		this.snaps = new ArrayList<SnapView>();
		this.selectableAreaPolygons = new ArrayList<Polygon>();
		this.model = model;

		List<Point> points = new ArrayList<Point>();
		points.addAll((Collection<? extends Point>) model.getSnaps().clone());
		model.getSnaps().clear();
		for (Point p : points)
			addSnap(p);

		update();

	}

	public ConnectionView(EntityView origin, EntityView end) {
		this.hideArrows = false;
		this.origin = origin;
		this.end = end;
		this.startArrwoPoints = new ArrayList<Point>();
		this.endArrwoPoints = new ArrayList<Point>();
		this.snaps = new ArrayList<SnapView>();
		this.startPoint = new Point();
		this.endPoint = new Point();
		this.selectableAreaPolygons = new ArrayList<Polygon>();
		this.model = new ConnectorModel(origin.getModel(), end.getModel());
		context = new DefaultConnectorContext();
		this.model.setDrawnType(context.getDrawnType());
		update();
		generateSnaps();
	}

	public ConnectionView(EntityView origin, EntityView end, ConnectorContextInterface context) {
		this.hideArrows = false;
		this.origin = origin;
		this.end = end;
		this.startPoint=new Point();
		this.endPoint=new Point();
		this.startArrwoPoints = new ArrayList<Point>();
		this.endArrwoPoints = new ArrayList<Point>();
		this.model = new ConnectorModel(origin.getModel(), end.getModel());
		this.selectableAreaPolygons = new ArrayList<Polygon>();
		context = new DefaultConnectorContext();
		update();
		generateSnaps();
	}

	public void generateSnaps() {
		if (origin.equals(end)) {
			Point ptLeftMid = new Point(origin.getX() - ModelPropertySheet.getInstance().getConnectorArrowPtsDistance() * 2 - 1, startPoint.y - 15);
			Point ptLeftBot = new Point(ptLeftMid.x, origin.getY() + origin.getHeight() + ModelPropertySheet.getInstance().getConnectorArrowPtsDistance() * 2 + 1);
			Point ptMidBot = new Point(startPoint.x - 18, ptLeftBot.y);// TODO
																		// wtf
																		// hapends
																		// here
			addSnap(ptLeftMid);
			addSnap(ptLeftBot);
			addSnap(ptMidBot);

		}
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		Color c = g.getColor();
		Stroke s = ((Graphics2D) g).getStroke();
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (hideArrows) {
			g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 9 }, 0));
		}
		if (hasFocus)
			g.setColor(ModelPropertySheet.getInstance().getSelectionColor());
		
		context.drawLine(g, snapLocations(), startPoint, endPoint);
		
		((Graphics2D) g).setStroke(s);
		
		context.drawEndArrow(g, endArrwoPoints);
		context.drawStartArrow(g, startArrwoPoints);
		
		g.setColor(c);
	
	}

	public void updateArrowPoints() {
		startArrwoPoints = Session.getIntersectionAlgorithm().createArrowPoints(this, ModelPropertySheet.getInstance().getConnectorArrowPtsDistance(), true);
		endArrwoPoints = Session.getIntersectionAlgorithm().createArrowPoints(this, ModelPropertySheet.getInstance().getConnectorArrowPtsDistance(), false);
		checkArrowPts();
	}

	private void checkArrowPts() {
		Rectangle startCheckRc = Session.getIntersectionAlgorithm().rectInflator(origin.getBounds(), ModelPropertySheet.getInstance().getConnectorArrowPtsDistance() * 2);
		Rectangle endCheckRc = Session.getIntersectionAlgorithm().rectInflator(end.getBounds(), ModelPropertySheet.getInstance().getConnectorArrowPtsDistance() * 2);

		hideArrows = false;

		if (getSnapCount() > 0) {
			if (startCheckRc.contains(this.firstSnapLocation())) {
				startArrwoPoints.clear();
				for (int i = 0; i < 9; i++)
					startArrwoPoints.add(startPoint);
				hideArrows = true;
			}
			if (endCheckRc.contains(this.lastSnapLocation())) {
				endArrwoPoints.clear();
				for (int i = 0; i < 9; i++)
					endArrwoPoints.add(endPoint);
				hideArrows = true;
			}
		}
		else if (startCheckRc.intersects(endCheckRc)) {
			startArrwoPoints.clear();
			endArrwoPoints.clear();
			for (int i = 0; i < 9; i++) {
				startArrwoPoints.add(startPoint);
				endArrwoPoints.add(endPoint);
			}
			hideArrows = true;
		}
	}

	public void updateStartPoint() {
		Line2D line;
		Point startMid = Session.getIntersectionAlgorithm().rectangleMid(origin.getBounds());
		Point endMid = Session.getIntersectionAlgorithm().rectangleMid(end.getBounds());

		// convert data to sheet coordonates (needed for nested entities)
		startMid = SwingUtilities.convertPoint(origin.getParent(), startMid.x, startMid.y, Session.getSelectedSheet());
		endMid = SwingUtilities.convertPoint(end.getParent(), endMid.x, endMid.y, Session.getSelectedSheet());
		Rectangle originBounds = SwingUtilities.convertRectangle(origin.getParent(), origin.getBounds(), Session.getSelectedSheet());

		if (snaps.size() == 0)
			line = new Line2D.Float(startMid, endMid);
		else
			line = new Line2D.Float(startMid, snaps.get(0).getLocation());

		Point2D point2D = Session.getIntersectionAlgorithm().getIntersectionPoint(line, originBounds);
		setStartPoint(new Point((int) point2D.getX(), (int) point2D.getY()));

	}

	public void updateEndPoint() {
		Line2D line;
		Point startMid = Session.getIntersectionAlgorithm().rectangleMid(origin.getBounds());
		Point endMid = Session.getIntersectionAlgorithm().rectangleMid(end.getBounds());

		startMid = SwingUtilities.convertPoint(origin.getParent(), startMid.x, startMid.y, Session.getSelectedSheet());
		endMid = SwingUtilities.convertPoint(end.getParent(), endMid.x, endMid.y, Session.getSelectedSheet());
		Rectangle endBounds = SwingUtilities.convertRectangle(end.getParent(), end.getBounds(), Session.getSelectedSheet());

		if (snaps.size() == 0)
			line = new Line2D.Float(endMid, startMid);
		else
			line = new Line2D.Float(endMid, snaps.get(snaps.size() - 1).getLocation());

		Point2D point2D = Session.getIntersectionAlgorithm().getIntersectionPoint(line, endBounds);
		setEndPoint(new Point((int) point2D.getX(), (int) point2D.getY()));
	}

	public void updateSelectionArea() {
		selectableAreaPolygons.clear();
		if (snaps.size() == 0)
			selectableAreaPolygons.add(Session.getIntersectionAlgorithm()
					.generatePoligon(new Line2D.Float(getStartPoint(), getEndPoint()), ModelPropertySheet.getInstance().getConectorSelectionArea()));
		else {
			selectableAreaPolygons.add(Session.getIntersectionAlgorithm().generatePoligon(new Line2D.Float(getStartPoint(), snaps.get(0).getLocation()),
					ModelPropertySheet.getInstance().getConectorSelectionArea()));

			for (int i = 0; i < snaps.size() - 1; i++)
				selectableAreaPolygons.add(Session.getIntersectionAlgorithm().generatePoligon(new Line2D.Float(snaps.get(i).getLocation(), snaps.get(i + 1).getLocation()),
						ModelPropertySheet.getInstance().getConectorSelectionArea()));

			selectableAreaPolygons.add(Session.getIntersectionAlgorithm().generatePoligon(new Line2D.Float(snaps.get(snaps.size() - 1).getLocation(), getEndPoint()),
					ModelPropertySheet.getInstance().getConectorSelectionArea()));
		}
	}

	public int snapIndex(Point p) {
		int ret = -1;
		for (Polygon po : selectableAreaPolygons) {
			ret++;
			if (po.contains(p))
				return ret;
		}
		return ret;
	}

	private ArrayList<Point> snapLocations() {
		ArrayList<Point> points = new ArrayList<Point>();
		for (SnapView s : snaps)
			points.add(Session.getIntersectionAlgorithm().rectangleMid(s.getBounds()));
		return points;
	}

	public Point firstSnapLocation() {
		if (getSnapCount() == 0)
			return getEndPoint();
		else
			return Session.getIntersectionAlgorithm().rectangleMid(getSnap(0).getBounds());// .getLocation();
	}

	public Point lastSnapLocation() {
		if (getSnapCount() == 0)
			return getStartPoint();
		else
			return Session.getIntersectionAlgorithm().rectangleMid(getSnap(getSnapCount() - 1).getBounds());
	}

	public SnapView addSnap(Point location) {
		SnapView snapView = new SnapView(new Point(location.x - (SnapView.size.width - SnapView.paintAdjuster) / 2, location.y - (SnapView.size.height - SnapView.paintAdjuster) / 2), this);
		int index = 0;
		for (int i = 0; i < selectableAreaPolygons.size(); i++) {
			if (selectableAreaPolygons.get(i).contains(location))
				index = i;
		}
		this.snaps.add(index, snapView);
		this.model.addSnap(index, snapView.getLocation());

		if (hasFocus)
			Session.getSelectedDragableViews().add(snapView);

		Session.getSelectedSheet().add(snapView);
		Session.getSelectedSheet().repaint(Session.getSelectedSheet().getVisibleRect());
		update();
		updateSelectionArea();
		return snapView;
	}

	public SnapView addSnap(Point location, int index) {
		SnapView snapView = new SnapView(new Point(location.x - (SnapView.size.width - SnapView.paintAdjuster) / 2, location.y - (SnapView.size.height - SnapView.paintAdjuster) / 2), this);
		this.snaps.add(index, snapView);
		this.model.addSnap(index, snapView.getLocation());

		if (hasFocus)
			Session.getSelectedDragableViews().add(snapView);

		Session.getSelectedSheet().add(snapView);
		Session.getSelectedSheet().repaint(Session.getSelectedSheet().getVisibleRect());
		update();
		updateSelectionArea();
		return snapView;

	}

	public void removeSnap(SnapView snap) {
		Session.getSelectedSheet().remove(snap);
		snaps.remove(snap);
		this.model.removeSnap(snap.getLocation());

		if (hasFocus)
			Session.getSelectedDragableViews().remove(snap);

		update();
		updateSelectionArea();
	}

	public void setContext(ConnectorContextInterface context) {
		this.context = context;
		this.model.setDrawnType(context.getDrawnType());
	}

	public ConnectorContextInterface getContext() {
		return context;
	}

	public EntityView getOriginEntity() {
		return origin;
	}

	public EntityView getEndEntity() {
		return end;
	}

	public void setStartPoint(Point newStartPoint) {
		startPoint = newStartPoint;
	}

	public void setEndPoint(Point newEndPoint) {
		endPoint = newEndPoint;
	}

	public Point getStartPoint() {
		return startPoint;
	}

	public Point getEndPoint() {
		return endPoint;
	}

	public int getSnapCount() {
		return snaps.size();
	}

	public SnapView getSnap(int index) {
		return snaps.get(index);
	}

	@Override
	public ArrayList<ViewDragableInterface> getDragableItems() {

		ArrayList<ViewDragableInterface> ret = new ArrayList<ViewDragableInterface>();
		Component c[] = Session.getSelectedSheet().getComponents();
		for (int i = 0; i < c.length; i++)
			if (c[i] instanceof SnapView && ((SnapView) c[i]).isOnConnector(this))
				ret.add((ViewDragableInterface) c[i]);
		return ret;
	}

	@Override
	public void getFocus() {
		MainForm.getInstance().getMenu().activateDeleteMit();
		MainForm.getInstance().getGeneralToolbar().activateDelBtn();
		MainForm.getInstance().onSelectPaneToFront();
		
		Session.getSelectedViews().add(this);
		Session.getSelectedDragableViews().addAll(snaps);
		
		hasFocus = true;

		Session.st_triggerSelectPanUpdate();
		Session.getSelectedSheet().repaint(Session.getSelectedSheet().getVisibleRect());
	}

	@Override
	public void releaseFocus() {
		Session.getSelectedViews().remove(this);
		
		if (Session.getSelectedViews().isEmpty()){
			MainForm.getInstance().getMenu().deactivateDeleteMit();
			MainForm.getInstance().getGeneralToolbar().deactivateDelBtn();
		}
		
		Session.getSelectedDragableViews().removeAll(snaps);
		hasFocus = false;

		Session.st_triggerSelectPanUpdate();
		Session.getSelectedSheet().repaint(Session.getSelectedSheet().getVisibleRect());
	}

	public boolean hasFocus() {
		return hasFocus;
	}

	public boolean isSelected(MouseEvent event) {
		for (Polygon p : selectableAreaPolygons)
			if (p.contains(event.getPoint()))
				return true;
		return false;
	}

	public void update() {
		model.getSnaps().clear();
		for (SnapView s : snaps)
			model.addSnap(s.getLocation());
		updateStartPoint();
		updateEndPoint();
		checkArrowPts();

		updateSelectionArea();
		updateArrowPoints();
	}

	@Override
	public  abstract AbstractControlJPanel generateControlPan();
	
	public void repaint() {
		Session.getSelectedSheet().repaint(Session.getSelectedSheet().getVisibleRect());
	}

	public ConnectorModelInterface getModel() {
		return model;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ConnectionView))
			return false;

		ConnectionView conObj = (ConnectionView) obj;
		return conObj.getModel().equals(getModel());
	}

	@Override
	public int hashCode() {
		return getModel().hashCode();
	}

	public void delete() {
		this.releaseFocus();
		Session.getSelectedSheet().remove(this);
		Session.st_triggerSelectPanUpdate();

		for (SnapView s : snaps)
			Session.getSelectedSheet().remove(s);
		Session.getSelectedSheet().getModel().remove(this.getModel());
		Session.getSelectedSheet().repaint(Session.getSelectedSheet().getVisibleRect());
	}

	public void restore(Glyph parent) {
		Session.getSelectedSheet().add(this);

		for (SnapView s : snaps)
			Session.getSelectedSheet().add(s);

		Session.getSelectedSheet().getModel().add(this.getModel());
		Session.getSelectedSheet().repaint(Session.getSelectedSheet().getVisibleRect());
	}
}
