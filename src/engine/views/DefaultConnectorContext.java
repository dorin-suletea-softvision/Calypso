package engine.views;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import ui.components.EmptyControlPann;

import bridge.transferable.context.ConnectorContextInterface;
import bridge.transferable.interfaces.AbstractControlJPanel;
import bridge.transferable.proxy.ConnectorViewProxy;

public class DefaultConnectorContext implements ConnectorContextInterface {

	@Override
	public String getDrawnType() {
		return "DefaultConnectorType";
	}
	@Override
	public boolean isLabeled() {
		return false;
	}

	@Override
	public void drawEndArrow(Graphics g, ArrayList<Point> points) {
	}

	@Override
	public void drawStartArrow(Graphics g, ArrayList<Point> points) {
		return;

	}

	@Override
	public void drawLine(Graphics g, ArrayList<Point> snaps, Point start, Point end) {
//		System.out.println(start);
//		System.out.println(end);
		if (snaps.isEmpty())
			g.drawLine(start.x, start.y, end.x, end.y);
		else {
			// draw simple line
			g.drawLine(start.x, start.y, snaps.get(0).x, snaps.get(0).y);
			g.drawLine(end.x, end.y, snaps.get(snaps.size() - 1).x, snaps.get(snaps.size() - 1).y);
			for (int i = 0; i < snaps.size() - 1; i++)
				g.drawLine(snaps.get(i).x, snaps.get(i).y, snaps.get(i + 1).x, snaps.get(i + 1).y);
		}
	}
	@Override
	public AbstractControlJPanel generateUiPan(ConnectorViewProxy connector) {
		return new EmptyControlPann();
	}
}
