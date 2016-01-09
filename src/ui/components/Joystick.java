package ui.components;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.JPanel;

import sesion.Session;
import sesion.props.UIPropSheet;
import ui.controller.FacadeController;
import engine.views.EntityView;

public class Joystick extends JPanel{
	private static final long serialVersionUID = 1L;
	private EntityView onTopOf;
	private JButton    connectBtn;
	
	

	
	public Joystick(EntityView onTopOf) {
		this.onTopOf=onTopOf;
		setOpaque(false);
		initComponents();
		addListeners();
		setCursor(Cursor.getDefaultCursor());
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		g.clearRect(0, 0, getWidth(), getHeight());
		Graphics2D g2d = (Graphics2D) g;
		
		Composite exComposite = g2d.getComposite();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        
		Color exColor = g.getColor();
		g.setColor(Color.GREEN);
		g.fillRect(0, 0, getWidth(), getHeight());
		super.paintComponent(g);
		g.setColor(exColor);
		g2d.setComposite(exComposite);
	}
	
	public void updateBounds(){
		int colFit = (onTopOf.getHeight())/
					 (UIPropSheet.SMALL_BTN_DIM.height +  
					 UIPropSheet.SMALL_BTN_PAD*2);
	
		int colNr = (int) Math.ceil(((float)getComponentCount())/colFit);
		
		int maxColH=Math.min(colFit,getComponentCount())*(UIPropSheet.SMALL_BTN_DIM.height+
														  UIPropSheet.SMALL_BTN_PAD)
														+UIPropSheet.SMALL_BTN_PAD;
	
		
		setSize(colNr*(UIPropSheet.SMALL_BTN_DIM.width+UIPropSheet.SMALL_BTN_PAD)
				     + UIPropSheet.SMALL_BTN_PAD, 
				maxColH);
		setLocation(onTopOf.getWidth()-onTopOf.getBorder().getBorderInsets(onTopOf).right-getWidth(),
					onTopOf.getBorder().getBorderInsets(onTopOf).top);
		
		
		
		Component cl [] = getComponents();
		Point lastLoc = new Point(getWidth(),//-UserInterfacePropertySheet.JOYSTICK_BTN_PAD-UserInterfacePropertySheet.JOYSTICK_BTN_DIM.width,
								  0-UIPropSheet.SMALL_BTN_DIM.height);
		
		for (int i=0;i<getComponentCount();i++){
			if (i%colFit!=0){
				cl[i].setLocation(lastLoc.x,
								  lastLoc.y+UIPropSheet.SMALL_BTN_PAD+UIPropSheet.SMALL_BTN_DIM.height);
				lastLoc=cl[i].getLocation();
			}else{
				cl[i].setLocation(lastLoc.x-UIPropSheet.SMALL_BTN_PAD-UIPropSheet.SMALL_BTN_DIM.width,
						  		  UIPropSheet.SMALL_BTN_PAD);
				lastLoc=cl[i].getLocation();
			}
		}
		if (Session.getSelectedSheet()!=null)
			Session.getSelectedSheet().repaint(Session.getSelectedSheet().getVisibleRect());
	}
	
	private void initComponents(){
		setLayout(null);
		connectBtn = new JButton();
		addBtn(connectBtn);
	}
	

	public Component addBtn(Component comp) {
		comp.setSize(UIPropSheet.SMALL_BTN_DIM);
		Component ret = super.add(comp);
		updateBounds();
		return ret;
	}

	
	private void addListeners(){
		FacadeController.getInstance().conSeq_connectBtnListener(onTopOf, connectBtn);
//		FacadeController.getInstance().conSeq_startEvtListener(onTopOf, connectBtn);
//		FacadeController.getInstance().conSeq_stopEvtListener();
	}
}
