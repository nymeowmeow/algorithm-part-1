import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
//import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import java.util.List;
import java.util.ArrayList;

public class KdTree {
	private Node root = null;
	private int count = 0;

	private class Node {
		private double x;
		private double y;
		private Node left = null;
		private Node right = null;;
		
		public Node(Point2D p)
		{
			this.x = p.x();
			this.y = p.y();
		}
		
		public double getX() { return this.x; }
		public double getY() { return this.y; }
		public Node getLeft() { return left; }
		public Node getRight() { return right; }
		public void setLeft(Node left) { this.left = left; }
		public void setRight(Node right) { this.right = right; }
		public boolean lessThanX(Point2D p) { return p.x() < this.x; }
		public boolean lessThanY(Point2D p) { return p.y() < this.y; }
	}
	
	public KdTree() {
		// construct an empty set of points 
	}
	
	public boolean isEmpty() {
		// is the set empty? 
		return (root == null)?true:false;
	}
	
	public int size() {
		// number of points in the set
		return this.count;
	}
	
	public void insert(Point2D p) {
		// add the point to the set (if it is not already in the set)
		if (p == null)
			throw new NullPointerException("p is null");
		
		if (root == null) {
			root = new Node(p);
			++count;
		} else {		
			insert(p, root, 0);
		}
	}
	
	private void insert(Point2D p, Node node, int depth)
	{
		//check for duplicate
		if (p.x() == node.getX() && p.y() == node.getY())
			return;

		boolean lessThan = (depth == 0)?node.lessThanX(p):node.lessThanY(p);
		int level = (depth == 0)?1:0;
		
		if (lessThan)
		{
			if (node.getLeft() == null)
			{
				node.setLeft(new Node(p));
				++count;
			} else {
				insert(p, node.getLeft(), level);
			}
		} else {
			if (node.getRight() == null)
			{
				node.setRight(new Node(p));
				++count;
			} else {
				insert(p, node.getRight(), level);
			}
		}
	}

	private boolean contains(Point2D p, Node node, int depth)
	{
		if (node == null) return false;

		if (p.x() == node.getX() && p.y() == node.getY())
			return true;
		
		boolean lessThan = (depth == 0)?node.lessThanX(p):node.lessThanY(p);
		int level = (depth == 0)?1:0;
		boolean result = (lessThan)?contains(p, node.getLeft(), level):
							contains(p, node.getRight(), level);
		return result;
	}
	
	public boolean contains(Point2D p) {
		// does the set contain point p? 
		if (p == null)
			throw new NullPointerException("p is null");
		
		return contains(p, root, 0);		
	}

	private void draw(Node node, int depth, double xlo, double xhi, double ylo, double yhi)
	{
		if (node == null) return;
		
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(0.03);
		StdDraw.point(node.getX(), node.getY());
		StdDraw.setPenRadius(0.01);
		if (depth == 0)
		{
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.line(node.getX(), ylo, node.getX(), yhi);
			draw(node.getLeft(), 1, xlo, node.getX(), ylo, yhi);
			draw(node.getRight(), 1, node.getX(), xhi, ylo, yhi);
		} else {
			StdDraw.setPenColor(StdDraw.BLUE);
			StdDraw.line(xlo, node.getY(), xhi, node.getY());
			draw(node.getLeft(), 0, xlo, xhi, ylo, node.getY());
			draw(node.getRight(), 0, xlo, xhi, node.getY(), yhi);
		}
	}
	
	public void draw() {
		// draw all points to standard draw
		draw(root, 0, 0.0, 1.0, 0.0, 1.0);
	}

	private void range(Node node, RectHV rect, int depth, double xlo, double xhi,
			double ylo, double yhi, List<Point2D> rangelist)
	{
		if (node == null)
			return;

		Point2D currentPoint = new Point2D(node.getX(), node.getY());
		if (rect.contains(currentPoint))
			rangelist.add(currentPoint);

		RectHV currentRec = new RectHV(xlo, ylo, xhi, yhi);
		if (rect.intersects(currentRec))
		{
			if (depth == 0)
			{
				RectHV leftRec = new RectHV(xlo, ylo, node.getX(), yhi);
				if (rect.intersects(leftRec))
					range(node.getLeft(), rect, 1, leftRec.xmin(), leftRec.xmax(), 
							leftRec.ymin(), leftRec.ymax(), rangelist);
				RectHV rightRec = new RectHV(node.getX(), ylo, xhi, yhi);
				if (rect.intersects(rightRec))
					range(node.getRight(), rect, 1, rightRec.xmin(), rightRec.xmax(),
							rightRec.ymin(), rightRec.ymax(), rangelist);
			} else {
				RectHV leftRec = new RectHV(xlo, ylo, xhi, node.getY());
				if (rect.intersects(leftRec))
					range(node.getLeft(), rect, 0, leftRec.xmin(), leftRec.xmax(),
									leftRec.ymin(), leftRec.ymax(), rangelist);
				RectHV rightRec = new RectHV(xlo, node.getY(), xhi, yhi);
				if (rect.intersects(rightRec))
					range(node.getRight(), rect, 0, rightRec.xmin(), rightRec.xmax(),
									rightRec.ymin(), rightRec.ymax(), rangelist);
			}

		}
	}
	public Iterable<Point2D> range(RectHV rect)  {
		// all points that are inside the rectangle 
		if (rect == null)
			throw new NullPointerException("rect is null");

		List<Point2D> rangelist = new ArrayList<Point2D>();
		range(root, rect, 0, 0.0, 1.0, 0.0, 1.0, rangelist);
		return rangelist;
	}

	private Point2D nearest(Point2D p, Node node, int depth, double xlo, double xhi,
						double ylo, double yhi, double mindistance, Point2D currentmin)
	{
		if (node == null)
			return currentmin;
		
		RectHV rect = new RectHV(xlo, ylo, xhi, yhi);
		if (rect.distanceTo(p) > mindistance)
			return currentmin;
		
		Point2D currentPoint = new Point2D(node.getX(), node.getY());
		double currentdistance = p.distanceTo(currentPoint);
		if (currentdistance < mindistance)
		{
			mindistance = currentdistance;
			currentmin = currentPoint;
		}
		if (depth == 0)
		{
			RectHV leftRec = new RectHV(xlo, ylo, node.getX(), yhi);
			RectHV rightRec = new RectHV(node.getX(), ylo, xhi, yhi);
			if (leftRec.distanceTo(p) < rightRec.distanceTo(p)){
			    currentmin = nearest(p, node.getLeft(), 1, leftRec.xmin(), leftRec.xmax(),
						leftRec.ymin(), leftRec.ymax(), mindistance, currentmin);
			    currentmin = nearest(p, node.getRight(), 1, rightRec.xmin(), rightRec.xmax(),
						rightRec.ymin(), rightRec.ymax(), currentmin.distanceTo(p), currentmin);
			} else {
			    currentmin = nearest(p, node.getRight(), 1, rightRec.xmin(), rightRec.xmax(),
			    		rightRec.ymin(), rightRec.ymax(), mindistance, currentmin);
			    currentmin = nearest(p, node.getLeft(), 1, leftRec.xmin(), leftRec.xmax(),
						leftRec.ymin(), leftRec.ymax(), currentmin.distanceTo(p), currentmin);
			}
		} else {
			RectHV leftRec = new RectHV(xlo, ylo, xhi, node.getY());
			RectHV rightRec = new RectHV(xlo, node.getY(), xhi, yhi);
			if (leftRec.distanceTo(p) < rightRec.distanceTo(p)){
				currentmin = nearest(p, node.getLeft(), 0, leftRec.xmin(), leftRec.xmax(),
						leftRec.ymin(), leftRec.ymax(), mindistance, currentmin);
				currentmin = nearest(p, node.getRight(), 0, rightRec.xmin(), rightRec.xmax(),
			    		rightRec.ymin(), rightRec.ymax(), currentmin.distanceTo(p), currentmin);	
			} else {
			    currentmin = nearest(p, node.getRight(), 0, rightRec.xmin(), rightRec.xmax(),
			    		rightRec.ymin(), rightRec.ymax(), mindistance, currentmin);
			    currentmin = nearest(p, node.getLeft(), 0, leftRec.xmin(), leftRec.xmax(),
						leftRec.ymin(), leftRec.ymax(), currentmin.distanceTo(p), currentmin);	
			}
		}
		return currentmin;
	}
	
	public Point2D nearest(Point2D p) {
		// a nearest neighbor in the set to point p; null if the set is empty 
		if (p == null)
			throw new NullPointerException("point is null");

		return nearest(p, root, 0, 0.0, 1.0, 0.0, 1.0, Double.POSITIVE_INFINITY, null);
	}

	public static void main(String[] args) {
		/*
        RectHV rect = new RectHV(0.0, 0.0, 1.0, 1.0);
        StdDraw.enableDoubleBuffering();
        KdTree kdtree = new KdTree();
        while (true) {
            if (StdDraw.mousePressed()) {
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();
                StdOut.printf("%8.6f %8.6f\n", x, y);
                Point2D p = new Point2D(x, y);
                if (rect.contains(p)) {
                    StdOut.printf("%8.6f %8.6f\n", x, y);
                    kdtree.insert(p);
                    StdDraw.clear();
                    kdtree.draw();
                    StdDraw.show();
                }
            }
            StdDraw.pause(50);
        }
        */
		/*
        String filename = args[0];
        In in = new In(filename);

        StdDraw.enableDoubleBuffering();

        // initialize the data structures with N points from standard input
        PointSET brute = new PointSET();
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            brute.insert(p);
        }

        double x0 = 0.0, y0 = 0.0;      // initial endpoint of rectangle
        double x1 = 0.0, y1 = 0.0;      // current location of mouse
        boolean isDragging = false;     // is the user dragging a rectangle

        // draw the points
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        brute.draw();
        StdDraw.show();

        while (true) {

            // user starts to drag a rectangle
            if (StdDraw.mousePressed() && !isDragging) {
                x0 = StdDraw.mouseX();
                y0 = StdDraw.mouseY();
                isDragging = true;
                continue;
            }

            // user is dragging a rectangle
            else if (StdDraw.mousePressed() && isDragging) {
                x1 = StdDraw.mouseX();
                y1 = StdDraw.mouseY();
                continue;
            }

            // mouse no longer pressed
            else if (!StdDraw.mousePressed() && isDragging) {
                isDragging = false;
            }


            RectHV rect = new RectHV(Math.min(x0, x1), Math.min(y0, y1),
                                     Math.max(x0, x1), Math.max(y0, y1));
            // draw the points
            StdDraw.clear();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            brute.draw();

            // draw the rectangle
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius();
            rect.draw();

            // draw the range search results for brute-force data structure in red
            StdDraw.setPenRadius(0.03);
            StdDraw.setPenColor(StdDraw.RED);
            for (Point2D p : brute.range(rect))
                p.draw();

            // draw the range search results for kd-tree in blue
            StdDraw.setPenRadius(.02);
            StdDraw.setPenColor(StdDraw.BLUE);
            for (Point2D p : kdtree.range(rect))
                p.draw();

            StdDraw.show();
            StdDraw.pause(40);
        }
        */
        String filename = args[0];
        In in = new In(filename);

        StdDraw.enableDoubleBuffering();

        // initialize the two data structures with point from standard input
        PointSET brute = new PointSET();
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            brute.insert(p);
        }

        while (true) {

            // the location (x, y) of the mouse
            double x = StdDraw.mouseX();
            double y = StdDraw.mouseY();
            Point2D query = new Point2D(x, y);

            // draw all of the points
            StdDraw.clear();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            brute.draw();

            // draw in red the nearest neighbor (using brute-force algorithm)
            StdDraw.setPenRadius(0.03);
            StdDraw.setPenColor(StdDraw.RED);
            brute.nearest(query).draw();
            StdDraw.setPenRadius(0.02);

            // draw in blue the nearest neighbor (using kd-tree algorithm)
            StdDraw.setPenColor(StdDraw.BLUE);
            kdtree.nearest(query).draw();
            StdDraw.show();
            StdDraw.pause(40);
        }
	}
}
