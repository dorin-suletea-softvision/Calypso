package engine.views;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

import sesion.Session;
import sesion.props.ModelPropertySheet;
import bridge.transferable.interfaces.CommandInterface;
import engine.command.RelocateCommand;
import engine.views.interfaces.ViewDragableInterface;

public class SnapView extends JComponent implements ViewDragableInterface {
	private static final long serialVersionUID = 1L;
	public final static Dimension size = new Dimension(15, 15);
	public final static int paintAdjuster = 1;
	private Point realLocation;
	private ConnectionView onConnector;

	public SnapView(Point location, ConnectionView onConnector) {
		this.onConnector = onConnector;
		this.setLocation(location);
		this.setSize(size);
		this.addMouseListener(generateDragListeners());
		this.addMouseMotionListener(generateDragListeners());
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Color tmp = g.getColor();
		g.setColor(ModelPropertySheet.OPAQUE_LIGHT_GRAY);
		g.fillOval(0, 0, getWidth() - paintAdjuster, getHeight() - paintAdjuster);

		g.setColor(ModelPropertySheet.OPAQUE_DARK_GRAY);
		g.drawOval(0, 0, getWidth() - paintAdjuster, getHeight() - paintAdjuster);

		g.setColor(tmp);
	}

	private MouseInputAdapter generateDragListeners() {
		MouseInputAdapter ml = new MouseInputAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				int dx = getX() + SnapView.paintAdjuster - realLocation.x;
				int dy = getY() + SnapView.paintAdjuster - realLocation.y;
				setLocation(getX() - dx, getY() - dy);
				
				CommandInterface command = new RelocateCommand(Session.getSelectedDragableViews(), dx, dy);
				if (command.execute()){
					Session.addCommand(command);
				}
				
				realLocation = getLocation();
				setLocation(realLocation.x - paintAdjuster, realLocation.y - paintAdjuster);
				update();
				onConnector.update();
				
				if (!onConnector.hasFocus()){
					Session.getSelectedDragableViews().remove(SnapView.this);
				}
				
				Session.getSelectedSheet().getGlassPane().clearConnectorLines();
				Session.getSelectedSheet().getGlassPane().clearEntityRectangles();
				Session.getSelectedSheet().repaint();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				setCursor(new Cursor(Cursor.MOVE_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				//aici
				
				if (!onConnector.hasFocus()) {
					System.out.println(Session.getSelectedDragableViews().contains(SnapView.this));
					Session.getSelectedDragableViews().add(SnapView.this);
					System.out.println(Session.getSelectedDragableViews());
				}
//				else{
//					for (SnapView sn : Session.getSelectedSheet().getSnapsOf(onConnector))
//						if (!Session.getSelectedViews().contains(sn))
//							
//					
//				}
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				Point point = e.getPoint();
				point = SwingUtilities.convertPoint(SnapView.this, point, Session.getSelectedSheet());
				
				setLocation(new Point(point.x - (SnapView.size.width - SnapView.paintAdjuster) / 2, point.y - (SnapView.size.height - SnapView.paintAdjuster) / 2));
				int dx = realLocation.x - getX();
				int dy = realLocation.y - getY();
				
				
				Session.getSelectedSheet().getGlassPane().clearEntityRectangles();
				Session.getSelectedSheet().getGlassPane().clearConnectorLines();

				Rectangle drawRectangle;
				for (ViewDragableInterface v : Session.getSelectedDragableViews())
					if (v != SnapView.this) {
						drawRectangle = new Rectangle(v.getX() - dx, v.getY() - dy, v.getWidth(), v.getHeight());
						Session.getSelectedSheet().getGlassPane().addEntityRectangle(drawRectangle);
						Session.getSelectedSheet().getGlassPane()
								.addConnectorLine(new Line2D.Float(Session.getIntersectionAlgorithm().rectangleMid(v.getBounds()), Session.getIntersectionAlgorithm().rectangleMid(drawRectangle)));
					}
				onConnector.update();
				Session.getSelectedSheet().getGlassPane().addConnectorLine(new Line2D.Float(realLocation, Session.getIntersectionAlgorithm().rectangleMid(getBounds())));
				Session.getSelectedSheet().repaint(Session.getSelectedSheet().getVisibleRect());
			}
		};
		return ml;
	}

	// updates the parent connector of the snap
	@Override
	public void update() {
		onConnector.update();
	}

	public boolean isOnConnector(ConnectionView conector) {
		return this.onConnector.equals(conector) ? true : false;
	}

	public ConnectionView onConnector() {
		return onConnector;
	}

	@Override
	public void setLocation(Point p) {
		if (realLocation == null)
			realLocation = p;
		onConnector.update();
		super.setLocation(p);
	}

	@Override
	public boolean validNewPosition(int dx, int dy) {
		if (getX() + dx < 0 || getY() + dy < 0)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return realLocation.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof SnapView))
			return false;
		SnapView objView = (SnapView) obj;
		if (!(objView.onConnector.equals(onConnector)))
			return false;
		if (!(objView.getLocation().equals(getLocation())))
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		return 1;
	}
}
