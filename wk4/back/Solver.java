import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

public class Solver {
	private boolean solvable = false;
	private List<Board> minmoves = new ArrayList<Board>();

	private class SearchNode {
		private SearchNode prev;
		private Board current;
		private int   step;
		private int   count;
		
		public SearchNode(Board current, SearchNode prev, int step)
		{
			this.prev = prev;
			this.current = current;
			this.step = step;
			this.count = step + current.hamming();
		}
		
		public Board getCurrent() { return current; }
		public SearchNode getPrev() { return prev; }
		public int   getStep() { return step; }
		public int   getCount() { return count; }
		public int   getDistance() { return count - step; }
	}
	
    public Solver(Board initial) {
    	// find a solution to the initial board (using the A* algorithm)
    	Board twin = initial.twin();
    	Comparator<SearchNode> comp = new Comparator<SearchNode>() {
    		public int compare(SearchNode first, SearchNode second)
    		{
			return (first.getCount() == second.getCount())?(first.getDistance() - second.getDistance()):first.getCount() - second.getCount();
			//return first.getCount() - second.getCount();
    		}
    	};
    	MinPQ<SearchNode> pq = new MinPQ<SearchNode>(comp);
    	pq.insert(new SearchNode(initial, null, 0));
    	MinPQ<SearchNode> tpq = new MinPQ<SearchNode>(comp);
    	tpq.insert(new SearchNode(twin, null, 0));
    	while (!pq.isEmpty() || !tpq.isEmpty()) 
    	{
    		SearchNode curr = pq.delMin();
    		if (curr.getCurrent().isGoal())
    		{
    			//find a solution
    			solvable = true;
    			while (curr != null) {
    				minmoves.add(0, curr.getCurrent());
    				curr = curr.getPrev();
    			}
    			break;
    		}
    		addNeighbors(curr, pq);
    		//add the neighbors for this node and check if
    		//we already visit the node
    		SearchNode tcurr = tpq.delMin();
    		if (tcurr.getCurrent().isGoal())
    		{
    			solvable = false;
    			break;
    		}
    		addNeighbors(tcurr, tpq);
    	}
    }

    private boolean isRepeated(SearchNode node, Board board)
    {
	while (node != null)
	{
	     if (node.getCurrent().equals(board))
		return true;
	     node = node.getPrev();
	}
	return false;
    }

    private void addNeighbors(SearchNode current, MinPQ<SearchNode> queue)
    {
    	Board board = current.getCurrent();
    	int steps = current.getStep();
    	SearchNode prev = current.getPrev();
    	Iterable<Board> nns = board.neighbors();
    	Iterator<Board> it = nns.iterator();
    	while (it.hasNext()) {
    		Board potential = it.next();
    		if (prev != null && prev.getCurrent() != null)
    		{
    			if (!isRepeated(prev, board))
    			{
    				queue.insert(new SearchNode(potential, current, steps+1));
    			}
    		} else {
    			queue.insert(new SearchNode(potential, current, steps+1));
    		}
    	}
    }
    
    public boolean isSolvable()  {
    	// is the initial board solvable?
    	return solvable;
    }
    
    public int moves() {
    	// min number of moves to solve initial board; -1 if unsolvable
    	return (isSolvable())?(minmoves.size()-1):-1;
    }
    
    public Iterable<Board> solution()  {
    	// sequence of boards in a shortest solution; null if unsolvable
    	return (isSolvable())?minmoves:null;
    }
    
    public static void main(String[] args) {
    	// solve a slider puzzle (given below)
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

	long start = System.currentTimeMillis();
        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
	StdOut.println("total time " + (System.currentTimeMillis() - start));
    }
}
