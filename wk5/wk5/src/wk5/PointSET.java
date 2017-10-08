package wk5;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.TreeSet;
import java.util.List;
import java.util.ArrayList;

public class PointSET {	
	private TreeSet<Point2D> root = new TreeSet<Point2D>();
	
	public PointSET() {
		// construct an empty set of points 
	}
	
	public boolean isEmpty() {
		// is the set empty? 
		return root.isEmpty();
	}
	
	public int size() {
		// number of points in the set
		return root.size();
	}
	
	public void insert(Point2D p) {
		// add the point to the set (if it is not already in the set)
		if (p == null)
			throw new NullPointerException("p is null");
		
		root.add(p);
	}
	
	public boolean contains(Point2D p) {
		// does the set contain point p? 
		if (p == null)
			throw new NullPointerException("p is null");
		
		return root.contains(p);
	}
	
	public void draw() {
		// draw all points to standard draw
		for (Point2D p : root)
		{
			p.draw();
		}
	}
	
	public Iterable<Point2D> range(RectHV rect)  {
		// all points that are inside the rectangle 
		if (rect == null)
			throw new NullPointerException("rect is null");
		
		List<Point2D> list = new ArrayList<Point2D>();
		for (Point2D p : root)
		{
			if (rect.contains(p))
				list.add(p);
		}
		return list;
	}
	
	public Point2D nearest(Point2D p) {
		// a nearest neighbor in the set to point p; null if the set is empty 
		if (p == null)
			throw new NullPointerException("point is null");
		
		Point2D nearest = null;
		double distance = Double.POSITIVE_INFINITY;
		for (Point2D pp : root)
		{
			double d= pp.distanceSquaredTo(p);
			if (d < distance)
			{
				nearest = pp;
				distance = d;
			}
		}
		return nearest;
	}

	public static void main(String[] args) {
		// unit testing of the methods (optional)
		PointSET root = new PointSET();
		root.insert(new Point2D(0,0));
		root.insert(new Point2D(3, 2));
		root.insert(new Point2D(10, 9));
		System.out.println("root contains 2, 3? " + root.contains(new Point2D(2, 3)));
		System.out.println("root contains 3, 2? " + root.contains(new Point2D(3, 2)));
		System.out.println("nearest to 4, 4? " + root.nearest(new Point2D(4,4)));
	}
}
