package engine.geometry;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import engine.views.ConnectionView;

import sesion.Session;

public class IntersectionAlgorithm1 implements IntersectionAlgorithmFamily {
	private static final int defaultPolyLineSize = 5;

	// returns the point where line collides with rectangle or the rectangle mid
	// if there is no such point
	@Override
	public Point2D getIntersectionPoint(Line2D line, Rectangle intersectRect) {
		Point2D r = null;
		ArrayList<Point> ptList = new ArrayList<Point>();
		Polygon compareBounds = generatePoligon(line, defaultPolyLineSize);

		Line2D lr1 = new Line2D.Float(intersectRect.x, intersectRect.y, intersectRect.x,
				intersectRect.y + intersectRect.height);
		Line2D lr2 = new Line2D.Float(intersectRect.x, intersectRect.y, intersectRect.x
				+ intersectRect.width, intersectRect.y);
		Line2D lr3 = new Line2D.Float(intersectRect.x, intersectRect.y + intersectRect.height,
				intersectRect.x + intersectRect.width, intersectRect.y + intersectRect.height);
		Line2D lr4 = new Line2D.Float(intersectRect.x + intersectRect.width, intersectRect.y,
				intersectRect.x + intersectRect.width, intersectRect.y + intersectRect.height);

		if ((r = lineIntersection(line, lr1)) != null)
			ptList.add(new Point((int) r.getX(), (int) r.getY()));
		if ((r = lineIntersection(line, lr2)) != null)
			ptList.add(new Point((int) r.getX(), (int) r.getY()));
		if ((r = lineIntersection(line, lr3)) != null)
			ptList.add(new Point((int) r.getX(), (int) r.getY()));
		if ((r = lineIntersection(line, lr4)) != null)
			ptList.add(new Point((int) r.getX(), (int) r.getY()));

		while (!ptList.isEmpty()) {
			Point2D l = closestPoint(line.getP1(), ptList);
			if (compareBounds.contains(l))
				return l;
			else
				ptList.remove(l);
		}
		return rectangleMid(intersectRect);
	}

	@Override
	public Point2D lineIntersection(Line2D line1, Line2D line2) {
		Point2D cp = new Point2D.Double(0, 0);
		double a1, b1, c1, a2, b2, c2, denom;
		a1 = line1.getY2() - line1.getY1();
		b1 = line1.getX1() - line1.getX2();
		c1 = line1.getX2() * line1.getY1() - line1.getX1() * line1.getY2();
		a2 = line2.getY2() - line2.getY1();
		b2 = line2.getX1() - line2.getX2();
		c2 = line2.getX2() * line2.getY1() - line2.getX1() * line2.getY2();
		denom = a1 * b2 - a2 * b1;
		if (denom != 0)
			cp = new Point2D.Double((b1 * c2 - b2 * c1) / denom, (a2 * c1 - a1 * c2) / denom); // else
																								// paralel
																								// lines
		return cp;
	}

	@Override
	public Point2D closestPoint(Point2D reference, ArrayList<Point> compareList) {
		Point min = compareList.get(0);
		for (Point p : compareList) {
			if (Math.abs(reference.getX() - p.x) + Math.abs(reference.getY() - p.y) < Math
					.abs(reference.getX() - min.x) + Math.abs(reference.getY() - min.y))
				min = p;
		}
		return min;
	}

	// polygon generator
	// generates a polygon that contains the given line with the margin size
	// given by size parameter
	public Polygon generatePoligon(Line2D line, int polyLineSize) {
		return new Polygon(new int[] { ox1(line, polyLineSize), ox2(line, polyLineSize),
				ex1(line, polyLineSize), ex2(line, polyLineSize) }, new int[] {
				oy1(line, polyLineSize), oy2(line, polyLineSize), ey1(line, polyLineSize),
				ey2(line, polyLineSize) }, 4);
	}

	// vectorial transform
	private int ox1(Line2D line, int lineWidth) {
		return (int) (line.getP1().getX() - (line.getP1().getY() - line.getP2().getY())
				* scale(line, lineWidth));

	}

	private int ox2(Line2D line, int lineWidth) {
		return (int) (line.getP1().getX() + (line.getP1().getY() - line.getP2().getY())
				* scale(line, lineWidth));
	}

	private int oy2(Line2D line, int lineWidth) {
		return (int) (line.getP1().getY() - (line.getP1().getX() - line.getP2().getX())
				* scale(line, lineWidth));
	}

	private int ex1(Line2D line, int lineWidth) {
		return (int) (line.getP2().getX() - (line.getP2().getY() - line.getP1().getY())
				* scale(line, lineWidth));
	}

	private int ex2(Line2D line, int lineWidth) {
		return (int) (line.getP2().getX() + (line.getP2().getY() - line.getP1().getY())
				* scale(line, lineWidth));
	}

	private int ey1(Line2D line, int lineWidth) {
		return (int) (line.getP2().getY() + (line.getP2().getX() - line.getP1().getX())
				* scale(line, lineWidth));
	}

	private int ey2(Line2D line, int lineWidth) {
		return (int) (line.getP2().getY() - (line.getP2().getX() - line.getP1().getX())
				* scale(line, lineWidth));
	}

	private int oy1(Line2D line, int lineWidth) {
		return (int) (line.getP1().getY() + (line.getP1().getX() - line.getP2().getX())
				* scale(line, lineWidth));
	}

	private float scale(Line2D line, int lineWidth) {
		return (float) (lineWidth / Math.sqrt((line.getP1().getX() - line.getP2().getX())
				* (line.getP1().getX() - line.getP2().getX())
				+ (line.getP1().getY() - line.getP2().getY())
				* (line.getP1().getY() - line.getP2().getY())));
	}

	public Point[] paralelToLine(Line2D.Float line, int paralelLineSize) {
		Point[] ret = new Point[2];
		ret[0] = new Point(ox1(line, paralelLineSize / 2), oy1(line, paralelLineSize / 2));

		ret[1] = new Point(ox2(line, paralelLineSize / 2), oy2(line, paralelLineSize / 2));
		return ret;
	}

	// !polygon generation

	// returns the mid point of a rectangle
	public Point rectangleMid(Rectangle rectangle) {
		return new Point(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);
	}

	// return an inflated rectangle
	public Rectangle rectInflator(Rectangle rectangle, int inflateBy) {
		return new Rectangle(rectangle.x - inflateBy, rectangle.y - inflateBy, rectangle.width
				+ inflateBy * 2, rectangle.height + inflateBy * 2);
	}

	public ArrayList<Point> createArrowPoints(ConnectionView connector, int pointDistance,
			boolean forOrigin) {
		Point auxNextPoint = null;
		auxNextPoint = forOrigin ? connector.firstSnapLocation() : connector.lastSnapLocation();

		// can be origin or end bounds/point;
		Rectangle boundedRectangle;
		Point boundedPoint;

		// bounded point is not converted because it's converted to screen coord
		// system in ConnectionView updateStart() method
		if (forOrigin) {
			boundedRectangle = connector.getOriginEntity().getBounds();
			boundedPoint = connector.getStartPoint();
			boundedRectangle = SwingUtilities.convertRectangle(connector.getOriginEntity().getParent(),
					boundedRectangle, Session.getSelectedSheet());
		}
		else {
			boundedRectangle = connector.getEndEntity().getBounds();
			boundedPoint = connector.getEndPoint();
			boundedRectangle = SwingUtilities.convertRectangle(
					connector.getEndEntity().getParent(), boundedRectangle, Session.getSelectedSheet());
		}

		Rectangle rcSmall = rectInflator(boundedRectangle, 1);
		Rectangle rcMedium = rectInflator(rcSmall, pointDistance);
		Rectangle rcBig = rectInflator(rcMedium, pointDistance);

		Point2D pS = Session.getIntersectionAlgorithm().getIntersectionPoint(new Line2D.Float(
				boundedPoint, auxNextPoint), rcSmall);
		Point2D pM = Session.getIntersectionAlgorithm().getIntersectionPoint(new Line2D.Float(
				boundedPoint, auxNextPoint), rcMedium);
		Point2D pB = Session.getIntersectionAlgorithm().getIntersectionPoint(new Line2D.Float(
				boundedPoint, auxNextPoint), rcBig);

		// small rectangle
		ArrayList<Point> startPoints = new ArrayList<Point>();
		Point[] sidePts = paralelToLine(new Line2D.Float(pS, auxNextPoint), pointDistance);
		Point2D midPoint = Session.getIntersectionAlgorithm().lineIntersection(new Line2D.Float(
				sidePts[0], sidePts[1]), new Line2D.Float(boundedPoint, auxNextPoint));

		startPoints.add(sidePts[0]);
		startPoints.add(new Point((int) midPoint.getX(), (int) midPoint.getY()));
		startPoints.add(sidePts[1]);
		// ! small rectangle

		// medium rectangle
		sidePts = paralelToLine(new Line2D.Float(pM, auxNextPoint), pointDistance);
		midPoint = Session.getIntersectionAlgorithm().lineIntersection(new Line2D.Float(sidePts[0],
				sidePts[1]), new Line2D.Float(boundedPoint, auxNextPoint));

		startPoints.add(sidePts[0]);
		startPoints.add(new Point((int) midPoint.getX(), (int) midPoint.getY()));
		startPoints.add(sidePts[1]);
		// ! medium rectangle

		// big rectangle
		sidePts = paralelToLine(new Line2D.Float(pB, auxNextPoint), pointDistance);
		midPoint = Session.getIntersectionAlgorithm().lineIntersection(new Line2D.Float(sidePts[0],
				sidePts[1]), new Line2D.Float(boundedPoint, auxNextPoint));

		startPoints.add(sidePts[0]);
		startPoints.add(new Point((int) midPoint.getX(), (int) midPoint.getY()));
		startPoints.add(sidePts[1]);
		// ! big rectangle
		return startPoints;
	}

	public boolean polyIntersect(Polygon p1, Polygon p2) {
		Point p;
		for (int i = 0; i < p2.npoints; i++) {
			p = new Point(p2.xpoints[i], p2.ypoints[i]);
			if (p1.contains(p))
				return true;
		}
		for (int i = 0; i < p1.npoints; i++) {
			p = new Point(p1.xpoints[i], p1.ypoints[i]);
			if (p2.contains(p))
				return true;
		}
		return false;
	}

	@Override
	public Point2D lineMidPoint(Line2D line) {
		return rectangleMid(line.getBounds());
	}
}