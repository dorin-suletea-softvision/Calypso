package ui.components;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

import sesion.Session;
import sesion.props.ModelPropertySheet;
import bridge.transferable.interfaces.CommandInterface;
import engine.command.NestCommand;
import engine.command.RelocateCommand;
import engine.command.ResizeCommand;
import engine.views.EntityView;
import engine.views.Glyph;
import engine.views.interfaces.ViewDragableInterface;


public class ResizableBorder implements Border {
	private final int 	  borderThickness =   10;
	private EntityView parent;
	private Rectangle newBounds;

	int locations[] = { SwingConstants.NORTH, SwingConstants.SOUTH,
					          SwingConstants.WEST, SwingConstants.EAST,
					          SwingConstants.NORTH_WEST, SwingConstants.NORTH_EAST,
					          SwingConstants.SOUTH_WEST, SwingConstants.SOUTH_EAST };

	int cursors[] = { Cursor.N_RESIZE_CURSOR, Cursor.S_RESIZE_CURSOR,
							Cursor.W_RESIZE_CURSOR, Cursor.E_RESIZE_CURSOR,
							Cursor.NW_RESIZE_CURSOR, Cursor.NE_RESIZE_CURSOR,
							Cursor.SW_RESIZE_CURSOR, Cursor.SE_RESIZE_CURSOR };

	public ResizableBorder(EntityView parent) {
		this.parent=parent;
		addParentListeners();
	}

	public Insets getBorderInsets(Component component) {
		return new Insets(borderThickness, borderThickness, borderThickness, borderThickness);
	}

	public boolean isBorderOpaque() {
		return false;
	}

	public void paintBorder(Component component, Graphics g, int x, int y,int w, int h) {
			for (int i = 0; i < locations.length; i++) {
				Rectangle rect = getRectangle(x, y, w, h, locations[i]);
				
				if (parent.hasFocus()){
					g.setColor(ModelPropertySheet.OPAQUE_LIGHT_GRAY);
					g.fillOval(rect.x, rect.y, rect.width-1, rect.height-1);
					g.setColor(ModelPropertySheet.OPAQUE_DARK_GRAY);
					g.drawOval(rect.x, rect.y, rect.width-1, rect.height-1);
				}
		}
	}

	private Rectangle getRectangle(int x, int y, int w, int h, int location) {
		switch (location) {
		case SwingConstants.NORTH:
			return new Rectangle(x + w / 2 - borderThickness / 2, y, borderThickness, borderThickness);
		case SwingConstants.SOUTH:
			return new Rectangle(x + w / 2 - borderThickness / 2, y + h - borderThickness,borderThickness, borderThickness);
		case SwingConstants.WEST:
			return new Rectangle(x, y + h / 2 - borderThickness / 2, borderThickness, borderThickness);
		case SwingConstants.EAST:
			return new Rectangle(x + w - borderThickness, y + h / 2 - borderThickness / 2,borderThickness, borderThickness);
		case SwingConstants.NORTH_WEST:
			return new Rectangle(x, y, borderThickness, borderThickness);
		case SwingConstants.NORTH_EAST:
			return new Rectangle(x + w - borderThickness, y, borderThickness, borderThickness);
		case SwingConstants.SOUTH_WEST:
			return new Rectangle(x, y + h - borderThickness, borderThickness, borderThickness);
		case SwingConstants.SOUTH_EAST:
			return new Rectangle(x + w - borderThickness, y + h - borderThickness, borderThickness, borderThickness);
		}
		return null;
	}

	public int getCursor(MouseEvent me) {
		if (!parent.hasFocus())
			return Cursor.DEFAULT_CURSOR;
		Component c = me.getComponent();
		int w = c.getWidth();
		int h = c.getHeight();

		for (int i = 0; i < locations.length; i++) {
			Rectangle rect = getRectangle(0, 0, w, h, locations[i]);
			if (rect.contains(me.getPoint()))
				return cursors[i];
		}

		return Cursor.MOVE_CURSOR;
	}
	
	public void resize(Point  mousePoint){
		if (!parent.isResizable() && parent.getCursor().getType()!=Cursor.MOVE_CURSOR) {
			newBounds=parent.getBounds();
			return;
		}
		int x = parent.getX();
		int y = parent.getY();
		int w = parent.getWidth();
		int h = parent.getHeight();
		int mex = mousePoint.x;
		int mey=mousePoint.y;
		int newW;
		int newH;
		int newX;
		int newY;
		
		
		
		Point absoluteLocation=mousePoint;
		absoluteLocation = SwingUtilities.convertPoint(parent, absoluteLocation, Session.getSelectedSheet());
		
//    THIS IS BUGGY , the implemenetation above is better
//		Point absoluteLocation = MouseInfo.getPointerInfo().getLocation();
//		SwingUtilities.convertPointFromScreen(absoluteLocation, Session.selectedSheet);



		switch (parent.getCursor().getType()) {
			case Cursor.N_RESIZE_CURSOR:
				newW=w;
				newH=h+(y-absoluteLocation.y);
				newX=x;
				newY=absoluteLocation.y;

				if (validParent(newW,newH))
					newBounds=new Rectangle(newX, newY, newW, newH);
				break;
				//
			case Cursor.S_RESIZE_CURSOR:
				newW=w;
				newH=mey;
				newX=x;
				newY=y;
				if (validParent(newW,newH)) 
					newBounds=new Rectangle(newX, newY, newW, newH);
				break;
				//
			case Cursor.W_RESIZE_CURSOR:
				newW=w+(x-absoluteLocation.x);
				newH=h;
				newX=absoluteLocation.x;
				newY=y;
				if (validParent(newW,newH))
					newBounds=new Rectangle(newX, newY, newW, newH);
				break;
				//
			case Cursor.E_RESIZE_CURSOR:
				newW=mex;
				newH=h;
				newX=x;
				newY=y;
				if (validParent(newW,newH)) 
					newBounds=new Rectangle(newX, newY, newW, newH);
				break;
				//
			case Cursor.NW_RESIZE_CURSOR:
				newW=w+(x-absoluteLocation.x);
				newH=h+(y-absoluteLocation.y);
				newX=absoluteLocation.x;
				newY=absoluteLocation.y;
				if (validParent(newW,newH)) 
					newBounds=new Rectangle(newX, newY, newW, newH);
				break;
				//
			case Cursor.NE_RESIZE_CURSOR:
				newW=w+(absoluteLocation.x-(x+w));
				newH= h-(absoluteLocation.y-y);
				newX=x;
				newY=absoluteLocation.y;
				if (validParent(newW,newH)) 
					newBounds=new Rectangle(newX, newY, newW, newH);
				break;
				//
			case Cursor.SW_RESIZE_CURSOR:
				newW=w - (absoluteLocation.x-x);
				newH= h-((y+h)-absoluteLocation.y);
				newX=absoluteLocation.x;
				newY=y;
				if (validParent(newW,newH)) 
					newBounds=new Rectangle(newX, newY, newW, newH);
				break;
				//
			case Cursor.SE_RESIZE_CURSOR:
				newW=mex;
				newH=mey;
				newX=x;
				newY=y;
				if (validParent(newW,newH)) 
					newBounds=new Rectangle(newX, newY, newW, newH);
				break;
				//
			case Cursor.MOVE_CURSOR:
				newBounds = new Rectangle(absoluteLocation.x-parent.getWidth()/2,absoluteLocation.y-parent.getHeight()/2,parent.getWidth(),parent.getHeight());
				break;
			}
//		if (newBounds.x<0)
//			newBounds.setLocation(0, newBounds.y);
//		if (newBounds.y<0)
//			newBounds.setLocation(newBounds.x, 0);


		
	}
	
	private boolean validParent(int newW, int newH){
		if (newW>=parent.getMinimumSize().width && newH>=parent.getMinimumSize().height )
			return true;
		else
			return false;
	}
	
	private void addParentListeners(){
		MouseInputListener listener = new MouseInputAdapter() {	
			
			@Override
			public void mousePressed(MouseEvent e) {
				newBounds=parent.getBounds();
			}
			
			public void mouseMoved(MouseEvent e) {
				parent.setCursor(Cursor.getPredefinedCursor(getCursor(e)));
			}
			
			@Override
			public void mouseDragged(MouseEvent me) {
				if (!parent.hasFocus())
					return;
				resize(me.getPoint());
				Session.getSelectedSheet().getGlassPane().clearEntityRectangles();
				Session.getSelectedSheet().getGlassPane().clearConnectorLines();
				if (parent.getCursor().getType()==Cursor.MOVE_CURSOR){
					int dx = newBounds.x - parent.getX()  ;
					int dy = newBounds.y -parent.getY()  ;

					for (ViewDragableInterface v : Session.getSelectedDragableViews()) {
						Rectangle drawRectangle = new Rectangle(v.getX()+dx,v.getY()+dy,v.getWidth(),v.getHeight());
							Session.getSelectedSheet().getGlassPane().addEntityRectangle(drawRectangle);
							Session.getSelectedSheet().getGlassPane().addConnectorLine(new Line2D.Float(Session.getIntersectionAlgorithm().rectangleMid(SwingUtilities.convertRectangle(v.getParent(),v.getBounds(),Session.getSelectedSheet())), Session.getIntersectionAlgorithm().rectangleMid(drawRectangle)));
					}
				}
				else{
						Session.getSelectedSheet().getGlassPane().addEntityRectangle(SwingUtilities.convertRectangle(parent.getParent(),newBounds,Session.getSelectedSheet()));
				}
				Session.getSelectedSheet().repaint(Session.getSelectedSheet().getVisibleRect());
			}
			
			@SuppressWarnings("unchecked")
			public void mouseReleased(MouseEvent e) {
				if (newBounds==null || !parent.hasFocus())
					return;
				Session.getSelectedSheet().getGlassPane().clearEntityRectangles();
				Session.getSelectedSheet().getGlassPane().clearConnectorLines();
				
				int dy = 0;
				int dx = 0;
				
				CommandInterface command = null;
				Rectangle checkRect=null;
				
				Hashtable<Glyph, List<Glyph>> intersectionTable=null;
				ArrayList<Glyph> intersectionArray=null;
				
				if (parent.getCursor().getType()==Cursor.MOVE_CURSOR){
					dx = newBounds.x - parent.getX();
					dy = newBounds.y -parent.getY();
					
					command = new RelocateCommand(Session.getSelectedDragableViews(), dx, dy); //moves target
					if (command.execute())
						Session.addCommand(command);
//					else 
//						Session.
					
					//nesting start 
					List<Glyph> allViews = Session.getSelectedSheet().getAllGlyphs();
					
					intersectionTable = new Hashtable<Glyph, List<Glyph>>();
					intersectionArray = new ArrayList<Glyph>();
					ArrayList<Glyph> selectedGlyphs  = new ArrayList<Glyph>();
					
					for (ViewDragableInterface v : Session.getSelectedDragableViews())
						if (v instanceof Glyph)
							selectedGlyphs.add((Glyph)v);
					

					for (Glyph v : selectedGlyphs){
						for (Glyph g : allViews){
							checkRect=SwingUtilities.convertRectangle(v.getParent(), v.getBounds(), g.getParent());
							if ((!g.equals(v)) && !g.hasNested((Glyph) v) && !v.hasNested(g) && (checkRect.intersects(g.getBounds()) || checkRect.contains(g.getBounds()) || g.getBounds().contains(checkRect))) 
								intersectionArray.add(g);			
							else 
								if (!Session.getSelectedSheet().hasNested(v)){
									command = new NestCommand(Session.getSelectedSheet(), v, (Glyph) v.getParent());
									Session.addCommand(command);
									command.execute();
									break;
								}
								
						}
						if (!intersectionArray.isEmpty()){
							intersectionTable.put(v, (ArrayList<Glyph>) intersectionArray.clone());
							intersectionArray.clear();
						}
						
					}
					

//					Hashtable<Glyph, List<Glyph>> nestTable=new Hashtable<Glyph, List<Glyph>>();
					ArrayList<Glyph> intersectionList = parent.createIntersectionList();
					
					if (parent.isNestingEnabled()) {
						throw new RuntimeException("Feature not suported");
						// nest procedure
						// if (!intersectionTable.isEmpty()) {
						// NestChoiceDialog diag = new NestChoiceDialog();
						// diag.initComponents(intersectionTable);
						// diag.setVisible(true);
						// }
						// !!
					}
					else {
						// reorder procedure
						intersectionList.add(parent);
						Collections.sort(intersectionList, new Comparator<Glyph>() {
							@Override
							public int compare(Glyph o1, Glyph o2) {
								if (o1.getWidth() * o1.getHeight() > o2.getWidth() * o2.getHeight())
									return 1;
								if (o1.getWidth() * o1.getHeight() < o2.getWidth() * o2.getHeight())
									return -1;
								return 0;
							};
						});
						for (int i = 0; i < intersectionList.size(); i++) {
							intersectionList.get(i).getParent().setComponentZOrder(intersectionList.get(i), i);
						}
						// !!
					}
					
				}
				else {
					Session.deselectAll();
					parent.getFocus();
					Session.st_triggerSelectPanUpdate();
					
					command = new ResizeCommand(parent, newBounds);
					Session.addCommand(command);
					command.execute();
					
//					intersectionTable=new Hashtable<Glyph, List<Glyph>>();
					intersectionArray=parent.createIntersectionList();
	
					
					if (parent.isNestingEnabled()){
						throw new RuntimeException("Feature not suported");
						//nest procedure
//						if (!intersectionArray.isEmpty()){
//							intersectionTable.put(parent, intersectionArray);
//							NestChoiceDialog diag = new NestChoiceDialog();
//							diag.initComponents(intersectionTable);
//							diag.setVisible(true);
//						}
						//!!
					}else {
						//reorder procedure
						intersectionArray.add(parent);
						Collections.sort(intersectionArray, new Comparator<Glyph>() {
							@Override
							public int compare(Glyph o1, Glyph o2) {
								if (o1.getWidth()*o1.getHeight()>o2.getWidth()*o2.getHeight())
									return 1;
								if (o1.getWidth()*o1.getHeight()<o2.getWidth()*o2.getHeight())
									return -1;
								return 0;
							};
						});
						for (int i=0;i<intersectionArray.size();i++){
							intersectionArray.get(i).getParent().setComponentZOrder(intersectionArray.get(i), i);
						}
						//!!
					}
					
					
					
					
					
				}
				

				Session.getSelectedSheet().repaint(Session.getSelectedSheet().getVisibleRect());
			}
		};

		parent.addMouseListener(listener);
		parent.addMouseMotionListener(listener);
	}
	
	

}
