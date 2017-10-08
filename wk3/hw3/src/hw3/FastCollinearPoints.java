package hw3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
	List<LineSegment> segments = new ArrayList<LineSegment>();
	
	private class Item {
		private double slope;
		private Point point;
		
		public Item(Point origin, Point pt)
		{
			this.point = pt;
			this.slope = origin.slopeTo(pt);
		}
		
		public double getSlope() { return slope; }
		public Point  getPoint() { return point; }
	}

	public static final Comparator<Item> ItemComparator = new Comparator<Item> () {
		public int compare(Item item1, Item item2) {
			double m = item1.getSlope() - item2.getSlope();
			return (m < 0)?-1:((m > 0)?1:0);
		}
	};
	
	public FastCollinearPoints(Point[] points)  {
		// finds all line segments containing 4 or more points
    	if (points == null)
    		throw new NullPointerException("input missing");

    	//if fewer than 4 input lines, there will not be any collinear line with 4 points
    	if (points.length < 4)
    		return;

    	for (int i = 0; i < points.length; ++i)
    	{
    		Point origin = points[i];
    		Item[] slopes = new Item[points.length-1];
    		int currentindex = 0;
    		for (int ii = 0; ii < points.length; ++ii)
    		{
    			if (ii == i)
    				continue;
    			if (origin.compareTo(points[ii]) == 0)
    				throw new IllegalArgumentException("repeated points");
    			
    			slopes[currentindex++] = new Item(origin, points[ii]);
    		}
    		//sort the slopes
    		Arrays.sort(slopes, FastCollinearPoints.ItemComparator);
    		double currentslope = slopes[0].getSlope();
    		int startindex = 0;
    		int matchcount = 1;
    		for (int k = 1; k < slopes.length; ++k)
    		{
    			double slope = slopes[k].getSlope();
    			if (currentslope == slope)
    				++matchcount;
    			else {
    				//slopes changes
    				if (matchcount >= 3)
    				{
    					LineSegment line = getLine(origin, slopes, startindex, k);
    					if (line != null)
    						segments.add(line);
    				}
    				startindex = k;
    				matchcount = 1;
    				currentslope = slopes[k].getSlope();
    			}
    		}
    		if (matchcount >= 3)
    		{
    			LineSegment line = getLine(origin, slopes, startindex, slopes.length);
    			if (line != null)
    			    segments.add(line);
    		}
    	}    	
	}

	private LineSegment getLine(Point origin, Item[] items, int start, int end){
		int max = start;
		int min = start;
		for (int i = start; i < end; ++i)
		{
			if (items[max].getPoint().compareTo(items[i].getPoint()) < 0)
				max = i;
			if (items[min].getPoint().compareTo(items[i].getPoint()) > 0)
				min = i;
		}
		Point minptr = (origin.compareTo(items[min].getPoint())>0)?items[min].getPoint():origin;
		Point maxptr = (origin.compareTo(items[max].getPoint())>0)?origin:items[max].getPoint();
		if (minptr.compareTo(origin) == 0)
			return new LineSegment(minptr, maxptr);
		return null;
	}
	
	public int numberOfSegments() {
		// the number of line segments
		return segments.size();
	}
	
	public LineSegment[] segments() {
		// the line segments
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
