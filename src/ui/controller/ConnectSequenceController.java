package ui.controller;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import sesion.Session;
import engine.command.ConnectCommand;
import engine.views.EntityView;
import engine.views.Glyph;

public class ConnectSequenceController {
	//buffer operation
	private EntityView source;
	private EntityView destination;
	
	private Point          startMid; //buffered midBounds point of the start element
	
	
	public ConnectSequenceController() {
	
	}
	
	
	public void connectBtnListener(final EntityView source,final JButton connectBtn){
		
		MouseAdapter listener = new MouseAdapter(){

			public void mousePressed(MouseEvent e) {
				ConnectSequenceController.this.source=source;
				startMid = Session.getIntersectionAlgorithm().rectangleMid(source.getBounds());
				startMid = SwingUtilities.convertPoint(source.getParent(), startMid.x, startMid.y, Session.getSelectedSheet());
				
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				//only entity views can be conected
				
				Glyph destinationGlyph = Session.getSelectedSheet().getGlyphUnderMouse(e.getLocationOnScreen());
				if (!(destinationGlyph instanceof EntityView)){
					ConnectSequenceController.this.source=null;
					Session.getSelectedSheet().getGlassPane().clearConnectorLines();
					Session.getSelectedSheet().repaint(Session.getSelectedSheet().getVisibleRect());
					return;
				}
				
				ConnectSequenceController.this.destination=((EntityView) destinationGlyph);					
				ConnectCommand conExec = new ConnectCommand(ConnectSequenceController.this.source,ConnectSequenceController.this.destination);
				Session.addCommand(conExec);
				conExec.execute();
				ConnectSequenceController.this.source= null;
				Session.getSelectedSheet().getGlassPane().clearConnectorLines();
			}
		
			@Override
			public void mouseDragged(MouseEvent e) {
				Point lineEndPt = e.getLocationOnScreen();
				SwingUtilities.convertPointFromScreen(lineEndPt, Session.getSelectedSheet());
				
				Line2D.Float line = new Line2D.Float(startMid, lineEndPt);
				Point2D startPt = Session.getIntersectionAlgorithm().getIntersectionPoint(line, source.getBounds());
				
				Session.getSelectedSheet().getGlassPane().clearConnectorLines();
				Session.getSelectedSheet().getGlassPane().addConnectorLine(new Line2D.Float((int)startPt.getX(),(int)startPt.getY(),lineEndPt.x,lineEndPt.y));
				Session.getSelectedSheet().repaint();
			}
		};
		
		connectBtn.addMouseListener(listener);
		connectBtn.addMouseMotionListener(listener);
	}

}

