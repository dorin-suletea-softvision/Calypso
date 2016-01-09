package engine.geometry;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import engine.views.ConnectionView;


public interface IntersectionAlgorithmFamily {
	public Point2D getIntersectionPoint(Line2D line, Rectangle intersectRect);
	public Point2D lineIntersection(Line2D line1 , Line2D line2);
	public Point2D closestPoint(Point2D reference , ArrayList<Point> compareList );
	public ArrayList<Point> createArrowPoints(ConnectionView connector,int pointDistance,boolean forOrigin);
	public Point rectangleMid(Rectangle rectangle);
	public Polygon generatePoligon(Line2D line, int polyLineSize);
	public Rectangle rectInflator(Rectangle rectangle,int inflateBy);
	public boolean  polyIntersect(Polygon p1, Polygon p2);
	public Point2D  lineMidPoint(Line2D line);
}
