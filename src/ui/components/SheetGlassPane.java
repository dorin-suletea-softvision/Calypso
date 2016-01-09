package ui.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import sesion.props.ModelPropertySheet;

public class SheetGlassPane {
	private ArrayList<Line2D>     connecorMoveLines;
	private ArrayList<Rectangle>  entityMoveRectangle;
	
	private Rectangle                 multipleSelectionRectangle;
	
	public SheetGlassPane() {
		connecorMoveLines=new ArrayList<Line2D>();
		entityMoveRectangle=new ArrayList<Rectangle>();
	}
	
	public void paintComponent(Graphics g){
//		g.fillRect(0, 0, g.getClipRect().width, g.getClipBounds().height);
		Color preDrawColor = g.getColor();
		g.setColor(ModelPropertySheet.getInstance().getMoveViewColor());
		Graphics2D g2 = (Graphics2D) g;
		Stroke s = g2.getStroke();
		g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
		//pre set
		
		
		for (Line2D line : connecorMoveLines)
			g.drawLine((int)line.getP1().getX(), (int)line.getP1().getY(),(int) line.getP2().getX(), (int)line.getP2().getY());
		for (Rectangle rectangle : entityMoveRectangle)
			g.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
		
		//post set
		g.setColor(preDrawColor);
		g2.setStroke(s);
		
		if (multipleSelectionRectangle!=null)
			g.drawRect(multipleSelectionRectangle.x, multipleSelectionRectangle.y, multipleSelectionRectangle.width, multipleSelectionRectangle.height);
	}


	public boolean addConnectorLine(Line2D e) {
		return connecorMoveLines.add(e);
	}
	public boolean addEntityRectangle(Rectangle e) {
		return entityMoveRectangle.add(e);
	}
	public void clearConnectorLines() {
		connecorMoveLines.clear();
	}
	
	public void clearEntityRectangles() {
		entityMoveRectangle.clear();
	}

	public void setMultipleSelectionRectangle(Rectangle multipleSelectionRectangle) {
		this.multipleSelectionRectangle = multipleSelectionRectangle;
	}
}
