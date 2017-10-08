import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

public class BruteCollinearPoints {
    private ArrayList<LineSegment> segments = new ArrayList<LineSegment>();
	
    public BruteCollinearPoints(Point[] points)
    {
    	if (points == null)
    		throw new NullPointerException("input missing");

    	//if fewer than 4 input lines, there will not be any collinear line with 4 points
    	if (points.length < 4)
    		return;
    	
    	//find all line segments containing 4 points
    	for (int i = 0; i < points.length-3; ++i)
    	{
    		if (points[i] == null)
    			throw new NullPointerException("Point is NULL");
    		for (int j = i+1; j < points.length-2; ++j)
    		{
    			if (points[j] == null)
    				throw new NullPointerException("Point is NULL");
    			for (int k = j+1; k < points.length-1; ++k)
    			{
    				if (points[k] == null)
    					throw new NullPointerException("Point is NULL");
    				for (int l = k+1; l < points.length; ++l)
    				{
    					if (points[l] == null)
    						throw new NullPointerException("Point is NULL");
     					Point[] mypoints = new Point[] { points[i], points[j], points[k], points[l]};
    					if (isSameLine(mypoints))
    					{
     						segments.add(new LineSegment(mypoints[0], mypoints[3]));
    					}
    				}
    			}
    		}
    	}
    }
    
    private boolean isSameLine(Point[] points)
    {
    	if (points.length < 4)
    		throw new NullPointerException("must have 4 entries");
    	
    	for (int i = 1; i < points.length; ++i)
    		for (int j = i; j > 0; --j)
    		{
    			int res = points[j].compareTo(points[j-1]);
     			if (res == 0)
    				throw new IllegalArgumentException("repeated points");
    			if (res < 0)
    			{
    				Point tmp = points[j];
    				points[j] = points[j-1];
    				points[j-1] = tmp;
    			} else {
    				break;
    			}
    		}
    	Comparator<Point> cp = points[0].slopeOrder();
    	if (cp.compare(points[1],  points[2]) == 0 && cp.compare(points[1],  points[3]) == 0)
    	{
    		return true;
    	}
    	return false;
    }
    
    public int numberOfSegments()
    {
    	//the number of line segments
    	return segments.size();
    }
    
    public LineSegment[] segments()
    {
    	//the line segements
    	return segments.toArray(new LineSegment[segments.size()]);
    }
    
    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
