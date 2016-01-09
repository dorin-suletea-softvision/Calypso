package ui.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import javax.swing.JComponent;

import sesion.props.UIPropSheet;

public class LineLabel extends JComponent{
	private static final long serialVersionUID = 1L;


	public LineLabel(int width) {
		setSize(width,UIPropSheet.LINE_LABEL_H);
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		Color c = g.getColor();
		Stroke st= g2.getStroke();
		g2.setStroke(new BasicStroke(3,BasicStroke.CAP_ROUND,0));
		g.setColor(UIPropSheet.LINE_LABEL_BG_COLOR);
		g2.drawLine(0, 0, getWidth(), 0);
		g2.setStroke(st);
		g.setColor(c);
		super.paintComponent(g);
	}

}
