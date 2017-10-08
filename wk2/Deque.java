import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
	private Node first = null;
	private Node last = null;
	private int size = 0;
	
	private class Node {
		Item item;
		Node next;
		Node prev;
	}
	
	private class DequeIterator implements Iterator<Item> {
		private Node current = null;
		
		public DequeIterator(Node first)
		{
			this.current = first;
		}
		
		public boolean hasNext()
		{
			return this.current != null;
		}
		
		public Item next()
		{
			if (!hasNext())
				throw new NoSuchElementException("queue empty");
			
			Node node = this.current;
			this.current = this.current.next;
			return node.item;
		}
		
		public void remove()
		{
			throw new UnsupportedOperationException("not implemented");
		}
	}
	
    public Deque() {
    	//construct an empty Deque
    }
	public boolean isEmpty() {
		//is the Deque empty?
		return first == null;
	}
	public int size() {
		//return the number of items on the Deque
		return size;
	}
	public void addFirst(Item item) {
		//add the item to the front
		if (item == null)
			throw new NullPointerException("item is null");
		Node node = new Node();
		node.item = item;
		node.next = first;
		node.prev = null;
		if (first == null)
			last = node;
		else
			first.prev = node;
		first = node;
		++size;
	}
	public void addLast(Item item) {
		//add the item to the end
		if (item == null)
			throw new NullPointerException("item is null");
		Node node = new Node();
		node.item = item;
		node.next = null;
		node.prev = last;
		if (first == null)
			first = node;
		if (last != null)
		{
			last.next = node;
		}
		last = node;
		++size;
	}
	public Item removeFirst() {
		//remove and return the item from the front
		if (first == null)
			throw new NoSuchElementException("the queue is empty");
		Node node = first;
		first = first.next;
		node.next = null;
		if (first == null)
			last = null;
		else
			first.prev = null;
		--size;
		return node.item;
	}
	public Item removeLast() {
		//remove and return the item from the end
		if (last == null)
			throw new NoSuchElementException("the queue is empty");
		Node node = last;
		if (last.prev == null)
		{
			first = last = null;
		} else {
			last = last.prev;
			last.next = null;
		}
		--size;
		node.prev = null;
		return node.item;
	}
	
	public Iterator<Item> iterator() {
		//return an iterator over items in order from front to end
		return new DequeIterator(first);
	}
	public static void main(String[] args)   // unit testing
	{
    	Deque<Integer> dqueue = new Deque<Integer>();
    	for (int i = 0; i < 100; ++i)
    		dqueue.addFirst(i);
    	for (int i = 0; i < 100; ++i)
    		dqueue.addLast(i);
     	for (int i = 99; i >= 0; --i)
    	{
    		int value = dqueue.removeFirst();
    		if (value != i)
    		{
    			StdOut.println("expecting " + i + ", get " + value);
    		}
    	}
    	for (int i = 0; i < 100; ++i)
    	{
    		StdOut.println("the count is " + i);
    		int last = dqueue.removeLast();
    		if (last != (99-i))
    			StdOut.println("removelast, expecting " + i + ", get " + last);
    	}
    	if (!dqueue.isEmpty())
    		StdOut.println("dqueue should be empty");
    	for (int item : dqueue)
    		StdOut.println("dqueue " + item);
	}
}
