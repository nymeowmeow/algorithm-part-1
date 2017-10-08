package hw2;

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
	private int size = 0;
	private Item[] queue = null;

	private class QueueIterator implements Iterator {
		private int current = 0;
		Item[] myqueue;
		
		@SuppressWarnings("unchecked")
		public QueueIterator(Item[] queue, int size)
		{
			this.myqueue = (Item[])new Object[size];
			for (int i = 0; i < size; ++i)
				myqueue[i] = queue[i];
			StdRandom.shuffle(myqueue);
		}
		public boolean hasNext() {
			return this.current < myqueue.length;
		}

		public Item next() {
			if (!hasNext())
				throw new NoSuchElementException("queue empty");

			Item item = myqueue[current++];
			return item;
		}

		public void remove()
		{
			throw new UnsupportedOperationException("not implemented");
		}

		
	}
	
	@SuppressWarnings("unchecked")
	public RandomizedQueue() {
		// construct an empty randomized queue
		queue = (Item[])new Object[8];
	}
	
	public boolean isEmpty() {
		// is the queue empty?
		return this.size == 0;
	}
	
	public int size() {
		// return the number of items on the queue
		return this.size;
	}
	   
	public void enqueue(Item item) {
		if (item == null)
			throw new NullPointerException("item is null");
		   
		// add the item
		if (size == queue.length)
			resize(2*size);
		queue[size] = item;
		++this.size;
	}
	   
	public Item dequeue()  {
		// remove and return a random item
		if (isEmpty())
			throw new NoSuchElementException("the queue is empty"); 
		//pick a random element, exchange its content w the last element
		//and returns the last element
		int i = StdRandom.uniform(0, size);
		Item item = queue[i];
		exch(i, size-1);
		queue[size-1] = null;
		--size;
		return item;
	}

	private void exch(int i, int j)
	{
		Item tmp = queue[i];
		queue[i] = queue[j];
		queue[j] = tmp;
	}
	   
	@SuppressWarnings("unchecked")
	private void resize(int capacity)
	{
		Item[] temp = (Item[])new Object[capacity];
		for (int i = 0; i < size; ++i)
			temp[i] = queue[i];
		queue = temp;
	}
	   
	public Item sample()  {
		// return (but do not remove) a random item
		if (isEmpty())
			throw new NoSuchElementException("the queue is empty");
		   
		int i = StdRandom.uniform(0, size);
		return queue[i];
	}
	
	public Iterator<Item> iterator()  {
		// return an independent iterator over items in random order
		return new QueueIterator(this.queue, size());
	}
	   
	public static void main(String[] args) {
		// unit testing
		RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
		for (int i = 0; i < 10; ++i)
			queue.enqueue(i);
		StdOut.println("size of queue after adding 10 " + queue.size());
		int item = queue.dequeue();
		StdOut.println("dequeue item : " + item);
		StdOut.println("sample : " + queue.sample());
		StdOut.println("queue size after dequeue " + queue.size());
		Iterator<Integer> it = queue.iterator();
	    while (it.hasNext())
	    	StdOut.println("iter item : " + it.next());
	}
}