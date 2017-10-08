package hw2;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import java.util.Iterator;

public class Subset {
    public static void main(String[] args)
    {
    	 if (args.length != 1)
    	 {
    		 throw new NullPointerException("expecting size k");
    	 }
    	 int k = Integer.parseInt(args[0]);
    	 RandomizedQueue<String> queue = new RandomizedQueue<String>();
    	 while (!StdIn.isEmpty())
    	 {
    		 String value = StdIn.readString();
    		 queue.enqueue(value);
    	 }
    	 Iterator<String> iter = queue.iterator();
    	 int count = 0;
    	 while (iter.hasNext())
    	 {
    		 StdOut.println(iter.next());
    		 if (++count >= k)
    			 break;
    	 }
    }
}
